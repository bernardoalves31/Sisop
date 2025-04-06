package src.Sistema;

public interface GPInterface {

    boolean createProcess(Word[] programImage, int[] tabelaPaginas);
    
    boolean freeProcess(int program);
}