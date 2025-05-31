package src.Sistema;

import src.Sistema.GP.PCB.ProgramPage;

public interface GMInterface {

    int canAlloc();

    void free(ProgramPage[] tabelaPaginas);

    void load(Word[] programImage, int numPage);

    void pageControl();
}