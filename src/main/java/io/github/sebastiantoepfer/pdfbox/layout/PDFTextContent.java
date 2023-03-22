/*
 * The MIT License
 *
 * Copyright 2023 sebastian.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package io.github.sebastiantoepfer.pdfbox.layout;

import io.github.sebastiantoepfer.pdfbox.layout.PageStyle.PDFContentStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.regex.Pattern;
import org.apache.pdfbox.pdmodel.font.PDType1Font;

class PDFTextContent implements PDFPages.PDFContents {

    private final PDFPage owner;
    private final String text;

    public PDFTextContent(final PDFPage owner, final String text) {
        this.owner = owner;
        this.text = text;
    }

    @Override
    public PDFContents withText(final String text) {
        return owner.withText(this).withText(text);
    }

    @Override
    public PDFContents newPage() {
        return owner.withText(this).newPage();
    }

    @Override
    public PDFContents newPage(final PageStyle style) {
        return owner.withText(this).newPage(style);
    }

    @Override
    public void renderTo(final OutputStream os) throws IOException {
        owner.withText(this).renderTo(os);
    }

    void apply(final PDFContentStream stream) {
        try {
            stream.beginText();
            final PDFFont font = new PDFFont(PDType1Font.COURIER, 24);
            font.apply(stream);
            for (String line : new MultiLineText(text, font, stream.width()).lines()) {
                stream.showText(line);
                stream.newLine();
            }
            stream.endText();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static final class MultiLineText {

        private static final Pattern NEW_LINE = Pattern.compile("[\\n\\r]");
        private final String text;
        private final PDFFont font;
        private final float maxWidth;

        public MultiLineText(final String text, final PDFFont font, final float maxWidth) {
            this.text = text;
            this.font = font;
            this.maxWidth = maxWidth;
        }

        Collection<String> lines() throws IOException {
            final List<String> result = new ArrayList<>();
            final String[] lines = NEW_LINE.split(text);
            for (String line : lines) {
                result.addAll(new WrappedTextLine(line, font, maxWidth).lines());
            }
            return result;
        }

        private static class WrappedTextLine {

            private static final Pattern WORD_DELIMITER = Pattern.compile("[\\s]");
            private final String line;
            private final PDFFont font;
            private final float maxWidth;

            public WrappedTextLine(final String line, final PDFFont font, final float maxWidth) {
                this.line = line;
                this.font = font;
                this.maxWidth = maxWidth;
            }

            Collection<String> lines() throws IOException {
                final List<String> result = new ArrayList<>();
                final String[] words = WORD_DELIMITER.split(line);
                StringBuilder sb = new StringBuilder();
                for (String word : words) {
                    if (font.calculateLineWidthFor(sb.toString().concat(" ").concat(word)) > maxWidth) {
                        result.add(sb.toString());
                        sb = new StringBuilder(word);
                    } else {
                        if (sb.length() > 0) {
                            sb = sb.append(' ');
                        }
                        sb = sb.append(word);
                    }
                }
                result.add(sb.toString());
                return result;
            }
        }
    }
}
