package regex;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
// ============================================================
// TOPIC: String Methods That Use Regex
// ============================================================
// Java String methods that accept regex:
//   str.matches(regex)                 -> true if ENTIRE string matches
//   str.replaceAll(regex, replacement) -> replace ALL occurrences
//   str.replaceFirst(regex, repl)      -> replace FIRST occurrence
//   str.split(regex)                   -> split around matches
//   str.split(regex, limit)            -> split with max parts
//
// Group references in replacement strings:
//   $1, $2 ... -> captured group 1, 2 ...
//   ${name}    -> named captured group
//   $$         -> literal $ sign
//
// NOTE: str.matches() is equivalent to Pattern.matches("^re$", str).
// ============================================================
public class _07_StringMethodsWithRegex {
    public static void main(String[] args) {
        System.out.println("=== String Methods with Regex Demo ===\n");
        // ----------------------------------------------------------
        // Demo 1: String.matches() -- full-string match (auto-anchored)
        // ----------------------------------------------------------
        System.out.println("--- Demo 1: String.matches() ---");
        String[] inputs = {"12345", "  12345", "1234", "123456"};
        System.out.println("  Testing \"\\d{5}\" (exactly 5 digits):");
        for (String s : inputs) {
            System.out.printf("    %-12s -> %s%n", "\"" + s + "\"", s.matches("\\d{5}"));
        }
        System.out.println("  matches() is fully anchored:");
        System.out.println("    \"abc123\".matches(\"\\\\d+\")        = " + "abc123".matches("\\d+"));
        System.out.println("    \"abc123\".matches(\"[a-z]+\\\\d+\")  = " + "abc123".matches("[a-z]+\\d+"));
        System.out.println();
        // ----------------------------------------------------------
        // Demo 2: replaceAll() -- replace all matches
        // ----------------------------------------------------------
        System.out.println("--- Demo 2: String.replaceAll() ---");
        String messy = "  hello   world   from    Java  ";
        System.out.println("  Compress spaces   : \"" + messy.strip().replaceAll("\\s+", " ") + "\"");
        System.out.println("  Remove punctuation: \"" + "Hello, World! How?".replaceAll("[^a-zA-Z0-9 ]", "") + "\"");
        System.out.println("  Mask digits       : \"" + "Phone: 555-1234".replaceAll("\\d", "#") + "\"");
        System.out.println();
        // ----------------------------------------------------------
        // Demo 3: replaceAll() with group references ($1, $2)
        // ----------------------------------------------------------
        System.out.println("--- Demo 3: replaceAll() with Group References ---");
        // Reformat date: MM/DD/YYYY -> YYYY-MM-DD
        String date = "06/16/2026";
        System.out.println("  Date reformat : \"" + date + "\" -> \""
                + date.replaceAll("(\\d{2})/(\\d{2})/(\\d{4})", "$3-$1-$2") + "\"");
        // Wrap words in <b> tags
        String words = "hello world java";
        System.out.println("  Wrap in <b>   : \"" + words.replaceAll("(\\w+)", "<b>$1</b>") + "\"");
        // Swap last, first name
        String name = "Smith, John";
        System.out.println("  Swap names    : \"" + name.replaceAll("(\\w+),\\s*(\\w+)", "$2 $1") + "\"");
        System.out.println();
        // ----------------------------------------------------------
        // Demo 4: replaceFirst() -- only the first match
        // ----------------------------------------------------------
        System.out.println("--- Demo 4: String.replaceFirst() ---");
        String repeated = "apple banana apple cherry apple";
        System.out.println("  Original      : \"" + repeated + "\"");
        System.out.println("  replaceAll    : \"" + repeated.replaceAll("apple", "mango") + "\"");
        System.out.println("  replaceFirst  : \"" + repeated.replaceFirst("apple", "mango") + "\"");
        System.out.println();
        // ----------------------------------------------------------
        // Demo 5: split() -- split by a regex
        // ----------------------------------------------------------
        System.out.println("--- Demo 5: String.split() ---");
        String padded = "one  two   three    four";
        System.out.println("  split(\"\\\\s+\")  : " + Arrays.toString(padded.split("\\s+")));
        String csv = "apple , banana,cherry ,  date";
        System.out.println("  split(CSV)     : " + Arrays.toString(csv.split("\\s*,\\s*")));
        String multiDelim = "red,green;blue|yellow";
        System.out.println("  split([,;|])   : " + Arrays.toString(multiDelim.split("[,;|]")));
        String log = "INFO:2026-06-16:Application started";
        System.out.println("  split(\":\", 3)  : " + Arrays.toString(log.split(":", 3)));
        System.out.println();
        // ----------------------------------------------------------
        // Demo 6: split() edge cases
        // ----------------------------------------------------------
        System.out.println("--- Demo 6: split() Edge Cases ---");
        String trailing = "a,b,c,,,";
        System.out.println("  split(\",\")       : " + Arrays.toString(trailing.split(",")));
        System.out.println("  split(\",\", -1)   : " + Arrays.toString(trailing.split(",", -1)));
        // Must escape dot when splitting on it
        String pkg = "com.example.myapp.Main";
        System.out.println("  split(\"\\\\.\")    : " + Arrays.toString(pkg.split("\\.")));
        System.out.println();
        // ----------------------------------------------------------
        // Demo 7: Find all matches with Matcher
        // ----------------------------------------------------------
        System.out.println("--- Demo 7: Find all matches with Matcher ---");
        String logText = "Error at line 42, Warning at line 17, Error at line 99";
        Pattern lineNum = Pattern.compile("line (\\d+)");
        Matcher lm = lineNum.matcher(logText);
        System.out.println("  Line numbers mentioned:");
        while (lm.find()) {
            System.out.println("    line " + lm.group(1)
                    + "  (index " + lm.start() + "-" + lm.end() + ")");
        }
        // -------------------------------------------------------
        // KEY POINTS:
        // - matches()      -> full-string match (equivalent to ^pattern$)
        // - replaceAll()   -> replaces every match; $1/$2 reference groups
        // - replaceFirst() -> replaces only the first match
        // - split()        -> splits on regex; trailing empties removed by default
        // - split(re, -1)  -> keeps trailing empty strings
        // - Always escape literal dot in split: "\\."
        // - $$ in replacement string -> literal $ sign
        // -------------------------------------------------------
        System.out.println("\n=== String Methods with Regex Complete ===");
    }
}
