package xyz.its_me;

import java.io.*;

/**
 * Zips the src folder into the file build/src.zip using the classes {@link FilesystemFetcher} and {@link ZipCollector}.
 */
public class ZipperApplication {
    public static void main(String[] args) {
        zip("src", "build/src.zip");
    }

    private static void zip(String rootFolder, String target) {
        try (final ZipCollector collector = new ZipCollector(new FileOutputStream(target))) {
            new FilesystemFetcher().iterate(new File(rootFolder))
                    .forEach((path, file) -> collector.add(path, file2Stream(file)));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static InputStream file2Stream(File file) {
        try {
            return new FileInputStream(file);
        } catch (FileNotFoundException e) {
            throw new RuntimeException("failed to read file " + file.getName());
        }
    }
}
