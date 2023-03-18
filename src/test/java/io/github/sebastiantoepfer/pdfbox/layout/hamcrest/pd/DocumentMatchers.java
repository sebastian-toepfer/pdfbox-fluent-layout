package io.github.sebastiantoepfer.pdfbox.layout.hamcrest.pd;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.hamcrest.Matcher;

public final class DocumentMatchers {

    public static Matcher<PDDocument> hasNumberOfPages(final int number) {
        return new DocumentHasNumberOfPages(number);
    }

    public static Matcher<PDDocument> hasPageWith(final Matcher<PDPage> matcher) {
        return new DocumentHasPageWith(matcher);
    }

    public static Matcher<PDDocument> hasPageNumberWith(final int pagenumber, final Matcher<PDPage> matcher) {
        return new DocumentHasPageWith(matcher, pagenumber);
    }

    private DocumentMatchers() {}
}
