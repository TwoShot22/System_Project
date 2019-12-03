package os;

public class OperatingSystem {
	
	// Component
    private UXManager uxManager;
    private Loader loader;
    private ProcessManager processManager;
    private MemoryManager memoryManager;
    private FileManager fileManager;

    // Constructor
    public OperatingSystem(){
        this.uxManager = new UXManager();
        this.loader = new Loader();
        this.processManager = new ProcessManager();
        this.memoryManager = new MemoryManager();
        this.fileManager = new FileManager();
    }

    public void associate(){
        this.uxManager.associate(this.processManager, this.loader, this.fileManager);
        this.processManager.associate(this.memoryManager,this.fileManager);
    }

    public void run(){
        this.uxManager.run();
    }
}
