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
            gp.freeProcess(startPcb);
        }
    }

    public void interruptTimeOut() {
        cpu.setInterruption(Interrupts.timeOut);
    }

    public void changeProcess() {
        PCB pcb = cpu.getPCB();
        pcb.status = ProcessStates.READY;
        gp.getProcessQueue().add(pcb);
        PCB nextRunningPCB = gp.getProcessQueue().remove();
        nextRunningPCB.status = ProcessStates.RUNNING;
        cpu.setContext(nextRunningPCB);
    }

    public synchronized void removeProcess(PCB pcb, boolean debug) {
        if (!debug) {
            gp.freeProcess(pcb);
        }

        cpu.setContext(gp.getProcessQueue().remove());
    }

    public void setDebug(boolean debug) {
        this.debug = debug;
    }

}
