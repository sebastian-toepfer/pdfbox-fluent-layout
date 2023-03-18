package io.github.sebastiantoepfer.pdfbox.layout;

import java.io.IOException;
import java.io.OutputStream;

public interface Renderable {
    void renderTo(OutputStream os) throws IOException;
}
