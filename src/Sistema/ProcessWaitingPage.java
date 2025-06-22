package src.Sistema;

import java.util.LinkedList;
import java.util.Queue;

import src.Sistema.GP.PCB;

public class ProcessWaitingPage {
    private Queue<ProcessWaitingPage> processWaitingPages;

    public ProcessWaitingPage() {
        this.processWaitingPages = new LinkedList<>();
    }

    private class ProcessPageTarget {
        PCB pcb;
        int pageTarget;

        public ProcessPageTarget(PCB pcb, int pageTarget) {
            this.pcb = pcb;
            this.pageTarget = pageTarget;
        }
    }

}
