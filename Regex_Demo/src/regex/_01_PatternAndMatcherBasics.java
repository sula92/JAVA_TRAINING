package regex;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

// ============================================================
// TOPIC: Pattern and Matcher Basics
// ============================================================
// java.util.regex provides:
//   Pattern → compiled representation of a regex
//   Matcher → engine that applies the pattern to an input string
//
// Workflow:
//   1. Pattern p = Pattern.compile("regex");
//   2. Matcher m = p.matcher("input string");
//   3. Use m.find() / m.matches() / m.group() etc.
//
// IMPORTANT: Reuse the compiled Pattern object.
//   Compiling regex is expensive — do it once, outside loops.
// ============================================================

public class _01_PatternAndMatcherBasics {

    public static void main(String[] args) {

        System.out.println("=== Pattern and Matcher Basics ===\n");

        // ----------------------------------------------------------
        // Demo 1: find() — search for a pattern anywhere in the string
        // ----------------------------------------------------------
        System.out.println("--- Demo 1: find() ---");

        Pattern pattern = Pattern.compile("hello");
        Matcher matcher = pattern.matcher("say hello to the world, hello again!");

        int matchCount = 0;
        while (matcher.find()) {
            matchCount++;
            System.out.println("  Match #" + matchCount
                    + " found: \"" + matcher.group() + "\""
                    + "  start=" + matcher.start()
                    + "  end=" + matcher.end());
        }
        System.out.println("  Total matches: " + matchCount + "\n");

        // ----------------------------------------------------------
        // Demo 2: matches() — entire string must match the pattern
        // ----------------------------------------------------------
        System.out.println("--- Demo 2: matches() vs find() ---");

        Pattern digits = Pattern.compile("\\d+"); // one or more digits

        String[] inputs = {"12345", "123abc", "abc"};
        for (String input : inputs) {
            Matcher m = digits.matcher(input);
            System.out.printf("  Input: %-10s | matches()=%-5s | find()=%s%n",
                    "\"" + input + "\"",
                    m.matches(),
                    digits.matcher(input).find()); // reset with new Matcher
        }
        System.out.println();

        // ----------------------------------------------------------
        // Demo 3: lookingAt() — matches from the beginning (not necessarily whole)
        // ----------------------------------------------------------
        System.out.println("--- Demo 3: lookingAt() ---");

        Pattern p = Pattern.compile("\\d+");
        System.out.println("  \"123abc\".lookingAt(\\d+) : " + p.matcher("123abc").lookingAt()); // true
        System.out.println("  \"abc123\".lookingAt(\\d+) : " + p.matcher("abc123").lookingAt()); // false
        System.out.println();

        // ----------------------------------------------------------
        // Demo 4: group(), start(), end()
        // ----------------------------------------------------------
        System.out.println("--- Demo 4: group(), start(), end() ---");

        Pattern wordPattern = Pattern.compile("[A-Z][a-z]+"); // capitalized word
        Matcher wm = wordPattern.matcher("Hello World from Java");

        while (wm.find()) {
            System.out.printf("  group()=\"%s\"  start=%d  end=%d%n",
                    wm.group(), wm.start(), wm.end());
        }
        System.out.println();

        // ----------------------------------------------------------
        // Demo 5: reset() and find(int start)
        // ----------------------------------------------------------
        System.out.println("--- Demo 5: reset() and find(int start) ---");

        Matcher rm = Pattern.compile("\\d+").matcher("10 cats and 20 dogs and 30 birds");
        rm.find();
        System.out.println("  First match : " + rm.group()); // "10"
        rm.reset(); // reset back to beginning
        rm.find();
        System.out.println("  After reset, first match again: " + rm.group()); // "10"

        // find(int start) — start search from a specific index
        rm.find(10); // skip first 10 characters
        System.out.println("  find(10): " + rm.group()); // "20"
        System.out.println();

        // ----------------------------------------------------------
        // Demo 6: replaceAll() and replaceFirst() via Matcher
        // ----------------------------------------------------------
        System.out.println("--- Demo 6: Matcher replaceAll / replaceFirst ---");

        String text = "The price is $100 or maybe $200 or $300";
        Matcher prices = Pattern.compile("\\$\\d+").matcher(text);

        System.out.println("  Original    : " + text);
        System.out.println("  replaceAll  : " + prices.replaceAll("[PRICE]"));
        prices.reset(); // reset before using replaceFirst
        System.out.println("  replaceFirst: " + prices.replaceFirst("[PRICE]"));
        System.out.println();

        // ----------------------------------------------------------
        // Demo 7: PatternSyntaxException — invalid regex
        // ----------------------------------------------------------
        System.out.println("--- Demo 7: PatternSyntaxException ---");

        try {
            Pattern bad = Pattern.compile("[unclosed"); // invalid!
            bad.matcher("test").find();
        } catch (PatternSyntaxException e) {
            System.out.println("  PatternSyntaxException caught!");
            System.out.println("  Message    : " + e.getMessage().split("\n")[0]);
            System.out.println("  Pattern    : " + e.getPattern());
            System.out.println("  Error index: " + e.getIndex());
        }

        // ----------------------------------------------------------
        // Demo 8: Pattern.matches() — one-shot convenience method
        //         (WARNING: recompiles the pattern every call — avoid in loops)
        // ----------------------------------------------------------
        System.out.println("\n--- Demo 8: Pattern.matches() static method ---");
        System.out.println("  Pattern.matches(\"\\\\d+\", \"123\") : " + Pattern.matches("\\d+", "123"));
        System.out.println("  Pattern.matches(\"\\\\d+\", \"12x\") : " + Pattern.matches("\\d+", "12x"));

        // -------------------------------------------------------
        // KEY POINTS:
        // - Pattern.compile() once, then reuse the Pattern object
        // - find()      → partial match (anywhere in string)
        // - matches()   → full match (entire string must match)
        // - lookingAt() → match from start of string (not necessarily all)
        // - group()     → text of the last match
        // - start()/end() → position of match in input string
        // - reset()     → restart from beginning of the input
        // -------------------------------------------------------
        System.out.println("\n=== Pattern and Matcher Basics Complete ===");
    }
}

