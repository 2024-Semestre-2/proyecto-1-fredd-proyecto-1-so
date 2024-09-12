
package com.mycompany.mavenproject1.controller;

import com.mycompany.Bslogic.Instruction;
import com.mycompany.mavenproject1.model.Model;
import com.mycompany.mavenproject1.view.App;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import javax.swing.JFileChooser;

/**
 *
 * @author fredd
 */
public class ControllerLoadFile implements ActionListener {

    private App view;
    private Model model;

    public ControllerLoadFile(App view, Model model) {
        this.view = view;
        this.model = model;
        view.loadFile.addActionListener(this);
        view.startExecution.setEnabled(false);
    }

    public void start() {
        view.setTitle("SO");
        view.setLocationRelativeTo(null);
    }
    
    public void actionPerformed(ActionEvent e) {
        Object source = e.getSource();

        if (source == view.loadFile) {
            handleLoadFile();
            prepareExec();
        }
        else {
            System.out.println("Event unknown.");
        }
    }
    
    public void handleLoadFile() {
        JFileChooser fileChooser = new JFileChooser();
        view.moveExecution.setEnabled(false); // Asegúrate de que esta línea está dentro de un contexto adecuado
        cleanRegisters(); // Asegúrate de que esta línea está dentro de un contexto adecuado

        // Habilitar selección múltiple de archivos
        fileChooser.setMultiSelectionEnabled(true);

        int returnValue = fileChooser.showOpenDialog(null);

        if (returnValue == JFileChooser.APPROVE_OPTION) {
            // Obtener los archivos seleccionados
            File[] selectedFiles = fileChooser.getSelectedFiles();
            
            for (File selectedFile : selectedFiles) {
                //This is meanwhile validations are created
                model.getDispatcher().createPCB(getELementsOfFile(selectedFile));
            }
        } else {
            System.out.println("No file found.");
        }
        
    }
    
    public void prepareExec() {
        model.setActualPCB(model.getDispatcher().getAllProcesses().get(0));
        model.setUserInsToMemo();
        writeBlockMemory();
        view.startExecution.setEnabled(true);
        view.codeArea.setText(getText(model.getDispatcher().getAllProcesses().get(0).getLines()));
    }
    
    public ArrayList<String> getELementsOfFile(File selectedFile) {
        System.out.println("File selected: " + selectedFile.getAbsolutePath());
        ArrayList<String> lines = new ArrayList<>();
        

        // Leer cada archivo seleccionado
        try (BufferedReader reader = new BufferedReader(new FileReader(selectedFile))) {
            String line;
            while ((line = reader.readLine()) != null) {
                lines.add(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        return lines;
    }
    
    public String getText(ArrayList<String> lines) {
        String text = "";
        
        for (String line : lines) {
            text = text + "\n" + line;
        }
        
        return text;
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
    
    public void writeBlockMemory() {
        Instruction[] memoTemp = model.getMemory().getMemoryInstrucs();
        String text = "BCP SPACE USED BY BCP 0\n\n";
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
}
