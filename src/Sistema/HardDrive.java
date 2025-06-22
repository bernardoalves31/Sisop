package src.Sistema;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.Queue;

import src.Sistema.GP.PCB;

public class HardDrive {
    private final int tamPg;
    private HW hw;
    private LinkedList<ProcessMemory> pagesSaved;
    private volatile Queue<ProcessRequest> saveRequestQueue;
    private volatile Queue<ProcessRequest> loadRequestQueue;

    public HardDrive(HW hw) {
        this.hw = hw;
        this.tamPg = hw.mem.getTamPg();
        this.pagesSaved = new LinkedList<>();
        this.saveRequestQueue = new LinkedList<>();
        this.loadRequestQueue = new LinkedList<>();
    }

    private class ProcessRequest {
        private int processId;
        private int pageRequested;
        private int pageTarget;

        public ProcessRequest(int processId, int pageRequested, int pageTarget) {
            this.processId = processId;
            this.pageRequested = pageRequested;
            this.pageTarget = pageTarget;
        }
    }

    private class ProcessMemory {
        private int processId;
        private Page[] pages;
    }

    private class Page {
        Word[] word;
        int pageIndex;
    }

    public void run() {
        while(true) {
            if(this.loadRequestQueue.size() < 0) {
                ProcessRequest process = this.loadRequestQueue.poll();
                this.load(process);
            }
        }
    }

    public void load(ProcessRequest processRequest) {
        ProcessMemory process = this.pagesSaved.stream().filter(pm -> pm.processId == processRequest.processId).findFirst().orElse(null);

        // if (process == null) {
        //     throw new Exception();
        // }

        Page selectedPage = Arrays.stream(process.pages).filter(p -> p.pageIndex == processRequest.pageRequested).findFirst().orElse(null);

        // if (selectedPage == null) {
        //     throw new Exception();
        // }

        // int canAlloc = so.gm.canAlloc();

        // if (canAlloc == -1) {
        //     System.out.println("Sem páginas dispóniveis");
        // }




    }

    public void addRequest(int processId, int pageRequested, int pageTarget) {
        ProcessRequest newRequest = new ProcessRequest(processId, pageRequested, pageTarget);
        this.loadRequestQueue.add(newRequest);
    }

    public void save() {

    }
}