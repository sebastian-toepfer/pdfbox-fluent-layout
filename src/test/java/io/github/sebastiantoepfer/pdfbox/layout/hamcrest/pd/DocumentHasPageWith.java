package io.github.sebastiantoepfer.pdfbox.layout.hamcrest.pd;

import static java.util.function.Predicate.not;

import java.util.Objects;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;

class DocumentHasPageWith extends TypeSafeMatcher<PDDocument> {

    private final Matcher<PDPage> pageMatcher;
    private final int pageNumber;

    public DocumentHasPageWith(final Matcher<PDPage> pageMatcher) {
        this(pageMatcher, 0);
    }

    public DocumentHasPageWith(final Matcher<PDPage> pageMatcher, final int pageNumber) {
        this.pageMatcher = Objects.requireNonNull(pageMatcher);
        this.pageNumber = pageNumber;
    }

    @Override
    protected boolean matchesSafely(final PDDocument item) {
        return pageStreamOf(item).anyMatch(currentPage -> pageMatcher.matches(currentPage));
    }

    @Override
    public void describeTo(final Description description) {
        description.appendText("PDDocument ").appendDescriptionOf(pageMatcher).appendText(pageNumber()).appendText(".");
    }

    @Override
    protected void describeMismatchSafely(final PDDocument item, final Description mismatchDescription) {
        pageStreamOf(item)
            .filter(not(p -> pageMatcher.matches(p)))
            .findFirst()
            .ifPresent(p -> pageMatcher.describeMismatch(p, mismatchDescription));
    }

    private Stream<PDPage> pageStreamOf(final PDDocument item) {
        return StreamSupport.stream(item.getPages().spliterator(), false).skip(pagesToSkip()).limit(untilPageNumber());
    }

    private String pageNumber() {
        final String result;
        if (pageNumber < 1) {
            result = "";
        } else {
            result = String.format(" at page %d", pageNumber);
        }
        return result;
    }

    private long pagesToSkip() {
        return Math.max(pageNumber - 1, 0);
    }

    private long untilPageNumber() {
        final long result;
        if (pageNumber < 1) {
            result = Long.MAX_VALUE;
        } else {
            result = pageNumber;
        }
        return result;
    }
}
