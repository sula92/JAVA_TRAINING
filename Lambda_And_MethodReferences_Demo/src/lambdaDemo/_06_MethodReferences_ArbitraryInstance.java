package lambdaDemo;

import java.util.function.*;
import java.util.Arrays;
import java.util.List;
import java.util.Comparator;

// ============================================================
// TOPIC: Method References — Type 3: Arbitrary Instance Method Reference
// ============================================================
// Syntax:  ClassName::instanceMethodName
//
// The FIRST PARAMETER of the lambda becomes the OBJECT on which
// the instance method is called.
//
// Pattern:
//   Lambda:           (obj)    -> obj.method()
//   Method reference: ClassName::method
//
//   Lambda:           (obj, x) -> obj.method(x)
//   Method reference: ClassName::method
//
// This looks like a static method reference (same syntax: Class::method)
// but it calls an INSTANCE method — the distinction is whether the
// method is static or instance in the class.
//
// KEY QUESTION: Is the first parameter the OBJECT or an ARGUMENT?
//   Type 1 (static):   ClassName::staticMethod  → all params are arguments
//   Type 3 (instance): ClassName::instanceMethod → FIRST param is the object
// ============================================================

public class _06_MethodReferences_ArbitraryInstance {

    static class Product {
        private String name;
        private double price;

        Product(String name, double price) {
            this.name  = name;
            this.price = price;
        }

        public String getName()   { return name; }
        public double getPrice()  { return price; }
        public boolean isExpensive() { return price > 50.0; }
        public String describe()  { return name + " ($" + price + ")"; }

        @Override
        public String toString()  { return describe(); }
    }

    public static void main(String[] args) {

        System.out.println("=== Method References — Type 3: Arbitrary Instance ===\n");

        // ----------------------------------------------------------
        // Demo 1: String instance methods — the most common examples
        // ----------------------------------------------------------
        System.out.println("--- Demo 1: String Instance Methods ---");

        // Lambda:           (String s) -> s.toUpperCase()
        // Method reference: String::toUpperCase
        // The String parameter 's' becomes the object on which toUpperCase() is called

        Function<String, String>  toUpper  = String::toUpperCase;   // (s) -> s.toUpperCase()
        Function<String, String>  toLower  = String::toLowerCase;   // (s) -> s.toLowerCase()
        Function<String, String>  trimmed  = String::trim;          // (s) -> s.trim()
        Function<String, Integer> length   = String::length;        // (s) -> s.length()
        Predicate<String>         isEmpty  = String::isEmpty;       // (s) -> s.isEmpty()

        System.out.println("  toUpper(\"hello\")    = " + toUpper.apply("hello"));
        System.out.println("  toLower(\"WORLD\")    = " + toLower.apply("WORLD"));
        System.out.println("  trimmed(\"  hi  \")   = \"" + trimmed.apply("  hi  ") + "\"");
        System.out.println("  length(\"java\")      = " + length.apply("java"));
        System.out.println("  isEmpty(\"\")         = " + isEmpty.test(""));
        System.out.println("  isEmpty(\"hi\")       = " + isEmpty.test("hi"));
        System.out.println();

        // ----------------------------------------------------------
        // Demo 2: Two-parameter case — (obj, arg) -> obj.method(arg)
        // ----------------------------------------------------------
        System.out.println("--- Demo 2: Two-Parameter Case (obj + argument) ---");

        // Lambda:           (String s, String prefix) -> s.startsWith(prefix)
        // Method reference: String::startsWith
        // First param 's' is the OBJECT; second param 'prefix' is the ARGUMENT

        BiPredicate<String, String>  startsWith = String::startsWith;
        BiPredicate<String, String>  endsWith   = String::endsWith;
        BiFunction<String, String, Boolean> contains = String::contains;
        BiFunction<String, Integer, String> substring = String::substring;

        System.out.println("  startsWith(\"hello\", \"he\") = " + startsWith.test("hello", "he"));
        System.out.println("  startsWith(\"hello\", \"wo\") = " + startsWith.test("hello", "wo"));
        System.out.println("  endsWith(\"world\", \"ld\")   = " + endsWith.test("world", "ld"));
        System.out.println("  contains(\"java\", \"av\")    = " + contains.apply("java", "av"));
        System.out.println("  substring(\"hello\", 2)      = " + substring.apply("hello", 2));
        System.out.println();

        // ----------------------------------------------------------
        // Demo 3: Custom class instance methods
        // ----------------------------------------------------------
        System.out.println("--- Demo 3: Custom Class Instance Methods ---");

        List<Product> products = Arrays.asList(
            new Product("Laptop",     999.99),
            new Product("Book",        15.00),
            new Product("Headphones", 79.99),
            new Product("Pen",          1.50),
            new Product("Monitor",    349.00)
        );

        // Lambda:           (Product p) -> p.getName()
        // Method reference: Product::getName
        Function<Product, String>  getName     = Product::getName;
        Function<Product, Double>  getPrice    = Product::getPrice;
        Predicate<Product>         isExpensive = Product::isExpensive;
        Function<Product, String>  describe    = Product::describe;

        System.out.println("  Product names:");
        products.stream().map(getName).forEach(n -> System.out.println("    " + n));

        System.out.println("  Expensive products (>$50):");
        products.stream()
                .filter(isExpensive)
                .map(describe)
                .forEach(d -> System.out.println("    " + d));
        System.out.println();

        // ----------------------------------------------------------
        // Demo 4: Comparator with arbitrary instance method reference
        // ----------------------------------------------------------
        System.out.println("--- Demo 4: Comparator with Arbitrary Instance Ref ---");

        // Lambda:           (Product a, Product b) -> a.getName().compareTo(b.getName())
        // Using comparing: Comparator.comparing(Product::getName)
        //   Product::getName is used as a key extractor (Function<Product, String>)

        List<Product> sortedByName  = products.stream()
            .sorted(Comparator.comparing(Product::getName))
            .collect(java.util.stream.Collectors.toList());

        List<Product> sortedByPrice = products.stream()
            .sorted(Comparator.comparingDouble(Product::getPrice))
            .collect(java.util.stream.Collectors.toList());

        System.out.println("  Sorted by name:");
        sortedByName.forEach(p -> System.out.println("    " + p));

        System.out.println("  Sorted by price (ascending):");
        sortedByPrice.forEach(p -> System.out.println("    " + p));
        System.out.println();

        // ----------------------------------------------------------
        // Demo 5: Used in stream map/filter/sorted
        // ----------------------------------------------------------
        System.out.println("--- Demo 5: Arbitrary Instance Ref in Stream Operations ---");

        List<String> words = Arrays.asList("  hello  ", "WORLD", "  java  ", "CODE");

        System.out.print("  After trim then toLower: ");
        words.stream()
             .map(String::trim)         // (s) -> s.trim()
             .map(String::toLowerCase)  // (s) -> s.toLowerCase()
             .forEach(s -> System.out.print(s + " "));
        System.out.println("\n");

        List<String> mixed = Arrays.asList("apple", "", "banana", "  ", "cherry", "");
        System.out.print("  Non-empty strings: ");
        mixed.stream()
             .map(String::trim)
             .filter(s -> !s.isEmpty())   // can't use String::isEmpty directly (would keep empty)
             .forEach(s -> System.out.print(s + " "));
        System.out.println("\n");

        // ----------------------------------------------------------
        // Demo 6: Integer instance methods
        // ----------------------------------------------------------
        System.out.println("--- Demo 6: Integer Instance Methods ---");

        // Integer::compareTo — (Integer a, Integer b) -> a.compareTo(b)
        Comparator<Integer> intComp = Integer::compareTo;
        System.out.println("  Integer::compareTo(3, 5)  = " + intComp.compare(3, 5));
        System.out.println("  Integer::compareTo(5, 3)  = " + intComp.compare(5, 3));
        System.out.println("  Integer::compareTo(3, 3)  = " + intComp.compare(3, 3));

        // -------------------------------------------------------
        // KEY POINTS:
        // - Syntax: ClassName::instanceMethod
        // - The FIRST lambda parameter becomes the OBJECT the method is called on
        // - Additional parameters become arguments to the method
        // - Looks identical to static method ref — distinction is static vs instance
        // - Very common in stream operations: String::trim, String::isEmpty,
        //   String::toUpperCase, String::length
        // - Used as key extractor in Comparator.comparing(ClassName::getter)
        // -------------------------------------------------------
        System.out.println("\n=== Arbitrary Instance Method References Demo Complete ===");
    }
}

