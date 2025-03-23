package src.Sistema;

public class Sistema {
    public HW hw;
	public SO so;
	public Programs progs;

	public Sistema(Memory mem) {
		hw = new HW(mem);           // memoria do HW tem tamMem palavras
		so = new SO(hw);
		hw.cpu.setUtilities(so.utils); // permite cpu fazer dump de memoria ao avancar
		progs = new Programs();
	}

	public void run() {
		so.utils.loadAndExec(progs.retrieveProgram("fatorialV2"));
	}
    
	public static void main(String args[]) {
		int tamMem = 1024;
		int tamPg = 8;
		
		Sistema s = new Sistema(new Memory(tamMem, tamPg));
		s.run();
	}
}