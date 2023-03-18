package io.github.sebastiantoepfer.pdfbox.layout.hamcrest.pd;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;

class DocumentHasNumberOfPages extends TypeSafeMatcher<PDDocument> {

    private final int numberOfPages;

    public DocumentHasNumberOfPages(final int numberOfPages) {
        this.numberOfPages = numberOfPages;
    }

    @Override
    protected boolean matchesSafely(final PDDocument item) {
        return item.getNumberOfPages() == numberOfPages;
    }

    @Override
    public void describeTo(final Description description) {
        description.appendText("PDDocument with ").appendValue(numberOfPages).appendText(" pages.");
    }

    @Override
    protected void describeMismatchSafely(final PDDocument item, final Description mismatchDescription) {
        mismatchDescription.appendText("PDDocument has ").appendValue(item.getNumberOfPages()).appendText(" pages.");
    }
}
