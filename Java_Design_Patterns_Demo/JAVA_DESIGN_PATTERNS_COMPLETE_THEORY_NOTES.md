# Core Java Design Patterns - Complete Theory Notes

## 1. What Are Design Patterns?

Design patterns are proven solutions to common software design problems.

They are not libraries or fixed code that must be copied exactly. They are reusable design ideas that help you write Java code that is easier to change, test, and maintain.

---

## 2. Main Categories

Design patterns are usually grouped into three categories:

| Category | Purpose | Examples |
|----------|---------|----------|
| Creational | object creation | Singleton, Factory, Abstract Factory, Builder |
| Structural | object/class composition | Adapter, Facade, Decorator, Proxy |
| Behavioral | communication and behavior | Strategy, Observer, Command, Template Method |

---

## 3. Singleton Pattern

Singleton ensures only one object of a class exists in the application.

Common structure:

- private constructor
- static access method
- one shared instance

Use cases:

- application configuration
- logger
- shared cache
- connection manager

Demo classes:

- `AppConfiguration`
- `SingletonPatternCompleteDemo`

Important idea:

```text
Many callers -> same shared object
```

---

## 4. Factory Pattern

Factory centralizes object creation.

Instead of the client writing:

```java
new PdfReport()
```

the client asks:

```java
ReportFactory.createReport(ReportType.PDF)
```

Use cases:

- creating reports
- creating payment handlers
- creating notification senders
- creating file parsers

Demo classes:

- `Report`
- `PdfReport`
- `ExcelReport`
- `HtmlReport`
- `ReportFactory`
- `FactoryPatternCompleteDemo`

Important idea:

```text
Client asks for an object -> Factory decides which concrete class to create
```

---

## 5. Abstract Factory Pattern

Abstract Factory creates families of related objects.

In the demo, one factory creates Windows UI components and another factory creates Mac UI components.

Use cases:

- platform-specific UI
- database-specific components
- cloud-provider-specific services
- related object families that must be used together

Demo classes:

- `UiComponentFactory`
- `WindowsUiFactory`
- `MacUiFactory`
- `Button`
- `Checkbox`
- `ApplicationWindow`
- `AbstractFactoryPatternCompleteDemo`

Factory vs Abstract Factory:

| Pattern | Creates |
|---------|---------|
| Factory | one product type |
| Abstract Factory | families of related product types |

---

## 6. Builder Pattern

Builder creates complex objects step by step.

It is useful when an object has:

- many constructor parameters
- optional values
- validation rules
- readable construction needs

In the demo, an e-commerce order has customer details, delivery address, multiple items, coupon code, gift wrap, and note.

Demo classes:

- `EcommerceOrder`
- `EcommerceOrderBuilder`
- `CustomerInfo`
- `DeliveryAddress`
- `OrderItem`
- `BuilderPatternCompleteDemo`

Important idea:

```text
Avoid huge constructors -> build readable complex objects step by step
```

---

## 7. Adapter Pattern

Adapter makes an incompatible class work with the interface expected by the client.

In the demo:

```text
Client expects ModernPaymentGateway
LegacyBankPaymentService has a different method
LegacyBankPaymentAdapter converts the old API to the new interface
```

Use cases:

- using legacy code
- integrating third-party libraries
- converting old APIs to new interfaces
- wrapping incompatible classes

Demo classes:

- `ModernPaymentGateway`
- `CardPaymentGateway`
- `LegacyBankPaymentService`
- `LegacyBankPaymentAdapter`
- `PaymentResult`
- `AdapterPatternCompleteDemo`

Important idea:

```text
Adapter translates one interface into another
```

---

## 8. Facade Pattern

Facade provides one simple interface over a complex subsystem.

In the demo, placing an order requires:

- reserving inventory
- charging payment
- creating shipment
- creating invoice
- sending confirmation email

The client only calls:

```java
checkoutFacade.placeOrder(request);
```

Use cases:

- checkout systems
- startup/shutdown workflows
- complex API simplification
- hiding subsystem details

Demo classes:

- `CheckoutFacade`
- `CheckoutRequest`
- `InventoryService`
- `CheckoutPaymentService`
- `ShippingService`
- `InvoiceService`
- `CheckoutEmailService`
- `FacadePatternCompleteDemo`

Important idea:

```text
Complex subsystem -> simple public method
```

---

## 9. Decorator Pattern

Decorator adds new behavior to an object without changing its original class.

In the demo, notification behavior is built by wrapping decorators:

```text
BasicNotifier
  -> EmailNotifierDecorator
  -> SmsNotifierDecorator
  -> AuditNotifierDecorator
```

Use cases:

- adding logging
- adding compression
- adding encryption
- adding notification channels
- adding validation around existing behavior

Demo classes:

- `Notifier`
- `BasicNotifier`
- `NotifierDecorator`
- `EmailNotifierDecorator`
- `SmsNotifierDecorator`
- `AuditNotifierDecorator`
- `DecoratorPatternCompleteDemo`

Important idea:

```text
Wrap object -> add behavior -> keep same interface
```

---

## 10. Strategy Pattern

Strategy makes algorithms interchangeable.

In the demo, shipping cost calculation can use:

- standard shipping
- express shipping
- international shipping

The calculator does not need large `if-else` or `switch` blocks.

Use cases:

- payment algorithms
- discount rules
- sorting rules
- shipping calculations
- validation policies

Demo classes:

- `ShippingCostStrategy`
- `StandardShippingStrategy`
- `ExpressShippingStrategy`
- `InternationalShippingStrategy`
- `ShippingCalculator`
- `StrategyPatternCompleteDemo`

Important idea:

```text
Same context object -> different algorithms can be plugged in
```

---

## 11. Observer Pattern

Observer creates a one-to-many relationship.

When the subject changes, all registered observers are notified.

In the demo:

```text
StockMarket changes price
MobileAppSubscriber receives update
EmailAlertSubscriber receives update
```

Use cases:

- event systems
- UI listeners
- notification systems
- stock/price alerts
- publish-subscribe flows

Demo classes:

- `StockMarket`
- `StockObserver`
- `MobileAppSubscriber`
- `EmailAlertSubscriber`
- `ObserverPatternCompleteDemo`

Important idea:

```text
Subject changes -> observers are notified automatically
```

---

## 12. Command Pattern

Command turns a request into an object.

In the demo, a remote control stores commands for buttons:

- turn light on
- turn light off
- set air conditioner temperature

The remote control does not need to know how each device works.

Use cases:

- undo/redo
- task queues
- remote controls
- menu actions
- job scheduling

Demo classes:

- `RemoteCommand`
- `TurnLightOnCommand`
- `TurnLightOffCommand`
- `SetTemperatureCommand`
- `RemoteControl`
- `LightDevice`
- `AirConditionerDevice`
- `CommandPatternCompleteDemo`

Important idea:

```text
Request becomes an object that can be stored, passed, queued, or executed later
```

---

## 13. Template Method Pattern

Template Method defines the skeleton of an algorithm in a parent class.

Subclasses customize selected steps.

In the demo:

```text
DataImporter.importData()
  open file
  read data       -> subclass customizes
  validate data   -> subclass can override
  save data
  close file
```

Use cases:

- import/export workflows
- report generation steps
- game lifecycle methods
- framework hooks

Demo classes:

- `DataImporter`
- `CsvDataImporter`
- `JsonDataImporter`
- `TemplateMethodPatternCompleteDemo`

Important idea:

```text
Parent controls algorithm structure -> child customizes steps
```

---

## 14. Proxy Pattern

Proxy controls access to another object.

In the demo, `SecureDocumentProxy` checks if the user is an admin before loading and displaying the confidential document.

Use cases:

- access control
- lazy loading
- logging
- remote objects
- caching

Demo classes:

- `Document`
- `ConfidentialDocument`
- `SecureDocumentProxy`
- `UserSession`
- `ProxyPatternCompleteDemo`

Important idea:

```text
Client -> Proxy -> Real object
```

---

## 15. Pattern Comparison

| Pattern | Main Problem It Solves |
|---------|------------------------|
| Singleton | need one shared instance |
| Factory | object creation should be centralized |
| Abstract Factory | create families of related objects |
| Builder | complex object construction |
| Adapter | incompatible interface |
| Facade | complex subsystem needs simple access |
| Decorator | add behavior without modifying original class |
| Strategy | switch algorithms at runtime |
| Observer | notify many objects when one changes |
| Command | represent request as an object |
| Template Method | fixed algorithm structure with customizable steps |
| Proxy | control access to another object |

---

## 16. Choosing the Correct Pattern

| Situation | Good Pattern |
|-----------|--------------|
| only one config object should exist | Singleton |
| many concrete object types are created | Factory |
| object has many optional fields | Builder |
| old API must work with new code | Adapter |
| many services must run in a workflow | Facade |
| need to add features around an object | Decorator |
| many algorithms are selected dynamically | Strategy |
| many subscribers need updates | Observer |
| actions must be stored or executed later | Command |
| algorithm steps are mostly fixed | Template Method |
| access must be controlled | Proxy |

---

## 17. Common Mistakes

### Mistake 1
Using patterns when simple code is enough.

### Mistake 2
Confusing Factory and Builder.

Factory chooses which object to create.

Builder creates one complex object step by step.

### Mistake 3
Confusing Adapter and Facade.

Adapter changes an interface.

Facade simplifies a subsystem.

### Mistake 4
Confusing Strategy and Template Method.

Strategy changes the whole algorithm object.

Template Method keeps the algorithm structure in the parent class.

### Mistake 5
Overusing Singleton.

Singleton can create hidden global state. Use it carefully.

---

## 18. Demo Classes Included

Main runner:

```text
DesignPatternsMasterDemo
```

Run this class to execute all examples.

Included pattern demos:

- `SingletonPatternCompleteDemo`
- `FactoryPatternCompleteDemo`
- `AbstractFactoryPatternCompleteDemo`
- `BuilderPatternCompleteDemo`
- `AdapterPatternCompleteDemo`
- `FacadePatternCompleteDemo`
- `DecoratorPatternCompleteDemo`
- `StrategyPatternCompleteDemo`
- `ObserverPatternCompleteDemo`
- `CommandPatternCompleteDemo`
- `TemplateMethodPatternCompleteDemo`
- `ProxyPatternCompleteDemo`

---

## 19. Interview Questions and Answers

### Question 1
What is a design pattern?

**Answer:** A design pattern is a proven reusable solution to a common software design problem.

### Question 2
What are the main categories of design patterns?

**Answer:** Creational, structural, and behavioral.

### Question 3
What is Singleton?

**Answer:** A pattern that ensures only one instance of a class exists and provides a shared access point.

### Question 4
What is Factory?

**Answer:** A pattern that centralizes object creation and hides concrete class creation from the client.

### Question 5
What is Builder?

**Answer:** A pattern used to create complex objects step by step.

### Question 6
What is Adapter?

**Answer:** A pattern that allows an incompatible class to work with the interface expected by the client.

### Question 7
What is Facade?

**Answer:** A pattern that provides a simple interface to a complex subsystem.

### Question 8
What is Decorator?

**Answer:** A pattern that adds behavior to an object dynamically without changing its class.

### Question 9
What is Strategy?

**Answer:** A pattern that lets an algorithm be selected or changed at runtime.

### Question 10
What is Observer?

**Answer:** A pattern where many observers are notified when a subject changes.

### Question 11
What is Command?

**Answer:** A pattern that encapsulates a request as an object.

### Question 12
What is Proxy?

**Answer:** A pattern that controls access to another object.

---

## 20. Quick Revision

```text
Singleton        -> one shared instance
Factory          -> create object without exposing creation logic
Abstract Factory -> create related object families
Builder          -> build complex object step by step
Adapter          -> convert incompatible interface
Facade           -> simplify complex subsystem
Decorator        -> add behavior by wrapping object
Strategy         -> swap algorithms
Observer         -> notify subscribers
Command          -> request as object
Template Method  -> fixed algorithm with customizable steps
Proxy            -> controlled access to real object
```

---

## 21. Final Conclusion

Design patterns are most useful when they solve a real design problem.

In core Java, the key is not only remembering pattern names. The real skill is understanding:

- what problem the pattern solves
- which classes take which responsibility
- how the pattern improves flexibility
- when a simple solution is better

Good design patterns make Java code easier to extend, test, and maintain.
