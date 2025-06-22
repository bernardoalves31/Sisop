package src.Sistema;

import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;

import src.Sistema.GP.PCB.ProgramPage;

public class GP implements GPInterface {
    private HW hw;
    private SO so;
    private int countIds;
    private Queue<PCB> processesInQueue;
    private Queue<PCB> processBlockedIOInQueue;
    private Queue<PCB> processBlockedVMInQueue;
    private ProcessWaitingPage processWaitingTargetPage;

    public class PCB {
        Program[] programs;
        volatile ProgramPage[] tabelaPaginas;
        int id;
        int priority;
        int pc;
        ProcessStates status;
        int[] contextData;
        String pname;

        public PCB(ProgramPage[] tabelaPaginas, String pname) {
            this.tabelaPaginas = tabelaPaginas;
            this.id = countIds++;
            this.pc = 0;
            this.priority = 0;
            this.status = ProcessStates.READY;
            this.contextData = new int[10];
            this.pname = pname;

            for (int i = 0; i < tabelaPaginas.length; i++) { // Initialize the page table
                tabelaPaginas[i] = new ProgramPage();
            }
        }

        public class ProgramPage {
            public int numPage;
            public boolean modified;

            public ProgramPage() {
                this.numPage = -1;
                this.modified = false;
            }

            @Override
            public String toString() {
                return String.valueOf(this.numPage);
            }
        }
    }

    public GP(HW hw, SO so) {
        this.so = so;
        this.hw = hw;
        this.countIds = 0;
        this.processesInQueue = new LinkedList<PCB>();
        this.processBlockedIOInQueue = new LinkedList<PCB>();
        this.processBlockedVMInQueue= new LinkedList<PCB>();
    }

    public boolean createProcess(String programName) {
        int allocatedFrame = so.gm.canAlloc();

        if (allocatedFrame == -1) {
            return false;
        }

        Word[] programImage = Programs.retrieveProgramPage(programName, 0, hw.mem.getTamPg());
        ProgramPage[] tabelaPaginas = new ProgramPage[1];

        so.gm.load(programImage, allocatedFrame);
        PCB pcb = new PCB(tabelaPaginas, programName);

        tabelaPaginas[0].numPage = allocatedFrame;
        tabelaPaginas[0].modified = true;

        processesInQueue.add(pcb);

        return true;
    }

    public void removeBlockedIOProcess() {
        PCB pcb = this.processBlockedIOInQueue.remove();
        pcb.status = ProcessStates.READY;
        this.processesInQueue.add(pcb);
    }

    public void removeBlockedVMProcess() {
        PCB pcb = this.processBlockedVMInQueue.remove();
        pcb.status = ProcessStates.READY;
        this.processesInQueue.add(pcb);
    }

    public void loadPage(PCB pcb, int pageFaultPage, int pageTarget) {
        Word[] programImage = Programs.retrieveProgramPage(pcb.pname, pageFaultPage, hw.mem.getTamPg());

        if (pcb.tabelaPaginas.length > pageFaultPage) {
            ProgramPage newPage = pcb.new ProgramPage();
            newPage.numPage = pageTarget;
            newPage.modified = true;
            pcb.tabelaPaginas[pageFaultPage] = newPage;
            so.gm.load(programImage, pageTarget);
        } else {
            ProgramPage[] newTabelaPaginas = new ProgramPage[pageFaultPage + 1];

            for (int i = 0; i < pcb.tabelaPaginas.length; i++) {
                newTabelaPaginas[i] = pcb.tabelaPaginas[i];
            }

            for (int i = pcb.tabelaPaginas.length; i < newTabelaPaginas.length; i++) {
                if(i != pageFaultPage) {
                    ProgramPage newPage = pcb.new ProgramPage();
                    newPage.numPage = -1;
                    newPage.modified = false;
                    newTabelaPaginas[i] = newPage;
                }
                else{
                    ProgramPage newPage = pcb.new ProgramPage();
                    newPage.numPage = pageTarget;
                    newPage.modified = true;
                    newTabelaPaginas[i] = newPage;
                }
            }
            pcb.tabelaPaginas = newTabelaPaginas;
        }
        so.gm.load(programImage, pageTarget);
        hw.cpu.setInterruption(Interrupts.intLoaded);
    }

    public Word[] copyPage(int page) {
        Word[] words = new Word[hw.mem.getTamPg()];

        for (int i = 0; i < words.length; i++) {
            int indexPage = page * hw.mem.getTamPg() + i;
            words[i] = hw.mem.pos[indexPage];
        }
        return words;
    }

    public void vitimate() {
        so.gm.vitimate();
        hw.cpu.setInterruption(null);
    }

    public int calcNumPages(Word[] programImage) {
        return Math.ceilDiv(programImage.length, hw.mem.getTamPg());
    }

    public void freeProcess(PCB pcb) {
        so.gm.free(pcb.tabelaPaginas); // Remove item from memory
        processesInQueue.remove(pcb); // Remove from queue
    }

    public void listProcess(int id) {
        PCB pcb = getProcess(id);
        if (pcb == null) {
            System.out.println("Invalid process id");
            return;
        }
        System.out.printf("Process id: %d  pages: %s  priority: %d  pc: %d  status: %s\n", pcb.id,
                Arrays.toString(pcb.tabelaPaginas), pcb.priority, pcb.pc, pcb.status);
        for (int i = 0; i < pcb.tabelaPaginas.length; i++) {
            int page = pcb.tabelaPaginas[i].numPage;
            for (int j = 0; j < hw.mem.getTamPg(); j++) {
                int posMem = hw.mem.calculatePage(page) + j;
                Word w = hw.mem.pos[posMem];
                System.out.print("Frame: " + page);
                System.out.print(" Position: " + posMem);
                System.out.print(" [ ");
                System.out.print(w.opc);
                System.out.print(", ");
                System.out.print(w.ra);
                System.out.print(", ");
                System.out.print(w.rb);
                System.out.print(", ");
                System.out.print(w.p);
                System.out.println("  ] ");
            }
        }
    }

    public PCB getProcess(int id) {
        Iterator<PCB> iterator = processesInQueue.iterator();
        while (iterator.hasNext()) {
            PCB pcb = iterator.next();
            if (pcb.id == id)
                return pcb;
        }
        return null;
    }

    public void ps() {
        Iterator<PCB> iterator = processesInQueue.iterator();
        if (!iterator.hasNext()) {
            System.out.println("List is empty");
            return;
        }
        while (iterator.hasNext()) {
            PCB pcb = iterator.next();
            if (pcb == null) {
                System.out.println("List is empty");
                return;
            }

            int[] pages = Arrays.stream(pcb.tabelaPaginas)
                    .mapToInt(page -> page.numPage)
                    .toArray();

            System.out.printf("Process id: %d  pages: %s  priority: %d  pc: %d  status: %s\n", pcb.id,
                    Arrays.toString(pages), pcb.priority, pcb.pc, pcb.status);
        }
    }

    public synchronized PCB peekProcessInQueue() {
        return processesInQueue.peek();
    }

    public Queue<PCB> getProcessQueue() {
        return processesInQueue;
    }

    public synchronized PCB peekBlockedProcessInQueue() {
        return processesInQueue.peek();
    }

    public Queue<PCB> getBlockedIOProcessQueue() {
        return processBlockedIOInQueue;
    }

    public Queue<PCB> getBlockedVMProcessQueue() {
        return processBlockedVMInQueue;
    }
}