/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package T37_PREN.Logic;

import T37_PREN.PythonInterop.ASignalHandler;
import java.util.ArrayList;
import java.util.regex.Pattern;
/**
 * Created by AW on 23.02.15.
 */
public class UltrasonicHandler extends ASignalHandler{

    public UltrasonicHandler(String scriptPath, ArrayList<String> scriptArguments){
        super.setPythonScriptPath(scriptPath);
        super.setScriptArguments(scriptArguments);
    }

    @Override
    public void evaluateScriptOutput() {
        try {
            boolean success=false;
            String pythonOutput =getPythonHandler().getPythonOutput();            
            //String[] splitUltrasonicOutput = pythonOutput.split(";");
            System.out.println(pythonOutput);
            success = pythonOutput.isEmpty();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
