package os;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.Vector;

public class Loader {
    // EXE를 Process로 바꿔서 Memory에 올려줌
    public Process load(String fileName){
        try {
            int stackSegmentSize = 0;
            int[] codes = null;

            File file = new File(fileName);
            Scanner scanner = new Scanner(file);

            while(scanner.hasNextLine()){
                String line = scanner.nextLine();
                if(line.contains(".stack")){
                    stackSegmentSize = this.parseStack(scanner);
                    System.out.println("stackSegmentSize : "+stackSegmentSize);
                } else if(line.contains(".code")){
                    codes = this.parseCode(scanner);
                }
            }

            Process process = new Process(stackSegmentSize, codes);
            scanner.close();
            return process;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    private int parseStack(Scanner scanner){
        return Integer.parseInt(scanner.nextLine().split(" ")[2]);
    }

    private int[] parseCode(Scanner scanner){
        Vector<Integer> temp = new Vector<>();
        while (scanner.hasNext()) {
            String inst = scanner.next();
            if (inst.contains("//")||inst.isEmpty()) {
                scanner.nextLine();
                continue;
            }
            int insti = Instruction.valueOf(inst).ordinal();
            int address;
            try {
                address = scanner.nextInt();
            } catch (Exception e) {
                address = 0;
            }
            temp.add((insti<<16)+address);
            System.out.println("instruction : "+inst+" // Address or Value : "+address);
        }
        int[] array = new int[temp.size()];
        for (int i = 0;i<array.length;i++){
            array[i] = temp.get(i);
        }
        return array;
    }
    private enum Instruction{
        ldi, lda, sta, addi, cmp, igz, halt
    }
}
