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
import java.util.Objects;
import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;

class AtPosition extends TypeSafeMatcher<Position> {

    private final Position expected;

    public AtPosition(final float x, final float y) {
        this(new Position(x, y));
    }

    public AtPosition(final Position expected) {
        this.expected = Objects.requireNonNull(expected);
    }

    @Override
    protected boolean matchesSafely(final Position item) {
        return expected.equals(item);
    }

    @Override
    public void describeTo(final Description description) {
        description.appendText("postion ").appendValue(expected);
    }

    public static final class Position {

        private final float x;
        private final float y;

        public Position(final float x, final float y) {
            this.x = x;
            this.y = y;
        }

        @Override
        public String toString() {
            return "x=" + x + ", y=" + y;
        }

        @Override
        public int hashCode() {
            int hash = 7;
            hash = 23 * hash + Float.floatToIntBits(this.x);
            hash = 23 * hash + Float.floatToIntBits(this.y);
            return hash;
        }

        @Override
        public boolean equals(final Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null) {
                return false;
            }
            if (getClass() != obj.getClass()) {
                return false;
            }
            final Position other = (Position) obj;
            if (Float.floatToIntBits(this.x) != Float.floatToIntBits(other.x)) {
                return false;
            }
            return Float.floatToIntBits(this.y) == Float.floatToIntBits(other.y);
        }
    }
}
