package src.Sistema;

public class InterruptHandling {
    private HW hw; // referencia ao hw se tiver que setar algo
    private ProcessScheduler ps;

    public InterruptHandling(HW _hw, ProcessScheduler ps) {
        this.ps = ps;
        hw = _hw;
    }

    public void handle(Interrupts irpt, Word ir) {
        if(ir.opc != Opcode.NOP) {
            System.out.println(
            "                                               Interrupcao " + irpt + "   pc: " + hw.cpu.translatePosition(hw.cpu.pc));
        }
        if(irpt == Interrupts.intIO) {
            ps.gp.removeBlockedProcess();
            hw.cpu.irpt = Interrupts.noInterrupt;
            return;
        }

        ps.changeProcess();
        
    }
}