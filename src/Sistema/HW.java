package src.Sistema;

public class HW {
    public Memory mem;
    public CPU cpu;
    public Device device;
    public HardDrive hardDrive;

    public HW(Memory mem) {
        this.mem = mem;
        cpu = new CPU(mem,false); // boolean turns debug
        this.device = new Device(cpu, mem);
        this.hardDrive = new HardDrive(this);
    }
}
