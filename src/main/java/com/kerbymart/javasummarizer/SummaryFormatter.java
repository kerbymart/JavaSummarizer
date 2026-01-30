package com.kerbymart.javasummarizer;

import java.util.StringJoiner;

/**
 * Renders summaries into a human-readable, grep-friendly console format.
 *
 * <p>This formatter intentionally produces plain text without color or control codes to keep it
 * composable in shell pipelines.
 */
public final class SummaryFormatter {

    /**
     * Converts a file summary into a printable block of text.
     *
     * <p>The format is stable on purpose: users often copy-paste or diff summaries when
     * investigating changes across revisions.
     */
    public String format(JavaFileSummary summary) {
        StringBuilder builder = new StringBuilder();
        for (TypeSummary type : summary.types()) {
            builder.append("Package: ").append(type.packageName()).append('\n');
            builder.append("Class: ").append(type.typeName()).append('\n');

            for (String parent : type.extendedTypes()) {
                builder.append("  extends: ").append(parent).append('\n');
            }
            for (String iface : type.implementedTypes()) {
                builder.append("  implements: ").append(iface).append('\n');
            }

            for (MethodSummary method : type.methods()) {
                builder.append("  Method: ").append(method.signature()).append('\n');

                for (String call : method.methodCalls()) {
                    builder.append("    calls method: ").append(call).append('\n');
                }
                for (String creation : method.objectCreations()) {
                    builder.append("    creates instance: ").append(creation).append('\n');
                }
            }

            builder.append('\n');
        }

        return builder.toString();
    }
}
