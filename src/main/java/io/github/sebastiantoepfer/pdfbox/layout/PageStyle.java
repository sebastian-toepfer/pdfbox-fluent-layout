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
