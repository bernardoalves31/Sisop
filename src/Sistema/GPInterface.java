package src.Sistema;

import src.Sistema.GP.PCB;

public interface GPInterface {

    boolean createProcess(Word[] programImage, int[] tabelaPaginas);
    
    void freeProcess(PCB program);
}