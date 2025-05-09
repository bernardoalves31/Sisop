package src.Sistema;

import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;

public class GP implements GPInterface {
    private HW hw;
    private SO so;
    private int countIds;
    private Queue<PCB> processesInQueue;

    public class PCB {
        Program[] programs;
        int[] tabelaPaginas;
        int id;
        int priority;
        int pc;
        String status;
        int[] contextData;

        public PCB(int[] tabelaPaginas) {
            this.tabelaPaginas = tabelaPaginas;
            this.id = countIds++;
            this.pc = 0;
            this.priority = 0;
            this.contextData = new int[10];
        }
    }

    public GP(HW hw, SO so) {
        this.so = so;
        this.hw = hw;
        this.countIds = 0;
        this.processesInQueue = new LinkedList<PCB>();
    }

    public boolean createProcess(Word[] programImage, int[] tabelaPaginas) {

        if (!so.gm.canAlloc(programImage.length, tabelaPaginas)) {
            return false;
        }

        so.gm.load(programImage, tabelaPaginas);
        PCB pcb = new PCB(tabelaPaginas);
        processesInQueue.add(pcb);

        return true;
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
            int page = pcb.tabelaPaginas[i];
            for (int j = 0; j < hw.mem.getTamPg(); j++) {
                int posMem = hw.mem.calculatePage(page) + j;
                Word w = hw.mem.pos[posMem];
                System.out.print("Page: " + page);
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
        if(!iterator.hasNext()) {
            System.out.println("List is empty");
            return;
        }
        while (iterator.hasNext()) {
            PCB pcb = iterator.next();
            if(pcb == null) {
                System.out.println("List is empty");
                return;
            }
            System.out.printf("Process id: %d  pages: %s  priority: %d  pc: %d  status: %s\n", pcb.id,
                    Arrays.toString(pcb.tabelaPaginas), pcb.priority, pcb.pc, pcb.status);
        }
    }

    public synchronized PCB peekProcessInQueue() {
        return processesInQueue.peek();
    }

    public Queue<PCB> getProcessQueue() {
        return processesInQueue;
    }
}