package src.Sistema;

import java.util.LinkedList;
import java.util.Queue;

import src.Sistema.GP.PCB;

public class ProcessWaitingPage {
    private Queue<ProcessPageTarget> processWaitingPages;

    public ProcessWaitingPage() {
        this.processWaitingPages = new LinkedList<>();
    }

    public class ProcessPageTarget {
        PCB pcb;
        int pageFaultPage;
        int pageTarget;

        public ProcessPageTarget(PCB pcb, int pageFaultPage, int pageTarget) {
            this.pcb = pcb;
            this.pageFaultPage = pageFaultPage;
            this.pageTarget = pageTarget;
        }
    }

    public Queue<ProcessPageTarget> getQueue() {
        return this.processWaitingPages;
    }

}
