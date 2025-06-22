package src.Sistema;

import src.Sistema.GP.PCB.ProgramPage;

public class GM implements GMInterface {
    private Memory mem;
    public int indexFifoDelete;

    public GM(Memory memory) {
        this.mem = memory;
        this.indexFifoDelete = 1;
    }

    public int canAlloc() { // Check if there a free page in memory
        for (int i = 0; i < mem.getTotalPages(); i++) {
            if (mem.getPages().get(i).isFree()) {
                return i; // Found a free page
            }
        }
        return -1; // No free pages available
    }

    public void free(ProgramPage[] tabelaPaginas) {
        for (int i = 0; i < tabelaPaginas.length; i++) {
            mem.getPages().get(tabelaPaginas[i].numPage).setFree(true); // Cleaning pages
        }
        for (int i = 0; i < tabelaPaginas.length; i++) {
            for (int j = 0; j < mem.getTamPg(); j++) {
                int posMem = mem.calculatePage(tabelaPaginas[i].numPage) + j;
                mem.pos[posMem] =  new Word(Opcode.___, -1, -1, -1); // Cleaning memory
            }
        }
        System.gc();
    }

    // Load with pages
    public void load(Word[] programImagePages, int numFrame) {
        mem.getPages().get(numFrame).setFree(false);  // Setting page to false for load
        
        int count = 0;

        for (int j = 0; j < mem.getTamPg(); j++) {
            int posMem = numFrame * mem.getTamPg() + j; // Address with page
            mem.pos[posMem] = programImagePages[count]; // Load
            count++;

            if(count >= programImagePages.length) return;    
        }
    }

    public void vitimate() {
         for (int i = 0; i < mem.getTamPg(); i++) {
            int index = this.indexFifoDelete * mem.getTamPg() + i;
            this.mem.pos[index] = new Word(Opcode.___, -1, -1, -1);                    
        }
        mem.getPages().get(this.indexFifoDelete).setFree(true);
        this.next();
    }

    private int next() {
        ++this.indexFifoDelete;
        if (this.indexFifoDelete * mem.getTamPg() >= mem.getTamMem()) {
            this.indexFifoDelete = 1;
            return this.indexFifoDelete;
        }
        return this.indexFifoDelete;
        
    }

    public void pageControl() {
        System.out.printf("%-8s%-8s%-8s%n", "Frame", "Início", "Fim");
    
        for (int i = 0; i < mem.getTotalPages(); i++) {
            System.out.printf("%-8d%-8d%-8d%n", i, i * mem.getTamPg(), (i + 1) * mem.getTamPg() - 1);
        }
    }    
}