package com.kerbymart.javasummarizer;

import java.util.List;
import java.util.Objects;

/**
 * Summary of a single class or interface declaration.
 *
 * <p>We keep only the pieces that are stable for users reading the output: name, inheritance
 * relationships, and method-level behavior hints.
 */
public record TypeSummary(
        String packageName,
        String typeName,
        List<String> extendedTypes,
        List<String> implementedTypes,
        List<MethodSummary> methods
) {
    public TypeSummary {
        Objects.requireNonNull(packageName, "packageName");
        Objects.requireNonNull(typeName, "typeName");
        extendedTypes = List.copyOf(Objects.requireNonNull(extendedTypes, "extendedTypes"));
        implementedTypes = List.copyOf(Objects.requireNonNull(implementedTypes, "implementedTypes"));
        methods = List.copyOf(Objects.requireNonNull(methods, "methods"));
    }
}
