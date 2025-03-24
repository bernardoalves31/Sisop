package src.Sistema;

public interface GMInterface {

    boolean canAlloc(int nroPalavras, int[] tabelaPaginas);

    void free(int[] tabelaPaginas);

    void load(Word[] programImage, int[] tabelaPaginas);

    void pageControl();
}