package io.github.sebastiantoepfer.pdfbox.layout.hamcrest.pd;

import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.hamcrest.Matcher;

public final class PageMatchers {

    public static Matcher<PDPage> hasTextContent(final String content) {
        return new PageHasTextContent(content);
    }

    public static Matcher<PDPage> hasTextContent(final String content, final float x, final float y) {
        return new PageHasTextContent(content, new AtPosition(x, y));
    }

    public static Matcher<PDPage> hasSize(final PDRectangle size) {
        return new PageHasSize(size);
    }

    private PageMatchers() {}
}
