package io.github.sebastiantoepfer.pdfbox.layout;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.common.PDRectangle;

public class PDFDocument implements PDFPages {

    private final PageStyle defaultPageStyle;
    private final List<PDFPage> pages;

    public PDFDocument() {
        this(new PageStyle(PDRectangle.LETTER));
    }

    public PDFDocument(final PageStyle defaultPageStyle) {
        this(defaultPageStyle, new ArrayList<>());
    }

    private PDFDocument(final PageStyle defaultPageStyle, final List<PDFPage> pages) {
        this.defaultPageStyle = defaultPageStyle;
        this.pages = List.copyOf(pages);
    }

    @Override
    public void renderTo(final OutputStream os) throws IOException {
        try (PDDocument doc = new PDDocument()) {
            pages.stream().forEach(p -> p.apply(doc));
            doc.save(os);
        }
    }

    @Override
    public PDFContents newPage() {
        return newPage(defaultPageStyle);
    }

    @Override
    public PDFContents newPage(final PageStyle style) {
        return new PDFPage(this, style);
    }

    PDFDocument withPage(final PDFPage newPage) {
        final List<PDFPage> newPages = new ArrayList<>(pages);
        newPages.add(newPage);
        return new PDFDocument(defaultPageStyle, newPages);
    }
}
