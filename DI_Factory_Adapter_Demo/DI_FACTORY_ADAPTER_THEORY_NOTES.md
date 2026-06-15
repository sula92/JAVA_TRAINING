# Dependency Injection with Factory and Adapter Pattern - Theory Notes

## 1. Main Idea

This demo combines three important design ideas:

- Dependency Injection
- Factory Design Pattern
- Adapter Design Pattern

The real-world example is:

```text
Laptop depends on HardDisk
```

A laptop needs a hard disk to save and read data. But the laptop should not create the hard disk by itself. Instead, the hard disk is provided from outside.

That is Dependency Injection.

---

## 2. Dependency in This Example

The dependency is the `HardDisk`.

The dependent class is the `Laptop`.

```text
Laptop needs HardDisk to work
```

If the laptop directly creates a specific hard disk like `new SataHardDisk()`, the laptop becomes tightly coupled to that hard disk type.

---

## 3. Dependency Injection in This Example

The `Laptop` class receives a `HardDisk` through its constructor.

```java
public Laptop(String model, HardDisk hardDisk) {
    this.model = model;
    this.hardDisk = hardDisk;
}
```

This is constructor injection.

The laptop depends on the `HardDisk` interface, not on a specific class like `SataHardDisk` or `NvmeHardDisk`.

---

## 4. Why Use an Interface?

The `HardDisk` interface gives all hard disk types the same common behavior:

```java
interface HardDisk {
    String getDiskName();
    void writeData(String data);
    String readData();
}
```

Because of this, the laptop can use:

- `SataHardDisk`
- `NvmeHardDisk`
- `UsbHardDiskAdapter`

without changing the `Laptop` class.

---

## 5. Factory Design Pattern in This Example

The factory creates the correct hard disk object.

```java
HardDisk hardDisk = HardDiskFactory.createHardDisk(HardDiskType.NVME);
```

The `LaptopFactory` then injects that hard disk into the laptop.

```java
HardDisk hardDisk = HardDiskFactory.createHardDisk(hardDiskType);
return new Laptop(model, hardDisk);
```

So object creation is handled by the factory, while the laptop only focuses on using the hard disk.

---

## 6. Factory and DI Flow

```text
User selects hard disk type
        |
        v
HardDiskFactory creates correct HardDisk object
        |
        v
LaptopFactory injects HardDisk into Laptop constructor
        |
        v
Laptop uses HardDisk interface
```

This keeps the laptop loosely coupled.

---

## 7. Adapter Design Pattern in This Example

The `ExternalUsbDrive` is useful, but it does not implement the `HardDisk` interface.

It has different method names:

```java
copyFileToUsb()
loadFileFromUsb()
```

The laptop expects:

```java
writeData()
readData()
```

So we create an adapter:

```java
class UsbHardDiskAdapter implements HardDisk
```

The adapter wraps the USB drive and translates the laptop's expected method calls into USB drive method calls.

---

## 8. Adapter Flow

```text
Laptop calls writeData()
        |
        v
UsbHardDiskAdapter receives the call
        |
        v
Adapter calls copyFileToUsb() on ExternalUsbDrive
```

The laptop does not know that it is using a USB drive. It only knows that it has a `HardDisk`.

---

## 9. Why This Design Is Better

| Problem | Solution |
|--------|----------|
| Laptop should not create its own hard disk | Use Dependency Injection |
| Object creation logic should be centralized | Use Factory Pattern |
| External USB drive has incompatible methods | Use Adapter Pattern |
| Laptop should work with many disk types | Depend on `HardDisk` interface |

---

## 10. Important Classes in the Demo

| Class or Interface | Responsibility |
|-------------------|----------------|
| `HardDisk` | Common abstraction for all disk types |
| `SataHardDisk` | Normal hard disk implementation |
| `NvmeHardDisk` | Faster SSD implementation |
| `ExternalUsbDrive` | Incompatible external class |
| `UsbHardDiskAdapter` | Converts USB drive into a `HardDisk` |
| `HardDiskFactory` | Creates hard disk objects |
| `LaptopFactory` | Creates laptop and injects the hard disk |
| `Laptop` | Uses the injected hard disk |

---

## 11. Key Interview Explanation

In this example, the laptop depends on the `HardDisk` interface. The actual hard disk object is created by a factory and injected into the laptop through the constructor. If the hard disk is an external USB drive with incompatible methods, an adapter converts it into the `HardDisk` interface. This gives loose coupling, flexible object creation, and compatibility with existing classes.

---

## 12. Quick Revision

```text
Dependency Injection -> give the laptop its hard disk from outside
Factory Pattern      -> create the correct hard disk object
Adapter Pattern      -> make an incompatible USB drive act like a hard disk
Loose Coupling       -> laptop depends on HardDisk interface
```

---

## 13. Final Conclusion

Dependency Injection, Factory Pattern, and Adapter Pattern work well together.

In this laptop and hard disk example:

- DI keeps `Laptop` flexible.
- Factory keeps object creation separate.
- Adapter allows an incompatible USB drive to be used like a normal hard disk.

This is a clean way to design Java programs that are easy to change, test, and extend.
