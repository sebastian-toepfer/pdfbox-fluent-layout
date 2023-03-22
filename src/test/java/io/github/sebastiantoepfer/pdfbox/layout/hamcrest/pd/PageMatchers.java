package io.github.sebastiantoepfer.pdfbox.layout.hamcrest.pd;

import static org.hamcrest.Matchers.containsString;

import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.hamcrest.Matcher;

public final class PageMatchers {

    public static Matcher<PDPage> hasTextContent(final String content) {
        return hasTextContent(containsString(content));
    }

    public static Matcher<PDPage> hasTextContent(final String content, final float x, final float y) {
        return hasTextContent(containsString(content), x, y);
    }

    public static Matcher<PDPage> hasTextContent(final Matcher<String> content) {
        return new PageHasTextContent(content);
    }

    public static Matcher<PDPage> hasTextContent(final Matcher<String> content, final float x, final float y) {
        return new PageHasTextContent(content, new AtPosition(x, y));
    }

    public static Matcher<PDPage> hasSize(final PDRectangle size) {
        return new PageHasSize(size);
    }

    private PageMatchers() {}
}
