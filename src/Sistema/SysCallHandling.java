package src.Sistema;

import src.Sistema.GP.PCB;

public class SysCallHandling {
    private HW hw; // referencia ao hw se tiver que setar algo
    private ProcessScheduler ps;

    public SysCallHandling(HW _hw, ProcessScheduler ps) {
        hw = _hw;
        this.ps = ps;
    }

    public void stop(PCB pcb, boolean debug) { // chamada de sistema indicando final de programa
                         // nesta versao cpu simplesmente pára
        ps.removeProcess(pcb, debug);
        if(debug) {

            System.out.println("                                               SYSCALL STOP");
        }
    }

    public void handle() { // chamada de sistema
                           // suporta somente IO, com parametros
                           // reg[8] = in ou out e reg[9] endereco do inteiro
        System.out.println("SYSCALL pars:  " + hw.cpu.reg[8] + " / " + hw.cpu.reg[9]);

        if (hw.cpu.reg[8] == 1) {
            // leitura ...
            for (int i = 0; i < hw.cpu.reg.length; i++) {
                hw.cpu.getPCB().contextData[i] = hw.cpu.reg[i]; 
            }

            hw.device.setMemoryWritePosition(hw.cpu.translatePosition(hw.cpu.reg[9])); // Sends translated position
            
            hw.cpu.getPCB().pc = ++hw.cpu.pc;


            hw.device.mutexShell = true;
            System.out.println("Program input: ");
            PCB blocked = hw.cpu.getPCB();
            blocked.status = ProcessStates.BLOCKED;
            ps.gp.getBlockedIOProcessQueue().add(blocked);
            ps.changeProcess();

        } else if (hw.cpu.reg[8] == 2) {
            // escrita - escreve o conteuodo da memoria na posicao dada em reg[9]
            System.out.println("OUT:   " + hw.mem.pos[hw.cpu.reg[9]].p);
            hw.cpu.pc++;
        } else {
            System.out.println("  PARAMETRO INVALIDO");
        }
    }
}