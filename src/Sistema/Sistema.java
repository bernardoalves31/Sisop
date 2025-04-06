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
		menu();
//		so.utils.loadAndExec(progs.retrieveProgram("fatorialV2"));
		System.out.println(hw.mem.pos[0].p);
		System.out.println(hw.mem.pos[8].p);

		Word[] programImage = progs.retrieveProgram("fatorialV2");

		int[] tabelaPaginas = new int[so.gp.calcNumPages(programImage)];

		// hw.mem.getPages().get(0).setFree(false);
		hw.mem.getPages().get(1).setFree(false);
		// hw.mem.getPages().get(2).setFree(false);
		hw.mem.getPages().get(3).setFree(false);
		// hw.mem.getPages().get(4).setFree(false);
		so.gp.createProcess(programImage, tabelaPaginas);
		// if (so.gm.canAlloc(programImage.length, tabelaPaginas)) {
		// so.gm.load(programImage, tabelaPaginas);
		// }
		so.utils.dump(0, programImage.length);
		System.out.println(hw.mem.pos[0].p);
		System.out.println(hw.mem.pos[8].p);
		hw.cpu.setContext(0);
		so.gp.listProcess(0);
		hw.cpu.run(tabelaPaginas);
		System.out.println("-------------------------------------------- end of program 1\n\n");
		hw.mem.getPages().get(1).setFree(true);
		hw.mem.getPages().get(3).setFree(true);
		int[] tabelaPaginas2 = new int[so.gp.calcNumPages(programImage)];
		Word[] programImage2 = progs.retrieveProgram("fatorialV2");
		so.gp.createProcess(programImage2, tabelaPaginas2);
		hw.cpu.setContext(0);
		hw.cpu.run(tabelaPaginas2);

		so.utils.dump(0, 64);
	//	so.gp.listProcess(0);
	//	so.gp.freeProcess(0);
	//	so.gp.ps();
	//	so.utils.dump(0, 64);
	//	so.gm.free(tabelaPaginas);
	//	so.utils.dump(0, programImage.length);

		//so.gm.pageControl();
	}

	public void menu() {
		System.out.println("System started");
		Scanner scanner = new Scanner(System.in);
		String option = "";

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
					System.out.println("Programs list:");
					System.out.println("1 - fatorial");
					System.out.println("2 - fatorialV2");
					System.out.println("3 - progMinimo");
					System.out.println("4 - fibonacci10");
					System.out.println("5 - fibonacci10v2");
					System.out.println("6 - fibonacciREAD");
					System.out.println("7 - PB");
					System.out.println("8 - PC");
					System.out.println("0 - back");

					option = scanner.next();

					switch (option) {
						case "1":
							int[] tabelaPaginas = new int[so.gp.calcNumPages(progs.retrieveProgram("fatorial"))];
							if(!so.gp.createProcess(progs.retrieveProgram("fatorial"), tabelaPaginas)){
								System.out.println("Error creating process");
							}
							System.out.println("Process created successfully");

							break;
						case "2":
							tabelaPaginas = new int[so.gp.calcNumPages(progs.retrieveProgram("fatorialV2"))];
							if(!so.gp.createProcess(progs.retrieveProgram("fatorialV2"), tabelaPaginas)){
								System.out.println("Error creating process");
							}
							System.out.println("Process created successfully");
							break;
						case "3":
							tabelaPaginas = new int[so.gp.calcNumPages(progs.retrieveProgram("progMinimo"))];
							if(!so.gp.createProcess(progs.retrieveProgram("progMinimo"), tabelaPaginas)){
								System.out.println("Error creating process");
							}
							System.out.println("Process created successfully");
							break;
						case "4":
							tabelaPaginas = new int[so.gp.calcNumPages(progs.retrieveProgram("fibonacci10"))];
							if(!so.gp.createProcess(progs.retrieveProgram("fibonacci10"), tabelaPaginas)){
								System.out.println("Error creating process");
							}
							System.out.println("Process created successfully");
							break;
						case "5":	
							tabelaPaginas = new int[so.gp.calcNumPages(progs.retrieveProgram("fibonacci10v2"))];
							if(!so.gp.createProcess(progs.retrieveProgram("fibonacci10v2"), tabelaPaginas)){
								System.out.println("Error creating process");
							}
							System.out.println("Process created successfully");
							break;
						case "6":
							tabelaPaginas = new int[so.gp.calcNumPages(progs.retrieveProgram("fibonacciREAD"))];
							if(!so.gp.createProcess(progs.retrieveProgram("fibonacciREAD"), tabelaPaginas)){
								System.out.println("Error creating process");
							}
							System.out.println("Process created successfully");
							break;
						case "7":
							tabelaPaginas = new int[so.gp.calcNumPages(progs.retrieveProgram("PB"))];
							if(!so.gp.createProcess(progs.retrieveProgram("PB"), tabelaPaginas)){
								System.out.println("Error creating process");
							}
							System.out.println("Process created successfully");
							break;
						case "8":
							tabelaPaginas = new int[so.gp.calcNumPages(progs.retrieveProgram("PC"))];
							if(!so.gp.createProcess(progs.retrieveProgram("PC"), tabelaPaginas)){
								System.out.println("Error creating process");
							}
							System.out.println("Process created successfully");
							break;
						case "0":
							System.out.println("Back to main menu");
							break;
						default:
							break;
					}
					
					break;
				case "2":
					System.out.println("Insert Id to remove");
					String input = scanner.next();
					int id = Integer.parseInt(input);
					if(!so.gp.freeProcess(id)) {
						System.out.println("Invalid id process");
					}
					System.out.println("Process removed sucessfully");
					break;

				case "3":
					so.gp.ps(); // List all processes
					break;
				
				case "4":
					System.out.println("Insert Id to list");
					input = scanner.next();
					id = Integer.parseInt(input);
					so.gp.listProcess(id); // List process
					break;

				case "5":
					System.out.println("List memory from/at");
					System.out.println("Insert start position");
					input = scanner.next();
					int start = Integer.parseInt(input);
					System.out.println("Insert end position");
					input = scanner.next();
					int end = Integer.parseInt(input);
					so.utils.dump(start, end); // List memory
					break;
				case "6":
					hw.cpu.setDebug(true);
					System.out.println("Debug mode on");
					break;
				case "7":
					hw.cpu.setDebug(false);
					System.out.println("Debug mode off");
					break;

				case "0":
					System.out.println("Exiting...");
					break;

				default:
					System.out.println("Error - invalid option");
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