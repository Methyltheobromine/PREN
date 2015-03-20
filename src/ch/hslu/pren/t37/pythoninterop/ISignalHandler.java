package ch.hslu.pren.t37.pythoninterop;

/**
 * Interface for Signal Handlers.
 * @author Team 37
 */
public interface ISignalHandler {

    /**
     * Runs the Python Script.
     */
    void runPythonScript();

    /**
     * Sets the Script Path.
     * @param path
     */
    void setPythonScriptPath(String path);

    /**
     * Evaluates the Script output.
     * @return Script Output
     */
    String evaluateScriptOutput();

    /**
     * Stops the Python Process.
     */
    void stopPythonProcess();
}
