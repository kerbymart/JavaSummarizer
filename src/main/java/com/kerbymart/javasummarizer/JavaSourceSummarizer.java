package com.kerbymart.javasummarizer;

import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.expr.MethodCallExpr;
import com.github.javaparser.ast.expr.ObjectCreationExpr;
import com.github.javaparser.ast.type.ClassOrInterfaceType;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Turns a single Java source file into a structured summary.
 *
 * <p>The goal is to build a small model that can be formatted in different ways (console today,
 * JSON later) without entangling parsing with presentation.
 */
public final class JavaSourceSummarizer {

    /**
     * Parses a Java file and extracts a summary of its declared types and their methods.
     *
     * @param javaFile path to a {@code .java} file
     * @return summary model of the file's contents
     * @throws IOException when the file cannot be read
     */
    public JavaFileSummary summarize(Path javaFile) throws IOException {
        Objects.requireNonNull(javaFile, "javaFile");

        CompilationUnit compilationUnit = StaticJavaParser.parse(javaFile);
        String packageName =
                compilationUnit.getPackageDeclaration().map(pd -> pd.getName().toString()).orElse(JavaFileSummary.DEFAULT_PACKAGE);

        List<TypeSummary> types = new ArrayList<>();
        for (ClassOrInterfaceDeclaration type : compilationUnit.findAll(ClassOrInterfaceDeclaration.class)) {
            types.add(summarizeType(type, packageName));
        }

        return new JavaFileSummary(javaFile, packageName, types);
    }

    private TypeSummary summarizeType(ClassOrInterfaceDeclaration type, String packageName) {
        List<String> extendedTypes = new ArrayList<>();
        for (ClassOrInterfaceType extendedType : type.getExtendedTypes()) {
            extendedTypes.add(extendedType.getNameAsString());
        }

        List<String> implementedTypes = new ArrayList<>();
        for (ClassOrInterfaceType implementedType : type.getImplementedTypes()) {
            implementedTypes.add(implementedType.getNameAsString());
        }

        List<MethodSummary> methods = type.getMethods().stream().map(method -> {
            String signature = method.getDeclarationAsString(true, false, true) + ";";
            List<String> calls = method.findAll(MethodCallExpr.class).stream().map(MethodCallExpr::getNameAsString).toList();
            List<String> creations =
                    method.findAll(ObjectCreationExpr.class).stream().map(creation -> creation.getType().asString()).toList();
            return new MethodSummary(signature, calls, creations);
        }).toList();

        return new TypeSummary(packageName, type.getNameAsString(), extendedTypes, implementedTypes, methods);
    }
}
