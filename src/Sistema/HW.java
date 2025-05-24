package src.Sistema;

public class HW {
    public Memory mem;
    public CPU cpu;
    public Device device;

    public HW(Memory mem) {
        this.mem = mem;
        cpu = new CPU(mem,false); // boolean turns debug
        this.device = new Device(cpu, mem);
    }
}
