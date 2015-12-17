package xyz.its_me;

import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * Abstract implementation of fetching files from a hierarchical storage. It does not assume any details of the storage.
 *
 * @param <T> type of a file or folder
 */
public class AbstractFileFetcher<T> {
    private final Predicate<T> isFolder;
    private final Function<T, String> getName;
    private final Function<T, List<T>> getChildren;

    /**
     * A concrete implementation must provide the functions for iterating the storage.
     *
     * @param isFolder    is the current item a folder?
     * @param getName     the name of the current item
     * @param getChildren the list of children if the item is a folder
     */
    protected AbstractFileFetcher(
            Predicate<T> isFolder, Function<T, String> getName, Function<T, List<T>> getChildren) {
        this.isFolder = Objects.requireNonNull(isFolder);
        this.getName = Objects.requireNonNull(getName);
        this.getChildren = Objects.requireNonNull(getChildren);
    }

    /**
     * Starts iterating at the rootFolder and returns a map of path names and file items. Folder items are not
     * returned.
     *
     * @param rootFolder must be a folder
     * @return key: path names that are slash '/' separated, value: file item
     */
    public Map<String, T> iterate(T rootFolder) {
        Objects.requireNonNull(rootFolder);
        final String name = getName.apply(rootFolder);
        if (!isFolder.test(rootFolder)) {
            throw new RuntimeException(name + " is not a folder");
        }

        final Map<String, T> files = new TreeMap<>();
        addTo(files, new StringJoiner("/"), rootFolder);
        return files;
    }

    private void addTo(Map<String, T> files, StringJoiner folderNames, T item) {
        final StringJoiner allNames = new StringJoiner("/");
        allNames.merge(folderNames);
        allNames.add(getName.apply(item));
        if (isFolder.test(item)) {
            getChildren.apply(item).forEach(child -> addTo(files, allNames, child));
        } else {
            files.put(allNames.toString(), item);
        }
    }
}
