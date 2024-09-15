package com.mycompany.mavenproject1.controller;

import com.mycompany.Bslogic.Instruction;
import com.mycompany.Bslogic.PCB;
import com.mycompany.mavenproject1.model.Model;
import com.mycompany.mavenproject1.view.App;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.Timer;

/**
 *
 * @author fredd
 */
public class ControllerRegisters implements ActionListener {
    private App view;
    private Model model;
    private String consoleInp = "";

    public ControllerRegisters(App view, Model model) {
        this.view = view;
        this.model = model;
        view.stepsExec.addActionListener(this);
        view.automaticExec.addActionListener(this);
        setupKeyListener();
    }
    
    private void setupKeyListener() {
        view.consoleBlock.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (model.flagInt09 && e.getKeyCode() == KeyEvent.VK_ENTER) {
                    e.consume(); 
                    
                    String[] lines = view.consoleBlock.getText().split("\n");
                    consoleInp = lines[lines.length - 1];
                    processConsoleInput();
                }
            }
        });
    }
    
    private void processConsoleInput() {
        // Verifica que la entrada no esté vacía
        if (!consoleInp.trim().isEmpty()) {
            view.consoleBlock.setEditable(false);
            model.flagInt09 = false;
            model.restartStorage();
            model.getActualStorage().fillStorage(model.getDispatcher());
            view.storageBlock.setText(model.getActualStorage().storageToString());
            view.storageBlock.setCaretPosition(view.storageBlock.getDocument().getLength()); 

            model.getDX().setValue(consoleInp);
            checkRegisters();

           
            new Timer(3000, e -> {
                view.stepsExec.setEnabled(true);
            }).start();
        }
    }

    public void actionPerformed(ActionEvent e) {
        Object source = e.getSource();

        if (source == view.stepsExec) {
            try {
                handleMoveExec();
            } catch (InterruptedException ex) {
                Logger.getLogger(ControllerRegisters.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else if (source == view.automaticExec) {
            handleAutoExec();
        }
        else {
            System.out.println("Event unknown.");
        }
    }
    
    public void handleMoveExec() throws InterruptedException {
        
        if (!model.flagExec) {
            prepareExec();
            
        } else {
            model.executionProgramSteps();
            checkRegisters();
            view.pcRegister.setText(String.valueOf(model.getActualInstruc()));
            view.irRegister.setText(String.valueOf(model.getActualInstrucString()));
            view.automaticExec.setEnabled(false);
            
            
            if (model.flagInt10) {
                view.consoleBlock.append(">> Element in DX: " + model.getDX().getValue() + "\n\n");
                model.flagInt10 = false;
                model.restartStorage();
                model.getActualStorage().fillStorage(model.getDispatcher());
                view.storageBlock.setText(model.getActualStorage().storageToString());
                view.storageBlock.setCaretPosition(70);
                
                Thread.sleep(3000);
            } else if (model.flagInt09) {
                view.consoleBlock.append(">> Write a number between 0-255: \n");
                view.consoleBlock.setEditable(true);
                view.stepsExec.setEnabled(false);

                // Espera hasta que el usuario presione Enter
                setupKeyListener();
                view.consoleBlock.append("\n\n");

            } else {
                model.getActualPCB().setState("Executing");
                model.getDispatcher().updatePCBS(model.getActualPCB());
                model.getDispatcher().updateStates();

                model.restartStorage();
                model.getActualStorage().fillStorage(model.getDispatcher());
                view.storageBlock.setText(model.getActualStorage().storageToString());
            }
            

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
        PCB nextPcb = model.getDispatcher().getNextPCB();
        if (nextPcb != null && !model.getDispatcher().checkIfAllFinished()) {
            if (model.getActualPCB() != null) {
                view.automaticExec.setEnabled(true);
                JOptionPane.showMessageDialog(null, "Process finished successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
                model.getActualStorage().fillStorage(model.getDispatcher());
                view.storageBlock.setText(model.getActualStorage().storageToString());
            }
            model.setActualPCB(nextPcb);
            
            
            cleanRegisters();
            model.getActualPCB().setState("Executing");
            
            model.getDispatcher().updatePCBS(model.getActualPCB());
            model.getDispatcher().updateStates();
            
            model.flagExec = true;
            model.setUserInsToMemo();
            writeBlockMemory();
            view.codeArea.setText(getText(nextPcb.getLines()));
            
            model.restartStorage();
            model.getActualStorage().fillStorage(model.getDispatcher());
            view.storageBlock.setText(model.getActualStorage().storageToString());
        } else {
            JOptionPane.showMessageDialog(null, "All process has been executed", "Success", JOptionPane.INFORMATION_MESSAGE);
            view.automaticExec.setEnabled(true);
            
            model.restartStorage();
            model.getActualStorage().fillStorage(model.getDispatcher());
            view.storageBlock.setText(model.getActualStorage().storageToString());
            
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
