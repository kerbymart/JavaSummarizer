package com.kerbymart.javasummarizer;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;
import java.util.stream.Stream;

/**
 * Locates Java source files under a root directory.
 *
 * <p>The scanner knows nothing about parsing. Its job is to decide "which files should be offered"
 * to the rest of the pipeline.
 */
public final class JavaProjectScanner {

    /**
     * Recursively finds {@code .java} files under the given root.
     *
     * <p>The returned stream must be closed by the caller (typically via try-with-resources).
     *
     * @param root directory to scan
     * @return a lazy stream of Java source file paths
     * @throws IOException when walking the directory tree fails
     */
    public Stream<Path> findJavaFiles(Path root) throws IOException {
        Objects.requireNonNull(root, "root");
        return Files.walk(root).filter(this::isJavaSourceFile);
    }

    /**
     * Identifies Java source files by extension and regular-file status.
     *
     * <p>This method is intentionally small and deterministic so tests can express "what counts as a
     * Java file" without needing a full directory walk.
     */
    public boolean isJavaSourceFile(Path path) {
        return Files.isRegularFile(path) && path.toString().endsWith(".java");
    }
}
