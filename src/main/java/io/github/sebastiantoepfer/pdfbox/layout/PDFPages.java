package io.github.sebastiantoepfer.pdfbox.layout;

public interface PDFPages extends Renderable {
    PDFContents newPage();

    PDFContents newPage(PageStyle style);

    interface PDFContents extends PDFPages {
        PDFContents withText(String text);
    }
}
