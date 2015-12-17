package xyz.its_me;

import java.io.File;
import java.util.Arrays;
import java.util.List;

public class FilesystemFetcher extends AbstractFileFetcher<File> {
    public FilesystemFetcher() {
        super(File::isDirectory, File::getName, FilesystemFetcher::getChildren);
    }

    private static List<File> getChildren(File file) {
        final File[] children = file.listFiles();
        if (children == null) {
            throw new RuntimeException("error getting children of " + file.getName());
        }
        return Arrays.asList(children);
    }
}
