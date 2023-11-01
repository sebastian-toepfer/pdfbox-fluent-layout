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
package io.github.sebastiantoepfer.pdfbox.layout.hamcrest;

import static java.util.function.Predicate.not;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Locale;
import org.apache.pdfbox.Loader;
import org.apache.pdfbox.io.RandomAccessReadBuffer;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.hamcrest.Matcher;
import org.hamcrest.MatcherAssert;

public class PDDocumentAssert {

    public static void assertThat(final InputStream actual, final Matcher<? super PDDocument> matcher)
        throws IOException {
        assertThat(Loader.loadPDF(new RandomAccessReadBuffer(actual)), matcher);
    }

    public static void assertThat(final PDDocument actual, final Matcher<? super PDDocument> matcher) {
        try {
            MatcherAssert.assertThat(actual, matcher);
        } finally {
            writePDDocument(actual);
        }
    }

    private static void writePDDocument(final PDDocument doc) {
        Arrays
            .stream(Thread.currentThread().getStackTrace())
            .skip(1)
            .filter(not(s -> s.getClassName().equals(PDDocumentAssert.class.getName())))
            .findFirst()
            .ifPresent(s -> writePDDocument(doc, s));
    }

    private static void writePDDocument(final PDDocument doc, final StackTraceElement s) {
        try {
            final File baseDir = determineBaseDir(s);
            baseDir.mkdirs();
            doc.save(new File(baseDir, String.format("%s.pdf", s.getMethodName())));
        } catch (IOException ignore) {}
    }

    private static File determineBaseDir(final StackTraceElement s) {
        final File result;
        final File target = new File("./target/test-pdfs/");
        if (s.getClassName().lastIndexOf('.') > 0) {
            result =
                new File(
                    target,
                    s.getClassName().substring(s.getClassName().lastIndexOf('.') + 1).toLowerCase(Locale.US)
                );
        } else {
            result = target;
        }
        return result;
    }
}
