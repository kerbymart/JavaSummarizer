package com.kerbymart.javasummarizer;

import org.junit.jupiter.api.Test;

import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class SummaryFormatterTest {

    @Test
    void format_rendersAStableHumanReadableShape() {
        JavaFileSummary summary = new JavaFileSummary(
                Path.of("Example.java"),
                "p",
                List.of(new TypeSummary(
                        "p",
                        "Example",
                        List.of("Base"),
                        List.of("Runnable"),
                        List.of(new MethodSummary(
                                "void run();",
                                List.of("println"),
                                List.of("Thread")
                        ))
                ))
        );

        String text = new SummaryFormatter().format(summary);

        assertTrue(text.contains("Package: p"));
        assertTrue(text.contains("Class: Example"));
        assertTrue(text.contains("  extends: Base"));
        assertTrue(text.contains("  implements: Runnable"));
        assertTrue(text.contains("  Method: void run();"));
        assertTrue(text.contains("    calls method: println"));
        assertTrue(text.contains("    creates instance: Thread"));
    }
}

