package os;

public class FileManager {

	private enum Exe { // ~HDD
		p1("p1", "exe/p1.ppp"),
		p2("p2", "exe/p2.ppp"),
		p3("p3", "exe/p3.ppp"),
		;
		private String name, address;
		private Exe (String name, String address) {
			this.name=name; 
			this.address=address; 
		}
		public String getName() {return name;}
		public String getAddress() {return address;}
	}
	
	public String getFileAddress(String fileName) {
		for(Exe exe : Exe.values()) {
			if(exe.getName().equals(fileName)) {return exe.getAddress();}
		}
		return null;
	}
}
