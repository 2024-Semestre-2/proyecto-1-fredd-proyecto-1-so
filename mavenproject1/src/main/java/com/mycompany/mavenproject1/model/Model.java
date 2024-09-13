
package com.mycompany.mavenproject1.model;

import com.mycompany.Bslogic.Dispatcher;
import com.mycompany.Bslogic.Instruction;
import com.mycompany.Bslogic.Memory;
import com.mycompany.Bslogic.PCB;
import com.mycompany.Bslogic.Register;
import java.util.ArrayList;

/**
 *
 * @author fredd
 */
public class Model{
    int memorySize = 256;
    int userMemStart = 40;
    String actualInstrucString = "";
    Memory memory = new Memory(memorySize, 0, userMemStart);
    int actualInstruc = memory.getIndexUser();
    Register AX = new Register("AX", "0");
    Register BX = new Register("BX", "0");
    Register CX = new Register("CX", "0");
    Register DX = new Register("DX", "0");
    Register AC = new Register("AC", "0");
    
    public boolean flagExec = false;
    
    Dispatcher dispatcher = new Dispatcher();
    
    PCB actualPCB;

    
    public Model() {
        
    }
    
    
    public Instruction[] convertLinesToIns() {
        Instruction[] insProv = new Instruction[actualPCB.getLines().size()];
        int index = 0;
        for (String line: actualPCB.getLines()) {
            insProv[index] = new Instruction(line);
            index++;
        }
        return insProv;
    }
    
    public void setUserInsToMemo() {
        Instruction[] insProv = convertLinesToIns();
        memory = new Memory(memorySize, 0, userMemStart);
        AX.setValue("0"); 
        BX.setValue("0"); 
        CX.setValue("0"); 
        DX.setValue("0"); 
        AC.setValue("0"); 
        actualInstrucString = "";
        Instruction[] actualIns = memory.getMemoryInstrucs();
        int actualInd = memory.getActualIndexUser();
        
        while (true) {
            int randomNum = (int) (Math.random() * (memorySize - userMemStart + 1)) + userMemStart;
            if (((memorySize - randomNum) + insProv.length) < memorySize) {
                actualInd = randomNum;
                break;
            }
        }
        
        System.out.println(actualInd);
        memory.setIndexUser(actualInd);
        actualInstruc = memory.getIndexUser();
        
        for (Instruction element: insProv) {
            actualIns[actualInd] = element;
            actualInd++;
        }
        memory.setActualIndexUser(actualInd);
        memory.setMemoryInstrucs(actualIns);
     
        //memory.printMemory();
    }
    
    
    public void executionProgramSteps() {
        
        if (actualInstruc < memory.getActualIndexUser()) {
            Instruction tempIns = memory.getMemoryInstrucs()[actualInstruc];
            tempIns.printInst();
            actualInstrucString = tempIns.getCompIns();

            if (tempIns.getEachPartIns().length < 3) {
                setToMemorySimp(tempIns);
            } else {
                setToMemoryComp(tempIns);
            }
            actualInstruc++;
            
            actualPCB.setAC(AC);
            actualPCB.setAX(AX);
            actualPCB.setBX(BX);
            actualPCB.setCX(CX);
            actualPCB.setDX(DX);
        } else {
            actualPCB.setState("Finished");
            dispatcher.updatePCBS(actualPCB);
            dispatcher.updateStates();
            flagExec = false;
        }
    }
    
    public void setToMemorySimp(Instruction ins) {
        String instruction = ins.getEachPartIns()[0].trim();
        String value = ins.getEachPartIns()[1].trim();
        String[] registerNames = {"AX", "BX", "CX", "DX"};
        String ACProvVal = "0";
        if (AC.getValue() != null) {
            ACProvVal = AC.getValue().trim();
        }
        
        if (instruction.toUpperCase().equals("MOV")) {
            if (searchElement(registerNames, value.toUpperCase())) {
                AC.setValue(getElemReg(value));
            } else {
                AC.setValue(value);
            }
        } else if (instruction.toUpperCase().equals("LOAD")) {
            if (searchElement(registerNames, value.toUpperCase())) {
                AC.setValue(getElemReg(value));
            } else {
                AC.setValue(value);
            }
        } else if (instruction.toUpperCase().equals("STORE")) {
            if (searchElement(registerNames, value.toUpperCase())) {
                getReg(value).setValue(AC.getValue());
            } else {
                AC.setValue(value);
            }
        } else if (instruction.toUpperCase().equals("ADD")) {
            if (searchElement(registerNames, value.toUpperCase())) {
                AC.setValue(String.valueOf(Integer.parseInt(getElemReg(value).trim()) + Integer.parseInt(ACProvVal)));
            } else {
                AC.setValue(String.valueOf(Integer.parseInt(value) + Integer.parseInt(ACProvVal)));
            }
        } else {
            if (searchElement(registerNames, value.toUpperCase())) {
                AC.setValue(String.valueOf(Integer.parseInt(ACProvVal) - Integer.parseInt(getElemReg(value))));
            } else {
                AC.setValue(String.valueOf(Integer.parseInt(ACProvVal) - Integer.parseInt(value)));
            } 
        }
    }
    
    public void setToMemoryComp(Instruction ins) {
        String instruction = ins.getEachPartIns()[0].trim();
        String value = ins.getEachPartIns()[1].trim();
        String value2 = ins.getEachPartIns()[2].trim();

        String[] registerNames = {"AX", "BX", "CX", "DX"};
        
        if (instruction.toUpperCase().equals("MOV")) {
            if (searchElement(registerNames, value2.toUpperCase())) {
                getReg(value).setValue(getReg(value2).getValue());
            } else {
                getReg(value).setValue(value2);
            }
        } else if (instruction.toUpperCase().equals("ADD")) {
            if (searchElement(registerNames, value2.toUpperCase())) {
                getReg(value).setValue(String.valueOf(Integer.parseInt(getElemReg(value)) + Integer.parseInt(getElemReg(value2))));
            } else {
                getReg(value).setValue(String.valueOf(Integer.parseInt(getElemReg(value)) + Integer.parseInt(value2)));
            }
        } else {
            if (searchElement(registerNames, value2.toUpperCase())) {
                getReg(value).setValue(String.valueOf(Integer.parseInt(getElemReg(value)) - Integer.parseInt(getElemReg(value2))));
            } else {
                getReg(value).setValue(String.valueOf(Integer.parseInt(getElemReg(value)) - Integer.parseInt(value2)));
            }
        }
    }
    
    public String getElemReg(String nameR) {
        if (nameR.equals(AX.getName())) {
            return AX.getValue();
        } else if (nameR.equals(BX.getName())) {
            return BX.getValue();
        } else if (nameR.equals(CX.getName())) {
            return CX.getValue();
        } else {
            return DX.getValue();
        }
    }
    
    public Register getReg(String nameR) {
        if (nameR.equals(AX.getName())) {
            return AX;
        } else if (nameR.equals(BX.getName())) {
            return BX;
        } else if (nameR.equals(CX.getName())) {
            return CX;
        } else {
            return DX;
        }
    }
    
    public boolean searchElement(String[] array, String toSearch) {
        for (String register : array) {
            if (register.equals(toSearch)) {
                return true;
            }
        }
        return false;
    }

    
    
    
    
    //ignore
    public void printLines() {
        System.out.println(this.actualPCB.getLines());
    }
    
    public void setUserMemStart(int userMemStart) {
        this.userMemStart = userMemStart;
        memory = new Memory(memorySize, 0, userMemStart);
    }

    
    //Getters and setters
    public Register getAX() {
        return actualPCB.getAX();
    }
    
    public Register getBX() {
        return actualPCB.getBX();
    }

    public Register getCX() {
        return actualPCB.getCX();
    }

    public Register getDX() {
        return actualPCB.getDX();
    }

    public Register getAC() {
        return actualPCB.getAC();
    }

    public Memory getMemory() {
        return memory;
    }

    public int getMemorySize() {
        return memorySize;
    }

    public void setMemorySize(int memorySize) {
        this.memorySize = memorySize;
    }

    public int getActualInstruc() {
        return actualInstruc;
    }

    public String getActualInstrucString() {
        return actualInstrucString;
    }
    
    public PCB getActualPCB() {
        return actualPCB;
    }

    public void setActualPCB(PCB actualPCB) {
        this.actualPCB = actualPCB;
    }
    
    public void setLines(ArrayList<String> lines) {
        this.actualPCB.setLines(lines);
    }

    public Dispatcher getDispatcher() {
        return dispatcher;
    }
    
}
