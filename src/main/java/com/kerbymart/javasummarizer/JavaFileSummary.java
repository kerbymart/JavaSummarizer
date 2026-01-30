package com.kerbymart.javasummarizer;

import java.nio.file.Path;
import java.util.List;
import java.util.Objects;

/**
 * Summary of one Java source file.
 *
 * <p>This is the "data we know" after parsing, with no formatting decisions baked in.
 *
 * @param file path of the summarized file
 * @param packageName package declared in the file (or {@link #DEFAULT_PACKAGE})
 * @param types type summaries discovered in the file (includes nested types)
 */
public record JavaFileSummary(Path file, String packageName, List<TypeSummary> types) {
    public static final String DEFAULT_PACKAGE = "<default>";

    public JavaFileSummary {
        Objects.requireNonNull(file, "file");
        Objects.requireNonNull(packageName, "packageName");
        types = List.copyOf(Objects.requireNonNull(types, "types"));
    }
}
