package com.mycompany.Bslogic;

import java.util.ArrayList;

/**
 *
 * @author fredd
 */
public class PCB {
    private Register AX = new Register("AX", "0");
    private Register BX = new Register("BX", "0");
    private Register CX = new Register("CX", "0");
    private Register DX = new Register("DX", "0");
    private Register AC = new Register("AC", "0");
    
    private String state = "New";
    
    private int PCBID = 0;
    
    private ArrayList<String> stack = new ArrayList<>(5);
    
    //Tiempo empleado y tiempo de inicio
    //Contador
    
    private String path = "";
    
    private String priority = "";
    
    private ArrayList<String> lines = new ArrayList<>();
    
    //Constructor
    public PCB () {
        
    }
    
    public PCB(ArrayList<String> lines) {
        this.lines = lines;
    }
    

    //Getters and setters
    public int getPCBID() {
        return PCBID;
    }

    
    public void setPCBID(int PCBID) {    
        this.PCBID = PCBID;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
    
    public Register getAX() {
        return AX;
    }

    public void setAX(Register AX) {
        this.AX = AX;
    }

    public Register getBX() {
        return BX;
    }

    public void setBX(Register BX) {
        this.BX = BX;
    }

    public Register getCX() {
        return CX;
    }

    public void setCX(Register CX) {
        this.CX = CX;
    }

    public Register getDX() {
        return DX;
    }

    public void setDX(Register DX) {
        this.DX = DX;
    }

    public Register getAC() {
        return AC;
    }

    public void setAC(Register AC) {
        this.AC = AC;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public ArrayList<String> getStack() {
        return stack;
    }

    public void setStack(ArrayList<String> stack) {
        this.stack = stack;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public ArrayList<String> getLines() {
        return lines;
    }

    public void setLines(ArrayList<String> lines) {
        this.lines = lines;
    }
    
}
