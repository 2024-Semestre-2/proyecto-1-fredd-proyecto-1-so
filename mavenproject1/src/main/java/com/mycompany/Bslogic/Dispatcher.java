
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
    
}
