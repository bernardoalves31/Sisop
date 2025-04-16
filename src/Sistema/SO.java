package src.Sistema;

public class SO {
    public InterruptHandling ih;
    public SysCallHandling sc;
    public Utilities utils;
    public GM gm;
    public GP gp;
    public ProcessScheduler ps;

    public SO(HW hw) {
        utils = new Utilities(hw);
        gm = new GM(hw.mem);
        gp = new GP(hw, this);
        ps = new ProcessScheduler(gp, hw.cpu);
        sc = new SysCallHandling(hw, ps); // chamadas de sistema
        ih = new InterruptHandling(hw, ps); // rotinas de tratamento de int
        hw.cpu.setAddressOfHandlers(ih, sc);
    }
}
