
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
    
    public void createPCB(ArrayList<String> lines) {
        allProcesses.add(new PCB(lines));
    }

    public ArrayList<PCB> getAllProcesses() {
        return allProcesses;
    }
    
}
