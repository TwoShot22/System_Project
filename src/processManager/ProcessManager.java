package processManager;

import os.FileManager;
import os.MemoryManager;

public class ProcessManager {
    private Loader loader;

    private ProcessQueue readyQueue;
    private ProcessQueue ioQueue;

    private FileManager fileManager;
    private MemoryManager memoryManager;

    public ProcessManager(){
        this.loader = new Loader();
        this.readyQueue = new ProcessQueue();
        this.ioQueue = new ProcessQueue();
    }

    public void associate(MemoryManager memoryManager, FileManager fileManager){
        this.fileManager = fileManager;
        this.memoryManager = memoryManager;
    }

    public static int TIMESLICE = 10;

    public void execute(Process process){
        Process.EState eProcessState = Process.EState.running;
        process.initialize(TIMESLICE);

        while (eProcessState==Process.EState.running) {
            eProcessState = process.executeALine();
            if(eProcessState == Process.EState.terminated){ } else if (eProcessState == Process.EState.wait){ } else if (eProcessState == Process.EState.ready){ }
        }
        // this.cpu.restore(process.getPCB());
    }
}
