package lambdaDemo;

import java.util.function.*;

// ============================================================
// TOPIC: Variable Capture & Scope in Lambdas
// ============================================================
// A lambda can READ variables from its enclosing scope, but with rules:
//
// LOCAL VARIABLES: must be FINAL or EFFECTIVELY FINAL
//   - final          → declared with the final keyword
//   - effectively final → never re-assigned after initial assignment
//
// INSTANCE VARIABLES: can be read AND written (via 'this')
// STATIC VARIABLES:   can be read AND written
//
// WHY the restriction on locals?
//   Local variables live on the stack. If the lambda runs later
//   (e.g. in a different thread), the stack frame may be gone.
//   The lambda gets a COPY of the local value — if the value
//   could change, which copy would be correct? Java prevents this.
// ============================================================

public class _03_VariableCaptureDemo {

    // ----------------------------------------------------------
    // Instance and static fields — lambdas can READ and WRITE
    // ----------------------------------------------------------
    String instanceMessage = "Instance field value";
    static int staticCounter = 0;

    void demo1_CapturingLocalVariables() {
        System.out.println("--- Demo 1: Capturing Local Variables (effectively final) ---");

        String greeting = "Hello";    // effectively final — never re-assigned
        int    base     = 10;         // effectively final

        Runnable r = () -> System.out.println("  " + greeting + " — base=" + base);
        r.run();

        // greeting = "Hi";  // ← uncommenting this would cause a compile error
        // base = 20;        // ← uncommenting this would cause a compile error
        System.out.println();
    }

    void demo2_ExplicitFinalCapture() {
        System.out.println("--- Demo 2: Explicit final Keyword ---");

        final String title = "Mr.";   // explicitly final

        Function<String, String> addTitle = name -> title + " " + name;
        System.out.println("  " + addTitle.apply("Smith"));
        System.out.println("  " + addTitle.apply("Jones"));
        System.out.println();
    }

    void demo3_CapturingInstanceFields() {
        System.out.println("--- Demo 3: Capturing Instance Fields (read AND write) ---");

        // Lambda reads and modifies an INSTANCE FIELD — this is allowed
        Runnable r = () -> {
            System.out.println("  Before: instanceMessage = " + instanceMessage);
            instanceMessage = "Modified by lambda";  // ✅ writing to instance field is allowed
            System.out.println("  After:  instanceMessage = " + instanceMessage);
        };
        r.run();
        System.out.println();
    }

    void demo4_CapturingStaticFields() {
        System.out.println("--- Demo 4: Capturing Static Fields (read AND write) ---");

        Runnable r = () -> {
            staticCounter++;  // ✅ writing to static field is allowed
            System.out.println("  staticCounter incremented to: " + staticCounter);
        };
        r.run();
        r.run();
        r.run();
        System.out.println();
    }

    void demo5_EachLambdaGetsItsOwnCopy() {
        System.out.println("--- Demo 5: Each Lambda Captures Its Own Copy of a Local ---");

        for (int i = 1; i <= 3; i++) {
            final int captured = i;  // create a new effectively-final local each iteration
            Runnable r = () -> System.out.println("  Lambda captured: " + captured);
            r.run();
        }

        // The common mistake: capturing loop variable directly would fail
        // for (int i = 1; i <= 3; i++) {
        //     Runnable r = () -> System.out.println(i); // ❌ 'i' is NOT effectively final
        // }
        System.out.println();
    }

    void demo6_LambdaInsideLambda() {
        System.out.println("--- Demo 6: Lambda Capturing Lambda's Own Parameter ---");

        // A lambda that returns another lambda (closure over 'factor')
        Function<Integer, Function<Integer, Integer>> adder =
            factor -> (value -> value + factor);
        //           ^          ^
        //    outer lambda   inner lambda; 'factor' is captured from outer

        Function<Integer, Integer> add5  = adder.apply(5);
        Function<Integer, Integer> add10 = adder.apply(10);

        System.out.println("  add5.apply(3)  = " + add5.apply(3));    // 8
        System.out.println("  add10.apply(3) = " + add10.apply(3));   // 13
        System.out.println();
    }

    void demo7_ThisInLambdaVsAnonClass() {
        System.out.println("--- Demo 7: 'this' in Lambda vs Anonymous Class ---");

        // Lambda: 'this' = enclosing _03_VariableCaptureDemo instance
        Runnable lambdaThis = () ->
            System.out.println("  Lambda 'this': " + this.getClass().getSimpleName());

        // Anonymous class: 'this' = the anonymous Runnable instance
        Runnable anonThis = new Runnable() {
            @Override
            public void run() {
                System.out.println("  Anon  'this': " + this.getClass().getSimpleName());
            }
        };

        lambdaThis.run();
        anonThis.run();
        System.out.println();
    }

    void demo8_WorkaroundForMutableCounter() {
        System.out.println("--- Demo 8: Workaround — Mutable State in a Lambda ---");

        // You can't increment a local variable in a lambda.
        // Workarounds:
        //   (a) Use an instance/static field (shown above)
        //   (b) Use an AtomicInteger
        //   (c) Use a single-element array

        // Workaround (c): single-element array — the array reference is final,
        // but the element inside can be mutated
        int[] counter = {0};  // array reference is effectively final; element is mutable

        Runnable increment = () -> counter[0]++;

        increment.run();
        increment.run();
        increment.run();
        System.out.println("  counter[0] after 3 increments = " + counter[0]);
        System.out.println("  (Note: this is a workaround; prefer AtomicInteger in multithreaded code)");

        // ⚠ WARNING: this workaround is NOT thread-safe!
        // In multithreaded context use AtomicInteger:
        java.util.concurrent.atomic.AtomicInteger atomicCount = new java.util.concurrent.atomic.AtomicInteger(0);
        Runnable safeIncrement = atomicCount::incrementAndGet;
        safeIncrement.run();
        safeIncrement.run();
        System.out.println("  atomicCount after 2 increments = " + atomicCount.get());
        System.out.println();
    }

    public static void main(String[] args) {
        System.out.println("=== Variable Capture and Scope Demo ===\n");

        _03_VariableCaptureDemo demo = new _03_VariableCaptureDemo();
        demo.demo1_CapturingLocalVariables();
        demo.demo2_ExplicitFinalCapture();
        demo.demo3_CapturingInstanceFields();
        demo.demo4_CapturingStaticFields();
        demo.demo5_EachLambdaGetsItsOwnCopy();
        demo.demo6_LambdaInsideLambda();
        demo.demo7_ThisInLambdaVsAnonClass();
        demo.demo8_WorkaroundForMutableCounter();

        // -------------------------------------------------------
        // KEY POINTS:
        // - Local variables: read-only in lambdas; must be final/effectively final
        // - Instance fields: read AND write allowed (via this.field)
        // - Static fields:   read AND write allowed
        // - 'this' inside lambda = enclosing class instance
        // - Each lambda iteration needs its own final copy for loop variables
        // - Workaround for mutable counter: single-element array or AtomicInteger
        // - The restriction on locals prevents subtle concurrency bugs
        // -------------------------------------------------------
        System.out.println("=== Variable Capture and Scope Demo Complete ===");
    }
}

