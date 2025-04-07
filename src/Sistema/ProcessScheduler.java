package src.Sistema;

import src.Sistema.GP.PCB;

public class ProcessScheduler {
    private GP gp;
    private CPU cpu;
    private boolean debug;

    public ProcessScheduler(GP gp, CPU cpu) {
        this.gp = gp;
        this.cpu = cpu;
    }

    public void running() {
        while (true) {
            PCB currentPcb = gp.peekProcessInQueue();
            if (currentPcb == null)
                continue;

            cpu.setContext(currentPcb.pc, currentPcb.contextData);
            if (!cpu.sysCall.stop) {
                cpu.run(currentPcb);
            } else {
                removeProcess(currentPcb);
                cpu.sysCall.stop = false;
            }
        }
    }

    public void interruptTimeOut() {
        cpu.setInterruption(Interrupts.timeOut);
        changeProcess();
    }

    public void changeProcess() {
        PCB pcb = gp.peekProcessInQueue();

        removeProcess(pcb);
        gp.getProcessQueue().add(pcb);
    }

    public synchronized void removeProcess(PCB pcb) {
        gp.getProcessQueue().remove(pcb);
    }

}
