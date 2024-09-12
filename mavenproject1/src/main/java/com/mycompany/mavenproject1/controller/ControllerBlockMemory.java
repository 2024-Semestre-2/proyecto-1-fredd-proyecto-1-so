
package com.mycompany.mavenproject1.controller;

import com.mycompany.mavenproject1.model.Model;
import com.mycompany.mavenproject1.view.App;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JOptionPane;

/**
 *
 * @author fredd
 */
public class ControllerBlockMemory implements ActionListener {
    private App view;
    private Model model;
    
    public ControllerBlockMemory(App view, Model model) {
        this.view = view;
        this.model = model;
        view.setInput.addActionListener(this);
        initTextArea();
    }
    
    public void initTextArea() {
        String text = "";
        int indexUs = model.getMemory().getIndexUser();
        
        for (int i = 0; i < model.getMemorySize(); i++) {
            if (i <= indexUs) {
                text = text + (String.valueOf(i) + " BCP empty space\n\n");
                continue;
            }
            text = text + (String.valueOf(i) + " User empty space\n\n");
        }
        
        view.memoryBlock.setText(text);
    }
    
    
    public void actionPerformed(ActionEvent e) {
        Object source = e.getSource();

        if (source == view.setInput) {
            handleInput();
        }
        else {
            System.out.println("Event unknown.");
        }
    }
    
    
    public void handleInput() {
        String input1 = view.memoryInputSize.getText();
        String input2 = view.memoryStartInput.getText();
        
        if (!input1.matches("^-?\\d+$")) {
            JOptionPane.showMessageDialog(null, "Type an integer to the memory size", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        if (!input2.matches("^-?\\d+$")) {
            JOptionPane.showMessageDialog(null, "Type an integer to the start of the user memory", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        int number1 = Integer.parseInt(input1);
        int number2 = Integer.parseInt(input2);
        
        if (number2 > number1) {
            JOptionPane.showMessageDialog(null, "The start of the memory can´t be higher than memory size", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        model.setMemorySize(number1);
        model.setUserMemStart(number2);
        
        JOptionPane.showMessageDialog(null, "Memory changed successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
        initTextArea();
        view.memoryInputSize.setText("");
        view.memoryStartInput.setText("");
        cleanMemory();
    }
    
    
    public void cleanMemory() {
        view.textBox1.setText("Empty");
        view.textBox2.setText("Empty");
        view.textBox3.setText("Empty");
        view.textBox4.setText("Empty");
        view.textBox5.setText("Empty");
        view.pcRegister.setText("Empty");
        view.irRegister.setText("Empty");
        
        model.getAC().setValue(null);
        model.getAX().setValue(null);
        model.getBX().setValue(null);
        model.getCX().setValue(null);
        model.getDX().setValue(null);
        
        view.codeArea.setText("");
    }
}
