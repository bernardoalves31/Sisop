package src.Sistema;

import java.util.Iterator;
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
        this.hw = hw;
        this.countIds = 0;
    }

    public boolean createProcess(Word[] programImage) {
        int numPages = (int) Math.ceilDiv(programImage.length, hw.mem.getTamPg());
        if (numPages > hw.mem.getTotalPages()) {
            return false;
        }

        int[] tabelaPaginas = new int[numPages];

        if (!so.gm.canAlloc(programImage.length, tabelaPaginas)) {
            return false;
        }

        so.gm.load(programImage, tabelaPaginas);
        PCB pcb = new PCB(tabelaPaginas);
        processesInQueue.add(pcb);

        return true;
    }

    public boolean freeProcess(int id) {
        PCB removeItem = null;
        Iterator<PCB> iterator = processesInQueue.iterator();
        while (iterator.hasNext()) {
            if (iterator.next().id == id) {
                removeItem = iterator.next(); // Remove item from queue
                iterator.remove();
                so.gm.free(removeItem.tabelaPaginas); // Remove item from memory
                return true;
            }
        }
        return false;
    }

    public void listProcess(int id) {
        PCB pcb = getProcess(id);
        System.out.printf("Process id: %d  pages: %d  priority: %d  pc: %d  status: %s\n", pcb.id , pcb.tabelaPaginas, pcb.priority, pcb.pc, pcb.status);
    }

    public PCB getProcess(int id) {
        Iterator<PCB> iterator = processesInQueue.iterator();
        PCB pcb = (PCB) iterator;
        while(iterator.hasNext()) {
            if(pcb.id == id) return pcb;
        }
        return null;
    }

    public void ps() {
        Iterator<PCB> iterator = processesInQueue.iterator();
        while(iterator.hasNext()) {
            PCB pcb = (PCB) iterator;
            System.out.printf("Process id: %d  pages: %d  priority: %d  pc: %d  status: %s\n", pcb.id , pcb.tabelaPaginas, pcb.priority, pcb.pc, pcb.status);
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