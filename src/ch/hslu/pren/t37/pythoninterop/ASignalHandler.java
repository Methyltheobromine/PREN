package ch.hslu.pren.t37.pythoninterop;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Abstract Class for Signal Handling.
 * @author Team 37
 */
public abstract class ASignalHandler implements ISignalHandler {

    private PythonHandler _pythonHandler;
    private String _pythonScriptPath = "";
    private List<String> _scriptArguments;

    /**
     * Constructor.
     */
    public ASignalHandler() {
        this._scriptArguments = new ArrayList<>();
    }

    /**
     * Gets the current Script Arguments.
     * @return ArrayList<String> Script Arguments
     */
    public List<String> getScriptArguments() {
        return _scriptArguments;
    }

    /**
     * Sets the Script Arguments.
     * @param scriptArguments
     */
    public void setScriptArguments(final List<String> scriptArguments) {
        this._scriptArguments = scriptArguments;
    }

    /**
     * Gets the current Script Path.
     * @return Script Path
     */
    public String getPythonScriptPath() {
        return _pythonScriptPath;
    }

    @Override
    public void setPythonScriptPath(final String _pythonScriptPath) {
        this._pythonScriptPath = _pythonScriptPath;
    }

    /**
     * Gets the current Python Handler.
     * @return PythonHandler
     */
    public PythonHandler getPythonHandler() {
        return _pythonHandler;
    }

    /**
     * Set the Python Handler.
     * @param _pythonHandler 
     */
    public void setPythonHandler(final PythonHandler _pythonHandler) {
        this._pythonHandler = _pythonHandler;
    }

    @Override
    public void stopPythonProcess() {
        if (_pythonHandler != null) {
            try {
                _pythonHandler.stopPythonProcess();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void runPythonScript() {
        try {
            _pythonHandler = new PythonHandler(getPythonScriptPath(), getScriptArguments());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
