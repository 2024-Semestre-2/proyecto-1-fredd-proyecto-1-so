
package com.mycompany.mavenproject1.model;

import com.mycompany.Bslogic.Dispatcher;
import com.mycompany.Bslogic.Instruction;
import com.mycompany.Bslogic.Memory;
import com.mycompany.Bslogic.PCB;
import com.mycompany.Bslogic.Register;
import com.mycompany.Bslogic.Storage;
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
    public boolean flagInt10 = false;
    public boolean flagInt09 = false;

    
    Dispatcher dispatcher = new Dispatcher();
    
    PCB actualPCB;
    
    //Storage section
    private Storage actualStorage = new Storage(512, 64);

    
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
    
    
    public void executionProgramSteps() throws InterruptedException {
        
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
    
    public void setToMemorySimp(Instruction ins) throws InterruptedException {
        String instruction = ins.getEachPartIns()[0].trim();
        String value = "";
        if (ins.getEachPartIns().length > 1) {
            value = ins.getEachPartIns()[1].trim();
        }
        
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
        } else if (instruction.toUpperCase().equals("SUB")){
            if (searchElement(registerNames, value.toUpperCase())) {
                AC.setValue(String.valueOf(Integer.parseInt(ACProvVal) - Integer.parseInt(getElemReg(value))));
            } else {
                AC.setValue(String.valueOf(Integer.parseInt(ACProvVal) - Integer.parseInt(value)));
            } 
        } else if (instruction.toUpperCase().equals("INC") && !value.equals("")) {
            getReg(value).setValue(String.valueOf(Integer.parseInt(getReg(value).getValue()) + 1 ));
        } else if (instruction.toUpperCase().equals("INC")) {
            AC.setValue(String.valueOf(Integer.parseInt(ACProvVal) + 1));
        } else if (instruction.toUpperCase().equals("DEC") && !value.equals("")) {
            getReg(value).setValue(String.valueOf(Integer.parseInt(getReg(value).getValue()) - 1 ));
        } else if (instruction.toUpperCase().equals("DEC")) {
            AC.setValue(String.valueOf(Integer.parseInt(ACProvVal) - 1));
        } else if (instruction.toUpperCase().equals("JMP")) {
            int valueTemp = Integer.parseInt(value);
            
            if ((valueTemp + actualInstruc) < memory.getActualIndexUser()) {
                actualInstruc = valueTemp + actualInstruc;
            } else {
                actualPCB.setState("Finished");
                dispatcher.updatePCBS(actualPCB);
                dispatcher.updateStates();
                flagExec = false;
            }
        } else if (instruction.toUpperCase().equals("JNE")) {
            int valueFlag = Integer.parseInt(getElemReg("DX"));
            
            if (valueFlag == 0) {
                int valueTemp = Integer.parseInt(value);
                
                if ((valueTemp + actualInstruc) < memory.getActualIndexUser()) {
                                actualInstruc = valueTemp + actualInstruc;
                } else {
                    actualPCB.setState("Finished");
                    dispatcher.updatePCBS(actualPCB);
                    dispatcher.updateStates();
                    flagExec = false;
                }
            }
        } else if (instruction.toUpperCase().equals("JE")) {
            int valueFlag = Integer.parseInt(getElemReg("DX"));
            
            if (valueFlag == 1) {
                int valueTemp = Integer.parseInt(value);
                
                if ((valueTemp + actualInstruc) < memory.getActualIndexUser()) {
                                actualInstruc = valueTemp + actualInstruc;
                } else {
                    actualPCB.setState("Finished");
                    dispatcher.updatePCBS(actualPCB);
                    dispatcher.updateStates();
                    flagExec = false;
                }
            }
        } else if (instruction.toUpperCase().equals("PUSH")) {
            String elementReg = getElemReg(value);
            
            if (actualPCB.getSizeStack() < 5) {
                actualPCB.addToStack(elementReg);
            } else {
                actualPCB.setState("Finished");
                dispatcher.updatePCBS(actualPCB);
                dispatcher.updateStates();
                flagExec = false;
            }
        } else if (instruction.toUpperCase().equals("POP")) {
            if (actualPCB.getSizeStack() == 0) {
                getReg(value).setValue("0");
            } else {
                getReg(value).setValue(actualPCB.getStack().get(actualPCB.getSizeStack() - 1));
                actualPCB.getStack().remove(actualPCB.getSizeStack() - 1);
            }
        } else if (instruction.toUpperCase().equals("PARAM")) {
            if (actualPCB.getSizeStack() < 5) {
                actualPCB.addToStack(value);
            } else {
                actualPCB.setState("Finished");
                dispatcher.updatePCBS(actualPCB);
                dispatcher.updateStates();
                flagExec = false;
            } 
        } else if (instruction.toUpperCase().equals("INT") && value.trim().equals("20H")) {
                actualPCB.setState("Finished");
                dispatcher.updatePCBS(actualPCB);
                dispatcher.updateStates();
                flagExec = false;
        } else if (instruction.toUpperCase().equals("INT") && value.trim().equals("09H")) {
                flagInt09 = true;
                actualPCB.setState("Waiting");
                dispatcher.updatePCBS(actualPCB);
                dispatcher.updateStates();
        } else if (instruction.toUpperCase().equals("INT") && value.trim().equals("10H")) {
                flagInt10 = true;
                actualPCB.setState("Waiting");
                dispatcher.updatePCBS(actualPCB);
                dispatcher.updateStates();
        }
    }
    
    public void setToMemoryComp(Instruction ins) {
        String instruction = ins.getEachPartIns()[0].trim();
        String value = ins.getEachPartIns()[1].trim();
        String value2 = ins.getEachPartIns()[2].trim();
        String value3 = "";

        
        if (ins.getEachPartIns().length > 3) {
            value3 = ins.getEachPartIns()[3].trim();
        }


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
        } else if (instruction.toUpperCase().equals("SUB")) {
            if (searchElement(registerNames, value2.toUpperCase())) {
                getReg(value).setValue(String.valueOf(Integer.parseInt(getElemReg(value)) - Integer.parseInt(getElemReg(value2))));
            } else {
                getReg(value).setValue(String.valueOf(Integer.parseInt(getElemReg(value)) - Integer.parseInt(value2)));
            }
        } else if (instruction.toUpperCase().equals("SWAP")) {
            String elemValue1 = getElemReg(value);
            getReg(value).setValue(getElemReg(value2));
            getReg(value2).setValue(elemValue1);
        } else if (instruction.toUpperCase().equals("CMP")) {
            if (getElemReg(value).equals(getElemReg(value2))) {
                DX.setValue("1");
            } else {
                DX.setValue("0");
            }
        } else if (instruction.toUpperCase().equals("PARAM") && !value3.equals("")) {
            if (actualPCB.getSizeStack() + 3 < 5) {
                actualPCB.addToStack(value);
                actualPCB.addToStack(value2);
                actualPCB.addToStack(value3);
            } else {
                actualPCB.setState("Finished");
                dispatcher.updatePCBS(actualPCB);
                dispatcher.updateStates();
                flagExec = false;
            }
        } else if (instruction.toUpperCase().equals("PARAM") && value.equals("")) {
            if (actualPCB.getSizeStack() + 2 < 5) {
                actualPCB.addToStack(value);
                actualPCB.addToStack(value2);
            } else {
                actualPCB.setState("Finished");
                dispatcher.updatePCBS(actualPCB);
                dispatcher.updateStates();
                flagExec = false;
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

    public void restartStorage() {
        actualStorage = new Storage(512, 64);
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

    public Storage getActualStorage() {
        return actualStorage;
    }

    public void setActualStorage(Storage actualStorage) {
        this.actualStorage = actualStorage;
    }
    
}
