package src.Sistema;

import src.Sistema.GP.PCB;

public class ProcessScheduler {
    public GP gp;
    private CPU cpu;
    private boolean debug;

    public ProcessScheduler(GP gp, CPU cpu) {
        this.gp = gp;
        this.cpu = cpu;
        this.debug = false;
    }

    public void running() {
        PCB startPcb = gp.peekProcessInQueue();

        cpu.setContext(startPcb);
        cpu.run();
        if (!debug) {
            gp.freeProcess(startPcb.id);
        }
        // cpu.sysCall.stop = false;

    }

    public void interruptTimeOut() {
        cpu.setInterruption(Interrupts.timeOut);
    }

    public void changeProcess() {
        if(gp.getProcessQueue().size() == 1) {
            cpu.setContext(gp.peekProcessInQueue());
            return;
        }

        System.out.println(gp.getProcessQueue().toString());
        PCB pcb = gp.peekProcessInQueue();
        
        gp.getProcessQueue().remove();
        cpu.setContext(gp.peekProcessInQueue());
        gp.getProcessQueue().add(pcb);
    }

    public synchronized void removeProcess(PCB pcb, boolean debug) {
        int id = pcb.id;
        gp.getProcessQueue().remove();
        if(!debug){
            gp.freeProcess(id);
        }
    }

    public void setDebug(boolean debug) {
        this.debug = debug;
    }

}
