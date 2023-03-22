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

import java.io.IOException;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDType1Font;

public class PageStyle {

    private final PDRectangle mediaBox;
    private final Margins margins;

    public PageStyle(final PDRectangle mediaBox) {
        this(mediaBox, new Margins(0, 0, 0, 0));
    }

    public PageStyle(final PDRectangle mediaBox, final Margins margins) {
        this.mediaBox = new PDRectangle(mediaBox.getWidth(), mediaBox.getHeight());
        this.margins = margins;
    }

    PDFContentStream newContentStream(final PDDocument doc) throws IOException {
        final PDPage page = new PDPage(mediaBox);
        doc.addPage(page);
        return new PDFContentStream(doc, page);
    }

    PageStyle withMargin(final Margins margins) {
        return new PageStyle(mediaBox, margins);
    }

    public final class PDFContentStream implements AutoCloseable {

        private final PDPageContentStream pageContentStream;

        private PDFContentStream(final PDDocument doc, final PDPage page) throws IOException {
            pageContentStream = new PDPageContentStream(doc, page, PDPageContentStream.AppendMode.APPEND, true);
        }

        public void beginText() throws IOException {
            pageContentStream.beginText();
            pageContentStream.newLineAtOffset(
                margins.artBox(mediaBox).getLowerLeftX(),
                margins.artBox(mediaBox).getUpperRightY()
            );
        }

        float width() {
            return margins.artBox(mediaBox).getWidth();
        }

        void setLeading(final float leading) throws IOException {
            pageContentStream.setLeading(leading);
        }

        void setFont(final PDType1Font font, final int size) throws IOException {
            pageContentStream.setFont(font, size);
        }

        void showText(final String text) throws IOException {
            pageContentStream.showText(text);
        }

        void newLine() throws IOException {
            pageContentStream.newLine();
        }

        void endText() throws IOException {
            pageContentStream.endText();
        }

        @Override
        public void close() throws IOException {
            pageContentStream.close();
        }
    }
}
