package src.Sistema;

public class Sistema {
    public HW hw;
	public SO so;
	public Programs progs;
	
	public Sistema(Memory mem) {
		hw = new HW(mem);
		           // memoria do HW tem tamMem palavras
		so = new SO(hw);
		hw.cpu.setUtilities(so.utils); // permite cpu fazer dump de memoria ao avancar
		progs = new Programs();
	}

	public void run() {
		//so.utils.loadAndExec(progs.retrieveProgram("fatorialV2"));


		// System.out.println(hw.mem.pos[0].p);
		// Word[] programImage = progs.retrieveProgram("fatorialV2");

        // int numPages = (int) Math.ceilDiv(programImage.length, hw.mem.getTamPg());
		// int[] tabelaPaginas = new int[numPages];
		
		// hw.mem.getPages().get(1).setFree(false);
		// if (so.gm.alloc(programImage.length, tabelaPaginas)) {
		// 	so.gm.load(programImage, tabelaPaginas);
		// }
		// System.out.println(hw.mem.pos[0].p);
		// System.out.println(hw.mem.pos[8].p);

		//so.gm.pageControl();
	}
    
	public static void main(String args[]) {
		int tamMem = 1024;
		int tamPg = 8;
		
		Sistema s = new Sistema(new Memory(tamMem, tamPg));
		s.run();
	}
}