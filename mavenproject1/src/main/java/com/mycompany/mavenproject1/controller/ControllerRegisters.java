
package com.mycompany.mavenproject1.controller;

import com.mycompany.mavenproject1.model.Model;
import com.mycompany.mavenproject1.view.App;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

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
        view.moveExecution.addActionListener(this);
        view.startExecution.addActionListener(this);
        view.moveExecution.setEnabled(false);
    }

    public void actionPerformed(ActionEvent e) {
        Object source = e.getSource();

        if (source == view.moveExecution) {
            handleMoveExec();
        } else if (source == view.startExecution) {
            handleStartExecution();
        }
        else {
            System.out.println("Event unknown.");
        }
    }
    
    public void handleMoveExec() {
        model.executionProgram();
        checkRegisters();
        view.pcRegister.setText(String.valueOf(model.getActualInstruc()));
        view.irRegister.setText(String.valueOf(model.getActualInstrucString()));
    }
    
    public void handleStartExecution() {
        model.executionProgram();
        checkRegisters();
        view.pcRegister.setText(String.valueOf(model.getActualInstruc()));
        view.irRegister.setText(String.valueOf(model.getActualInstrucString()));
        view.startExecution.setEnabled(false);
        view.moveExecution.setEnabled(true);
    }
    
    public void checkRegisters() {
        setTextReg("AX", model.getAX().getValue());
        setTextReg("BX", model.getBX().getValue());
        setTextReg("CX", model.getCX().getValue());
        setTextReg("DX", model.getDX().getValue());
        setTextReg("AC", model.getAC().getValue());
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
