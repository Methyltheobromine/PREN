package T37_PREN.PythonInterop;

import java.awt.List;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by AW on 23.02.15.
 */
public abstract class ASignalHandler implements ISignalHandler {

    private PythonHandler _pythonHandler;
    private String _pythonScriptPath = "";
    private ArrayList<String> _scriptArguments;

    public ASignalHandler() {
        this._scriptArguments = new ArrayList<>();
    }

    public ArrayList<String> getScriptArguments() {
        return _scriptArguments;
    }

    public void setScriptArguments(final ArrayList<String> scriptArguments) {
        this._scriptArguments = scriptArguments;
    }

    public String getPythonScriptPath() {
        return _pythonScriptPath;
    }

    @Override
    public void setPythonScriptPath(final String _pythonScriptPath) {
        this._pythonScriptPath = _pythonScriptPath;
    }

    public PythonHandler getPythonHandler() {
        return _pythonHandler;
    }

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
