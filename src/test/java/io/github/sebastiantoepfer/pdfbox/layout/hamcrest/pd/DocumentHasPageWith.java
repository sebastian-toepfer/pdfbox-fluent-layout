/*
 * The MIT License
 *
 * Copyright 2023 sebastian.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
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
