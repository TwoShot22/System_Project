package os;

import java.util.Scanner;

public class UXManager {

    private ProcessManager processManager;
    private Loader loader;

    public UXManager(){

    }

    public void associate(ProcessManager processManager, Loader loader){
        this.processManager = processManager;
        this.loader = loader;
    }

    public void run(){
        Scanner scanner = new Scanner(System.in);
        String fileName = scanner.nextLine();

        Process process = this.loader.load(fileName);
        this.processManager.execute(process);
    }
}
