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

            cpu.setContext(currentPcb.pc);
            if (!cpu.sysCall.stop) {
                cpu.run(currentPcb);
            } else {
                gp.getProcessQueue().remove();
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

        gp.getProcessQueue().add(pcb);
        gp.getProcessQueue().remove();
    }

}
