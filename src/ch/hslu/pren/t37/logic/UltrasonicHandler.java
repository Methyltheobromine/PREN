package ch.hslu.pren.t37.logic;

import ch.hslu.pren.t37.pythoninterop.ASignalHandler;
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
     */
    @Override
    public String evaluateScriptOutput() {
        String output = "";
        try {
            final String pythonOutput = getPythonHandler().getPythonOutput();
            final String[] splittedOutput = pythonOutput.split(";");
            System.out.println("Links: " + splittedOutput[0] + " Rechts: " + splittedOutput[1] + " Differenz: " + splittedOutput[2]);
            output = splittedOutput[2];
        } catch (Exception e) {
            e.printStackTrace();
        }
        return output;
    }
}
