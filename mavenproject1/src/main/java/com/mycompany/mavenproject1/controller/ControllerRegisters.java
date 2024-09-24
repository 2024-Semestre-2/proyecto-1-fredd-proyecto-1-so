package com.mycompany.mavenproject1.controller;

import com.mycompany.Bslogic.Instruction;
import com.mycompany.Bslogic.PCB;
import com.mycompany.mavenproject1.model.Model;
import com.mycompany.mavenproject1.view.App;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.Timer;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 *
 * @author fredd
 */
public class ControllerRegisters implements ActionListener {
    private App view;
    private Model model;
    private String consoleInp = "";
    private boolean isAuto = false;
    
    private ScheduledExecutorService executorService;

    private LocalDateTime startTime;
    private LocalDateTime endTime;
    

    public ControllerRegisters(App view, Model model) {
        this.view = view;
        this.model = model;
        view.stepsExec.addActionListener(this);
        view.automaticExec.addActionListener(this);
        setupKeyListener();
    }
    
    

    


    private void clearKeyListeners() {
        KeyListener[] listeners = view.consoleBlock.getKeyListeners();
        for (KeyListener listener : listeners) {
            view.consoleBlock.removeKeyListener(listener);
        }
    }
    
    private void setupKeyListener() {
        clearKeyListeners();
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
            //model.restartStorage();
            //model.getActualStorage().fillStorage(model.getDispatcher());
            writeBlockMemory();
            view.memoryBlock.setCaretPosition(0); 

            model.getDX().setValue(consoleInp);
            checkRegisters();

            if (isAuto) {
                new Timer(3000, e -> {
                startAutomaticExecution();
                }).start();
            } else {
                new Timer(3000, e -> {
                view.stepsExec.setEnabled(true);
                }).start();
            }
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
    
    
    public void startAutomaticExecution() {
        if (executorService != null && !executorService.isShutdown()) {
            stopAutomaticExecution();
        }
        executorService = Executors.newSingleThreadScheduledExecutor();
        executorService.scheduleAtFixedRate(() -> {
            try {
                handleMoveExecAuto();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }, 0, 300, TimeUnit.MILLISECONDS); // Ejecuta cada 100 ms
    }

    public void stopAutomaticExecution() {
        if (executorService != null && !executorService.isShutdown()) {
            executorService.shutdownNow(); // Fuerza la detención
        }
    }
    

    
    
    public void handleMoveExecAuto() throws InterruptedException {
        
        if (!isAuto) {
            return;
        }
        
        if (!model.flagExec) {
            prepareExec();
            
        } else {

            checkRegisters();
            model.executionProgramSteps();
            view.pcRegister.setText(String.valueOf(model.getActualInstruc()));
            view.irRegister.setText(String.valueOf(model.getActualInstrucString()));
            view.automaticExec.setEnabled(false);
            
            
            if (model.flagInt10) {
                view.consoleBlock.append(">> Element in DX: " + model.getDX().getValue() + "\n\n");
                model.flagInt10 = false;
                //model.restartStorage();
                //model.getActualStorage().fillStorage(model.getDispatcher());
                writeBlockMemory();
                view.memoryBlock.setCaretPosition(0);
                
                Thread.sleep(3000);
            } else if (model.flagInt09) {
                view.consoleBlock.append(">> Write a number between 0-255: \n");
                view.consoleBlock.setEditable(true);
                view.stepsExec.setEnabled(false);

                stopAutomaticExecution();
                // Espera hasta que el usuario presione Enter
                setupKeyListener();
                view.consoleBlock.append("\n\n");

            } else {
                model.getActualPCB().setState("Executing");
                model.getDispatcher().updatePCBS(model.getActualPCB());
                model.getDispatcher().updateStates();

                //model.restartStorage();
                //model.getActualStorage().fillStorage(model.getDispatcher());
                view.storageBlock.setText(model.getActualStorage().storageToString());
                writeBlockMemory();
            }
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
                //model.restartStorage();
                //model.getActualStorage().fillStorage(model.getDispatcher());
                writeBlockMemory();
                view.memoryBlock.setCaretPosition(0);
                
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

                //model.restartStorage();
                //model.getActualStorage().fillStorage(model.getDispatcher());
                view.storageBlock.setText(model.getActualStorage().storageToString());
                writeBlockMemory();
            }
        }
    }
    
    
    public void handleAutoExec() {
        isAuto = true;
        startAutomaticExecution();
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
                view.consoleBlock.append(model.getMsgError());
                model.setMsgError("\n>> Process finished successfully. " + "\n");
                
                endTime = LocalDateTime.now();
                Duration duration = Duration.between(startTime, endTime);
                view.consoleBlock.append(String.format(">> Execution time: %02d:%02d seconds\n\n",
                duration.toMinutesPart(), duration.toSecondsPart()));

                model.getActualPCB().setStartTime(startTime);
                model.getActualPCB().setEndTime(endTime);
                
                model.getActualPCB().setState("Finished");
                model.getDispatcher().updatePCBS(model.getActualPCB());
                model.getDispatcher().updateStates();
                //model.getActualStorage().fillStorage(model.getDispatcher());
                
                model.setMemory(model.getDispatcher().manageMemo(model.getMemory()));
                model.getActualStorage().addToStorage(model.getActualPCB());
                
                model.setActualStorage(model.getDispatcher().manageStorage(model.getActualStorage()));
                //writeBlockMemory();
            }
            
            model.setActualPCB(nextPcb);
            
            
            cleanRegisters();
            model.getActualPCB().setState("Executing");
            
            model.getDispatcher().updatePCBS(model.getActualPCB());
            model.getDispatcher().updateStates();
            
            model.flagExec = true;
            model.setUserInsToMemo();

            view.codeArea.setText(getText(nextPcb.getLines()));
            
            //model.restartStorage();
            //model.getActualStorage().fillStorage(model.getDispatcher());
            writeBlockMemory();
            view.storageBlock.setText(model.getActualStorage().storageToString());
            
            
            startTime = LocalDateTime.now();
            
        } else {

            
            isAuto = false;

            view.consoleBlock.append(model.getMsgError());
            model.setMsgError("\n>> Process finished successfully. " + "\n");
            endTime = LocalDateTime.now();
            Duration duration = Duration.between(startTime, endTime);
            view.consoleBlock.append(String.format(">> Execution time: %02d:%02d seconds\n\n",
            duration.toMinutesPart(), duration.toSecondsPart()));
            
            view.consoleBlock.append("\n>> All process has been executed. " + "\n\n");
            
            model.getActualPCB().setStartTime(startTime);
            model.getActualPCB().setEndTime(endTime);
            
            
            model.getActualPCB().setState("Finished");
            model.getDispatcher().updatePCBS(model.getActualPCB());
            model.getDispatcher().updateStates();
            model.setMemory(model.getDispatcher().manageMemo(model.getMemory()));
            
            
            writeBlockMemory();
            
            //model.restartStorage();
            model.getActualStorage().addToStorage(model.getActualPCB());
            view.storageBlock.setText(model.getActualStorage().storageToString());
            
            
            view.stepsExec.setEnabled(false);
            view.automaticExec.setEnabled(false);
            
            view.setStorage.setEnabled(true);
            view.setMemory.setEnabled(true);
            
            showProcessStatistics();
            model.setActualPCB(null);
            stopAutomaticExecution();

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
        
        String text = "";
        int indexUs = model.getMemory().getIndexUser();
        
    
        for (int i = 0; i < model.getMemorySize(); i++) {
            if (i < indexUs) {
                if (memoTemp[i] != null && model.getDispatcher().searchPCB(memoTemp[i].getActualPCBID()) != null && !model.getDispatcher().searchPCB(memoTemp[i].getActualPCBID()).getState().equals("Finished")) {
                    PCB tempPCB = model.getDispatcher().searchPCB(memoTemp[i].getActualPCBID());
                    text = text + ("Storage position: " + String.valueOf(i) + " || PCB with id: " + String.valueOf(tempPCB.getPCBID()) + " || State: " + tempPCB.getState() + " || located in: " + tempPCB.getPath()) + "\n\n"; 
                    
                    continue;
                } else {
                    text = text + (String.valueOf(i) + " BCP empty space\n\n");
                    continue;
                }
            }
            if (memoTemp[i] != null) {
                text = text + String.valueOf(i) + " User instruction " + (memoTemp[i].getCompIns()) + "\n\n";
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

    public void showProcessStatistics() {
        ArrayList<PCB> allPcbs = model.getDispatcher().getAllProcesses();
        view.consoleBlock.append("Process Stadistics:\n");

        for (PCB pcb : allPcbs) {
            LocalDateTime startTimePCB = pcb.getStartTime();
            LocalDateTime endTimePCB = pcb.getEndTime();

            if (startTimePCB != null && endTimePCB != null) {
                
                Duration duration = Duration.between(startTimePCB, endTimePCB);

                view.consoleBlock.append(String.format(
                    "Process ID: %d | Start: %s | End: %s | Duration: %d seconds\n", 
                    pcb.getPCBID(), 
                    startTimePCB.toLocalTime(), 
                    endTimePCB.toLocalTime(), 
                    duration.getSeconds() 
                ));
            }
        }
        view.consoleBlock.append("\n");
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
