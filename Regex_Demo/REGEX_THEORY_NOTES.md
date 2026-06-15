# Java Regular Expressions (Regex) — Complete Theory Notes

---

## Table of Contents

1. [Introduction to Regex](#1-introduction-to-regex)
2. [Core Classes — Pattern & Matcher](#2-core-classes--pattern--matcher)
3. [Literal Characters and Metacharacters](#3-literal-characters-and-metacharacters)
4. [Character Classes](#4-character-classes)
5. [Predefined Character Classes (Shorthand)](#5-predefined-character-classes-shorthand)
6. [Quantifiers](#6-quantifiers)
7. [Anchors and Boundaries](#7-anchors-and-boundaries)
8. [Groups and Capturing](#8-groups-and-capturing)
9. [Backreferences](#9-backreferences)
10. [Alternation](#10-alternation)
11. [Lookahead and Lookbehind](#11-lookahead-and-lookbehind)
12. [Flags / Pattern Options](#12-flags--pattern-options)
13. [String Methods That Use Regex](#13-string-methods-that-use-regex)
14. [Common Real-World Patterns](#14-common-real-world-patterns)
15. [Greedy vs Reluctant vs Possessive Quantifiers](#15-greedy-vs-reluctant-vs-possessive-quantifiers)
16. [Performance Tips and Best Practices](#16-performance-tips-and-best-practices)
17. [Quick Reference Cheat Sheet](#17-quick-reference-cheat-sheet)

---

## 1. Introduction to Regex

A **regular expression** (regex / regexp) is a sequence of characters that defines a **search pattern**.

### What can you do with regex?
| Task | Example |
|------|---------|
| Validate input | Is this a valid email address? |
| Search text | Find all dates in a document |
| Extract data | Extract all phone numbers from a string |
| Replace text | Replace all whitespace with a single space |
| Split strings | Split a CSV line by commas |

### Regex in Java
Java's regex support is provided by the `java.util.regex` package, which contains:
- `Pattern` — compiled representation of a regex
- `Matcher` — engine that performs matching against an input string
- `PatternSyntaxException` — thrown when a regex is invalid

---

## 2. Core Classes — Pattern & Matcher

### `Pattern` class
```java
import java.util.regex.Pattern;
import java.util.regex.Matcher;

// Step 1: Compile the regex into a Pattern
Pattern pattern = Pattern.compile("hello");

// Step 2: Create a Matcher for the input
Matcher matcher = pattern.matcher("say hello to the world");

// Step 3: Use Matcher methods
boolean found = matcher.find();    // true — found "hello" somewhere
boolean full  = matcher.matches(); // false — input isn't ONLY "hello"
```

> **Note:** Always use `Pattern.compile()` and reuse the `Pattern` object for performance.  
> Never write `Pattern.compile(regex).matcher(input)` inside a loop — recompiling is expensive.

### Key `Matcher` Methods

| Method | Description |
|--------|-------------|
| `find()` | Finds the next match; returns `true` if found |
| `find(int start)` | Starts searching from index `start` |
| `matches()` | Returns `true` only if the ENTIRE input matches the pattern |
| `lookingAt()` | Returns `true` if the pattern matches from the BEGINNING (not necessarily the whole string) |
| `group()` | Returns the text matched by the last `find()`/`matches()` call |
| `group(int n)` | Returns text of capturing group `n` (1-based) |
| `group(String name)` | Returns text of a named capturing group |
| `start()` | Start index of last match |
| `end()` | End index (exclusive) of last match |
| `reset()` | Resets matcher to start of input |
| `replaceAll(String)` | Replace all matches with replacement string |
| `replaceFirst(String)` | Replace only the first match |

### `Pattern` Static Methods

| Method | Description |
|--------|-------------|
| `Pattern.compile(regex)` | Compile a regex string into a Pattern |
| `Pattern.compile(regex, flags)` | Compile with flags (e.g., `Pattern.CASE_INSENSITIVE`) |
| `Pattern.matches(regex, input)` | One-shot full-match check (compiles each time — avoid in loops) |
| `Pattern.quote(s)` | Returns a literal pattern for string `s` (escapes metacharacters) |

---

## 3. Literal Characters and Metacharacters

### Literal Characters
Most characters in a regex match themselves literally.
```
regex: cat
input: "the cat sat" → matches "cat"
```

### Metacharacters
These characters have **special meaning** in regex and must be **escaped** with `\` to match literally.

```
. ^ $ * + ? { } [ ] \ | ( )
```

| Metachar | Meaning |
|----------|---------|
| `.` | Any character except newline (unless DOTALL flag) |
| `^` | Start of line / string |
| `$` | End of line / string |
| `*` | Zero or more |
| `+` | One or more |
| `?` | Zero or one |
| `{n,m}` | Between n and m times |
| `[...]` | Character class |
| `\|` | Alternation (OR) |
| `(...)` | Grouping / capturing |
| `\` | Escape next character |

### Escaping in Java Strings
Because Java strings use `\` as an escape character, you must **double** the backslash in regex:

| Regex | Java String |
|-------|------------|
| `\d` | `"\\d"` |
| `\n` | `"\\n"` |
| `\.` | `"\\."` |
| `\\` | `"\\\\"` |

---

## 4. Character Classes

A **character class** `[...]` matches any **one character** from a set.

| Pattern | Matches |
|---------|---------|
| `[abc]` | a, b, or c |
| `[^abc]` | Any character EXCEPT a, b, c |
| `[a-z]` | Any lowercase letter |
| `[A-Z]` | Any uppercase letter |
| `[0-9]` | Any digit |
| `[a-zA-Z]` | Any letter (upper or lower) |
| `[a-zA-Z0-9]` | Any letter or digit |
| `[a-z&&[^aeiou]]` | Consonants (intersection) |

### Examples
```java
Pattern.compile("[aeiou]")  // matches any vowel
Pattern.compile("[^0-9]")   // matches any non-digit
Pattern.compile("[a-fA-F0-9]") // hex digit
```

---

## 5. Predefined Character Classes (Shorthand)

| Shorthand | Equivalent | Meaning |
|-----------|-----------|---------|
| `\d` | `[0-9]` | Digit |
| `\D` | `[^0-9]` | Non-digit |
| `\w` | `[a-zA-Z0-9_]` | Word character |
| `\W` | `[^a-zA-Z0-9_]` | Non-word character |
| `\s` | `[ \t\n\r\f]` | Whitespace |
| `\S` | `[^ \t\n\r\f]` | Non-whitespace |
| `.` | (any except `\n`) | Any character |

> In Java string literals, write these as `"\\d"`, `"\\w"`, `"\\s"`, etc.

---

## 6. Quantifiers

Quantifiers specify **how many times** the preceding element must appear.

| Quantifier | Meaning |
|-----------|---------|
| `*` | 0 or more |
| `+` | 1 or more |
| `?` | 0 or 1 (optional) |
| `{n}` | Exactly n times |
| `{n,}` | At least n times |
| `{n,m}` | Between n and m times (inclusive) |

### Examples
```
\d{3}       → exactly 3 digits         "123"
\d{2,4}     → 2 to 4 digits            "12", "123", "1234"
[a-z]+      → one or more lowercase    "hello"
colou?r     → "color" or "colour"
\s*         → zero or more whitespace
```

---

## 7. Anchors and Boundaries

Anchors match a **position** in the input, not a character.

| Anchor | Meaning |
|--------|---------|
| `^` | Start of input (or line in MULTILINE mode) |
| `$` | End of input (or line in MULTILINE mode) |
| `\b` | Word boundary (between `\w` and `\W`) |
| `\B` | Non-word boundary |
| `\A` | Absolute start of input (ignores MULTILINE) |
| `\Z` | Absolute end of input (ignores MULTILINE) |

### Examples
```
^\d{5}$           → entire string is exactly 5 digits (zip code)
\bword\b          → "word" as a whole word (not "sword" or "words")
^Hello            → string starts with "Hello"
\.java$           → string ends with ".java"
```

---

## 8. Groups and Capturing

### Capturing Groups `(...)`
Parentheses create a **capturing group** — the matched text can be retrieved later.

Groups are numbered **left-to-right** by their opening `(`, starting at **1**.  
Group **0** always refers to the entire match.

```java
Pattern p = Pattern.compile("(\\d{4})-(\\d{2})-(\\d{2})");
Matcher m = p.matcher("Date: 2024-06-15");
if (m.find()) {
    System.out.println(m.group(0)); // "2024-06-15" — entire match
    System.out.println(m.group(1)); // "2024"        — year
    System.out.println(m.group(2)); // "06"          — month
    System.out.println(m.group(3)); // "15"          — day
}
```

### Named Capturing Groups `(?<name>...)`
Assign a name to a group for more readable code.

```java
Pattern p = Pattern.compile("(?<year>\\d{4})-(?<month>\\d{2})-(?<day>\\d{2})");
Matcher m = p.matcher("2024-06-15");
if (m.find()) {
    System.out.println(m.group("year"));  // "2024"
    System.out.println(m.group("month")); // "06"
    System.out.println(m.group("day"));   // "15"
}
```

### Non-Capturing Groups `(?:...)`
Groups for **structure/alternation** only — does not create a numbered group.

```java
// (?:Mr|Ms|Dr)\.? — groups the prefix but doesn't capture it
Pattern.compile("(?:Mr|Ms|Dr)\\.?\\s+(\\w+)");
```

---

## 9. Backreferences

A **backreference** refers back to a previously captured group within the same pattern.

- `\1` — refers to group 1
- `\2` — refers to group 2
- `\k<name>` — refers to named group

```java
// Match doubled words: "the the", "is is"
Pattern p = Pattern.compile("\\b(\\w+)\\s+\\1\\b");
Matcher m = p.matcher("this is is a test the the end");
while (m.find()) {
    System.out.println("Doubled word: " + m.group(1));
    // Output: "is", "the"
}
```

---

## 10. Alternation

The `|` operator means **OR** — matches the left side OR the right side.

```
cat|dog         → matches "cat" or "dog"
gr(a|e)y        → matches "gray" or "grey"
(jpg|png|gif)   → matches image extensions
```

```java
Pattern.compile("\\b(cat|dog|bird)\\b") // whole-word pet names
```

> **Tip:** Alternation is short-circuit — the engine tries the left option first.  
> Order matters for performance but not correctness (both sides will be tried).

---

## 11. Lookahead and Lookbehind

**Lookarounds** are **zero-width** assertions — they match a position, not characters.  
The matched text is NOT consumed and NOT included in the result.

| Type | Syntax | Meaning |
|------|--------|---------|
| Positive lookahead | `(?=...)` | Must be followed by `...` |
| Negative lookahead | `(?!...)` | Must NOT be followed by `...` |
| Positive lookbehind | `(?<=...)` | Must be preceded by `...` |
| Negative lookbehind | `(?<!...)` | Must NOT be preceded by `...` |

### Examples
```java
// Find digits followed by "px"
Pattern.compile("\\d+(?=px)")
// Input: "12px 30em 45px" → matches "12", "45"

// Find digits NOT followed by "px"
Pattern.compile("\\d+(?!px)")
// Input: "12px 30em" → matches "30" (the "12" is followed by "px" so skipped)

// Find price amounts (numbers preceded by "$")
Pattern.compile("(?<=\\$)\\d+\\.\\d{2}")
// Input: "$19.99 €5.00" → matches "19.99"

// Find words not preceded by "no "
Pattern.compile("(?<!no )\\bentry\\b")
```

---

## 12. Flags / Pattern Options

Flags modify how the pattern is matched. Pass them as a second argument to `Pattern.compile()`.

| Flag | Constant | Inline | Meaning |
|------|---------|--------|---------|
| Case-insensitive | `Pattern.CASE_INSENSITIVE` | `(?i)` | Ignores letter case |
| Multiline | `Pattern.MULTILINE` | `(?m)` | `^` and `$` match start/end of EACH LINE |
| Dotall | `Pattern.DOTALL` | `(?s)` | `.` matches newline too |
| Comments | `Pattern.COMMENTS` | `(?x)` | Whitespace and `#` comments ignored in pattern |
| Unicode | `Pattern.UNICODE_CASE` | `(?u)` | Unicode-aware case folding |
| Literal | `Pattern.LITERAL` | — | All metacharacters treated as literals |

### Examples
```java
// Case-insensitive
Pattern.compile("hello", Pattern.CASE_INSENSITIVE)
// Matches "hello", "Hello", "HELLO"

// Multiline: ^ and $ match each line
Pattern.compile("^\\d+", Pattern.MULTILINE)
// Input: "123\nabc\n456" → matches "123" and "456"

// Dotall: . matches newlines
Pattern.compile("start.*end", Pattern.DOTALL)
// Input: "start\nsome\nend" → matches the whole string

// Inline flags inside the pattern
Pattern.compile("(?i)hello")  // case-insensitive, inline
Pattern.compile("(?im)^\\d+") // multiline + case-insensitive, inline

// Multiple flags using bitwise OR
Pattern.compile("hello", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE)
```

---

## 13. String Methods That Use Regex

`String` has several methods that accept regex patterns directly.

| Method | Description |
|--------|-------------|
| `str.matches(regex)` | Returns `true` if ENTIRE string matches pattern |
| `str.replaceAll(regex, replacement)` | Replace all matches |
| `str.replaceFirst(regex, replacement)` | Replace first match only |
| `str.split(regex)` | Split string around matches |
| `str.split(regex, limit)` | Split with a limit on number of parts |

### Replacement with Groups
In `replaceAll()`, you can reference captured groups in the replacement:
- `$1`, `$2` — group 1, group 2
- `${name}` — named group

```java
// Reformat date from MM/DD/YYYY to YYYY-MM-DD
String result = "06/15/2024".replaceAll(
    "(\\d{2})/(\\d{2})/(\\d{4})",
    "$3-$1-$2"
);
// result = "2024-06-15"

// Wrap every word in brackets
String s = "hello world".replaceAll("(\\w+)", "[$1]");
// s = "[hello] [world]"

// Split on one or more whitespace characters
String[] parts = "one  two   three".split("\\s+");
// ["one", "two", "three"]
```

---

## 14. Common Real-World Patterns

| Use Case | Pattern |
|----------|---------|
| Email (basic) | `^[a-zA-Z0-9._%+\-]+@[a-zA-Z0-9.\-]+\.[a-zA-Z]{2,}$` |
| Phone (US, flexible) | `^\+?1?\s*[\-.]?\(?\d{3}\)?[\s.\-]?\d{3}[\s.\-]?\d{4}$` |
| URL (basic) | `https?://[^\s/$.?#].[^\s]*` |
| IPv4 address | `^((25[0-5]\|2[0-4]\d\|[01]?\d\d?)\.){3}(25[0-5]\|2[0-4]\d\|[01]?\d\d?)$` |
| ZIP code (US) | `^\d{5}(-\d{4})?$` |
| Date (YYYY-MM-DD) | `^\d{4}-(0[1-9]\|1[0-2])-(0[1-9]\|[12]\d\|3[01])$` |
| Time (HH:MM) | `^([01]\d\|2[0-3]):[0-5]\d$` |
| Hex color | `^#([A-Fa-f0-9]{6}\|[A-Fa-f0-9]{3})$` |
| Username | `^[a-zA-Z0-9_]{3,20}$` |
| Password (strong) | `^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[@$!%*?&])[A-Za-z\d@$!%*?&]{8,}$` |
| Integer | `^-?\d+$` |
| Decimal number | `^-?\d+(\.\d+)?$` |
| HTML tag | `<([a-zA-Z][a-zA-Z0-9]*)\b[^>]*>.*?</\1>` |
| Whitespace cleanup | `\s+` → replace with `" "` |

---

## 15. Greedy vs Reluctant vs Possessive Quantifiers

By default, quantifiers are **greedy** — they match **as much as possible**.

| Type | Syntax | Behaviour |
|------|--------|-----------|
| Greedy | `*` `+` `?` `{n,m}` | Match as MUCH as possible |
| Reluctant (lazy) | `*?` `+?` `??` `{n,m}?` | Match as LITTLE as possible |
| Possessive | `*+` `++` `?+` `{n,m}+` | Match as much as possible; NEVER backtrack |

### Example — Greedy vs Lazy
```java
String input = "<b>bold</b> and <i>italic</i>";

// GREEDY — .* matches as much as possible
Pattern.compile("<.*>").matcher(input).find();
// Matches: "<b>bold</b> and <i>italic</i>"  (entire thing)

// RELUCTANT — .*? matches as little as possible
Pattern.compile("<.*?>").matcher(input).find();
// Matches: "<b>"  (stops at first >)
```

---

## 16. Performance Tips and Best Practices

| # | Practice | Why |
|---|----------|-----|
| 1 | Compile `Pattern` **once**; reuse it | Compiling is expensive; `Matcher` is cheap |
| 2 | Use `find()` instead of `matches()` for partial search | `matches()` forces anchoring the entire input |
| 3 | Prefer non-capturing groups `(?:...)` when you don't need the group | Fewer groups = less memory |
| 4 | Avoid catastrophic backtracking | Nested quantifiers like `(a+)+` on non-matching input can hang |
| 5 | Use `\\Q...\\E` or `Pattern.quote()` to escape user input | Prevents injection of metacharacters |
| 6 | Use specific character classes over `.` | `[0-9]` is faster than `.*` for digits |
| 7 | Use word boundaries `\b` for whole-word matching | Avoids false positives in substrings |
| 8 | Test regex on edge cases | Empty string, very long input, special characters |

---

## 17. Quick Reference Cheat Sheet

### Characters
| Symbol | Meaning |
|--------|---------|
| `.` | Any char (except newline) |
| `\d` | Digit `[0-9]` |
| `\D` | Non-digit |
| `\w` | Word char `[a-zA-Z0-9_]` |
| `\W` | Non-word char |
| `\s` | Whitespace |
| `\S` | Non-whitespace |
| `\n` | Newline |
| `\t` | Tab |

### Quantifiers
| Symbol | Meaning |
|--------|---------|
| `*` | 0 or more (greedy) |
| `+` | 1 or more (greedy) |
| `?` | 0 or 1 (greedy) |
| `*?` | 0 or more (lazy) |
| `+?` | 1 or more (lazy) |
| `{n}` | Exactly n |
| `{n,m}` | n to m |

### Anchors
| Symbol | Meaning |
|--------|---------|
| `^` | Start of line |
| `$` | End of line |
| `\b` | Word boundary |
| `\A` | Start of input |
| `\Z` | End of input |

### Groups
| Syntax | Meaning |
|--------|---------|
| `(abc)` | Capturing group |
| `(?:abc)` | Non-capturing group |
| `(?<name>abc)` | Named capturing group |
| `\1` | Backreference to group 1 |
| `\k<name>` | Backreference to named group |

### Lookarounds
| Syntax | Meaning |
|--------|---------|
| `(?=abc)` | Positive lookahead |
| `(?!abc)` | Negative lookahead |
| `(?<=abc)` | Positive lookbehind |
| `(?<!abc)` | Negative lookbehind |

---

## Code Files in This Demo

| File | Concept Demonstrated |
|------|--------------------|
| `_01_PatternAndMatcherBasics.java` | Pattern.compile(), Matcher.find(), matches(), group(), start(), end() |
| `_02_CharacterClassesAndQuantifiers.java` | [], predefined classes (\d \w \s), quantifiers (*, +, ?, {n,m}) |
| `_03_AnchorsAndBoundaries.java` | ^, $, \b, \A, \Z, multiline anchors |
| `_04_GroupsAndCapturing.java` | Capturing groups, named groups, non-capturing groups, backreferences |
| `_05_LookaroundsAndAlternation.java` | Lookahead, lookbehind, alternation (|), greedy vs lazy quantifiers |
| `_06_FlagsDemo.java` | CASE_INSENSITIVE, MULTILINE, DOTALL, COMMENTS, inline flags |
| `_07_StringMethodsWithRegex.java` | matches(), replaceAll(), replaceFirst(), split(), group references in replacement |
| `_08_CommonPatternsDemo.java` | Email, phone, URL, date, IP, password validation; find-all pattern |

