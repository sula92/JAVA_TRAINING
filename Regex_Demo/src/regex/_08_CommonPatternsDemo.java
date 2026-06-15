package regex;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
// ============================================================
// TOPIC: Common Real-World Patterns
// ============================================================
// This file demonstrates regex patterns for everyday validation
// and text-extraction tasks:
//   - Email address validation
//   - Phone number validation (US)
//   - URL matching
//   - Date validation (YYYY-MM-DD)
//   - IPv4 address validation
//   - Password strength validation
//   - Credit card number masking
//   - HTML tag extraction
//   - Find-all utility method
// ============================================================
public class _08_CommonPatternsDemo {
    // -------------------------------------------------------
    // Compiled patterns — compile ONCE, reuse many times
    // -------------------------------------------------------
    // Email (basic)
    static final Pattern EMAIL = Pattern.compile(
        "^[a-zA-Z0-9._%+\\-]+@[a-zA-Z0-9.\\-]+\\.[a-zA-Z]{2,}$"
    );
    // US Phone: accepts (555) 123-4567 / 555-123-4567 / +1 555 123 4567 etc.
    static final Pattern PHONE_US = Pattern.compile(
        "^\\+?1?[\\s.\\-]?\\(?\\d{3}\\)?[\\s.\\-]?\\d{3}[\\s.\\-]?\\d{4}$"
    );
    // URL (http or https)
    static final Pattern URL = Pattern.compile(
        "https?://[^\\s/$.?#][^\\s]*",
        Pattern.CASE_INSENSITIVE
    );
    // Date: YYYY-MM-DD with rough range check
    static final Pattern DATE_YMD = Pattern.compile(
        "^\\d{4}-(0[1-9]|1[0-2])-(0[1-9]|[12]\\d|3[01])$"
    );
    // IPv4 address
    static final Pattern IPV4 = Pattern.compile(
        "^((25[0-5]|2[0-4]\\d|[01]?\\d\\d?)\\.){3}(25[0-5]|2[0-4]\\d|[01]?\\d\\d?)$"
    );
    // US ZIP code: 12345 or 12345-6789
    static final Pattern ZIP = Pattern.compile(
        "^\\d{5}(-\\d{4})?$"
    );
    // Hex color: #FFF or #FFFFFF
    static final Pattern HEX_COLOR = Pattern.compile(
        "^#([A-Fa-f0-9]{6}|[A-Fa-f0-9]{3})$"
    );
    // Strong password: min 8 chars, 1 lowercase, 1 uppercase, 1 digit, 1 special char
    static final Pattern STRONG_PASSWORD = Pattern.compile(
        "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$"
    );
    // Credit card (Visa/MC/Amex — 4 groups): capture each group for masking
    static final Pattern CREDIT_CARD = Pattern.compile(
        "\\b(\\d{4})[\\s\\-]?(\\d{4})[\\s\\-]?(\\d{4})[\\s\\-]?(\\d{4})\\b"
    );
    // -------------------------------------------------------
    // Utility
    // -------------------------------------------------------
    static void validate(String label, Pattern pattern, String[] inputs) {
        System.out.println("  " + label + ":");
        for (String input : inputs) {
            System.out.printf("    %-35s → %s%n",
                    "\"" + input + "\"",
                    pattern.matcher(input).matches() ? "VALID   ✓" : "INVALID ✗");
        }
        System.out.println();
    }
    static void findAllMatches(String label, Pattern pattern, String input) {
        Matcher m = pattern.matcher(input);
        System.out.println("  " + label);
        System.out.println("  Input: \"" + input + "\"");
        System.out.print("  Found: ");
        boolean found = false;
        while (m.find()) {
            System.out.print("[" + m.group() + "] ");
            found = true;
        }
        if (!found) System.out.print("(none)");
        System.out.println("\n");
    }
    public static void main(String[] args) {
        System.out.println("=== Common Real-World Patterns Demo ===\n");
        // ----------------------------------------------------------
        // Email Validation
        // ----------------------------------------------------------
        System.out.println("--- Email Validation ---");
        validate("EMAIL", EMAIL, new String[]{
            "user@example.com",
            "user.name+tag@sub.domain.org",
            "invalid@",
            "@nodomain.com",
            "no-at-sign",
            "user@domain.c",           // TLD too short
            "valid123@company.co.uk"
        });
        // ----------------------------------------------------------
        // Phone Validation
        // ----------------------------------------------------------
        System.out.println("--- US Phone Validation ---");
        validate("PHONE_US", PHONE_US, new String[]{
            "555-123-4567",
            "(555) 123-4567",
            "+1 555 123 4567",
            "5551234567",
            "1-800-555-1234",
            "123-456-789",             // too short
            "5551234"
        });
        // ----------------------------------------------------------
        // URL Finding
        // ----------------------------------------------------------
        System.out.println("--- URL Extraction ---");
        String urlText = "Visit https://www.google.com or http://java.oracle.com/docs "
                       + "and also HTTPS://GITHUB.COM for more info.";
        findAllMatches("Extract URLs", URL, urlText);
        // ----------------------------------------------------------
        // Date Validation
        // ----------------------------------------------------------
        System.out.println("--- Date Validation (YYYY-MM-DD) ---");
        validate("DATE_YMD", DATE_YMD, new String[]{
            "2026-06-16",
            "2024-02-29",              // leap year
            "2023-02-29",              // NOT a leap year — regex allows it (pure date logic)
            "2026-13-01",              // invalid month
            "2026-00-15",              // invalid month
            "2026-6-5",               // missing leading zeros
            "16-06-2026"               // wrong order
        });
        // ----------------------------------------------------------
        // IPv4 Validation
        // ----------------------------------------------------------
        System.out.println("--- IPv4 Validation ---");
        validate("IPV4", IPV4, new String[]{
            "192.168.1.1",
            "255.255.255.0",
            "0.0.0.0",
            "256.0.0.1",              // 256 out of range
            "192.168.1",              // only 3 octets
            "10.0.0.300",             // 300 out of range
            "::1"                      // IPv6
        });
        // ----------------------------------------------------------
        // ZIP Code and Hex Color
        // ----------------------------------------------------------
        System.out.println("--- ZIP Code ---");
        validate("ZIP", ZIP, new String[]{"12345", "12345-6789", "1234", "123456", "12345-678"});
        System.out.println("--- Hex Color ---");
        validate("HEX_COLOR", HEX_COLOR, new String[]{"#FFF", "#FFFFFF", "#ff00aa", "#GGG", "#12345", "#1234567"});
        // ----------------------------------------------------------
        // Password Strength
        // ----------------------------------------------------------
        System.out.println("--- Password Strength ---");
        validate("STRONG_PASSWORD", STRONG_PASSWORD, new String[]{
            "password",               // no uppercase, no digit, no special
            "Password1",              // no special char
            "P@ssw0rd",               // STRONG
            "abc123!X",               // STRONG
            "SHORT1!",                // too short
            "ALLCAPS1!",              // no lowercase
            "alllower1!"              // no uppercase
        });
        // ----------------------------------------------------------
        // Credit Card Masking
        // ----------------------------------------------------------
        System.out.println("--- Credit Card Masking ---");
        String[] cards = {
            "4111 1111 1111 1234",
            "5500-1234-5678-9012",
            "371234567890123"          // Amex — 15 digits, different format
        };
        System.out.println("  Masking 16-digit cards (show only last 4 digits):");
        for (String card : cards) {
            Matcher cm = CREDIT_CARD.matcher(card);
            if (cm.find()) {
                String masked = cm.replaceAll("****-****-****-$4");
                System.out.println("    \"" + card + "\" → \"" + masked + "\"");
            } else {
                System.out.println("    \"" + card + "\" → no 16-digit pattern found");
            }
        }
        System.out.println();
        // ----------------------------------------------------------
        // HTML Tag Content Extraction
        // ----------------------------------------------------------
        System.out.println("--- HTML Tag Content Extraction ---");
        String html = "<h1>Welcome</h1><p>This is <b>bold</b> and <i>italic</i> text.</p>";
        Pattern tagContent = Pattern.compile("<(\\w+)>(.*?)</\\1>", Pattern.DOTALL);
        Matcher hm = tagContent.matcher(html);
        System.out.println("  Input: " + html);
        System.out.println("  Tag contents:");
        while (hm.find()) {
            System.out.println("    <" + hm.group(1) + "> : \"" + hm.group(2) + "\"");
        }
        System.out.println();
        // ----------------------------------------------------------
        // Log Line Parsing
        // ----------------------------------------------------------
        System.out.println("--- Log Line Parsing ---");
        Pattern logPattern = Pattern.compile(
            "(?<level>INFO|WARN|ERROR)\\s+(?<timestamp>\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2})\\s+(?<message>.+)"
        );
        String[] logLines = {
            "ERROR 2026-06-16 09:15:32 NullPointerException in UserService",
            "INFO  2026-06-16 09:15:33 Application started successfully",
            "WARN  2026-06-16 09:16:00 Memory usage above 80%",
            "DEBUG 2026-06-16 09:16:01 This line won't match"
        };
        System.out.println("  Parsed log entries:");
        for (String line : logLines) {
            Matcher lm = logPattern.matcher(line.trim());
            if (lm.matches()) {
                System.out.printf("    Level=%-5s  Time=%s  Msg=%s%n",
                        lm.group("level"),
                        lm.group("timestamp"),
                        lm.group("message"));
            } else {
                System.out.println("    (no match) \"" + line.trim() + "\"");
            }
        }
        // -------------------------------------------------------
        // KEY POINTS:
        // - Compile Pattern ONCE at class level (static final) for performance
        // - Email/URL regex should be as permissive as practical — real validation
        //   often needs an actual parser or library (Apache Commons Validator, etc.)
        // - Regex cannot validate leap year dates — use LocalDate.parse() for that
        // - Use named groups for readable log/data parsing patterns
        // - replaceAll() with group references is powerful for reformatting
        // - DOTALL flag needed when content may span multiple lines
        // -------------------------------------------------------
        System.out.println("\n=== Common Patterns Demo Complete ===");
    }
}
