
package com.mycompany.Bslogic;

import java.util.ArrayList;

/**
 *
 * @author fredd
 */
public class Storage {
    private ArrayList<String> storageElements = new ArrayList<>();
    private int virtualMenInd;
    private int size;
    
    
    public Storage(int size, int virtualMemInd) {
        this.size = size;
        this.virtualMenInd = virtualMemInd;
    }
    
    public void addElement(String element) {
        this.storageElements.add(element);
    }
    
    
    public void fillStorage() {
        for (int i = 0; i < size; i++) {
            if (i < virtualMenInd) {
                storageElements.add(String.valueOf(i) + " Virtual memory empty space");
            }
        }
        
        int tempInd = virtualMenInd;
        while (tempInd < size) {
            storageElements.add(String.valueOf(tempInd) + " Storage empty space"); 
            
            tempInd++;
        }
    }
    
    public void addToStorage(PCB tempPCB) {

        int tempInd = virtualMenInd;
        while (tempInd < size) {
            if (storageElements.get(tempInd).contains("Storage empty space")) {
                storageElements.set(tempInd, "Storage position: " + String.valueOf(tempInd) + " || PCB with id: " + String.valueOf(tempPCB.getPCBID()) + " || State: " + tempPCB.getState() + " || located in: " + tempPCB.getPath()); 
                break;
            }
            tempInd++;
        }
    }
    

    
    public String storageToString () {
        String text = "";
        
        for (String element: storageElements) {
            text = text + element + "\n\n";
        }
       
        return text;
    }

    
    //Getters and setters
    public ArrayList<String> getStorageElements() {
        return storageElements;
    }

    public void setStorageElements(ArrayList<String> storageElements) {
        this.storageElements = storageElements;
    }

    public int getVirtualMenInd() {
        return virtualMenInd;
    }

    public void setVirtualMenInd(int virtualMenInd) {
        this.virtualMenInd = virtualMenInd;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }
    
    
}
