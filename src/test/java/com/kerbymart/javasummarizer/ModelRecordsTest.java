package com.kerbymart.javasummarizer;

import org.junit.jupiter.api.Test;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ModelRecordsTest {

    @Test
    void records_makeDefensiveCopiesOfLists() {
        List<MethodSummary> methods = new ArrayList<>();
        methods.add(new MethodSummary("void a();", List.of(), List.of()));

        TypeSummary typeSummary = new TypeSummary("p", "T", List.of(), List.of(), methods);
        methods.add(new MethodSummary("void b();", List.of(), List.of()));

        assertEquals(1, typeSummary.methods().size());
        assertEquals("void a();", typeSummary.methods().getFirst().signature());
    }

    @Test
    void constructors_rejectNulls() {
        assertThrows(NullPointerException.class, () -> new MethodSummary(null, List.of(), List.of()));
        assertThrows(NullPointerException.class, () -> new MethodSummary("x", null, List.of()));
        assertThrows(NullPointerException.class, () -> new MethodSummary("x", List.of(), null));

        assertThrows(NullPointerException.class, () -> new TypeSummary(null, "T", List.of(), List.of(), List.of()));
        assertThrows(NullPointerException.class, () -> new TypeSummary("p", null, List.of(), List.of(), List.of()));
        assertThrows(NullPointerException.class, () -> new TypeSummary("p", "T", null, List.of(), List.of()));
        assertThrows(NullPointerException.class, () -> new TypeSummary("p", "T", List.of(), null, List.of()));
        assertThrows(NullPointerException.class, () -> new TypeSummary("p", "T", List.of(), List.of(), null));

        assertThrows(NullPointerException.class, () -> new JavaFileSummary(null, "p", List.of()));
        assertThrows(NullPointerException.class, () -> new JavaFileSummary(Path.of("x"), null, List.of()));
        assertThrows(NullPointerException.class, () -> new JavaFileSummary(Path.of("x"), "p", null));
    }
}

