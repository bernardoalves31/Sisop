package src.Sistema;

import java.util.Scanner;

import src.Sistema.GP.PCB;

public class Sistema {
	public HW hw;
	public SO so;
	public Programs progs;

	public Sistema(Memory mem) {
		hw = new HW(mem);
		// memoria do HW tem tamMem palavras
		so = new SO(hw);
		hw.cpu.setUtilities(so.utils); // permite cpu fazer dump de memoria ao avancar
		progs = new Programs(mem.getTamPg());
	}

	public void run() {
		int[] tabelaPaginas = new int[so.gp.calcNumPages(progs.retrieveProgram("NOP"))];
		if (!so.gp.createProcess(progs.retrieveProgram("NOP"), tabelaPaginas)) {
			System.out.println("Error initializing system");
		}

		Thread menuThread = new Thread(new Runnable() {
			@Override
			public void run() {
				menu();
			}
		});
		menuThread.start();
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
			System.out.println("8 - Execute all");
			System.out.println("0 - Exit");
			System.out.println("---------------------------");

			option = scanner.next(); // System takes the user input
			if (hw.device.mutexShell) { // If device wants to take the input
				hw.device.setUserInput(option);
				option = scanner.next(); // Shell takes the control again
			}

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

					option = scanner.next(); // System takes the user input
					if (hw.device.mutexShell) { // If device wants to take the input
						hw.device.setUserInput(option); 
						hw.device.mutexShell = false;
						option = scanner.next(); // Shell takes the control again
					}

					switch (option) {
						case "1":
							int[] tabelaPaginas = new int[so.gp.calcNumPages(progs.retrieveProgramPage("fatorial", 0))];
							if (!so.gp.createProcess(progs.retrieveProgramPage("fatorial",0), tabelaPaginas)) {
								System.out.println("Error creating process");
							}
							System.out.println("Process created successfully");

							break;
						case "2":
							tabelaPaginas = new int[so.gp.calcNumPages(progs.retrieveProgram("fatorialV2"))];
							if (!so.gp.createProcess(progs.retrieveProgram("fatorialV2"), tabelaPaginas)) {
								System.out.println("Error creating process");
							}
							System.out.println("Process created successfully");
							break;
						case "3":
							tabelaPaginas = new int[so.gp.calcNumPages(progs.retrieveProgram("progMinimo"))];
							if (!so.gp.createProcess(progs.retrieveProgram("progMinimo"), tabelaPaginas)) {
								System.out.println("Error creating process");
							}
							System.out.println("Process created successfully");
							break;
						case "4":
							tabelaPaginas = new int[so.gp.calcNumPages(progs.retrieveProgram("fibonacci10"))];
							if (!so.gp.createProcess(progs.retrieveProgram("fibonacci10"), tabelaPaginas)) {
								System.out.println("Error creating process");
							}
							System.out.println("Process created successfully");
							break;
						case "5":
							tabelaPaginas = new int[so.gp.calcNumPages(progs.retrieveProgram("fibonacci10v2"))];
							if (!so.gp.createProcess(progs.retrieveProgram("fibonacci10v2"), tabelaPaginas)) {
								System.out.println("Error creating process");
							}
							System.out.println("Process created successfully");
							break;
						case "6":
							tabelaPaginas = new int[so.gp.calcNumPages(progs.retrieveProgram("fibonacciREAD"))];
							if (!so.gp.createProcess(progs.retrieveProgram("fibonacciREAD"), tabelaPaginas)) {
								System.out.println("Error creating process");
							}
							System.out.println("Process created successfully");
							break;
						case "7":
							tabelaPaginas = new int[so.gp.calcNumPages(progs.retrieveProgram("PB"))];
							if (!so.gp.createProcess(progs.retrieveProgram("PB"), tabelaPaginas)) {
								System.out.println("Error creating process");
							}
							System.out.println("Process created successfully");
							break;
						case "8":
							tabelaPaginas = new int[so.gp.calcNumPages(progs.retrieveProgram("PC"))];
							if (!so.gp.createProcess(progs.retrieveProgram("PC"), tabelaPaginas)) {
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
					PCB pcb = so.gp.getProcess(id);
					if (so.gp.getProcess(id) == null) {
						System.out.println("Invalid id process");
						break;
					}
					so.gp.freeProcess(pcb);
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
					so.ps.setDebug(true);
					System.out.println("Debug mode on");
					break;
				case "7":
					hw.cpu.setDebug(false);
					so.ps.setDebug(false);
					System.out.println("Debug mode off");
					break;

				case "8":
					execAll();
					System.out.println("Execute all");
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

	public void execAll() {
		Thread processThread = new Thread(new Runnable() {
			@Override
			public void run() {
				so.ps.running();
			}
		});
		processThread.start();

		Thread timerThread = new Thread(new Runnable() {
			@Override
			public void run() {
				while (true) {
					try {
						Thread.sleep(10);
						so.ps.interruptTimeOut();
					} catch (InterruptedException e) {
						System.out.println("Thread de timeout interrompida: " + e.getMessage());
					}
				}
			}
		});
		timerThread.start();

		Thread deviceThread = new Thread(new Runnable() {
			@Override
			public void run() {
				hw.device.run();
			}
		});
		deviceThread.start();
	}

	public static void main(String args[]) {
		int tamMem = 1024;
		int tamPg = 8;

		Sistema s = new Sistema(new Memory(tamMem, tamPg));
		s.run();
	}
}