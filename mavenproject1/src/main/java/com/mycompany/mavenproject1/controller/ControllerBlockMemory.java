
package com.mycompany.mavenproject1.controller;

import com.mycompany.mavenproject1.model.Model;
import com.mycompany.mavenproject1.view.App;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import javax.swing.JFileChooser;
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
        view.setMemory.addActionListener(this);
        this.model = model;
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

        if (source == view.setMemory) {
            handleInput();
        }
        else {
            System.out.println("Event unknown.");
        }
    }
    
    
    public void handleInput() {
        String path = "C:\\Users\\fredd\\OneDrive\\Documentos\\GitHub\\proyecto-1-fredd-proyecto-1-so\\sizeMemory.txt"; 
        String path2 = "C:\\Users\\fredd\\OneDrive\\Documentos\\GitHub\\proyecto-1-fredd-proyecto-1-so\\sizeStorage.txt"; 
        
        ArrayList<String> memoryConf = openFile(path);

        try (BufferedReader br = new BufferedReader(new FileReader(path))) {
            String line;
            while ((line = br.readLine()) != null) {
                System.out.println(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        
        if (!memoryConf.get(0).matches("^-?\\d+$")) {
            JOptionPane.showMessageDialog(null, "Type an integer to the memory size", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        if (!memoryConf.get(1).matches("^-?\\d+$")) {
            JOptionPane.showMessageDialog(null, "Type an integer to the start of the user memory", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        int number1 = Integer.parseInt(memoryConf.get(0));
        int number2 = Integer.parseInt(memoryConf.get(1));
        
        if (number2 > number1) {
            JOptionPane.showMessageDialog(null, "The start of the memory can´t be higher than memory size", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        model.setMemorySize(number1);
        model.setUserMemStart(number2);
        
        JOptionPane.showMessageDialog(null, "Memory changed successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
        initTextArea();
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
    
    public ArrayList<String> openFile(String path) {
        ArrayList<String> temp = new ArrayList<String>();
        
        try (BufferedReader br = new BufferedReader(new FileReader(path))) {
            String line;
            while ((line = br.readLine()) != null) {
                temp.add(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        return temp;
    }
}
