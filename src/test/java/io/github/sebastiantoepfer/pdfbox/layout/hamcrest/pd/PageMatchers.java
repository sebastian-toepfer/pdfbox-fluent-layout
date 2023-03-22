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
