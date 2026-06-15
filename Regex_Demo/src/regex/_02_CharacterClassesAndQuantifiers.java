package regex;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

// ============================================================
// TOPIC: Character Classes and Quantifiers
// ============================================================
// CHARACTER CLASSES [...]:
//   [abc]        → a, b, or c
//   [^abc]       → anything except a, b, c
//   [a-z]        → a through z
//   [a-zA-Z0-9]  → any alphanumeric
//
// PREDEFINED SHORTHAND:
//   \d = [0-9]           \D = [^0-9]
//   \w = [a-zA-Z0-9_]   \W = [^\w]
//   \s = whitespace      \S = non-whitespace
//   .  = any char except newline
//
// QUANTIFIERS:
//   *     → 0 or more
//   +     → 1 or more
//   ?     → 0 or 1 (optional)
//   {n}   → exactly n times
//   {n,}  → at least n times
//   {n,m} → between n and m times
// ============================================================

public class _02_CharacterClassesAndQuantifiers {

    // Helper — print all matches of a pattern in a string
    static void printAllMatches(String label, String regex, String input) {
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(input);
        System.out.print("  " + label + ": ");
        StringBuilder sb = new StringBuilder();
        while (m.find()) {
            sb.append("[").append(m.group()).append("] ");
        }
        System.out.println(sb.length() == 0 ? "(no match)" : sb.toString().trim());
    }

    public static void main(String[] args) {

        System.out.println("=== Character Classes and Quantifiers Demo ===\n");

        // ----------------------------------------------------------
        // Demo 1: Basic character classes
        // ----------------------------------------------------------
        System.out.println("--- Demo 1: Character Classes ---");
        String text = "a1 b2 c3 X9 _! hello WORLD";

        printAllMatches("[aeiou]  — vowels",        "[aeiou]",        text);
        printAllMatches("[^aeiou ] — non-vowels",   "[^aeiou ]",      text);
        printAllMatches("[a-z]    — lowercase",     "[a-z]",          text);
        printAllMatches("[A-Z]    — uppercase",     "[A-Z]",          text);
        printAllMatches("[0-9]    — digits",        "[0-9]",          text);
        printAllMatches("[a-zA-Z] — letters",       "[a-zA-Z]",       text);
        printAllMatches("[a-fA-F0-9] — hex chars",  "[a-fA-F0-9]",   "ff 1G 0x4B c9 ZZ");
        System.out.println();

        // ----------------------------------------------------------
        // Demo 2: Predefined shorthand classes
        // ----------------------------------------------------------
        System.out.println("--- Demo 2: Predefined Shorthand Classes ---");
        String mixed = "Hello 123 World_42!  \t bye";

        printAllMatches("\\d  — digits",         "\\d",    mixed);
        printAllMatches("\\D  — non-digits",      "\\D",    "abc 123");
        printAllMatches("\\w  — word chars",      "\\w",    mixed);
        printAllMatches("\\W  — non-word chars",  "\\W",    mixed);
        printAllMatches("\\s  — whitespace",      "\\s",    mixed);
        printAllMatches("\\S  — non-whitespace",  "\\S",    mixed);
        printAllMatches(".    — any char",         ".",      "a1!");
        System.out.println();

        // ----------------------------------------------------------
        // Demo 3: Quantifiers
        // ----------------------------------------------------------
        System.out.println("--- Demo 3: Quantifiers ---");
        String numbers = "1 22 333 4444 55555";

        printAllMatches("\\d*   (0 or more)",    "\\d*",    numbers);
        printAllMatches("\\d+   (1 or more)",    "\\d+",    numbers);
        printAllMatches("\\d?   (0 or 1)",       "\\d?",    "a1b22c");
        printAllMatches("\\d{3} (exactly 3)",    "\\d{3}",  numbers);
        printAllMatches("\\d{2,3} (2 to 3)",     "\\d{2,3}",numbers);
        printAllMatches("\\d{2,}  (at least 2)", "\\d{2,}", numbers);
        System.out.println();

        // ----------------------------------------------------------
        // Demo 4: Quantifiers on character classes and groups
        // ----------------------------------------------------------
        System.out.println("--- Demo 4: Quantifiers on Classes ---");
        String words = "cat bat sat mat rat";

        printAllMatches("[cbsmr]at (specific first letter)", "[cbsmr]at", words);
        printAllMatches("[a-z]{3}  (any 3-letter word)",     "[a-z]{3}", words);
        printAllMatches("\\w+      (whole words)",            "\\w+",    "hello world 123");

        // Optional character — "colour" or "color"
        System.out.println("\n  colour? matching (u is optional):");
        Pattern colourP = Pattern.compile("colou?r", Pattern.CASE_INSENSITIVE);
        for (String s : new String[]{"color", "colour", "COLOUR", "COLR"}) {
            System.out.println("    \"" + s + "\" matches: " + colourP.matcher(s).matches());
        }
        System.out.println();

        // ----------------------------------------------------------
        // Demo 5: The dot (.) — any character except newline
        // ----------------------------------------------------------
        System.out.println("--- Demo 5: The Dot Metacharacter ---");

        Pattern dotPattern = Pattern.compile("c.t"); // c + ANY char + t
        String[] dotTests = {"cat", "cut", "cot", "c t", "ct", "cart"};
        for (String s : dotTests) {
            System.out.println("  \"" + s + "\" matches c.t : " + dotPattern.matcher(s).matches());
        }
        System.out.println();

        // ----------------------------------------------------------
        // Demo 6: Escaping metacharacters
        // ----------------------------------------------------------
        System.out.println("--- Demo 6: Escaping Metacharacters ---");

        // To match a literal dot, escape it with \.
        Pattern literalDot = Pattern.compile("\\d+\\.\\d+"); // matches "3.14"
        System.out.println("  \\d+\\.\\d+ matches \"3.14\"   : " + literalDot.matcher("3.14").matches());
        System.out.println("  \\d+\\.\\d+ matches \"314\"    : " + literalDot.matcher("314").matches());
        System.out.println("  \\d+\\.\\d+ matches \"3X14\"   : " + literalDot.matcher("3X14").matches());

        // Pattern.quote() escapes an entire string for literal matching
        String userInput = "3.14 is pi"; // user-provided, may contain metacharacters
        Pattern safe = Pattern.compile(Pattern.quote("3.14"));
        System.out.println("\n  Pattern.quote(\"3.14\") find in \"3.14 is pi\": "
                + safe.matcher(userInput).find());
        System.out.println("  Pattern.quote(\"3.14\") find in \"3X14 is pi\": "
                + safe.matcher("3X14 is pi").find());

        // -------------------------------------------------------
        // KEY POINTS:
        // - [abc]    → one character from the set
        // - [^abc]   → one character NOT in the set
        // - [a-z]    → range; [a-zA-Z0-9] common alphanumeric class
        // - \d \w \s → shorthand classes (double backslash in Java strings)
        // - *  +  ?  → 0+, 1+, 0-or-1 quantifiers
        // - {n,m}    → exact count range
        // - .        → any character except newline (use Pattern.DOTALL for newline too)
        // - \\.      → escaped literal dot (in Java string: "\\.")
        // - Pattern.quote(s) → treat s as literal text (no metacharacters)
        // -------------------------------------------------------
        System.out.println("\n=== Character Classes and Quantifiers Complete ===");
    }
}

