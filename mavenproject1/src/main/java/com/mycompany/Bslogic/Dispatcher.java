
package com.mycompany.Bslogic;

import java.util.ArrayList;

/**
 *
 * @author fredd
 */
public class Dispatcher {
    private ArrayList<PCB> allProcesses = new ArrayList<>();
    
    public Dispatcher() {
        
    }
    
    public void createPCB(ArrayList<String> lines, String path) {
        PCB pcbTemp = new PCB(lines);
        pcbTemp.setPath(path);
        pcbTemp.setPCBID(allProcesses.size() + 1);
        allProcesses.add(pcbTemp);
    }

    public ArrayList<PCB> getAllProcesses() {
        return allProcesses;
    }
    
    public void updateStates() {
        for (PCB temp : allProcesses) {
            if (countR() < 5) {
                if (!temp.getState().equals("Ready") && !temp.getState().equals("Finished") && !temp.getState().equals("Waiting") && !temp.getState().equals("Executing")) {
                    temp.setState("Ready"); 
                }
            }
        }
    }
    
    
    
    public int countR() {
        int count = 0;
        for (PCB temp : allProcesses) {
            
            if (temp.getState().equals("Ready")) {
                count++;
            }
        }
        
        return count;
    }
    
    public void updatePCBS(PCB pcbTemp) {
        for (int i = 0; i < allProcesses.size(); i++) {
            PCB temp = allProcesses.get(i);
            if (pcbTemp.getPCBID() == temp.getPCBID()) {
                allProcesses.set(i, pcbTemp);
                break; 
            }
        }
    }
    
    public PCB getNextPCB() {
        for (PCB temp : allProcesses) {
            if (temp.getState().equals("Ready")) {
                return temp;
            }
        }
        
        return null;
    }
    
    public boolean checkIfAllFinished() {
        for (PCB temp : allProcesses) {
            if (!temp.getState().equals("Finished")) {
                return false;
            }
        }
        
        return true;
    }
    
    public PCB searchPCB(int id) {
        for (PCB temp : allProcesses) {
            if (temp.getPCBID() == id) {
                return temp;
            }
        }
        
        return null;
    }
    
    
    public void initializeMemo(Memory tempMemo, Storage tempStorage) {
        for (int i = 0; i < tempMemo.getIndexUser(); i ++) {
            if (i < allProcesses.size()) {
                tempMemo.getMemoryInstrucs()[i] = new Instruction(allProcesses.get(i).getPCBID());
            } else {
                break;
            }  
        }
        
        if (tempMemo.getIndexUser() < allProcesses.size()) {
            for (int i = tempMemo.getIndexUser(); i < allProcesses.size(); i ++) {
                tempStorage.addToStorage(allProcesses.get(i));
            }
        }
    }
    
    public Memory manageMemo(Memory tempMemo) {
        int indexMemo = 0;

        for (int i = 0; i < tempMemo.getMemoryInstrucs().length; i++) {
           tempMemo.getMemoryInstrucs()[i] = null;
        }
        
        for (int j = 0; j < allProcesses.size(); j++) {
            
            if (indexMemo < tempMemo.getIndexUser() && !allProcesses.get(j).getState().equals("Finished")) {
                tempMemo.getMemoryInstrucs()[indexMemo] = new Instruction(allProcesses.get(j).getPCBID());
                indexMemo++;
                System.out.println("Instrucción añadida en la posición: " + indexMemo);
            }
        }

        return tempMemo;
    }
    
    public Storage manageStorage(Storage tempSto) {
        ArrayList<String> tempContent = tempSto.getStorageElements();
        int tempInd = tempSto.getVirtualMenInd();
        
        while(tempInd < tempSto.getSize()) {
            if (!tempContent.get(tempInd).contains("|| State: Finished")) {
                tempContent.set(tempInd, String.valueOf(tempInd) + " Storage empty space");
            }
            
            tempInd++;
        }
        
        tempSto.setStorageElements(tempContent);
        return tempSto;
    }
}
