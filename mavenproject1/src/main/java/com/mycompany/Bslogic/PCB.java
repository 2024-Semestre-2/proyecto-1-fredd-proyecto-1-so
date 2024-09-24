package com.mycompany.Bslogic;

import java.time.LocalDateTime;
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
    private int sizeStack = 0;
    
    private ArrayList<String> stack = new ArrayList<>();
    
    //Tiempo empleado y tiempo de inicio
    //Contador
    
    private String path = "";
    
    private String priority = "";
    
    private ArrayList<String> lines = new ArrayList<>();
    
    
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    
    //Constructor
    public PCB () {
        
    }
    
    public PCB(ArrayList<String> lines) {
        this.lines = lines;
    }
    
    public void addToStack(String value) {
        this.stack.add(value);
        this.sizeStack++;
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
       
    
    public PCB clonePCB() {
        PCB newPCB = new PCB();  
        
        newPCB.setAX(new Register(this.AX.getName(), this.AX.getValue()));
        newPCB.setBX(new Register(this.BX.getName(), this.BX.getValue()));
        newPCB.setCX(new Register(this.CX.getName(), this.CX.getValue()));
        newPCB.setDX(new Register(this.DX.getName(), this.DX.getValue()));
        newPCB.setAC(new Register(this.AC.getName(), this.AC.getValue()));

        newPCB.setState(this.state);
        newPCB.setPCBID(this.PCBID);
        newPCB.setPath(this.path);
        newPCB.setPriority(this.priority);
        
        newPCB.setStack(new ArrayList<>());

        newPCB.setLines(new ArrayList<>(this.lines));
        
        return newPCB;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    public void setSizeStack(int sizeStack) {
        this.sizeStack = sizeStack;
    }
    
    public int getSizeStack() {
        return this.sizeStack;
    }
    
}
