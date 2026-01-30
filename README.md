# Java Summarizer

Command-line tool that scans a directory of Java source files and prints a structural summary (packages, types, inheritance, method signatures, method-call names, and object creations).

## Features

- Recursively finds `*.java` files
- Prints `extends` / `implements` relationships
- Lists method signatures per type
- Shows method-call names and `new` type creations (lightweight, name-based)

## Install

```bash
git clone https://github.com/kerbymart/JavaSummarizer.git
cd JavaSummarizer
mvn package
```

## Usage

Scan the current directory:

```bash
java -jar target/java-summarizer-1.0-SNAPSHOT.jar .
```

Scan a specific project directory:

```bash
java -jar target/java-summarizer-1.0-SNAPSHOT.jar /path/to/project
```

## Example output

```
Package: a.b
Class: Foo
  extends: Bar
  implements: Baz
  Method: void m();
    calls method: helper
    creates instance: String
```

## Development

Run tests:

```bash
mvn test
```
