package src.Sistema;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.Queue;

import src.Sistema.GP.PCB;

public class HardDrive {
    private final int tamPg;
    private HW hw;
    private LinkedList<ProcessMemory> pagesSaved;
    private volatile Queue<ProcessSaveRequest> saveRequestQueue;
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

    private class ProcessSaveRequest {
        private int processId;
        private int page;
        private Word[] programImage;

        public ProcessSaveRequest(int processId, int page, Word[] programImage) {
            this.processId = processId;
            this.page = page;
            this.programImage = programImage;
        }
    }

    private class ProcessMemory {
        private int processId;
        private LinkedList<Page> pages;

        public ProcessMemory(int processId) {
            this.processId = processId;
            this.pages = new LinkedList<>();
        }
    }

    private class Page {
        Word[] word;
        int pageIndex;

        public Page(Word[] word, int pageIndex) {
            this.word = word;
            this.pageIndex = pageIndex;
        }
    }

    public void run() {
        while (true) {
            if (this.loadRequestQueue.size() > 0) {
                this.load();
            }

            if (this.saveRequestQueue.size() > 0) {
                this.save();
            }
        }
    }

    public void load() {
        ProcessRequest processRequest = this.loadRequestQueue.remove();

        ProcessMemory process = this.pagesSaved.stream().filter(pm -> pm.processId == processRequest.processId)
                .findFirst().orElse(null);

        for (int i = 0; i < process.pages.size(); i++) {
            System.out.println(process.pages.get(i).pageIndex);
        }

        System.out.println("Pagina requested: "+ processRequest.pageRequested);

        Page selectedPage = process.pages.stream().filter(p -> p.pageIndex == processRequest.pageRequested).findFirst()
                .orElse(null);


        for (int i = 0; i < selectedPage.word.length; i++) {
            hw.mem.pos[(processRequest.pageTarget * tamPg) + i] = selectedPage.word[i];
        }

        

        process.pages.remove(selectedPage);

        hw.cpu.setInterruption(Interrupts.intVMLoad);

        // Setar pagina carregada na tabela de paginas do programa
    }

    public void save() {
        ProcessSaveRequest request = this.saveRequestQueue.remove();

        ProcessMemory process = this.pagesSaved.stream().filter(pm -> pm.processId == request.processId)
                .findFirst().orElse(null);

        if (process != null) {
            process.pages.add(new Page(request.programImage, request.page));
            hw.cpu.setInterruption(Interrupts.intPageSaved);
            return;
        }

        ProcessMemory processMemory = new ProcessMemory(request.processId);
        System.out.println("Pagina salva como:" + request.page);
        processMemory.pages.add(new Page(request.programImage, request.page));
        this.pagesSaved.add(processMemory);

        System.out.println("Salvou a pagina");
        hw.cpu.setInterruption(Interrupts.intPageSaved);
    }

    public void addRequest(int processId, int pageRequested, int pageTarget) {
        ProcessRequest newRequest = new ProcessRequest(processId, pageRequested, pageTarget);
        this.loadRequestQueue.add(newRequest);
    }

    public void addSaveRequest(int processId, int page, Word[] programImage) {
        ProcessSaveRequest newRequest = new ProcessSaveRequest(processId, page, programImage);
        this.saveRequestQueue.add(newRequest);
    }
}