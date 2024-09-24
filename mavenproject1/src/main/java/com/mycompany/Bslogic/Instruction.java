
package com.mycompany.Bslogic;

/**
 *
 * @author fredd
 */
public class Instruction {
    private String compIns;
    private String[] eachPartIns;
    private int actualPCBID;
    
    public Instruction (String compIns) {
        this.compIns = compIns;
        divideIns(compIns.split(","));
    }
    
    public Instruction (int actualPCBID) {
        this.actualPCBID = actualPCBID;
    }
    
    public void divideIns(String[] parts) {
        
        
        if (parts.length > 1) {
            
            if (parts.length > 2) {
                this.eachPartIns = new String[4];
                this.eachPartIns[0] = parts[0].split(" ")[0].trim();
                this.eachPartIns[1] = parts[0].split(" ")[1].trim();
                this.eachPartIns[2] = parts[1].trim(); 
                this.eachPartIns[3] = parts[2].trim(); 
            } else {
                this.eachPartIns = new String[3];
                this.eachPartIns[0] = parts[0].split(" ")[0].trim();
                this.eachPartIns[1] = parts[0].split(" ")[1].trim();
                this.eachPartIns[2] = parts[1].trim(); 
            }
        } else {
            if (parts[0].trim().split(" ").length == 2) {
                this.eachPartIns = new String[2];
                this.eachPartIns[0] = parts[0].split(" ")[0].trim();
                this.eachPartIns[1] = parts[0].split(" ")[1].trim(); 
            } else {
                this.eachPartIns = new String[1];
                this.eachPartIns[0] = parts[0].trim();
            }
        }
    }

    public void printInst() {
        for (String eleme: eachPartIns) {
            System.out.println(eleme);
        }
    }
    
    public String getCompIns() {
        return compIns;
    }

    public void setCompIns(String compIns) {
        this.compIns = compIns;
    }

    public String[] getEachPartIns() {
        return eachPartIns;
    }

    public void setEachPartIns(String[] eachPartIns) {
        this.eachPartIns = eachPartIns;
    }

    public int getActualPCBID() {
        return actualPCBID;
    }

    public void setActualPCBID(int actualPCBID) {
        this.actualPCBID = actualPCBID;
    }
    
    
}


