package regex;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

// ============================================================
// TOPIC: Lookarounds, Alternation, and Greedy vs Lazy
// ============================================================
// ALTERNATION |:
//   cat|dog    → matches "cat" OR "dog"
//   gr(a|e)y   → matches "gray" or "grey"
//
// LOOKAROUNDS (zero-width — match a position, not characters):
//   (?=...)   positive lookahead  — must be followed by ...
//   (?!...)   negative lookahead  — must NOT be followed by ...
//   (?<=...)  positive lookbehind — must be preceded by ...
//   (?<!...)  negative lookbehind — must NOT be preceded by ...
//
// GREEDY vs LAZY:
//   *   +   ?   {n,m}    → greedy  (match as MUCH as possible)
//   *?  +?  ??  {n,m}?   → lazy    (match as LITTLE as possible)
// ============================================================

public class _05_LookaroundsAndAlternation {

    static void findAll(String label, String regex, String input) {
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(input);
        System.out.print("  " + label + ": ");
        StringBuilder sb = new StringBuilder();
        while (m.find()) sb.append("[").append(m.group()).append("] ");
        System.out.println(sb.length() == 0 ? "(no match)" : sb.toString().trim());
    }

    public static void main(String[] args) {

        System.out.println("=== Lookarounds, Alternation, and Greedy vs Lazy Demo ===\n");

        // ----------------------------------------------------------
        // Demo 1: Alternation |
        // ----------------------------------------------------------
        System.out.println("--- Demo 1: Alternation (|) ---");

        String animalText = "I have a cat and a dog and a bird and a cat again";
        findAll("cat|dog       ",         "cat|dog",          animalText);
        findAll("cat|dog|bird  ",         "cat|dog|bird",     animalText);

        // Alternation inside a group
        Pattern colorPattern = Pattern.compile("gr(a|e)y");
        String[] colors = {"gray", "grey", "gry", "graay"};
        System.out.println("  gr(a|e)y matching:");
        for (String c : colors) {
            System.out.println("    \"" + c + "\" → " + colorPattern.matcher(c).matches());
        }

        // Image extensions
        Pattern imgExt = Pattern.compile("\\.(jpg|jpeg|png|gif|webp)$",
                Pattern.CASE_INSENSITIVE);
        String[] files = {"photo.jpg", "image.PNG", "doc.pdf", "avatar.gif", "banner.WEBP"};
        System.out.println("\n  Image extension check (jpg|jpeg|png|gif|webp):");
        for (String f : files) {
            System.out.println("    \"" + f + "\" → " + imgExt.matcher(f).find());
        }
        System.out.println();

        // ----------------------------------------------------------
        // Demo 2: Positive Lookahead (?=...)
        //         Match X only if followed by Y (Y is NOT included in match)
        // ----------------------------------------------------------
        System.out.println("--- Demo 2: Positive Lookahead (?=...) ---");

        String prices = "12px 30em 45px 100% 7px";
        // Find numbers that are followed by "px"
        findAll("\\d+(?=px)  — numbers before px", "\\d+(?=px)", prices);

        // Find words followed by a comma
        String csv = "apple, banana, cherry, date";
        findAll("\\w+(?=,)   — words before comma", "\\w+(?=,)",  csv);
        System.out.println();

        // ----------------------------------------------------------
        // Demo 3: Negative Lookahead (?!...)
        //         Match X only if NOT followed by Y
        // ----------------------------------------------------------
        System.out.println("--- Demo 3: Negative Lookahead (?!...) ---");

        findAll("\\d+(?!px)  — numbers NOT before px", "\\d+(?!px)", prices);

        // Find "file" not followed by ".txt"
        String fileList = "file.txt file.csv file.txt report.txt note.csv";
        findAll("file(?!\\.txt) — 'file' not before .txt", "file(?!\\.txt)", fileList);
        System.out.println();

        // ----------------------------------------------------------
        // Demo 4: Positive Lookbehind (?<=...)
        //         Match X only if preceded by Y
        // ----------------------------------------------------------
        System.out.println("--- Demo 4: Positive Lookbehind (?<=...) ---");

        String moneyText = "USD 100 EUR 200 USD 50 GBP 75";
        // Find numbers preceded by "USD "
        findAll("(?<=USD )\\d+  — amounts after USD", "(?<=USD )\\d+", moneyText);

        String tagged = "<b>bold</b> <i>italic</i> <b>strong</b>";
        // Find content inside <b> tags (preceded by <b>)
        findAll("(?<=<b>)\\w+  — text after <b>", "(?<=<b>)\\w+", tagged);
        System.out.println();

        // ----------------------------------------------------------
        // Demo 5: Negative Lookbehind (?<!...)
        //         Match X only if NOT preceded by Y
        // ----------------------------------------------------------
        System.out.println("--- Demo 5: Negative Lookbehind (?<!...) ---");

        String logText = "ERROR found, noERROR safe, ERROR again, WARNING here";
        // Find "ERROR" not preceded by "no"
        findAll("(?<!no)ERROR  — ERROR not preceded by 'no'",
                "(?<!no)ERROR", logText);

        // Find digits not preceded by a minus sign
        String nums = "10 -20 30 -40 50";
        findAll("(?<!-)\\b\\d+  — positive numbers only", "(?<!-)\\b\\d+", nums);
        System.out.println();

        // ----------------------------------------------------------
        // Demo 6: Combining lookarounds — password validation
        // ----------------------------------------------------------
        System.out.println("--- Demo 6: Combined Lookarounds (Password Strength) ---");

        // A "strong" password must:
        //   - be at least 8 chars long
        //   - contain at least one lowercase letter
        //   - contain at least one uppercase letter
        //   - contain at least one digit
        //   - contain at least one special char
        Pattern strongPassword = Pattern.compile(
            "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$"
        );

        String[] passwords = {"password", "Password1", "P@ssw0rd", "abc123!X", "ALLCAPS1!"};
        System.out.println("  Password strength check:");
        for (String pw : passwords) {
            System.out.printf("    %-15s → %s%n",
                    "\"" + pw + "\"",
                    strongPassword.matcher(pw).matches() ? "STRONG ✓" : "WEAK   ✗");
        }
        System.out.println();

        // ----------------------------------------------------------
        // Demo 7: Greedy vs Lazy quantifiers
        // ----------------------------------------------------------
        System.out.println("--- Demo 7: Greedy vs Lazy Quantifiers ---");

        String html = "<b>bold</b> and <i>italic</i>";
        System.out.println("  Input: \"" + html + "\"");

        // GREEDY: <.*>  — matches as much as possible
        Matcher greedy = Pattern.compile("<.*>").matcher(html);
        if (greedy.find()) System.out.println("  GREEDY  <.*>  → \"" + greedy.group() + "\"");

        // LAZY:   <.*?> — matches as little as possible
        Matcher lazy = Pattern.compile("<.*?>").matcher(html);
        System.out.print("  LAZY    <.*?> → ");
        while (lazy.find()) System.out.print("\"" + lazy.group() + "\" ");
        System.out.println();
        System.out.println();

        // Another greedy vs lazy example with digits
        String text = "num: 123456789";
        Matcher greedyDigits = Pattern.compile("\\d{2,6}").matcher(text);
        Matcher lazyDigits   = Pattern.compile("\\d{2,6}?").matcher(text);

        System.out.println("  Input: \"" + text + "\"");
        System.out.print("  GREEDY \\d{2,6}  → ");
        while (greedyDigits.find()) System.out.print("[" + greedyDigits.group() + "] ");

        System.out.print("\n  LAZY   \\d{2,6}? → ");
        while (lazyDigits.find()) System.out.print("[" + lazyDigits.group() + "] ");
        System.out.println("\n");

        // ----------------------------------------------------------
        // Demo 8: Practical — extract content between quotes (lazy)
        // ----------------------------------------------------------
        System.out.println("--- Demo 8: Extract Quoted Strings (lazy) ---");

        String quoted = "He said \"hello\" then \"goodbye\" then \"see you\"";
        Pattern qp = Pattern.compile("\"(.*?)\""); // lazy .*?
        Matcher qm = qp.matcher(quoted);
        System.out.println("  Input: " + quoted);
        System.out.print("  Quoted strings: ");
        while (qm.find()) System.out.print("[" + qm.group(1) + "] ");
        System.out.println();

        // -------------------------------------------------------
        // KEY POINTS:
        // - |          → OR; left option tried first
        // - (?=X)      → positive lookahead; X must follow (not consumed)
        // - (?!X)      → negative lookahead; X must NOT follow
        // - (?<=X)     → positive lookbehind; X must precede (not consumed)
        // - (?<!X)     → negative lookbehind; X must NOT precede
        // - Lookarounds are zero-width: they don't consume characters
        // - Greedy (*,+,?) → match as much as possible
        // - Lazy (*?,+?,??)→ match as little as possible
        // - Use lazy quantifiers inside HTML/XML parsing
        // -------------------------------------------------------
        System.out.println("\n=== Lookarounds, Alternation, and Greedy vs Lazy Complete ===");
    }
}

