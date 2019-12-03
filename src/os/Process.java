package os;

import java.util.Vector;

public class Process {
	// Components
	private PCB pcb;
	private Segment codeSegment;
	private Segment stackSegment;
	// Working Variables (Getter&Setter No Required)
	private int timeSlice;

	public Process(int stackSegmentSize, int[] codes) {
		this.pcb = new PCB();
		this.codeSegment = new Segment(codes);
		this.stackSegment = new Segment(stackSegmentSize);
	}

	public PCB getPcb() {
		return pcb;
	}

	public void setPcb(PCB pcb) {
		this.pcb = pcb;
	}

	public void initialize(int timeSlice) {
		this.pcb.seteState(Process.EState.running);
		this.timeSlice = timeSlice;
	}

	public EState executeALine() {
		Vector<Register> registers = this.pcb.getRegisters();
		registers.get(ERegister.eMAR.ordinal()).set(registers.get(ERegister.ePC.ordinal()).get());
		this.codeSegment.fetch();
		int instruction = registers.get(ERegister.eMBR.ordinal()).get();
		registers.get(ERegister.eIR.ordinal()).set(instruction);
		/////////////////////////////////////////////////////////////////////////////////////////////////////
		// CPU Execute
		instruction >>>= 16;
		Instruction inst = Instruction.values()[instruction];
		switch (inst) {
			case ldi: ldi();break;
			case lda: lda();break;
			case sta: sta();break;
			case addi: addi();break;
			case cmp: cmp();break;
			case igz: igz();break;
            case halt: halt();break;
            case prt: prt();break;
		}
		/////////////////////////////////////////////////////////////////////////////////////////////////////

		if (registers.get(ERegister.eStatus.ordinal()).get() == EInterrupt.eIO.ordinal()) {
			return Process.EState.wait;
		} else if (registers.get(ERegister.eStatus.ordinal()).get() == EInterrupt.eTerminate.ordinal()) {
			return Process.EState.terminated;
		}
		return EState.running;
	}

	private void prt() {
		Vector<Register> registers = this.pcb.getRegisters();
		Register pc = registers.get(ERegister.ePC.ordinal());
		pc.set(pc.get()+1);
		System.out.println("Prt : "+registers.get(ERegister.eAC.ordinal()).get());
	}

	private void halt() {
		System.out.println("halt");
		this.pcb.getRegisters().get(ERegister.eStatus.ordinal()).set(EInterrupt.eTerminate.ordinal());
	}
	// public Segment getCodeSegment() { return codeSegment; }
	// public void setCodeSegment(Segment codeSegment) { this.codeSegment = codeSegment; }
	// public Segment getStackSegment() { return stackSegment; }
	// public void setStackSegment(Segment stackSegment) { this.stackSegment = stackSegment; }

	private void igz() {
		Vector<Register> registers = this.pcb.getRegisters();
		System.out.println("igz");
		Register pc = registers.get(ERegister.ePC.ordinal());
		pc.set(pc.get()+1);
	}

	private void cmp() {
		Vector<Register> registers = this.pcb.getRegisters();
		System.out.println("cmp");
		Register pc = registers.get(ERegister.ePC.ordinal());
		pc.set(pc.get()+1);
	}

	private void addi() {
		System.out.println("addi");
		Vector<Register> registers = this.pcb.getRegisters();
		int address = registers.get(ERegister.eIR.ordinal()).get()&0x0000ffff;
		Register ac = registers.get(ERegister.eAC.ordinal());
		ac.set(address);
		ac.set(registers.get(ERegister.eMBR.ordinal()).get()+ac.get());
		Register pc = registers.get(ERegister.ePC.ordinal());
		pc.set(pc.get()+1);
	}

	private void sta() {
		System.out.println("sta");
		Vector<Register> registers = this.pcb.getRegisters();
		int address = registers.get(ERegister.eIR.ordinal()).get()&0x0000ffff;
		address /= 4;
		registers.get(ERegister.eMAR.ordinal()).set(address);
		this.stackSegment.store();
		Register pc = registers.get(ERegister.ePC.ordinal());
		pc.set(pc.get()+1);
	}

	private void lda() {
		System.out.println("lda");
	    Vector<Register> registers = this.pcb.getRegisters();
        int address = registers.get(ERegister.eIR.ordinal()).get()&0x0000ffff;
        address /= 4;
		registers.get(ERegister.eMAR.ordinal()).set(address);
		this.stackSegment.fetch();
		registers.get(ERegister.eAC.ordinal()).set(registers.get(ERegister.eMBR.ordinal()).get());
		Register pc = registers.get(ERegister.ePC.ordinal());
		pc.set(pc.get()+1);
	}

	private void ldi() {
		System.out.println("ldi");
		Vector<Register> registers = this.pcb.getRegisters();
		int address = registers.get(ERegister.eIR.ordinal()).get()&0x0000ffff;
		registers.get(ERegister.eAC.ordinal()).set(address);
		Register pc = registers.get(ERegister.ePC.ordinal());
		pc.set(pc.get()+1);
	}

	// Global Constants
	private enum Instruction {
		ldi, lda, sta, addi, cmp, igz, halt, prt
	}

	public enum EState {nnew, running, wait, ready, terminated}

	public enum ERegister {ePC, eCS, eDS, eSS, eHS, eMAR, eMBR, eIR, eStatus, eAC}

	public enum EInterrupt {eNone, eIO, eTerminate}

	///////////////////////////////////////////////////////////////////////////////////////
	private class PCB {
		private int id;
		private EState eState;

		private Vector<Register> registers;

		public PCB() {
			this.registers = new Vector<>();
			for (ERegister eRegister : ERegister.values()) {
				this.registers.add(new Register());
			}
		}

		public int getId() {
			return id;
		}

		public void setId(int id) {
			this.id = id;
		}

		public EState geteState() {
			return eState;
		}

		public void seteState(EState eState) {
			this.eState = eState;
		}

		public Vector<Register> getRegisters() {
			return registers;
		}

		public void setRegisters(Vector<Register> registers) {
			this.registers = registers;
		}
	}

	///////////////////////////////////////////////////////////////////////////////////////
	private class Segment {
		private int[] memory;

		public Segment(int size) {
			this.memory = new int[size];
		}

		public Segment(int[] memory) {
			this.memory = memory;
		}

		public void store() {
			Vector<Register> registers = pcb.getRegisters();
			this.memory[registers.get(ERegister.eMAR.ordinal()).get()] = registers.get(ERegister.eMBR.ordinal()).get();
		}

		public void fetch() {
			Vector<Register> registers = pcb.getRegisters();
			registers.get(ERegister.eMBR.ordinal()).set(this.memory[registers.get(ERegister.eMAR.ordinal()).get()]);
		}
	}

	///////////////////////////////////////////////////////////////////////////////////////
	private class Register {
		private int value;

		public Register() {
			this.value = 0;
		}

		public int get() {
			return value;
		}

		public void set(int value) {
			this.value = value;
		}
	}
}
