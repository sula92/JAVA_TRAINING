package lambdaDemo;

// ============================================================
// TOPIC: Anonymous Inner Classes
// ============================================================
// An anonymous inner class is a class that:
//   - has NO name
//   - is declared AND instantiated at the same point
//   - can implement an interface OR extend an abstract/concrete class
//   - can override MULTIPLE methods (unlike lambdas)
//
// Compiled class name: OuterClass$1, OuterClass$2, etc.
// The captured outer variables must be final or effectively final.
// 'this' inside an anonymous class refers to the ANONYMOUS class instance.
// ============================================================

public class _01_AnonymousInnerClassDemo {

    // ----------------------------------------------------------
    // Supporting types
    // ----------------------------------------------------------

    interface Greeter {
        void greet(String name);
    }

    interface Shape {
        double area();
        double perimeter();  // TWO abstract methods — lambda cannot implement this!
        default String describe() {
            return "Shape with area=" + area() + " perimeter=" + perimeter();
        }
    }

    static abstract class Animal {
        String species;
        Animal(String species) { this.species = species; }
        abstract void speak();
        void breathe() { System.out.println("  " + species + " is breathing."); }
    }

    // ----------------------------------------------------------
    // Demo 1: Anonymous class implementing a single-method interface
    // ----------------------------------------------------------
    static void demo1_SingleMethodInterface() {
        System.out.println("--- Demo 1: Anonymous Class with Single-Method Interface ---");

        // Declare-and-instantiate in one expression
        Greeter formalGreeter = new Greeter() {
            @Override
            public void greet(String name) {
                System.out.println("  Good day, " + name + ". Welcome.");
            }
        }; // <-- semicolon is REQUIRED — this is an assignment statement

        Greeter casualGreeter = new Greeter() {
            @Override
            public void greet(String name) {
                System.out.println("  Hey " + name + "! What's up?");
            }
        };

        formalGreeter.greet("Dr. Smith");
        casualGreeter.greet("Jake");
        System.out.println();
    }

    // ----------------------------------------------------------
    // Demo 2: Anonymous class with TWO abstract methods
    //         (lambdas CANNOT do this — anonymous classes can)
    // ----------------------------------------------------------
    static void demo2_MultiMethodInterface() {
        System.out.println("--- Demo 2: Anonymous Class with Multi-Method Interface ---");

        Shape rectangle = new Shape() {
            double width = 5.0, height = 3.0;

            @Override
            public double area() { return width * height; }

            @Override
            public double perimeter() { return 2 * (width + height); }
        };

        Shape circle = new Shape() {
            double radius = 4.0;

            @Override
            public double area() { return Math.PI * radius * radius; }

            @Override
            public double perimeter() { return 2 * Math.PI * radius; }
        };

        System.out.println("  Rectangle: " + rectangle.describe());
        System.out.println("  Circle:    " + circle.describe());
        System.out.println();
    }

    // ----------------------------------------------------------
    // Demo 3: Anonymous class extending an abstract class
    //         (lambdas CANNOT extend classes — interfaces only)
    // ----------------------------------------------------------
    static void demo3_ExtendAbstractClass() {
        System.out.println("--- Demo 3: Anonymous Class Extending Abstract Class ---");

        Animal dog = new Animal("Dog") {
            @Override
            void speak() { System.out.println("  Dog says: Woof!"); }
        };

        Animal cat = new Animal("Cat") {
            @Override
            void speak() { System.out.println("  Cat says: Meow!"); }

            @Override
            void breathe() {
                // also override the concrete method
                System.out.println("  Cat breathes quietly...");
            }
        };

        dog.speak();
        dog.breathe();  // uses Animal's breathe()
        cat.speak();
        cat.breathe();  // uses overridden breathe()
        System.out.println();
    }

    // ----------------------------------------------------------
    // Demo 4: Capturing outer variables
    //         Captured variables must be final or effectively final
    // ----------------------------------------------------------
    static void demo4_CapturingOuterVariables() {
        System.out.println("--- Demo 4: Capturing Outer Variables ---");

        String prefix = "Hello";          // effectively final (never re-assigned)
        int multiplier = 3;               // effectively final

        Greeter capturingGreeter = new Greeter() {
            @Override
            public void greet(String name) {
                // reads 'prefix' from outer scope
                System.out.println("  " + prefix + ", " + name + "! (x" + multiplier + ")");
            }
        };

        capturingGreeter.greet("Alice");
        capturingGreeter.greet("Bob");
        System.out.println();
    }

    // ----------------------------------------------------------
    // Demo 5: 'this' inside anonymous class vs enclosing class
    // ----------------------------------------------------------
    String instanceName = "OuterClass";

    void demo5_ThisKeyword() {
        System.out.println("--- Demo 5: 'this' Keyword Behaviour ---");

        Runnable anonClass = new Runnable() {
            String instanceName = "AnonymousClass";  // can shadow outer field!

            @Override
            public void run() {
                // 'this' refers to THIS ANONYMOUS CLASS instance
                System.out.println("  this.instanceName inside anon class = " + this.instanceName);
                // Access outer class field with OuterClass.this
                System.out.println("  outer instanceName = "
                        + _01_AnonymousInnerClassDemo.this.instanceName);
            }
        };

        Runnable lambda = () -> {
            // 'this' refers to the ENCLOSING CLASS instance
            System.out.println("  this.instanceName inside lambda     = " + this.instanceName);
        };

        anonClass.run();
        lambda.run();
        System.out.println();
    }

    // ----------------------------------------------------------
    // Demo 6: Anonymous class passed directly as method argument
    // ----------------------------------------------------------
    static void process(Greeter g, String name) {
        System.out.print("  Processing: ");
        g.greet(name);
    }

    static void demo6_PassedAsArgument() {
        System.out.println("--- Demo 6: Anonymous Class as Method Argument ---");

        // Pass anonymous class inline as argument
        process(new Greeter() {
            @Override
            public void greet(String name) {
                System.out.println("INLINE greeting for " + name);
            }
        }, "Charlie");

        // Same thing with a lambda (cleaner for single-method interface)
        process(name -> System.out.println("LAMBDA greeting for " + name), "Diana");
        System.out.println();
    }

    // ----------------------------------------------------------
    // Demo 7: Array of anonymous class instances
    // ----------------------------------------------------------
    static void demo7_ArrayOfAnonymousClasses() {
        System.out.println("--- Demo 7: Array of Anonymous Class Instances ---");

        Greeter[] greeters = {
            new Greeter() {
                @Override public void greet(String n) { System.out.println("  EN: Hello, " + n); }
            },
            new Greeter() {
                @Override public void greet(String n) { System.out.println("  ES: Hola, " + n); }
            },
            new Greeter() {
                @Override public void greet(String n) { System.out.println("  FR: Bonjour, " + n); }
            }
        };

        for (Greeter g : greeters) {
            g.greet("World");
        }
        System.out.println();
    }

    public static void main(String[] args) {
        System.out.println("=== Anonymous Inner Classes Demo ===\n");

        demo1_SingleMethodInterface();
        demo2_MultiMethodInterface();
        demo3_ExtendAbstractClass();
        demo4_CapturingOuterVariables();

        // Demo 5 needs an instance (uses 'this')
        new _01_AnonymousInnerClassDemo().demo5_ThisKeyword();

        demo6_PassedAsArgument();
        demo7_ArrayOfAnonymousClasses();

        // -------------------------------------------------------
        // KEY POINTS:
        // - Anonymous classes have no name; compiled as Outer$1, Outer$2
        // - Semicolon is required after the closing } of the class body
        // - Can implement multi-method interfaces (lambdas cannot)
        // - Can extend abstract classes (lambdas cannot)
        // - 'this' inside anonymous class = anonymous class instance
        // - 'this' inside lambda          = enclosing class instance
        // - Captured variables must be final or effectively final
        // - Prefer lambda for single-abstract-method interfaces
        // -------------------------------------------------------
        System.out.println("=== Anonymous Inner Classes Demo Complete ===");
    }
}

