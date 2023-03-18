package io.github.sebastiantoepfer.pdfbox.layout.hamcrest;

import static java.util.function.Predicate.not;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Locale;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.hamcrest.Matcher;
import org.hamcrest.MatcherAssert;

public class PDDocumentAssert {

    public static void assertThat(final InputStream actual, final Matcher<? super PDDocument> matcher)
        throws IOException {
        assertThat(PDDocument.load(actual), matcher);
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
