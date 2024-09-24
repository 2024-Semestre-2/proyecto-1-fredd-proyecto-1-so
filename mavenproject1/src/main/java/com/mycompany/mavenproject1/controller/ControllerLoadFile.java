
package com.mycompany.mavenproject1.controller;

import com.mycompany.Bslogic.Dispatcher;
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
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
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
        
        
        model.getActualStorage().fillStorage();
        view.storageBlock.setText(model.getActualStorage().storageToString());
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
        
        model.setDispatcher(new Dispatcher());

        // Habilitar selección múltiple de archivos
        fileChooser.setMultiSelectionEnabled(true);

        int returnValue = fileChooser.showOpenDialog(null);

        if (returnValue == JFileChooser.APPROVE_OPTION) {
            // Obtener los archivos seleccionados
            File[] selectedFiles = fileChooser.getSelectedFiles();
            
            for (File selectedFile : selectedFiles) {
                
                
                if (validateFile(getELementsOfFile(selectedFile))) {
                    model.getDispatcher().createPCB(getELementsOfFile(selectedFile), selectedFile.getAbsolutePath());
                } else {
                    view.consoleBlock.append(selectedFile.getAbsolutePath() + "\n\n");
                }
               
            }
        } else {
            System.out.println("No file found.");
        }
        
        
        model.getDispatcher().updateStates();
        //model.getActualStorage().fillStorage(model.getDispatcher());
        view.automaticExec.setEnabled(true);
        view.stepsExec.setEnabled(true);
        //view.storageBlock.setText(model.getActualStorage().storageToString());
        model.getMemory().resetMemory();
        model.getDispatcher().initializeMemo(model.getMemory(), model.getActualStorage());
        
        
        view.setStorage.setEnabled(false);
        view.setMemory.setEnabled(false);
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
    
    
    public boolean validateFile(ArrayList<String> lines) {
        String registros = "(AX|BX|CX|DX)";
        
        String regexLoad = "^LOAD\\s+" + registros + "$"; 
        String regexStore = "^STORE\\s+" + registros + "$"; 
        String regexMovRegToReg = "^MOV\\s+" + registros + ",\\s*" + registros + "$"; 
        String regexMovRegToVal = "^MOV\\s+" + registros + ",\\s*-?\\d+$";
        String regexAdd = "^ADD\\s+" + registros + "$"; 
        String regexSub = "^SUB\\s+" + registros + "$"; 
        String regexInc = "^INC(\\s+" + registros + ")?$"; 
        String regexDec = "^DEC(\\s+" + registros + ")?$"; 
        String regexSwap = "^SWAP\\s+" + registros + ",\\s*" + registros + "$"; 
        String regexCmp = "^CMP\\s+" + registros + ",\\s*" + registros + "$"; 
        String regexPush = "^PUSH\\s+" + registros + "$";
        String regexPop = "^POP\\s+" + registros + "$"; 
        String regexInt = "^INT\\s+\\d{2}H$";
        String regexJmp = "^JMP\\s+-\\d+$"; 
        String regexJeJne = "^(JE|JNE)\\s+-\\d+$"; 
        String regexParam = "^PARAM\\s+(\\d+,\\s*)*\\d+$"; 

        
        Pattern pattern = Pattern.compile(
                regexLoad + "|" + regexStore + "|" + regexMovRegToReg + "|" + regexMovRegToVal + "|" +
                regexAdd + "|" + regexSub + "|" + regexInc + "|" + regexDec + "|" + regexSwap + "|" +
                regexInt + "|" + regexJmp + "|" + regexCmp + "|" + regexJeJne + "|" + regexParam + "|" +
                regexPush + "|" + regexPop
        );

        
        for (String line: lines) {
            Matcher matcher = pattern.matcher(line.trim());
            if (!matcher.matches()) {
                view.consoleBlock.append("\n>> Error in " + line + " located in ");
                return false;
            }
        }
        
        return true;
    }
    
}
