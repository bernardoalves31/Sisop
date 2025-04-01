package src.Sistema;

import java.util.Scanner;

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
//		so.utils.loadAndExec(progs.retrieveProgram("fatorialV2"));
		System.out.println(hw.mem.pos[0].p);
		System.out.println(hw.mem.pos[8].p);

		Word[] programImage = progs.retrieveProgram("fatorialV2");

		int numPages = (int) Math.ceilDiv(programImage.length, hw.mem.getTamPg());
		int[] tabelaPaginas = new int[numPages];

//		hw.mem.getPages().get(1).setFree(false);
		if (so.gm.canAlloc(programImage.length, tabelaPaginas)) {
		so.gm.load(programImage, tabelaPaginas);
		}
		so.utils.dump(0, programImage.length);
		System.out.println(hw.mem.pos[0].p);
		System.out.println(hw.mem.pos[8].p);
		hw.cpu.setContext(0);
		hw.cpu.run();
		so.utils.dump(0, programImage.length);
		so.gm.free(tabelaPaginas);
		so.utils.dump(0, programImage.length);

		//so.gm.pageControl();
	}

	public void menu() {
		System.out.println("System started");
		Scanner scanner = new Scanner(System.in);
		String option = null;

		while (!option.equals("0")) {
			System.out.println("---------------------------");
			System.out.println("1 - New Program");
			System.out.println("2 - Remove id");
			System.out.println("3 - List all process");
			System.out.println("4 - List program");
			System.out.println("5 - List memory");
			System.out.println("6 - Trace on");
			System.out.println("7 - Trace off");
			System.out.println("0 - Exit");
			System.out.println("---------------------------");

			option = scanner.next();
			switch (option) {
				case "1":
					break;
				case "2":
					System.out.println("Insert Id to remove");
					String input = scanner.next();
					int id = Integer.parseInt(input);
					if(!this.so.gp.freeProcess(id)) {
						System.out.println("Invalid id process");
					}
					System.out.println("Process removed sucessfully");
					break;

				case "3":
					so.gp.ps(); // List all processes
					break;
				
				case "4":
					
				default:
					break;
			}
		}

		scanner.close();
	}

	public static void main(String args[]) {
		int tamMem = 1024;
		int tamPg = 8;

		Sistema s = new Sistema(new Memory(tamMem, tamPg));
		s.run();
	}
}