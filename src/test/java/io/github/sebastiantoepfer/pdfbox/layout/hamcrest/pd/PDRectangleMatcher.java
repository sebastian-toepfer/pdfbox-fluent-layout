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

import java.util.Objects;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;

public class PDRectangleMatcher extends TypeSafeMatcher<PDRectangle> {

    public static Matcher<PDRectangle> isRectangle(final float witdh, final float height) {
        return isRectangle(0, 0, witdh, height);
    }

    public static Matcher<PDRectangle> isRectangle(
        final float x,
        final float y,
        final float witdh,
        final float height
    ) {
        return isRectangle(new PDRectangle(x, y, witdh, height));
    }

    public static Matcher<PDRectangle> isRectangle(final PDRectangle expected) {
        return new PDRectangleMatcher(expected);
    }

    private final PDRectangle expected;

    PDRectangleMatcher(final PDRectangle expected) {
        this.expected = Objects.requireNonNull(expected);
    }

    @Override
    protected boolean matchesSafely(final PDRectangle item) {
        return (
            expected.getLowerLeftX() == item.getLowerLeftX() &&
            expected.getLowerLeftY() == item.getLowerLeftY() &&
            expected.getUpperRightX() == item.getUpperRightX() &&
            expected.getUpperRightY() == item.getUpperRightY()
        );
    }

    @Override
    public void describeTo(final Description description) {
        description.appendText("rectangle ").appendValue(expected);
    }

    @Override
    protected void describeMismatchSafely(final PDRectangle item, final Description mismatchDescription) {
        mismatchDescription.appendText(" rectangle was ").appendValue(item);
    }
}
