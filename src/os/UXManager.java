package os;

import java.util.Scanner;
import java.util.Vector;

import processManager.Loader;
import processManager.Process;
import processManager.ProcessManager;

public class UXManager {

	// Association
    private ProcessManager processManager;
    private Loader loader;
    private FileManager fileManager;
    
    public void associate(ProcessManager processManager, Loader loader, FileManager fileManager){
        this.processManager = processManager;
        this.loader = loader;
        this.fileManager = fileManager;
    }

    public void run(){// p3 p3 p3 입력시 섞여 나온다 -> 멀티 쓰레딩.
        Scanner scanner = new Scanner(System.in);
        String[] names = scanner.nextLine().split(" ");
        Vector<Process> processes = new  Vector<Process>();
        for(String name : names) {
        	String fileAddress = this.fileManager.getFileAddress(name);
        	Process process = this.loader.load(fileAddress);
        	processes.add(process);
        }
        scanner.close();
        for(Process p : processes) {
        	new Thread() {
        		public void run() {
        			processManager.execute(p);
        		}
        	}.start();
        }
    }
}
