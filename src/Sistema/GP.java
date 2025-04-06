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

    private class PCB {
        Program[] programs;
        int[] tabelaPaginas;
        int id;
        int priority;
        int pc;
        String status;
        String contextData;

        public PCB(int[] tabelaPaginas) {
            this.tabelaPaginas = tabelaPaginas;
            this.id = countIds++;
            this.pc = 0;
            this.priority = 0;
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

    public boolean freeProcess(int id) {
        PCB removeItem = null;
        Iterator<PCB> iterator = processesInQueue.iterator();
        while (iterator.hasNext()) {
            removeItem = iterator.next(); // Remove item from queue
            if(removeItem.id == id) {
                iterator.remove();
                so.gm.free(removeItem.tabelaPaginas); // Remove item from memory
                return true;
            }
        }
        return false;
    }

    public void listProcess(int id) {
        PCB pcb = getProcess(id);
        if(pcb == null) {
            System.out.println("Invalid process id");
            return;
        }
        System.out.printf("Process id: %d  pages: %s  priority: %d  pc: %d  status: %s\n", pcb.id , Arrays.toString(pcb.tabelaPaginas), pcb.priority, pcb.pc, pcb.status);
        for (int i = 0; i < pcb.tabelaPaginas.length; i++) {
            int page = pcb.tabelaPaginas[i];
            for (int j = 0; j < hw.mem.getTamPg(); j++) {
                int posMem = hw.mem.calculatePage(page) + j;
                Word w = hw.mem.pos[posMem];
                System.out.print("Page: " + page);
                System.out.print(" Position: "+ posMem);
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
        while(iterator.hasNext()) {
            PCB pcb = iterator.next();
            if(pcb.id == id) return pcb;
        }
        return null;
    }

    public void ps() {
        Iterator<PCB> iterator = processesInQueue.iterator();
        while(iterator.hasNext()) {
            PCB pcb = iterator.next();
            System.out.printf("Process id: %d  pages: %s  priority: %d  pc: %d  status: %s\n", pcb.id , Arrays.toString(pcb.tabelaPaginas), pcb.priority, pcb.pc, pcb.status);
        }
    }

    public PCB peekProcessInQueue() {
        return processesInQueue.peek();
    }

    public Queue<PCB> getProcessQueue() {
        Queue<PCB> aux = this.processesInQueue;
        aux.remove();
        return aux;
    }
}