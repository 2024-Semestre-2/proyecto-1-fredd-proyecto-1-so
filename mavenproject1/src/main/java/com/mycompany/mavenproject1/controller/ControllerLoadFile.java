
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
        view.automaticExec.setEnabled(false);
        view.stepsExec.setEnabled(false);
    }

    public void start() {
        view.setTitle("SO");
        view.setLocationRelativeTo(null);
    }
    
    public void actionPerformed(ActionEvent e) {
        Object source = e.getSource();

        if (source == view.loadFile) {
            handleLoadFile();
        }
        else {
            System.out.println("Event unknown.");
        }
    }
    
    public void handleLoadFile() {
        JFileChooser fileChooser = new JFileChooser();
        //view.stepsExec.setEnabled(false);

        // Habilitar selección múltiple de archivos
        fileChooser.setMultiSelectionEnabled(true);

        int returnValue = fileChooser.showOpenDialog(null);

        if (returnValue == JFileChooser.APPROVE_OPTION) {
            // Obtener los archivos seleccionados
            File[] selectedFiles = fileChooser.getSelectedFiles();
            
            for (File selectedFile : selectedFiles) {
                //This is meanwhile validations are created
                model.getDispatcher().createPCB(getELementsOfFile(selectedFile), selectedFile.getAbsolutePath());
            }
        } else {
            System.out.println("No file found.");
        }
        
        
        model.getDispatcher().updateStates();
        model.getActualStorage().fillStorage(model.getDispatcher());
        view.automaticExec.setEnabled(true);
        view.stepsExec.setEnabled(true);
        view.storageBlock.setText(model.getActualStorage().storageToString());
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
    
}
