/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package T37_PREN.Logic;

import T37_PREN.PythonInterop.ASignalHandler;
import java.util.ArrayList;

/**
 *
 * @author Severin
 */
public class DCEngineHandler extends ASignalHandler{

    public DCEngineHandler(String scriptPath, ArrayList<String> scriptArguments) {
        super.setPythonScriptPath(scriptPath);
        super.setScriptArguments(scriptArguments);
    }

    // String muss 3 zeichen enthalten format: "xxx"
    // zum beenden muss man 3x 0 ("000") schicken
    // range: 000 - 120

    @Override
    public void evaluateScriptOutput() {
        
    }
    
    
}
