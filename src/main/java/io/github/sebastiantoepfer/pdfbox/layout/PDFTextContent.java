package io.github.sebastiantoepfer.pdfbox.layout;

import io.github.sebastiantoepfer.pdfbox.layout.PageStyle.PDFContentStream;
import java.io.IOException;
import java.io.OutputStream;
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
            stream.setFont(PDType1Font.COURIER, 24);
            stream.showText(text);
            stream.endText();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
