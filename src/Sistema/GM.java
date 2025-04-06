package src.Sistema;

public class GM implements GMInterface {
    private Memory mem;

    public GM(Memory memory) {
        this.mem = memory;
    }

    public boolean canAlloc(int nroPalavras, int[] tabelaPaginas) {
        int count = 0;

        if (tabelaPaginas.length > mem.getTotalPages()) {
            return false;
        }

        for (int i = 0; i < mem.getTotalPages() && count < tabelaPaginas.length; i++) {
            if (mem.getPages().get(i).isFree()) {
                tabelaPaginas[count] = i;
                count++;
            }
        }
        if (count == tabelaPaginas.length)
            return true;

        return false;
    }

    public void free(int[] tabelaPaginas) {
        for (int i = 0; i < tabelaPaginas.length; i++) {
            mem.getPages().get(tabelaPaginas[i]).setFree(true); // Cleaning pages
        }
        for (int i = 0; i < tabelaPaginas.length; i++) {
            for (int j = 0; j < mem.getTamPg(); j++) {
                int posMem = mem.calculatePage(tabelaPaginas[i]) + j;
                mem.pos[posMem] =  new Word(Opcode.___, -1, -1, -1); // Cleaning memory
            }
        }
        System.gc();
    }

    // Load with pages
    public void load(Word[] programImage, int[] tabelaPaginas) {
        
        for (int i = 0; i < tabelaPaginas.length; i++) {
            mem.getPages().get(tabelaPaginas[i]).setFree(false);  // Setting pages to false for load
        }

        int count = 0;

        for (int i = 0; i < tabelaPaginas.length; i++) {
            for (int j = 0; j < mem.getTamPg(); j++) {
                int posMem = mem.calculatePage(tabelaPaginas[i]) + j; // Address with page
                mem.pos[posMem] = programImage[count]; // Load
                count++;

                if(count >= programImage.length) return;
            }
        }
    }

    public void pageControl() {
        System.out.printf("%-8s%-8s%-8s%n", "Frame", "Início", "Fim");
    
        for (int i = 0; i < mem.getTotalPages(); i++) {
            System.out.printf("%-8d%-8d%-8d%n", i, i * mem.getTamPg(), (i + 1) * mem.getTamPg() - 1);
        }
    }    
}