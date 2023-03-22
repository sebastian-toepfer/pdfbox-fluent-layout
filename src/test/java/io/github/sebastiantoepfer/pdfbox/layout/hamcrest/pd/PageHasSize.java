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
