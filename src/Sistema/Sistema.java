package src.Sistema;

public class Sistema {
    public HW hw;
	public SO so;
	public Programs progs;

	public Sistema(int tamMem) {
		hw = new HW(tamMem);           // memoria do HW tem tamMem palavras
		so = new SO(hw);
		hw.cpu.setUtilities(so.utils); // permite cpu fazer dump de memoria ao avancar
		progs = new Programs();
	}

	public void run() {
		so.utils.loadAndExec(progs.retrieveProgram("fatorialV2"));
	}
    
	public static void main(String args[]) {
		Sistema s = new Sistema(1024);
		s.run();
	}
}