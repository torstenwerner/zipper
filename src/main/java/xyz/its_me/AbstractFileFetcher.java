package xyz.its_me;

import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;

public class AbstractFileFetcher<T> {
    private final Predicate<T> isFolder;
    private final Function<T, String> getName;
    private final Function<T, List<T>> getChildren;

    protected AbstractFileFetcher(
            Predicate<T> isFolder, Function<T, String> getName, Function<T, List<T>> getChildren) {
        this.isFolder = Objects.requireNonNull(isFolder);
        this.getName = Objects.requireNonNull(getName);
        this.getChildren = Objects.requireNonNull(getChildren);
    }

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
