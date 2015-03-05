package T37_PREN.PythonInterop;

/**
 * Created by AW on 23.02.15.
 */
public interface ISignalHandler {
    /**
     * 
     */
    void runPythonScript();
    
    /**
     * 
     * @param path 
     */
    void setPythonScriptPath(String path);
    
    /**
     * 
     */
    void evaluateScriptOutput();
    
    /**
     * 
     */
    void stopPythonProcess();
}
