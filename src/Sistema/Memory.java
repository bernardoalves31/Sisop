package src.Sistema;

import java.util.ArrayList;

public class Memory {

    public Word[] pos;

    private ArrayList<Page> pages;
    private int numFrame;
    private int tamMem;
    private int tamPg;

    class Page {
        private boolean free;

        public Page() {
            this.free = true;
        }

        public boolean isFree() {
            return free;
        }

        public void setFree(boolean set) {
            this.free = set;
        }

    }

    public Memory(int tamMem, int tamPg) {
        this.numFrame = tamMem / tamPg;
        this.tamMem = tamMem;
        this.tamPg = tamPg;
        this.pos = new Word[tamMem];
        pages = new ArrayList<Page>();

        for (int i = 0; i < numFrame; i++) {
            Page page = new Page();
            pages.add(page);
        }

        for (int i = 0; i < pos.length; i++) {
            pos[i] = new Word(Opcode.___, -1, -1, -1);
        }
    }

    public int calculatePage(int page) {
        return page * tamPg;
    }

    public ArrayList<Page> getPages() {
        return pages;
    }

    public int getNumFrame() {
        return numFrame;
    }

    public int getTamMem() {
        return tamMem;
    }

    public int getTamPg() {
        return tamPg;
    }

}