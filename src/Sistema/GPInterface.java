package src.Sistema;

import src.Sistema.GP.PCB;

public interface GPInterface {

    boolean createProcess(String programName);
    
    void freeProcess(PCB program);
}