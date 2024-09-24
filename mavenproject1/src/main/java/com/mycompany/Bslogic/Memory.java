
package com.mycompany.Bslogic;

/**
 *
 * @author fredd
 */
public class Memory {
    private Instruction[] memoryInstrucs;
    private int indexOS;
    private int indexUser;
    private int actualIndexOS;
    private int actualIndexUser;
    private int memorySpace;
    
    public Memory(int memorySpace, int indexOS, int indexUser) {
        this.indexOS = indexOS;
        this.indexUser = indexUser;
        this.actualIndexOS = indexOS;
        this.actualIndexUser = indexUser;
        this.memorySpace = memorySpace;
        memoryInstrucs = new Instruction[memorySpace];
    }
    
    public void printMemory() {
        for (Instruction element : memoryInstrucs) {
            if (element != null) {
                System.out.println(element.getCompIns());
                continue;
            }
            System.out.println("Empty space");
        }
    }
    
    public void resetMemory() {
        for (int i = indexUser; i < memorySpace; i++) {
            memoryInstrucs[i] = null;
        }
        
        this.actualIndexOS = indexOS;
        this.actualIndexUser = indexUser;
    }
    
    public int getActualIndexOS() {
        return actualIndexOS;
    }

    public void setActualIndexOS(int actualIndexOS) {
        this.actualIndexOS = actualIndexOS;
    }

    public int getActualIndexUser() {
        return actualIndexUser;
    }

    public void setActualIndexUser(int actualIndexUser) {
        this.actualIndexUser = actualIndexUser;
    }

    public Instruction[] getMemoryInstrucs() {
        return memoryInstrucs;
    }

    public void setMemoryInstrucs(Instruction[] memoryInstrucs) {
        this.memoryInstrucs = memoryInstrucs;
    }

    public int getIndexOS() {
        return indexOS;
    }

    public void setIndexOS(int indexOS) {
        this.indexOS = indexOS;
    }

    public int getIndexUser() {
        return indexUser;
    }

    public void setIndexUser(int indexUser) {
        this.indexUser = indexUser;
    }
    
}
