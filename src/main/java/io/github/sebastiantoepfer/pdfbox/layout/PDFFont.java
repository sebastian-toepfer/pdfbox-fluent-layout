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

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import java.io.IOException;
import org.apache.pdfbox.pdmodel.font.PDType1Font;

public class PDFFont {

    private final PDType1Font font;
    private final int size;

    @SuppressFBWarnings("EI_EXPOSE_REP2")
    public PDFFont(final PDType1Font font, final int size) {
        this.font = font;
        this.size = size;
    }

    float calculateLineWidthFor(final String text) throws IOException {
        return forCurrentFontSize(font.getStringWidth(text));
    }

    void apply(final PageStyle.PDFContentStream stream) {
        try {
            stream.setFont(font, size);
            stream.setLeading(forCurrentFontSize(font.getBoundingBox().getHeight()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private float forCurrentFontSize(final float baseValue) {
        return baseValue / 1000 * size;
    }
}
