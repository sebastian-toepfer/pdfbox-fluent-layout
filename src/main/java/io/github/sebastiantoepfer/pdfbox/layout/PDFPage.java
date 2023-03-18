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
