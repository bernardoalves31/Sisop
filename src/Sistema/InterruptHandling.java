package src.Sistema;

import src.Sistema.GP.PCB;

public class InterruptHandling {
    private HW hw; // referencia ao hw se tiver que setar algo
    public ProcessScheduler ps;
    private SO so;

    public InterruptHandling(HW _hw, ProcessScheduler ps, SO so) {
        this.ps = ps;
        hw = _hw;
        this.so = so;
    }

    public void handle(Interrupts irpt, Word ir) {
        if (ir.opc != Opcode.NOP) {
            System.out.println(
                    "                                               Interrupcao " + irpt + "   pc: "
                            + hw.cpu.translatePosition(hw.cpu.pc));
        }
        if (irpt == Interrupts.intIO) {
            ps.gp.removeBlockedIOProcess();
            hw.cpu.irpt = Interrupts.noInterrupt;
            return;
        }

        ps.changeProcess();

    }

    public void handleLoaded() {
        ps.gp.removeBlockedVMProcess();
        hw.cpu.irpt = Interrupts.noInterrupt;
    }

    public void handleVM(int pageFaultPage) {
        boolean modified = false;
        /*if(hw.cpu.getPCB().tabelaPaginas[pageFaultPage].modified) {
            modified = true;
        }*/


        PCB blocked = hw.cpu.getPCB();
        blocked.status = ProcessStates.BLOCKED;
        ps.gp.getBlockedVMProcessQueue().add(blocked);
        ps.changeProcess();

        int pageTarget = so.gm.canAlloc();

        if(pageTarget == -1) { // Need to vitimate
            Word[] pageToSave = so.gp.copyPage(so.gm.indexFifoDelete);
            so.gp.vitimate();
            return;
        }

        if(!modified) {
            this.so.gp.loadPage(blocked, pageFaultPage ,pageTarget);
            return;
        }
        
    }
}