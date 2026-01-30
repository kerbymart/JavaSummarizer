package com.kerbymart.javasummarizer;

import java.util.List;
import java.util.Objects;

/**
 * Summary of a method's outward-facing signature and a few "what happens inside" hints.
 *
 * <p>We track call names and created types as a lightweight way to answer: "what does this method
 * touch?" without attempting full symbol resolution.
 */
public record MethodSummary(String signature, List<String> methodCalls, List<String> objectCreations) {
    public MethodSummary {
        Objects.requireNonNull(signature, "signature");
        methodCalls = List.copyOf(Objects.requireNonNull(methodCalls, "methodCalls"));
        objectCreations = List.copyOf(Objects.requireNonNull(objectCreations, "objectCreations"));
    }
}
