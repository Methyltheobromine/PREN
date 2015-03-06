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
 * Format dass übermittelt werden muss: XXX
 * 120 - maximum
 * 000 - ausschalten
 */
public class DCEngineHandler extends ASignalHandler {

    public DCEngineHandler(String scriptPath, ArrayList<String> scriptArguments) {
        super.setPythonScriptPath(scriptPath);
        super.setScriptArguments(scriptArguments);
    }

    @Override
    public void evaluateScriptOutput() {

    }

}
