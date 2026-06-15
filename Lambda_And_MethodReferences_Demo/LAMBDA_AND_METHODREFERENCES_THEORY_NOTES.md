# Anonymous Inner Classes, Lambda Expressions & Method References — Complete Theory Notes

---

## Table of Contents

1. [Introduction & Evolution](#1-introduction--evolution)
2. [Anonymous Inner Classes](#2-anonymous-inner-classes)
3. [Functional Interfaces](#3-functional-interfaces)
4. [Lambda Expressions — Syntax Deep Dive](#4-lambda-expressions--syntax-deep-dive)
5. [Variable Capture & Scope Rules](#5-variable-capture--scope-rules)
6. [Method References — All Four Types](#6-method-references--all-four-types)
7. [Built-in Functional Interfaces (java.util.function)](#7-built-in-functional-interfaces-javautilfunction)
8. [Chaining Functional Interfaces](#8-chaining-functional-interfaces)
9. [Comparator with Lambdas & Method References](#9-comparator-with-lambdas--method-references)
10. [Lambda vs Anonymous Class — Full Comparison](#10-lambda-vs-anonymous-class--full-comparison)
11. [Common Mistakes & Pitfalls](#11-common-mistakes--pitfalls)
12. [Best Practices](#12-best-practices)
13. [Quick Reference Cheat Sheet](#13-quick-reference-cheat-sheet)

---

## 1. Introduction & Evolution

### The Problem Before Java 8

Before Java 8, passing behaviour (a block of code) to a method required creating a whole class:

```java
// Java 7 — verbose just to pass one action
button.addActionListener(new ActionListener() {
    @Override
    public void actionPerformed(ActionEvent e) {
        System.out.println("Button clicked!");
    }
});
```

Java 8 introduced **lambda expressions** to drastically reduce this boilerplate.

### The Evolutionary Chain

```
Named Class  →  Anonymous Inner Class  →  Lambda Expression  →  Method Reference
   (full)          (inline, nameless)       (concise inline fn)   (shortest possible)
```

Each step removes more syntax while doing the same work.

---

## 2. Anonymous Inner Classes

### What Is an Anonymous Inner Class?

A class that:
- Has **no name**
- Is **declared and instantiated at the same point**
- Can either **implement an interface** or **extend a class**
- Lives only for a single use

### Syntax

```java
// Implementing an interface
Runnable r = new Runnable() {
    @Override
    public void run() {
        System.out.println("Running!");
    }
};   // <-- semicolon required

// Extending an abstract class
abstract class Animal {
    abstract void speak();
}
Animal dog = new Animal() {
    @Override
    void speak() { System.out.println("Woof!"); }
};
```

### Key Characteristics

| Feature | Details |
|---------|---------|
| Name | None — compiled as `OuterClass$1`, `OuterClass$2`, … |
| Scope | Only usable where declared |
| Extends / implements | Either an abstract class OR an interface |
| Multiple methods | Can override **as many methods as needed** |
| Captured variables | Must be `final` or **effectively final** |
| `this` inside | Refers to the **anonymous class instance** |
| Performance | Creates a separate `.class` file per usage |

### When to Use Anonymous Classes

✅ Implementing an interface that has **more than one abstract method**  
✅ **Extending an abstract class** (lambdas cannot do this)  
✅ One-time event handlers (Swing/JavaFX listeners)  
✅ When you need to override multiple methods together  

### When NOT to Use (Use Lambda Instead)

❌ Single abstract method interface where the body is simple  
❌ Any time the lambda form would be cleaner  

---

## 3. Functional Interfaces

### Definition

An interface that has **exactly one abstract method** (SAM — Single Abstract Method).

- May have any number of `default` and `static` methods  
- Methods from `Object` (e.g. `equals`, `toString`) don't count  
- The `@FunctionalInterface` annotation enforces this at compile time  

```java
@FunctionalInterface
interface Transformer<T, R> {
    R transform(T input);           // only ONE abstract method

    default String describe() {     // default methods are fine
        return "A transformer";
    }

    static Transformer<String, Integer> stringLength() {  // static is fine
        return s -> s.length();
    }
}
```

### Why They Exist

Functional interfaces are the **target type** for lambda expressions.  
The compiler needs to know which method a lambda is implementing.

```
Lambda  →  assigned to  →  Functional Interface variable
() -> "hi"  →  Supplier<String>
x -> x * 2  →  UnaryOperator<Integer> / Function<Integer, Integer>
```

---

## 4. Lambda Expressions — Syntax Deep Dive

### Core Syntax

```
(parameters) -> expression            // single expression, no braces, implicit return
(parameters) -> { statements; }       // block body, explicit return required
```

### All Syntax Variations

```java
// No parameters
() -> System.out.println("Hello")
() -> { System.out.println("Hello"); System.out.println("World"); }

// One parameter — parentheses OPTIONAL
x -> x * x
(x) -> x * x
(int x) -> x * x          // explicit type (compiler can always infer)

// Two parameters — parentheses REQUIRED
(a, b) -> a + b
(String s, int n) -> s.repeat(n)

// Block body — requires explicit return
(a, b) -> {
    int sum = a + b;
    return sum;
}

// Returning an object
() -> new ArrayList<>()
() -> { return new ArrayList<>(); }
```

### Type Inference

The compiler infers parameter types from the functional interface:

```java
Comparator<String> c = (a, b) -> a.compareTo(b);
// compiler infers: a is String, b is String
```

### `this` Keyword in a Lambda

Inside a lambda, `this` refers to the **enclosing class** (not the lambda itself, because a lambda is not a class — it is just a method body):

```java
class MyClass {
    String name = "MyClass";

    void demo() {
        Runnable r = () -> System.out.println(this.name); // refers to MyClass
        r.run(); // prints "MyClass"
    }
}
```

Contrast with anonymous class:
```java
Runnable r = new Runnable() {
    String name = "AnonClass";
    public void run() {
        System.out.println(this.name); // refers to anonymous class
    }
};
```

---

## 5. Variable Capture & Scope Rules

### Effectively Final

A lambda can **read** variables from its enclosing scope, but those variables must be:
- Declared `final`, OR
- **Effectively final** — never re-assigned after initial assignment

```java
int multiplier = 3;          // effectively final (never changed)
Function<Integer, Integer> f = x -> x * multiplier;  // ✅ OK

multiplier = 5;              // compile error — multiplier is no longer effectively final
```

### What Can a Lambda Capture?

| Variable type | Can capture? | Notes |
|--------------|-------------|-------|
| Local variable | ✅ Read-only | Must be effectively final |
| Instance variable | ✅ Read/write | Via `this.field` |
| Static variable | ✅ Read/write | Shared across all instances |
| Method parameter | ✅ Read-only | Must be effectively final |

### Why This Rule Exists

Lambdas may run in a different thread or at a later time. Mutating captured locals would lead to unpredictable behaviour.

---

## 6. Method References — All Four Types

A method reference is a **shorthand for a lambda** that does nothing but call one existing method.

```
lambda:            x -> System.out.println(x)
method reference:  System.out::println
```

The `::` operator is read as **"method of"**.

---

### Type 1 — Static Method Reference

```
ClassName::staticMethodName
```

```java
// Lambda version
Function<String, Integer> parse = s -> Integer.parseInt(s);

// Method reference version
Function<String, Integer> parse = Integer::parseInt;

// More examples
Function<Double, Double>  abs   = Math::abs;
BiFunction<String, String, String> concat = String::concat; // static... wait — concat is instance
// Actually:
Consumer<String> print = System.out::println;  // this is instance on 'System.out'
```

✅ Use when: lambda body calls a static method with the same parameter(s).

---

### Type 2 — Instance Method Reference (on a Specific Object)

```
objectReference::instanceMethodName
```

The lambda's input is passed as the argument to the method on that specific object.

```java
String greeting = "Hello, World!";

// Lambda
Supplier<String> upper = () -> greeting.toUpperCase();

// Method reference
Supplier<String> upper = greeting::toUpperCase;

// PrintStream instance
Consumer<String> printer = System.out::println;
// System.out is a specific PrintStream instance
```

✅ Use when: lambda calls an instance method on a **specific captured object**.

---

### Type 3 — Instance Method Reference (on a Parameter / Arbitrary Instance)

```
ClassName::instanceMethodName
```

The **first parameter** of the lambda becomes the object on which the method is called.

```java
// Lambda:         (String s) -> s.toUpperCase()
// Method ref:     String::toUpperCase

Function<String, String>    toUpper  = String::toUpperCase;
Function<String, Integer>   length   = String::length;
Predicate<String>           isEmpty  = String::isEmpty;
BiFunction<String, String, Boolean> startsWith = String::startsWith;
// (s, prefix) -> s.startsWith(prefix)
```

✅ Use when: lambda calls a method **on one of its own parameters** (usually the first one).

---

### Type 4 — Constructor Reference

```
ClassName::new
```

```java
// Lambda:        () -> new ArrayList<>()
// Constructor ref: ArrayList::new

Supplier<ArrayList<String>> listFactory = ArrayList::new;
ArrayList<String> list = listFactory.get(); // calls new ArrayList<>()

// With parameter
Function<String, StringBuilder> sbFactory = StringBuilder::new;
// (String s) -> new StringBuilder(s)

// With BiFunction
BiFunction<String, Integer, String> repeated = String::new; // doesn't exist — example
// Real example:
Function<Integer, int[]> arrayFactory = int[]::new;
// (int n) -> new int[n]
```

✅ Use when: lambda creates a new object with `new ClassName(...)`.

---

### When Can You Use a Method Reference?

| Lambda body | Method reference? |
|-------------|------------------|
| `x -> Foo.bar(x)` | `Foo::bar` (static) |
| `() -> obj.foo()` | `obj::foo` (specific instance) |
| `(x) -> x.foo()` | `Foo::foo` (arbitrary instance) |
| `(x, y) -> x.foo(y)` | `Foo::foo` (arbitrary instance, y is arg) |
| `() -> new Foo()` | `Foo::new` (constructor) |
| `x -> new Foo(x)` | `Foo::new` (constructor with arg) |
| Lambda body does more than call one method | ❌ Cannot use method reference |

---

## 7. Built-in Functional Interfaces (java.util.function)

Java 8 ships a rich set of ready-made functional interfaces:

### Core Four

| Interface | Method | Input → Output | Purpose |
|-----------|--------|---------------|---------|
| `Predicate<T>` | `boolean test(T t)` | T → boolean | Test a condition |
| `Consumer<T>` | `void accept(T t)` | T → void | Consume a value |
| `Supplier<T>` | `T get()` | none → T | Produce a value |
| `Function<T,R>` | `R apply(T t)` | T → R | Transform a value |

### Extended Variants

| Interface | Method | Notes |
|-----------|--------|-------|
| `BiPredicate<T,U>` | `boolean test(T,U)` | Two-arg predicate |
| `BiConsumer<T,U>` | `void accept(T,U)` | Two-arg consumer |
| `BiFunction<T,U,R>` | `R apply(T,U)` | Two-arg function |
| `UnaryOperator<T>` | `T apply(T)` | Same-type function (extends Function) |
| `BinaryOperator<T>` | `T apply(T,T)` | Same-type BiFunction |
| `IntPredicate` | `boolean test(int)` | Avoids boxing |
| `IntConsumer` | `void accept(int)` | Avoids boxing |
| `IntSupplier` | `int getAsInt()` | Avoids boxing |
| `IntFunction<R>` | `R apply(int)` | int → R |
| `ToIntFunction<T>` | `int applyAsInt(T)` | T → int |
| `IntUnaryOperator` | `int applyAsInt(int)` | int → int |

Primitive variants also exist for `long` and `double`.

---

## 8. Chaining Functional Interfaces

### Predicate Chaining

```java
Predicate<Integer> isPositive = n -> n > 0;
Predicate<Integer> isEven     = n -> n % 2 == 0;

isPositive.and(isEven)      // n > 0 AND n % 2 == 0
isPositive.or(isEven)       // n > 0 OR  n % 2 == 0
isPositive.negate()         // NOT (n > 0)
Predicate.not(isPositive)   // Java 11+ — same as negate()
```

### Function Chaining

```java
Function<Integer, Integer> triple  = n -> n * 3;
Function<Integer, String>  toStr   = n -> "Value: " + n;

// andThen: apply triple FIRST, then toStr
Function<Integer, String> tripleToStr = triple.andThen(toStr);
tripleToStr.apply(5); // → "Value: 15"

// compose: apply toStr FIRST, then triple  (opposite order of andThen)
// Note: both must be Function<Integer, Integer> for compose to work
Function<Integer, Integer> addTen  = n -> n + 10;
Function<Integer, Integer> result  = triple.compose(addTen);
result.apply(5); // → triple(addTen(5)) = triple(15) = 45
```

### Consumer Chaining

```java
Consumer<String> print  = System.out::println;
Consumer<String> log    = s -> System.out.println("[LOG] " + s);

Consumer<String> both = print.andThen(log);
both.accept("hello"); // prints "hello" then "[LOG] hello"
```

---

## 9. Comparator with Lambdas & Method References

`Comparator` is a functional interface, making it a perfect target for lambdas and method references.

```java
List<String> names = Arrays.asList("Charlie", "Alice", "Bob");

// Old way (anonymous class)
Collections.sort(names, new Comparator<String>() {
    public int compare(String a, String b) { return a.compareTo(b); }
});

// Lambda
names.sort((a, b) -> a.compareTo(b));

// Method reference (String::compareTo)
names.sort(String::compareTo);

// Comparator.comparing — key extractor
names.sort(Comparator.comparing(String::length));

// Reversed
names.sort(Comparator.comparing(String::length).reversed());

// Multi-level: sort by length, then alphabetically
names.sort(Comparator.comparing(String::length)
                     .thenComparing(Comparator.naturalOrder()));
```

---

## 10. Lambda vs Anonymous Class — Full Comparison

| Feature | Anonymous Inner Class | Lambda Expression |
|---------|----------------------|-------------------|
| Syntax | Verbose | Concise |
| Can extend abstract class | ✅ Yes | ❌ No — interfaces only |
| Multiple abstract methods | ✅ Yes | ❌ No — SAM only |
| `this` keyword | Refers to anonymous class | Refers to enclosing class |
| Captures outer variables | Final/effectively final | Final/effectively final |
| Separate `.class` file | ✅ Generated | ❌ Uses `invokedynamic` |
| Shadowing outer variables | ✅ Can declare same-name fields | ❌ Cannot shadow outer locals |
| Available since | Early Java | Java 8 |
| Preferred for | Multi-method / class extension | Single-method behaviour passing |

---

## 11. Common Mistakes & Pitfalls

### Mistake 1 — Lambda on a non-functional interface
```java
// Runnable is @FunctionalInterface → works
Runnable r = () -> System.out.println("ok");

// Cloneable has NO abstract methods → compile error
Cloneable c = () -> {};  // ❌ error
```

### Mistake 2 — Modifying a captured variable
```java
int count = 0;
Runnable r = () -> count++;  // ❌ compile error — count must be effectively final
```

### Mistake 3 — `this` confusion
```java
class Outer {
    void test() {
        Runnable r1 = new Runnable() {
            public void run() { System.out.println(this); }  // 'this' = anonymous instance
        };
        Runnable r2 = () -> System.out.println(this);       // 'this' = Outer instance
    }
}
```

### Mistake 4 — Method reference when lambda does extra work
```java
// WRONG — can't use method reference; lambda does more than just call a method
list.forEach(x -> { x = x.trim(); System.out.println(x); });

// Method reference only works when lambda ONLY calls one method
list.forEach(System.out::println);  // ✅ correct
```

### Mistake 5 — Forgetting semicolon after anonymous class
```java
Runnable r = new Runnable() {
    public void run() { }
};  // ← semicolon required — this is still an assignment statement
```

### Mistake 6 — Calling `run()` instead of `start()` with Runnable (threading mistake)
```java
Thread t = new Thread(() -> doWork());
t.run();    // ❌ runs in current thread — no new thread
t.start();  // ✅ creates a new OS thread
```

---

## 12. Best Practices

| # | Practice | Reason |
|---|----------|--------|
| 1 | Prefer method references over equivalent lambdas | More readable (`String::isEmpty` vs `s -> s.isEmpty()`) |
| 2 | Keep lambda bodies short (1–3 lines) | Long lambdas should be extracted to named methods |
| 3 | Use `@FunctionalInterface` on custom interfaces | Compiler enforces SAM; documents intent |
| 4 | Prefer built-in interfaces over custom ones | Reduces API surface; interoperates with Stream API |
| 5 | Name variables meaningfully | `user -> user.isActive()` vs `x -> x.isActive()` |
| 6 | Don't use lambdas for multi-method needs | Use anonymous class or named class instead |
| 7 | Avoid side effects in lambdas when used with streams | Parallel streams require stateless lambdas |
| 8 | Use `Comparator.comparing()` for sorting | Much more readable than manual compare logic |

---

## 13. Quick Reference Cheat Sheet

### Lambda Syntax

```
No args:              () -> expr
One arg (no parens):  x -> expr
Multiple args:        (x, y) -> expr
Block body:           (x, y) -> { ...; return expr; }
Explicit types:       (String s, int n) -> expr
```

### Method Reference Syntax

```
Static:               ClassName::staticMethod       Math::abs
Specific instance:    object::instanceMethod        System.out::println
Arbitrary instance:   ClassName::instanceMethod     String::toUpperCase
Constructor:          ClassName::new                ArrayList::new
```

### Core Functional Interfaces

```
Predicate<T>          test(T) → boolean
Consumer<T>           accept(T) → void
Supplier<T>           get() → T
Function<T,R>         apply(T) → R
BiFunction<T,U,R>     apply(T,U) → R
UnaryOperator<T>      apply(T) → T
BinaryOperator<T>     apply(T,T) → T
Comparator<T>         compare(T,T) → int
```

### Chaining Methods

```
Predicate:  .and()  .or()  .negate()
Function:   .andThen()  .compose()
Consumer:   .andThen()
Comparator: .thenComparing()  .reversed()  .comparing(keyExtractor)
```

---

## Code Files in This Demo

| File | Concept Demonstrated |
|------|--------------------|
| `_01_AnonymousInnerClassDemo.java` | Anonymous class with interface, abstract class, multi-method, captured variables, `this` behaviour |
| `_02_LambdaExpressionsDemo.java` | All lambda syntax forms, type inference, block body, scope, `this` keyword |
| `_03_VariableCaptureDemo.java` | Effectively final, capturing locals/fields/statics, common capture mistakes |
| `_04_MethodReferences_Static.java` | Type 1 — static method references with full examples |
| `_05_MethodReferences_SpecificInstance.java` | Type 2 — method refs on specific object instances |
| `_06_MethodReferences_ArbitraryInstance.java` | Type 3 — method refs on arbitrary instances (ClassName::method) |
| `_07_MethodReferences_Constructor.java` | Type 4 — constructor references including array constructors |
| `_08_BuiltInFunctionalInterfacesDemo.java` | Predicate, Consumer, Supplier, Function, BiFunction, UnaryOperator, BinaryOperator |
| `_09_FunctionChainingDemo.java` | andThen, compose, Predicate.and/or/negate, Consumer.andThen |
| `_10_ComparatorDemo.java` | Comparator with lambdas, method refs, comparing(), thenComparing(), reversed() |

