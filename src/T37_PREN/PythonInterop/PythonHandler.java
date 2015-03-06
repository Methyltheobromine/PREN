/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package T37_PREN.PythonInterop;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

/**
 *
 * @author Severin
 */
public final class PythonHandler {

    private Process _pythonProcess;

    public PythonHandler() {
    }

    public PythonHandler(String scriptName, ArrayList<String> scriptArguments) throws IOException {
        startPythonScript(scriptName, scriptArguments);
    }

    public void startPythonScript(String scriptName, ArrayList<String> scriptArguments) throws IOException {
        ArrayList<String> pythonCommand=new ArrayList<String>();
        pythonCommand.add("python");
        pythonCommand.add(scriptName);
                
        for(String arg: scriptArguments){
            pythonCommand.add(arg);
        }
        ProcessBuilder processBuilder = new ProcessBuilder(pythonCommand);
        _pythonProcess = processBuilder.start();
    }

    public String getPythonOutput() throws Exception {
        String pythonOutput = "";
        if (!isScriptInitialized()) {
            throw new Exception("Script must be Initialized!");
        }

        BufferedReader in = new BufferedReader(new InputStreamReader(_pythonProcess.getInputStream()));
        pythonOutput = in.readLine();
        return pythonOutput;
    }

    public void stopPythonProcess() throws InterruptedException {
        if (isScriptInitialized()) {
          //  _pythonProcess.destroy();
            _pythonProcess.waitFor();
        }
    }

    private boolean isScriptInitialized() {
        return _pythonProcess != null;
    }

}
