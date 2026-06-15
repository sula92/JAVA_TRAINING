package regex;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

// ============================================================
// TOPIC: Groups and Capturing
// ============================================================
// Parentheses ( ) in regex serve two purposes:
//   1. GROUPING   — treat multiple tokens as a single unit
//   2. CAPTURING  — save the matched text for retrieval
//
// Group numbering: left-to-right by opening parenthesis, from 1.
//   Group 0 = entire match (always)
//   Group 1 = first ( )
//   Group 2 = second ( ), and so on
//
// Types:
//   (...)        → capturing group
//   (?:...)      → non-capturing group (structure only, no capture)
//   (?<name>...) → named capturing group
//
// Backreferences inside the pattern:
//   \1, \2       → refer to group 1, group 2 (in Java string: \\1)
//   \k<name>     → refer to named group (in Java string: \\k<name>)
// ============================================================

public class _04_GroupsAndCapturing {

    public static void main(String[] args) {

        System.out.println("=== Groups and Capturing Demo ===\n");

        // ----------------------------------------------------------
        // Demo 1: Basic capturing groups
        // ----------------------------------------------------------
        System.out.println("--- Demo 1: Basic Capturing Groups ---");

        // Pattern: year-month-day
        Pattern datePattern = Pattern.compile("(\\d{4})-(\\d{2})-(\\d{2})");
        String dateText = "Events: 2024-01-15 and 2025-12-31 and 2026-06-16";

        Matcher dm = datePattern.matcher(dateText);
        while (dm.find()) {
            System.out.println("  Full match : " + dm.group(0)); // or dm.group()
            System.out.println("  Group 1 (year)  : " + dm.group(1));
            System.out.println("  Group 2 (month) : " + dm.group(2));
            System.out.println("  Group 3 (day)   : " + dm.group(3));
            System.out.println();
        }

        // ----------------------------------------------------------
        // Demo 2: Named capturing groups (?<name>...)
        // ----------------------------------------------------------
        System.out.println("--- Demo 2: Named Capturing Groups ---");

        Pattern named = Pattern.compile(
            "(?<year>\\d{4})-(?<month>\\d{2})-(?<day>\\d{2})"
        );
        Matcher nm = named.matcher("Release date: 2026-06-16");
        if (nm.find()) {
            System.out.println("  year  : " + nm.group("year"));
            System.out.println("  month : " + nm.group("month"));
            System.out.println("  day   : " + nm.group("day"));
        }
        System.out.println();

        // ----------------------------------------------------------
        // Demo 3: Non-capturing group (?:...)
        //         Groups for alternation/structure but does NOT create a numbered group
        // ----------------------------------------------------------
        System.out.println("--- Demo 3: Non-Capturing Group (?:...) ---");

        // Match titles: "Mr. Smith", "Ms. Jones", "Dr. Brown"
        // We want to capture the last name (group 1), NOT the title prefix
        Pattern titlePattern = Pattern.compile("(?:Mr|Ms|Dr)\\.\\s+(\\w+)");
        String[] people = {"Mr. Smith", "Ms. Jones", "Dr. Brown", "Prof. Davis"};

        for (String person : people) {
            Matcher tm = titlePattern.matcher(person);
            if (tm.matches()) {
                System.out.println("  \"" + person + "\"  → last name: " + tm.group(1));
            } else {
                System.out.println("  \"" + person + "\"  → no match");
            }
        }
        System.out.println();

        // ----------------------------------------------------------
        // Demo 4: Grouping for quantifiers
        //         Without a group, the quantifier applies to only the last character
        // ----------------------------------------------------------
        System.out.println("--- Demo 4: Groups with Quantifiers ---");

        // Without group: "ab+" means a followed by one-or-more b
        Pattern withoutGroup = Pattern.compile("ab+");
        // With group: "(ab)+" means one-or-more repetitions of "ab"
        Pattern withGroup    = Pattern.compile("(ab)+");

        String[] tests = {"ab", "abab", "ababab", "abbb"};
        System.out.println("  Pattern  ab+   vs  (ab)+");
        for (String t : tests) {
            System.out.printf("    %-10s  ab+=%s  (ab)+=%s%n",
                    "\"" + t + "\"",
                    withoutGroup.matcher(t).matches(),
                    withGroup.matcher(t).matches());
        }
        System.out.println();

        // ----------------------------------------------------------
        // Demo 5: Backreferences — refer back to a captured group
        // ----------------------------------------------------------
        System.out.println("--- Demo 5: Backreferences (\\1) ---");

        // Detect doubled words: "the the", "is is", etc.
        Pattern doubled = Pattern.compile("\\b(\\w+)\\s+\\1\\b");
        String sentence = "this is is a test where the the words repeat themselves";
        Matcher bm = doubled.matcher(sentence);

        System.out.println("  Input: \"" + sentence + "\"");
        System.out.print("  Doubled words: ");
        while (bm.find()) {
            System.out.print("\"" + bm.group(1) + "\" ");
        }
        System.out.println("\n");

        // Match tags with matching open/close: <b>...</b>
        Pattern htmlTag = Pattern.compile("<(\\w+)>[^<]*</\\1>");
        String[] htmlTests = {"<b>bold</b>", "<b>bold</i>", "<span>text</span>", "<div></p>"};
        System.out.println("  HTML matching open/close tags via backreference:");
        for (String html : htmlTests) {
            System.out.println("    \"" + html + "\"  → " + htmlTag.matcher(html).matches());
        }
        System.out.println();

        // ----------------------------------------------------------
        // Demo 6: Named backreferences \k<name>
        // ----------------------------------------------------------
        System.out.println("--- Demo 6: Named Backreference (\\k<name>) ---");

        // Match a word that appears twice, separated by a space
        Pattern namedBack = Pattern.compile("(?<word>\\w+)\\s+\\k<word>");
        Matcher nbm = namedBack.matcher("hello hello world world java");
        System.out.print("  Repeated words: ");
        while (nbm.find()) {
            System.out.print("\"" + nbm.group("word") + "\" ");
        }
        System.out.println("\n");

        // ----------------------------------------------------------
        // Demo 7: Nested groups — groups inside groups
        // ----------------------------------------------------------
        System.out.println("--- Demo 7: Nested Groups ---");

        // Match "2024-01-15" and also extract just the year and full month+day
        Pattern nested = Pattern.compile("((\\d{4})-(\\d{2}-\\d{2}))");
        Matcher ngm = nested.matcher("Date: 2024-01-15");
        if (ngm.find()) {
            System.out.println("  group(0) = entire match   : " + ngm.group(0));
            System.out.println("  group(1) = outer group    : " + ngm.group(1)); // 2024-01-15
            System.out.println("  group(2) = year           : " + ngm.group(2)); // 2024
            System.out.println("  group(3) = month-day      : " + ngm.group(3)); // 01-15
        }

        // ----------------------------------------------------------
        // Demo 8: group() returns null for optional groups that didn't match
        // ----------------------------------------------------------
        System.out.println("\n--- Demo 8: Optional Group May Return null ---");

        Pattern optional = Pattern.compile("(\\+1)?\\s*(\\d{3})-(\\d{4})");
        Matcher om1 = optional.matcher("+1 555-1234");
        Matcher om2 = optional.matcher("555-1234");

        if (om1.find()) System.out.println("  \"+1 555-1234\"  group(1)=" + om1.group(1)
                + "  group(2)=" + om1.group(2) + "  group(3)=" + om1.group(3));
        if (om2.find()) System.out.println("  \"555-1234\"     group(1)=" + om2.group(1) // null
                + "  group(2)=" + om2.group(2) + "  group(3)=" + om2.group(3));

        // -------------------------------------------------------
        // KEY POINTS:
        // - (...)          → capturing group; numbered from 1 (left to right)
        // - (?:...)        → non-capturing; same grouping power, no capture overhead
        // - (?<name>...)   → named capturing; use group("name") to retrieve
        // - \1 / \k<name>  → backreference inside pattern (Java string: \\1)
        // - $1 / ${name}   → backreference inside REPLACEMENT strings
        // - Nested groups are numbered by their OPENING parenthesis
        // - Optional groups that didn't match return null from group()
        // -------------------------------------------------------
        System.out.println("\n=== Groups and Capturing Demo Complete ===");
    }
}

