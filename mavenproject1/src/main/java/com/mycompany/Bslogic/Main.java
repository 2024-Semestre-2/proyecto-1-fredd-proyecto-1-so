package com.mycompany.Bslogic;

import com.mycompany.mavenproject1.controller.ControllerBlockMemory;
import com.mycompany.mavenproject1.controller.ControllerLoadFile;
import com.mycompany.mavenproject1.controller.ControllerRegisters;
import com.mycompany.mavenproject1.model.Model;
import com.mycompany.mavenproject1.view.App;

/**
 *
 * @author fredd
 */
public class Main {

    public static void main(String[] args) {
        
        App view = new App();
        Model model = new Model();
        
        ControllerLoadFile controller = new ControllerLoadFile(view, model);
        ControllerRegisters controllerRegisters = new ControllerRegisters(view, model);
        ControllerBlockMemory controllerMemo = new ControllerBlockMemory(view, model);
        controller.start();
        view.setVisible(true);
    }

}
