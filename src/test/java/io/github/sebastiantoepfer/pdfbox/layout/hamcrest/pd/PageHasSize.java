package io.github.sebastiantoepfer.pdfbox.layout.hamcrest.pd;

import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;

public class PageHasSize extends TypeSafeMatcher<PDPage> {

    private final Matcher<PDRectangle> mediabox;

    public PageHasSize(final PDRectangle mediabox) {
        this.mediabox = PDRectangleMatcher.isRectangle(mediabox);
    }

    @Override
    protected boolean matchesSafely(final PDPage item) {
        return mediabox.matches(item.getMediaBox());
    }

    @Override
    public void describeTo(final Description description) {
        description.appendText(" page with mediabox ").appendDescriptionOf(mediabox);
    }

    @Override
    protected void describeMismatchSafely(final PDPage item, final Description mismatchDescription) {
        mismatchDescription.appendText(" page has mediabox ").appendValue(item.getMediaBox());
    }
}
