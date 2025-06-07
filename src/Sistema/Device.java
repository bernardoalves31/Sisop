package src.Sistema;

public class Device {
    public volatile String userInput;
    private int memoryWritePosition;
    private CPU cpu;
    private Memory memory;
    public boolean mutexShell;

    Device(CPU cpu, Memory memory) {
        this.userInput = "";
        this.cpu = cpu;
        this.memory = memory;
        this.mutexShell = false;
    }

    public void run() {
        while (true) {
            if(!userInput.equals("")) {
                Word word = this.memory.pos[memoryWritePosition];
                word.p = Integer.parseInt(userInput);
                this.memory.pos[memoryWritePosition] = word;
                this.userInput = "";
                cpu.setInterruption(Interrupts.intIO);
                mutexShell = false;
            }
        }
    }

    public void setUserInput(String userInput) {
        this.userInput = userInput;
    }

    public void setMemoryWritePosition(int memoryWritePosition) {
        this.memoryWritePosition = memoryWritePosition;
    }

}
