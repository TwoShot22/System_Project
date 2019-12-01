package os;

public class OperatingSystem {
    private UXManager uxManager;
    private Loader loader;
    private ProcessManager processManager;
    private MemoryManager memoryManager;
    private FileManager fileManager;

    public OperatingSystem(){
        this.uxManager = new UXManager();
        this.loader = new Loader();
        this.processManager = new ProcessManager();
        this.memoryManager = new MemoryManager();
        this.fileManager = new FileManager();
    }

    public void associate(){
        // UX Manager가 모든 Manager를 총괄 - UX Manager는 모든걸 Associate 해야 함
        this.uxManager.associate(this.processManager, this.loader);
        this.processManager.associate(this.memoryManager,this.fileManager);
    }

    public void run(){
        // 구조체를 만들어내면 해당 Memory까지 - 해당 위치를 Return 시켜 주는 것
        // Process Queue에 이걸 넣어줘야 함
        // Hardware에서 Double Click Event가 발생하면 UI Manager에게 줌
        this.uxManager.run();
    }
}
