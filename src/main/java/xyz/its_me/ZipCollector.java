package xyz.its_me;

import org.apache.commons.io.IOUtils;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Objects;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * Reads any number of InputStreams and zips them into an OutputStream. The current implementation uses {@link
 * ZipOutputStream} but that can be changed easily. It implements {@link AutoCloseable} which allows using the
 * try with resources pattern.
 */
public class ZipCollector implements AutoCloseable {
    private final ZipOutputStream outputStream;

    /**
     * Initializes a new ZipCollector with outputStream. The outputStream will be wrapped into an additional {@link
     * BufferedOutputStream} for performance reasons if it is not a BufferedOutputStream itself.
     */
    public ZipCollector(OutputStream outputStream) {
        Objects.requireNonNull(outputStream);

        if (outputStream instanceof BufferedOutputStream) {
            this.outputStream = new ZipOutputStream(outputStream);
        } else {
            this.outputStream = new ZipOutputStream(new BufferedOutputStream(outputStream));
        }
    }

    /**
     * Adds a new entry to the Zip.
     *
     * @param path        the path of the new entry
     * @param inputStream the content of the new entry
     */
    public void add(String path, InputStream inputStream) {
        Objects.requireNonNull(path);
        Objects.requireNonNull(inputStream);

        try {
            outputStream.putNextEntry(new ZipEntry(path));
            final byte[] bytes = IOUtils.toByteArray(inputStream);
            outputStream.write(bytes);
            outputStream.closeEntry();
        } catch (IOException e) {
            throw new RuntimeException("failed to add zip entry with path " + path);
        }
    }

    @Override
    public void close() throws Exception {
        outputStream.close();
    }
}
