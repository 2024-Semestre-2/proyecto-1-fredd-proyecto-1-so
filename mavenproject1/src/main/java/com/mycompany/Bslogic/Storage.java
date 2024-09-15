
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
        this.virtualMenInd = virtualMemInd ;
    }
    
    public void addElement(String element) {
        this.storageElements.add(element);
    }
    
    public void fillStorage(Dispatcher disp) {
        ArrayList<PCB> tempPCB = disp.getAllProcesses();
        
        for (int i = 0; i < size; i++) {
            if (i < virtualMenInd) {
                storageElements.add(String.valueOf(i) + " Virtual memory empty space");
            }
        }
        
        int index = 0;
        int tempInd = virtualMenInd;
        while (tempInd < size) {
            
            if (index < tempPCB.size()) {
                storageElements.add("Storage position: " + String.valueOf(tempInd) + " || PCB with id: " + String.valueOf(tempPCB.get(index).getPCBID()) + " || State: " + tempPCB.get(index).getState() + " || located in: " + tempPCB.get(index).getPath()); 
                index++;
            } else {
                storageElements.add(String.valueOf(tempInd) + " Storage empty space"); 
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
