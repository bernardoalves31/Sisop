package src.Sistema;

import src.Sistema.GP.PCB;
import src.Sistema.GP.PCB.ProgramPage;
import src.Sistema.ProcessWaitingPage.ProcessPageTarget;

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
                            + hw.cpu.pc);
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

        if(blocked.tabelaPaginas.length > pageFaultPage) {
            if(blocked.tabelaPaginas[pageFaultPage].modified == true) {
                modified = true;
            }
        }

        blocked.status = ProcessStates.BLOCKED;
        ps.gp.getBlockedVMProcessQueue().add(blocked);
        ps.changeProcess();

        int pageTarget = so.gm.canAlloc();

        if(pageTarget == -1) { // Need to vitimate
            System.out.println("Entrou na vitimacao");
            Word[] pageToSave = so.gp.copyPage(so.gm.indexFifoDelete);
            so.gp.vitimate(blocked, pageFaultPage);
            hw.hardDrive.addSaveRequest(blocked.id, pageFaultPage, pageToSave);
            return;
        }

        if(!modified) {
            this.so.gp.loadPage(blocked, pageFaultPage, pageTarget);
            return;
        }

        hw.hardDrive.addRequest(blocked.id, pageFaultPage, pageTarget);
    }

    public void handlePageSaved() {
        ProcessPageTarget processPageTarget = so.gp.processWaitingTargetPage.getQueue().remove();
        int pageFaultPage = processPageTarget.pageFaultPage;
        PCB pcb = processPageTarget.pcb;
        if(pcb.tabelaPaginas.length <= pageFaultPage) {
            this.so.gp.loadPage(pcb, pageFaultPage, processPageTarget.pageTarget);
            return;
        }
        else if(pcb.tabelaPaginas[pageFaultPage -1].modified == false) {
            this.so.gp.loadPage(pcb, pageFaultPage, processPageTarget.pageTarget);
            return;
        }



        if (pcb.tabelaPaginas.length < pageFaultPage) {
            ProgramPage[] newTabelaPaginas = new ProgramPage[pageFaultPage + 1];

            for (int i = 0; i < pcb.tabelaPaginas.length; i++) {
                newTabelaPaginas[i] = pcb.tabelaPaginas[i];
            }

            for (int i = pcb.tabelaPaginas.length; i < newTabelaPaginas.length; i++) {
                if(i != pageFaultPage) {
                    ProgramPage newPage = pcb.new ProgramPage();
                    newPage.numPage = -1;
                    newPage.modified = false;
                    newTabelaPaginas[i] = newPage;
                }
                else{
                    ProgramPage newPage = pcb.new ProgramPage();
                    newPage.numPage = processPageTarget.pageTarget;
                    newPage.modified = true;
                    newTabelaPaginas[i] = newPage;
                }
            }
            pcb.tabelaPaginas = newTabelaPaginas;
        }

        for (int i = 0; i < pcb.tabelaPaginas.length; i++) {
            System.out.println(pcb.tabelaPaginas[i].toString());
        }
        
        hw.hardDrive.addRequest(processPageTarget.pcb.id, processPageTarget.pageFaultPage, processPageTarget.pageTarget);
    }
}