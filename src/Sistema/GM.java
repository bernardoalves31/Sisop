package src.Sistema;

public class GM implements GMInterface {
    private Memory mem;

    public boolean alloc(int nroPalavras, int[] tabelaPaginas) {
        int numPages = (int) Math.ceil(nroPalavras / mem.getNumFrame());
        int count = 0;
        tabelaPaginas = new int[tabelaPaginas.length];

        if (numPages > mem.getNumFrame()) {
            return false;
        }

        for (int i = 0; i < mem.getNumFrame() && count < numPages; i++) {
            if (mem.getPages().get(i).isFree()) {
                tabelaPaginas[count] = i;
                count++;
                mem.getPages().get(i).setFree(false);
            }
        }
        if (count == numPages)
            return true;

        tabelaPaginas = new int[tabelaPaginas.length];
        return false;
    }

    public void free(int[] tabelaPaginas) {
        int count = 0;
        for (int i = 0; i < tabelaPaginas.length; i++) {
            mem.getPages().get(tabelaPaginas[count]).setFree(true);
            count++;
        }
    }
}