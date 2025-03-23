package src.Sistema;

public interface GMInterface {

    boolean alloc(int nroPalavras, int[] tabelaPaginas);

    void free(int[] tabelaPaginas);

}