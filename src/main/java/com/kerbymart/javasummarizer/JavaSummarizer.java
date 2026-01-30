package com.kerbymart.javasummarizer;

import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;
import java.util.stream.Stream;

/**
 * Orchestrates the summarization workflow without owning parsing or formatting details.
 *
 * <p>This class coordinates three responsibilities that are easy to test independently:
 *
 * <ul>
 *   <li>finding Java source files ({@link JavaProjectScanner})
 *   <li>extracting a summary from each file ({@link JavaSourceSummarizer})
 *   <li>presenting that summary ({@link SummaryFormatter})
 * </ul>
 *
 * <p>The main intent is to keep I/O boundaries explicit and make failures local to a file, so a
 * single bad source file does not stop a whole-project scan.
 */
public final class JavaSummarizer {
    private final JavaProjectScanner scanner;
    private final JavaSourceSummarizer sourceSummarizer;
    private final SummaryFormatter formatter;

    /**
     * Creates a summarizer with injectable collaborators for testability.
     *
     * @param scanner finds {@code .java} files
     * @param sourceSummarizer parses and extracts a summary model
     * @param formatter renders a model into text
     */
    public JavaSummarizer(
            JavaProjectScanner scanner,
            JavaSourceSummarizer sourceSummarizer,
            SummaryFormatter formatter
    ) {
        this.scanner = Objects.requireNonNull(scanner, "scanner");
        this.sourceSummarizer = Objects.requireNonNull(sourceSummarizer, "sourceSummarizer");
        this.formatter = Objects.requireNonNull(formatter, "formatter");
    }

    /**
     * Provides the default, production-wired summarizer used by the CLI.
     *
     * <p>Keeping wiring in one place makes it obvious which parts are swappable and avoids hiding
     * object creation inside unrelated classes.
     */
    public static JavaSummarizer defaultSummarizer() {
        return new JavaSummarizer(new JavaProjectScanner(), new JavaSourceSummarizer(), new SummaryFormatter());
    }

    /**
     * Summarizes a project path.
     *
     * <p>If the path is a file, it is summarized directly. If it is a directory, it is scanned
     * recursively for {@code .java} files.
     *
     * @param inputPath a directory or a single {@code .java} file
     * @param out human-facing summary output
     * @param err human-facing error output; used for per-file parse failures
     * @throws IOException when directory scanning fails
     */
    public void summarize(Path inputPath, PrintStream out, PrintStream err) throws IOException {
        Objects.requireNonNull(inputPath, "inputPath");
        Objects.requireNonNull(out, "out");
        Objects.requireNonNull(err, "err");

        if (!Files.exists(inputPath)) {
            err.println("Path does not exist: " + inputPath);
            return;
        }

        if (Files.isRegularFile(inputPath)) {
            summarizeFile(inputPath, out, err);
            return;
        }

        try (Stream<Path> javaFiles = scanner.findJavaFiles(inputPath)) {
            javaFiles.forEach(path -> summarizeFile(path, out, err));
        }
    }

    private void summarizeFile(Path file, PrintStream out, PrintStream err) {
        try {
            JavaFileSummary summary = sourceSummarizer.summarize(file);
            out.print(formatter.format(summary));
        } catch (Exception e) {
            err.println("Error summarizing " + file + ": " + e.getMessage());
        }
    }
}
