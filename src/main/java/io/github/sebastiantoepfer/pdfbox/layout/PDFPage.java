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
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import org.apache.pdfbox.pdmodel.PDDocument;

class PDFPage implements PDFPages.PDFContents {

    private final PDFDocument owner;
    private final PageStyle style;
    private final List<PDFTextContent> pageContent;

    public PDFPage(final PDFDocument ancestor, final PageStyle style) {
        this(ancestor, style, List.of());
    }

    public PDFPage(final PDFDocument owner, final PageStyle style, final List<PDFTextContent> pageContent) {
        this.owner = Objects.requireNonNull(owner);
        this.style = style;
        this.pageContent = List.copyOf(pageContent);
    }

    @Override
    public PDFContents withText(final String text) {
        return new PDFTextContent(this, text);
    }

    public void apply(final PDDocument doc) {
        try (final PageStyle.PDFContentStream stream = style.newContentStream(doc)) {
            pageContent.stream().forEach(pc -> pc.apply(stream));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public PDFContents newPage() {
        return owner.withPage(this).newPage();
    }

    @Override
    public PDFContents newPage(final PageStyle style) {
        return owner.withPage(this).newPage(style);
    }

    @Override
    public void renderTo(final OutputStream os) throws IOException {
        owner.withPage(this).renderTo(os);
    }

    PDFPage withText(final PDFTextContent content) {
        final List<PDFTextContent> newPageContent = new ArrayList<>(pageContent);
        newPageContent.add(content);
        return new PDFPage(owner, style, newPageContent);
    }
}
