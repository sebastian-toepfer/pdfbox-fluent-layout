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

import io.github.sebastiantoepfer.pdfbox.layout.hamcrest.pd.AtPosition.Position;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import org.apache.pdfbox.contentstream.operator.Operator;
import org.apache.pdfbox.contentstream.operator.OperatorName;
import org.apache.pdfbox.cos.COSNumber;
import org.apache.pdfbox.cos.COSString;
import org.apache.pdfbox.pdfparser.PDFStreamParser;
import org.apache.pdfbox.pdmodel.PDPage;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.SelfDescribing;
import org.hamcrest.TypeSafeMatcher;

class PageHasTextContent extends TypeSafeMatcher<PDPage> {

    private final Matcher<String> content;
    private final Matcher<Position> hasPosition;

    PageHasTextContent(final Matcher<String> content) {
        this(content, new Any<>("Position"));
    }

    PageHasTextContent(final Matcher<String> content, final Matcher<Position> hasPosition) {
        this.content = Objects.requireNonNull(content);
        this.hasPosition = Objects.requireNonNull(hasPosition);
    }

    @Override
    protected boolean matchesSafely(final PDPage item) {
        final Text text = textOfPage(item);
        return text.hasText(content) && text.isAt(hasPosition);
    }

    @Override
    public void describeTo(final Description description) {
        description
            .appendText("with ")
            .appendDescriptionOf(content)
            .appendText(" and at ")
            .appendDescriptionOf(hasPosition);
    }

    @Override
    protected void describeMismatchSafely(final PDPage item, final Description mismatchDescription) {
        mismatchDescription.appendDescriptionOf(textOfPage(item));
    }

    private Text textOfPage(final PDPage item) throws RuntimeException {
        final StringBuilder sb = new StringBuilder();
        Position position = new Position(0, 0);
        try {
            if (item.hasContents()) {
                final List<Float> numbers = new ArrayList<>();
                boolean multiline = false;
                final PDFStreamParser parser = new PDFStreamParser(item);
                parser.parse();
                final List<Object> pageTokens = parser.getTokens();
                for (Object token : pageTokens) {
                    if (token instanceof Operator op) {
                        final String currentOperatorName = op.getName();
                        switch (currentOperatorName) {
                            case OperatorName.NEXT_LINE -> {
                                if (multiline) {
                                    sb.append('\n');
                                }
                            }
                            case OperatorName.SET_TEXT_LEADING -> {
                                multiline = numbers.size() == 1 && numbers.iterator().next() > 0;
                            }
                            case OperatorName.MOVE_TEXT -> {
                                if (numbers.size() == 2) {
                                    position = new Position(numbers.get(0), numbers.get(1));
                                }
                            }
                        }
                        numbers.clear();
                    } else if (token instanceof COSString cosStr) {
                        sb.append(cosStr.getString());
                    } else if (token instanceof COSNumber cosNumber) {
                        numbers.add(cosNumber.floatValue());
                    }
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return new Text(sb.toString(), position);
    }

    private static class Text implements SelfDescribing {

        private final String value;
        private final Position at;

        public Text(final String value, final Position at) {
            this.value = value;
            this.at = at;
        }

        public boolean hasText(final Matcher<String> hasText) {
            return hasText.matches(value);
        }

        public boolean isAt(final Matcher<Position> isAt) {
            return isAt.matches(at);
        }

        @Override
        public void describeTo(final Description description) {
            description.appendText(value).appendText(" and at ").appendValue(at);
        }
    }
}
