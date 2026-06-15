package diDemo;

// ============================================================
// TOPIC: Dependency Injection with Factory and Adapter Patterns
// EXAMPLE: Laptop depends on a HardDisk
// ============================================================
// - Laptop does not create the hard disk by itself.
// - Laptop receives a HardDisk object from outside.
// - Factory decides which HardDisk implementation to create.
// - Adapter converts an incompatible USB drive into the HardDisk interface.
// ============================================================

public class _01_LaptopHardDiskDIFactoryAdapterDemo {

    public static void main(String[] args) {
        System.out.println("=== DI + Factory + Adapter Demo ===\n");

        Laptop gamingLaptop = LaptopFactory.createLaptop("Predator Gaming Laptop", HardDiskType.NVME);
        gamingLaptop.start();
        gamingLaptop.saveProject("Java training notes");
        gamingLaptop.openProject();

        System.out.println();

        Laptop officeLaptop = LaptopFactory.createLaptop("Office Laptop", HardDiskType.SATA);
        officeLaptop.start();
        officeLaptop.saveProject("Monthly report");
        officeLaptop.openProject();

        System.out.println();

        Laptop backupLaptop = LaptopFactory.createLaptop("Backup Laptop", HardDiskType.EXTERNAL_USB);
        backupLaptop.start();
        backupLaptop.saveProject("Backup copy of documents");
        backupLaptop.openProject();

        System.out.println("\n=== Demo Complete ===");
    }
}

// ------------------------------------------------------------
// Common abstraction used by the Laptop
// ------------------------------------------------------------

interface HardDisk {
    String getDiskName();

    void writeData(String data);

    String readData();
}

// ------------------------------------------------------------
// Concrete dependency 1
// ------------------------------------------------------------

class SataHardDisk implements HardDisk {
    private String savedData = "No SATA data yet";

    @Override
    public String getDiskName() {
        return "SATA Hard Disk";
    }

    @Override
    public void writeData(String data) {
        savedData = data;
        System.out.println("  SATA disk saved data: " + data);
    }

    @Override
    public String readData() {
        return "SATA disk reading: " + savedData;
    }
}

// ------------------------------------------------------------
// Concrete dependency 2
// ------------------------------------------------------------

class NvmeHardDisk implements HardDisk {
    private String savedData = "No NVMe data yet";

    @Override
    public String getDiskName() {
        return "NVMe SSD";
    }

    @Override
    public void writeData(String data) {
        savedData = data;
        System.out.println("  NVMe SSD saved data very fast: " + data);
    }

    @Override
    public String readData() {
        return "NVMe SSD reading quickly: " + savedData;
    }
}

// ------------------------------------------------------------
// Incompatible class: it does not implement HardDisk
// ------------------------------------------------------------

class ExternalUsbDrive {
    private String copiedFile = "No USB file yet";

    public String getUsbDeviceName() {
        return "External USB Drive";
    }

    public void copyFileToUsb(String fileContent) {
        copiedFile = fileContent;
        System.out.println("  USB drive copied file: " + fileContent);
    }

    public String loadFileFromUsb() {
        return "USB drive loaded file: " + copiedFile;
    }
}

// ------------------------------------------------------------
// Adapter Pattern
// Makes ExternalUsbDrive compatible with the HardDisk interface.
// ------------------------------------------------------------

class UsbHardDiskAdapter implements HardDisk {
    private final ExternalUsbDrive externalUsbDrive;

    public UsbHardDiskAdapter(ExternalUsbDrive externalUsbDrive) {
        this.externalUsbDrive = externalUsbDrive;
    }

    @Override
    public String getDiskName() {
        return externalUsbDrive.getUsbDeviceName() + " connected through adapter";
    }

    @Override
    public void writeData(String data) {
        externalUsbDrive.copyFileToUsb(data);
    }

    @Override
    public String readData() {
        return externalUsbDrive.loadFileFromUsb();
    }
}

// ------------------------------------------------------------
// Consumer class
// Dependency Injection happens here through the constructor.
// ------------------------------------------------------------

class Laptop {
    private final String model;
    private final HardDisk hardDisk;

    public Laptop(String model, HardDisk hardDisk) {
        this.model = model;
        this.hardDisk = hardDisk;
    }

    public void start() {
        System.out.println(model + " started with " + hardDisk.getDiskName());
    }

    public void saveProject(String projectData) {
        System.out.println(model + " is saving project data...");
        hardDisk.writeData(projectData);
    }

    public void openProject() {
        System.out.println(model + " opened project -> " + hardDisk.readData());
    }
}

// ------------------------------------------------------------
// Factory Pattern
// Creates the correct dependency object for the Laptop.
// ------------------------------------------------------------

enum HardDiskType {
    SATA,
    NVME,
    EXTERNAL_USB
}

class HardDiskFactory {
    private HardDiskFactory() {
    }

    public static HardDisk createHardDisk(HardDiskType type) {
        switch (type) {
            case SATA:
                return new SataHardDisk();
            case NVME:
                return new NvmeHardDisk();
            case EXTERNAL_USB:
                return new UsbHardDiskAdapter(new ExternalUsbDrive());
            default:
                throw new IllegalArgumentException("Unsupported hard disk type: " + type);
        }
    }
}

// ------------------------------------------------------------
// Factory + DI together
// This factory creates the dependency and injects it into Laptop.
// ------------------------------------------------------------

class LaptopFactory {
    private LaptopFactory() {
    }

    public static Laptop createLaptop(String model, HardDiskType hardDiskType) {
        HardDisk hardDisk = HardDiskFactory.createHardDisk(hardDiskType);
        return new Laptop(model, hardDisk);
    }
}
