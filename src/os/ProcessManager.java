package os;

public class ProcessManager {
    // Process의 생성과 소멸, 무엇을 할 지를 제어하는 것을 관리한다고 함
    // Hard Disk를 관리하는 File Management System & Memory Manager를 알아야 함
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
        // Loader가 더블클릭 하면 Loader가 해당 Hard Disk에서 그대로 메모리에 쭉 올림 - File을 Read한다는 것은 Memory에 올려놓는 것
        // Parsing을 해서 EXE의 Header를 보고 해석을 할 것 - Process ID, 위치 등을 파악
        // FCB(File Control Block)에 File을 누가 언제 만들었는지 나와있음
        Process.EState eProcessState = Process.EState.running;
        process.initialize(TIMESLICE);

        // Interrupt가 발생 or Time Slice 만료 or 중간에 Program이 종료
        while (eProcessState==Process.EState.running) {
            eProcessState = process.executeALine();
            if(eProcessState == Process.EState.terminated){ } else if (eProcessState == Process.EState.wait){ } else if (eProcessState == Process.EState.ready){ }
        }
        // this.cpu.restore(process.getPCB());
    }
}
