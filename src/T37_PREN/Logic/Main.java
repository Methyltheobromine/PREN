package T37_PREN.Logic;

/**
 * Created by AW on 23.02.15.
 */
public class Main {
    public static void main(String args[]){
        final UltrasonicHandler ultrasonicHandler = new UltrasonicHandler("C:/WorkSpace/PREN/message.py","");
        ultrasonicHandler.runPythonScript();
        ultrasonicHandler.evaluateScriptOutput();
        ultrasonicHandler.stopPythonProcess();

    }
}
