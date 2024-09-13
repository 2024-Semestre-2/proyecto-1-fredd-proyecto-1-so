package com.mycompany.mavenproject1.controller;

import com.mycompany.Bslogic.Instruction;
import com.mycompany.mavenproject1.model.Model;
import com.mycompany.mavenproject1.view.App;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import javax.swing.JOptionPane;

/**
 *
 * @author fredd
 */
public class ControllerRegisters implements ActionListener {
    private App view;
    private Model model;

    public ControllerRegisters(App view, Model model) {
        this.view = view;
        this.model = model;
        view.stepsExec.addActionListener(this);
        view.automaticExec.addActionListener(this);
        
    }

    public void actionPerformed(ActionEvent e) {
        Object source = e.getSource();

        if (source == view.stepsExec) {
            handleMoveExec();
        } else if (source == view.automaticExec) {
            handleAutoExec();
        }
        else {
            System.out.println("Event unknown.");
        }
    }
    
    public void handleMoveExec() {
        
        if (!model.flagExec) {
            prepareExec();
        } else {
            model.executionProgramSteps();
            checkRegisters();
            view.pcRegister.setText(String.valueOf(model.getActualInstruc()));
            view.irRegister.setText(String.valueOf(model.getActualInstrucString()));
            view.automaticExec.setEnabled(false);
        }
    }
    
    public void handleAutoExec() {
        
    }
    
    public void checkRegisters() {
        setTextReg("AX", model.getAX().getValue());
        setTextReg("BX", model.getBX().getValue());
        setTextReg("CX", model.getCX().getValue());
        setTextReg("DX", model.getDX().getValue());
        setTextReg("AC", model.getAC().getValue());
    }
    
    
    public void prepareExec() {
        if (model.getDispatcher().getNextPCB() != null) {
            if (model.getActualPCB() != null) {
                view.automaticExec.setEnabled(true);
                JOptionPane.showMessageDialog(null, "Process finished successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
            }
            
            model.setActualPCB(model.getDispatcher().getNextPCB());
            cleanRegisters();
            model.getActualPCB().setState("Executing");
            model.flagExec = true;
            model.setUserInsToMemo();
            writeBlockMemory();
            view.codeArea.setText(getText(model.getDispatcher().getNextPCB().getLines()));
        } else {
            JOptionPane.showMessageDialog(null, "All process has been executed", "Success", JOptionPane.INFORMATION_MESSAGE);
            view.automaticExec.setEnabled(true);
        }
    }
    
    public String getText(ArrayList<String> lines) {
        String text = "";
        
        for (String line : lines) {
            text = text + "\n" + line;
        }
        
        return text;
    }
    
    public void writeBlockMemory() {
        Instruction[] memoTemp = model.getMemory().getMemoryInstrucs();
        String text = "BCP SPACE USED BY BCP " + String.valueOf(model.getActualPCB().getPCBID()) + "\n\n";
        int indexUs = model.getMemory().getIndexUser();
    
        for (int i = 1; i < model.getMemorySize(); i++) {
            if (memoTemp[i] != null) {
                text = text + String.valueOf(i) + " User instruction " + (memoTemp[i].getCompIns()) + "\n\n";
                continue;
            }
            if (i <= indexUs) {
                text = text + (String.valueOf(i) + " BCP empty space\n\n");
                continue;
            }
            text = text + (String.valueOf(i) + " User empty space\n\n");
        }
        
        view.memoryBlock.setText(text);
    }
    
    public void setTextReg(String regName, String value) {
        if (regName.equals("AC")) {
            if (value == null) {
                return;
            }
            view.textBox1.setText(value);
        } else if (regName.equals("AX")) {
            if (value == null) {
                return;
            }
            view.textBox2.setText(value);
        } else if (regName.equals("BX")) {
            if (value == null) {
                return;
            }
            view.textBox3.setText(value);
        } else if (regName.equals("CX")) {
            if (value == null) {
                return;
            }
            view.textBox4.setText(value);
        } else {
            if (value == null) {
                return;
            }
            view.textBox5.setText(value);
        }
    }
    
    public void cleanRegisters() {
        view.textBox1.setText("Empty");
        view.textBox2.setText("Empty");
        view.textBox3.setText("Empty");
        view.textBox4.setText("Empty");
        view.textBox5.setText("Empty");
        view.pcRegister.setText("Empty");
        view.irRegister.setText("Empty");
    }
}
