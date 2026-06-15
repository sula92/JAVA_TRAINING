package regex;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

// ============================================================
// TOPIC: Anchors and Boundaries
// ============================================================
// Anchors do NOT match characters — they match a POSITION.
//
//   ^    → start of input (or line in MULTILINE mode)
//   $    → end of input (or line in MULTILINE mode)
//   \b   → word boundary (transition between \w and \W)
//   \B   → non-word boundary
//   \A   → absolute start of input (ignores MULTILINE)
//   \Z   → absolute end of input (ignores MULTILINE)
//
// Use cases:
//   - Validate that an ENTIRE string matches a pattern (^\d{5}$)
//   - Match a word as a whole word and not as part of another (\bword\b)
//   - Match text that starts or ends each line in multiline input
// ============================================================

public class _03_AnchorsAndBoundaries {

    static void check(String label, String regex, String input) {
        boolean match = Pattern.compile(regex).matcher(input).matches();
        System.out.printf("  %-45s → %s%n", label, match ? "✓ MATCH" : "✗ NO MATCH");
    }

    static void findAll(String label, String regex, int flags, String input) {
        Pattern p = Pattern.compile(regex, flags);
        Matcher m = p.matcher(input);
        System.out.print("  " + label + ": ");
        StringBuilder sb = new StringBuilder();
        while (m.find()) sb.append("[").append(m.group()).append("] ");
        System.out.println(sb.length() == 0 ? "(no match)" : sb.toString().trim());
    }

    public static void main(String[] args) {

        System.out.println("=== Anchors and Boundaries Demo ===\n");

        // ----------------------------------------------------------
        // Demo 1: ^ (start) and $ (end) — full string validation
        // ----------------------------------------------------------
        System.out.println("--- Demo 1: ^ and $ — Full String Validation ---");

        // ZIP code: exactly 5 digits
        check("\"12345\"   matches ^\\d{5}$", "^\\d{5}$", "12345");
        check("\"1234\"    matches ^\\d{5}$", "^\\d{5}$", "1234");
        check("\"123456\"  matches ^\\d{5}$", "^\\d{5}$", "123456");
        check("\" 12345\"  matches ^\\d{5}$ (leading space)", "^\\d{5}$", " 12345");

        System.out.println();

        // find() with ^ and $ anchors (single-line mode)
        System.out.println("  find() with anchors — single-line mode:");
        findAll("  ^hello in \"hello world\"",     "^hello",   0, "hello world");
        findAll("  ^hello in \"say hello\"",        "^hello",   0, "say hello");
        findAll("  world$ in \"hello world\"",      "world$",   0, "hello world");
        findAll("  world$ in \"world peace\"",      "world$",   0, "world peace");
        System.out.println();

        // ----------------------------------------------------------
        // Demo 2: MULTILINE mode — ^ and $ match EACH line
        // ----------------------------------------------------------
        System.out.println("--- Demo 2: ^ and $ in MULTILINE mode ---");

        String multilineText = "apple\nbanana\ncherry\napricot";

        System.out.println("  Input (multiline):");
        for (String line : multilineText.split("\n")) System.out.println("    \"" + line + "\"");
        System.out.println();

        // Without MULTILINE: ^ matches only the very start of the entire string
        findAll("  ^[ab]\\w+ (no MULTILINE)", "^[ab]\\w+", 0,                   multilineText);
        // With MULTILINE: ^ matches start of EACH line
        findAll("  ^[ab]\\w+ (MULTILINE)",    "^[ab]\\w+", Pattern.MULTILINE,   multilineText);

        // Lines ending with 'y'
        findAll("  \\w+y$ (no MULTILINE)",    "\\w+y$",    0,                   multilineText);
        findAll("  \\w+y$ (MULTILINE)",       "\\w+y$",    Pattern.MULTILINE,   multilineText);
        System.out.println();

        // ----------------------------------------------------------
        // Demo 3: \b — word boundary
        // ----------------------------------------------------------
        System.out.println("--- Demo 3: \\b Word Boundary ---");

        String sentence = "cat concatenate cats scat catch";
        System.out.println("  Input: \"" + sentence + "\"");
        findAll("  cat    (no boundary)",  "cat",      0, sentence);
        findAll("  \\bcat\\b (boundary)",  "\\bcat\\b", 0, sentence);
        System.out.println();

        String logLine = "error errorCode noerror superError";
        System.out.println("  Input: \"" + logLine + "\"");
        findAll("  \\berror\\b  (whole word only)", "\\berror\\b", 0, logLine);
        System.out.println();

        // ----------------------------------------------------------
        // Demo 4: \B — non-word boundary (inside a word)
        // ----------------------------------------------------------
        System.out.println("--- Demo 4: \\B Non-Word Boundary ---");
        String text = "rethink think unthinkable";
        System.out.println("  Input: \"" + text + "\"");
        findAll("  think with \\B (inside larger word)", "\\Bthink\\B", 0, text);
        findAll("  think with \\b (standalone word)",    "\\bthink\\b", 0, text);
        System.out.println();

        // ----------------------------------------------------------
        // Demo 5: \A and \Z — absolute start/end (ignore MULTILINE)
        // ----------------------------------------------------------
        System.out.println("--- Demo 5: \\A and \\Z (absolute anchors) ---");

        String multiInput = "first line\nsecond line\nthird line";

        // \A always anchors to the VERY start of the string
        Matcher mA = Pattern.compile("\\A\\w+", Pattern.MULTILINE).matcher(multiInput);
        System.out.print("  \\A\\w+ (MULTILINE on): ");
        while (mA.find()) System.out.print("[" + mA.group() + "] ");
        System.out.println("  (only one match — \\ A ignores MULTILINE)");

        // compare with ^
        Matcher mCaret = Pattern.compile("^\\w+", Pattern.MULTILINE).matcher(multiInput);
        System.out.print("  ^\\w+  (MULTILINE on): ");
        while (mCaret.find()) System.out.print("[" + mCaret.group() + "] ");
        System.out.println("  (matches start of EACH line)\n");

        // ----------------------------------------------------------
        // Demo 6: Practical — validate inputs using anchors
        // ----------------------------------------------------------
        System.out.println("--- Demo 6: Practical Input Validation ---");

        Pattern username = Pattern.compile("^[a-zA-Z0-9_]{3,20}$");
        String[] names = {"alice", "Al", "alice_123", "alice 123", "a".repeat(25), "_admin"};
        System.out.println("  Username rule: ^[a-zA-Z0-9_]{3,20}$");
        for (String name : names) {
            System.out.printf("    %-25s → %s%n", "\"" + name + "\"",
                    username.matcher(name).matches() ? "VALID" : "INVALID");
        }

        // -------------------------------------------------------
        // KEY POINTS:
        // - ^      → start of string (or line with MULTILINE flag)
        // - $      → end of string (or line with MULTILINE flag)
        // - \b     → word boundary; use to match whole words only
        // - \B     → non-word boundary; match within a word
        // - \A     → absolute start (never affected by MULTILINE)
        // - \Z     → absolute end (never affected by MULTILINE)
        // - Use ^ and $ together to validate entire strings
        // -------------------------------------------------------
        System.out.println("\n=== Anchors and Boundaries Demo Complete ===");
    }
}

