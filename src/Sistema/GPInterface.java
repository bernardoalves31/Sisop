package src.Sistema;

public interface GPInterface {

    boolean createProcess(Word[] program);
    void freeProcess(int program);
}