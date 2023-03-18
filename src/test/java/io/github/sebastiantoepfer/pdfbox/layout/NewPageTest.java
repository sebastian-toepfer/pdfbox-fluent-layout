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
import static io.github.sebastiantoepfer.pdfbox.layout.hamcrest.pd.DocumentMatchers.hasNumberOfPages;
import static io.github.sebastiantoepfer.pdfbox.layout.hamcrest.pd.DocumentMatchers.hasPageNumberWith;
import static io.github.sebastiantoepfer.pdfbox.layout.hamcrest.pd.DocumentMatchers.hasPageWith;
import static io.github.sebastiantoepfer.pdfbox.layout.hamcrest.pd.PageMatchers.hasSize;
import static io.github.sebastiantoepfer.pdfbox.layout.hamcrest.pd.PageMatchers.hasTextContent;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.both;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.junit.jupiter.api.Test;

class NewPageTest {

    @Test
    void should_create_pdf_document_with_two_empty_pages() throws Exception {
        final PDFPages doc = new PDFDocument().newPage().newPage();
        final ByteArrayOutputStream baos = new ByteArrayOutputStream();
        doc.renderTo(baos);

        assertThat(new ByteArrayInputStream(baos.toByteArray()), hasNumberOfPages(2));
    }

    @Test
    void should_had_content() throws Exception {
        final PDFPages doc = new PDFDocument().newPage().newPage().withText("Hallo PDF");
        final ByteArrayOutputStream baos = new ByteArrayOutputStream();
        doc.renderTo(baos);

        assertThat(new ByteArrayInputStream(baos.toByteArray()), hasPageWith(hasTextContent("Hallo PDF")));
    }

    @Test
    void should_had_content_at_page() throws Exception {
        final PDFPages doc = new PDFDocument()
            .newPage()
            .withText("first page")
            .newPage()
            .withText("second page")
            .withText("more text content")
            .newPage(new PageStyle(PDRectangle.LEGAL))
            .withText("third page");
        final ByteArrayOutputStream baos = new ByteArrayOutputStream();
        doc.renderTo(baos);

        assertThat(
            new ByteArrayInputStream(baos.toByteArray()),
            both(hasPageNumberWith(1, hasTextContent("first page")))
                .and(hasPageNumberWith(2, both(hasTextContent("second page")).and(hasTextContent("more text content"))))
                .and(hasPageNumberWith(3, hasTextContent("third page")))
        );
    }

    @Test
    void should_use_default_pagesize_for_firstpage() throws Exception {
        final PDFPages doc = new PDFDocument(new PageStyle(PDRectangle.A4)).newPage();
        final ByteArrayOutputStream baos = new ByteArrayOutputStream();
        doc.renderTo(baos);

        assertThat(new ByteArrayInputStream(baos.toByteArray()), hasPageNumberWith(1, hasSize(PDRectangle.A4)));
    }

    @Test
    void should_use_custom_pagesize_for_page() throws Exception {
        final PDFPages doc = new PDFDocument(new PageStyle(PDRectangle.A4))
            .newPage()
            .newPage(new PageStyle(PDRectangle.A0))
            .newPage();
        final ByteArrayOutputStream baos = new ByteArrayOutputStream();
        doc.renderTo(baos);

        assertThat(
            new ByteArrayInputStream(baos.toByteArray()),
            allOf(
                hasPageNumberWith(1, hasSize(PDRectangle.A4)),
                hasPageNumberWith(2, hasSize(PDRectangle.A0)),
                hasPageNumberWith(3, hasSize(PDRectangle.A4))
            )
        );
    }
}
