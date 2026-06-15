package regex;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

// ============================================================
// TOPIC: Flags / Pattern Options
// ============================================================
// Flags modify how a pattern is applied to input.
// They are passed as a second argument to Pattern.compile(),
// OR embedded inline using (?flag) at the start of the pattern.
//
// Common flags:
//   Pattern.CASE_INSENSITIVE  (?i)  → ignore case
//   Pattern.MULTILINE         (?m)  → ^ and $ match per-line
//   Pattern.DOTALL            (?s)  → dot . matches newline too
//   Pattern.COMMENTS          (?x)  → allow whitespace and # comments in pattern
//   Pattern.LITERAL                 → treat entire pattern as literal text
//   Pattern.UNICODE_CASE      (?u)  → Unicode-aware case folding
//
// Multiple flags: use bitwise OR → Pattern.CASE_INSENSITIVE | Pattern.MULTILINE
// Inline:         (?im) applies both case-insensitive and multiline
// ============================================================

public class _06_FlagsDemo {

    static void findAll(String label, Pattern p, String input) {
        Matcher m = p.matcher(input);
        System.out.print("  " + label + ": ");
        StringBuilder sb = new StringBuilder();
        while (m.find()) sb.append("[").append(m.group()).append("] ");
        System.out.println(sb.length() == 0 ? "(no match)" : sb.toString().trim());
    }

    public static void main(String[] args) {

        System.out.println("=== Flags / Pattern Options Demo ===\n");

        // ----------------------------------------------------------
        // Demo 1: CASE_INSENSITIVE (?i)
        // ----------------------------------------------------------
        System.out.println("--- Demo 1: CASE_INSENSITIVE ---");

        String text = "Hello WORLD hello world HeLLo WoRLd";

        findAll("no flag     — hello",
                Pattern.compile("hello"),                        text);
        findAll("CASE_INSENSITIVE — hello",
                Pattern.compile("hello", Pattern.CASE_INSENSITIVE), text);
        findAll("inline (?i) — hello",
                Pattern.compile("(?i)hello"),                    text);

        System.out.println();
        // Case-insensitive with ranges
        findAll("[a-z]+ (case sensitive)",
                Pattern.compile("[a-z]+"),                text);
        findAll("[a-z]+ (case insensitive)",
                Pattern.compile("[a-z]+", Pattern.CASE_INSENSITIVE), text);
        System.out.println();

        // ----------------------------------------------------------
        // Demo 2: MULTILINE (?m) — ^ and $ match each line
        // ----------------------------------------------------------
        System.out.println("--- Demo 2: MULTILINE ---");

        String multiText = "Line 1: alpha\nLine 2: beta\nLine 3: gamma";
        System.out.println("  Input:\n    " + multiText.replace("\n", "\n    ") + "\n");

        findAll("^Line (no MULTILINE)",
                Pattern.compile("^Line"),                      multiText);
        findAll("^Line (MULTILINE)",
                Pattern.compile("^Line", Pattern.MULTILINE),   multiText);

        findAll("\\w+$ (no MULTILINE — only one end)",
                Pattern.compile("\\w+$"),                      multiText);
        findAll("\\w+$ (MULTILINE — each line end)",
                Pattern.compile("\\w+$", Pattern.MULTILINE),   multiText);
        System.out.println();

        // ----------------------------------------------------------
        // Demo 3: DOTALL (?s) — dot matches newline too
        // ----------------------------------------------------------
        System.out.println("--- Demo 3: DOTALL ---");

        String multiLine = "START\nsome content\nhere\nEND";
        System.out.println("  Input: \"START\\nsome content\\nhere\\nEND\"");

        // Without DOTALL: .* does not cross newlines
        Matcher noDs = Pattern.compile("START.*END").matcher(multiLine);
        System.out.println("  START.*END (no DOTALL)  : " + noDs.find()); // false

        // With DOTALL: .* matches newlines too
        Matcher ds = Pattern.compile("START.*END", Pattern.DOTALL).matcher(multiLine);
        System.out.println("  START.*END (DOTALL)     : " + ds.find()); // true
        if (ds.reset().find()) {
            System.out.println("  Match: \"" + ds.group().replace("\n", "\\n") + "\"");
        }
        System.out.println();

        // ----------------------------------------------------------
        // Demo 4: COMMENTS (?x) — readable patterns with whitespace and # comments
        // ----------------------------------------------------------
        System.out.println("--- Demo 4: COMMENTS (verbose mode) ---");

        // Without COMMENTS: compact and hard to read — same pattern as verboseDate below
        // With COMMENTS: readable version:
        Pattern verboseDate = Pattern.compile(
            "(\\d{4})   # year: 4 digits\n"  +
            "-          # separator\n"        +
            "(\\d{2})   # month: 2 digits\n" +
            "-          # separator\n"        +
            "(\\d{2})   # day: 2 digits",
            Pattern.COMMENTS
        );

        String dateStr = "2026-06-16";
        Matcher cm = verboseDate.matcher(dateStr);
        if (cm.matches()) {
            System.out.println("  Verbose pattern matched: " + dateStr);
            System.out.println("  Year=" + cm.group(1) + " Month=" + cm.group(2)
                    + " Day=" + cm.group(3));
        }
        System.out.println();

        // ----------------------------------------------------------
        // Demo 5: Combining multiple flags with bitwise OR
        // ----------------------------------------------------------
        System.out.println("--- Demo 5: Multiple Flags Combined ---");

        String logBlock = "ERROR: file not found\nwarning: disk low\nERROR: timeout\nINFO: ok";
        System.out.println("  Input:\n    " + logBlock.replace("\n", "\n    ") + "\n");

        // Find lines starting with "error" — case insensitive AND multiline
        Pattern errorPattern = Pattern.compile(
            "^error.*",
            Pattern.CASE_INSENSITIVE | Pattern.MULTILINE
        );
        findAll("^error.* (CASE_INSENSITIVE|MULTILINE)", errorPattern, logBlock);

        // Inline equivalent: (?im)
        findAll("(?im)^error.*  (inline flags)",
                Pattern.compile("(?im)^error.*"), logBlock);
        System.out.println();

        // ----------------------------------------------------------
        // Demo 6: LITERAL flag — treat entire pattern as plain text
        // ----------------------------------------------------------
        System.out.println("--- Demo 6: LITERAL flag ---");

        String specialText = "Price is $100 (tax included). [Ref: A+B*C]";
        System.out.println("  Input: \"" + specialText + "\"");

        // Without LITERAL: $ . ( ) [ ] + * are all metacharacters
        Pattern withoutLiteral = Pattern.compile("$100");
        System.out.println("  find(\"$100\") without LITERAL : "
                + withoutLiteral.matcher(specialText).find());  // may match wrong position

        // With LITERAL: every character is treated as a literal
        Pattern withLiteral = Pattern.compile("$100", Pattern.LITERAL);
        System.out.println("  find(\"$100\") with LITERAL    : "
                + withLiteral.matcher(specialText).find()); // true

        // Pattern.quote() is the alternative to LITERAL flag for substrings
        Pattern quoted = Pattern.compile(Pattern.quote("$100"));
        System.out.println("  find(Pattern.quote(\"$100\"))  : "
                + quoted.matcher(specialText).find()); // true
        System.out.println();

        // ----------------------------------------------------------
        // Demo 7: UNICODE_CASE (?u) — Unicode-aware case folding
        // ----------------------------------------------------------
        System.out.println("--- Demo 7: UNICODE_CASE ---");

        String turkish = "İstanbul Üniversitesi"; // Turkish characters
        Pattern asciiCI  = Pattern.compile("istanbul",
                Pattern.CASE_INSENSITIVE);
        Pattern unicodeCI = Pattern.compile("istanbul",
                Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE);

        System.out.println("  Input: \"" + turkish + "\"");
        System.out.println("  CASE_INSENSITIVE only        : "
                + asciiCI.matcher(turkish).find());    // may miss İ (dotted I)
        System.out.println("  CASE_INSENSITIVE|UNICODE_CASE: "
                + unicodeCI.matcher(turkish).find());  // correct Unicode folding

        // -------------------------------------------------------
        // KEY POINTS:
        // - CASE_INSENSITIVE  → ignores ASCII case; add UNICODE_CASE for full Unicode
        // - MULTILINE         → ^ and $ match per-line boundaries
        // - DOTALL            → dot matches newlines (default: excludes \n)
        // - COMMENTS          → allows whitespace/comments in pattern for readability
        // - LITERAL           → all chars treated as literals; no metacharacters
        // - Combine flags:    Pattern.compile(re, FLAG1 | FLAG2)
        // - Inline flags:     (?i), (?m), (?s), (?x), (?im), (?ims) etc.
        // -------------------------------------------------------
        System.out.println("\n=== Flags Demo Complete ===");
    }
}

