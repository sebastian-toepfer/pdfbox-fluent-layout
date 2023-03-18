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

import static io.github.sebastiantoepfer.pdfbox.layout.hamcrest.PDDocumentAssert.assertThat;
import static io.github.sebastiantoepfer.pdfbox.layout.hamcrest.pd.DocumentMatchers.hasPageNumberWith;
import static io.github.sebastiantoepfer.pdfbox.layout.hamcrest.pd.PageMatchers.hasTextContent;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.junit.jupiter.api.Test;

public class TextLayoutTest {

    @Test
    void should_create_text_at_top_with_given_margin() throws Exception {
        final PDFPages doc = new PDFDocument()
            .newPage(new PageStyle(PDRectangle.A4).withMargin(new Margins(50, 50, 50, 50)))
            .withText("Hallo PDF");
        final ByteArrayOutputStream baos = new ByteArrayOutputStream();
        doc.renderTo(baos);

        assertThat(
            new ByteArrayInputStream(baos.toByteArray()),
            hasPageNumberWith(1, hasTextContent("Hallo PDF", 50, PDRectangle.A4.getHeight() - 50))
        );
    }
}
