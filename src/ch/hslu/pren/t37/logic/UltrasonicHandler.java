package ch.hslu.pren.t37.logic;

import ch.hslu.pren.t37.pythoninterop.ASignalHandler;
import java.io.IOException;
import java.util.List;

/**
 * Ultrasonic Signal Handler.
 * Reads the output of the Ultrasonic Script.
 * @author Team 37
 */
public class UltrasonicHandler extends ASignalHandler {

    /**
     * Overloaded Constructor.
     * @param scriptPath
     * @param scriptArguments 
     */
    public UltrasonicHandler(final String scriptPath,final  List<String> scriptArguments) {
        super();
        super.setPythonScriptPath(scriptPath);
        super.setScriptArguments(scriptArguments);
    }

    /**
     * Evaluates the Ultrasonic output.
     * @return the difference between the left and right ultrasonic signals.
     * @throws java.io.IOException
     */
    @Override
    public String evaluateScriptOutput() throws IOException{
        String output = "";
            final String pythonOutput = getPythonHandler().getPythonOutput();
            final String[] splittedOutput = pythonOutput.split(";");
            System.out.println("Links: " + splittedOutput[0] + " Rechts: " + splittedOutput[1] + " Differenz: " + splittedOutput[2]);
            output = splittedOutput[2];
        return output;
    }
}
