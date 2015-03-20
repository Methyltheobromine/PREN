package T37_PREN.Logic;

import T37_PREN.Camera.BildAuswertungKorb;
import T37_PREN.Camera.BildVonWebcamAufnehmen;
import static java.lang.Math.abs;
import java.util.ArrayList;

public class Logic {

    private static final int TURRET_DIST_MIDDLE = 200;
    private static final int TURRET_MAX_LEFT = 200;
    private static final int MM_TO_STEP_CONVERSION = 20;
    private static final String DC_STOP_SIGNAL = "000";
    private int _ballCounter;
    
    /**
     * RPM Speed of DC-Engine
     * 000 = Stop
     * 170 = Max Speed
     */
    private String _dcSPEED;

    public String getDcSPEED() {
        return _dcSPEED;
    }

    public void setDcSPEED(String _dcSPEED) {
        this._dcSPEED = _dcSPEED;
    }

    public Logic() {
        this._dcSPEED = "170";
        this._ballCounter = 5;
        moveToInitialPosition();
    }

    /**
     * moves the turret to its initial position.
     */
    private void moveToInitialPosition() {
        // move to turretStop
        positionTurret(TURRET_MAX_LEFT, "0");
        // move to middle
        positionTurret(TURRET_DIST_MIDDLE, "1");
    }

    public void initialRun() throws InterruptedException {
        int camSteps = getCalculatedStepsFromCamera();
        if (camSteps != 0) {
            String direction = camSteps < 0 ? "0" : "1";
            positionTurret(abs(camSteps), direction);
            int ultrasonicStep = getUltrasonicSteps();
            while(ultrasonicStep!=0){
                direction = ultrasonicStep < 0 ? "0" : "1";
                positionTurret(ultrasonicStep, direction);
            }
        }
        startDCEngine();
        for(int i=1;i<_ballCounter;i++){
            releaseBalls();
        }
        dcEngineStop();
    }

    private int getCalculatedStepsFromCamera() {
        int steps = 0;
        //Foto aufnehmen
        BildVonWebcamAufnehmen pictureFromWebcam = new BildVonWebcamAufnehmen("../PeripherieAnsteuerung/Ready for Pi/Camera_PI_FINAL.py", new ArrayList<String>());
        pictureFromWebcam.runPythonScript();
        pictureFromWebcam.stopPythonProcess();

        //Foto auswerten
        BildAuswertungKorb bildauswertung = new BildAuswertungKorb();
        bildauswertung.bildAuswerten();

        //Berechnen der Drehung des Turmes
        //erfolgt in der BildAuswertungsKlasse 
        return steps;
    }

    private void positionTurret(int camSteps, String direction) {
        ArrayList<String> argsP = new ArrayList<>();
        argsP.add(Integer.toString(camSteps)); //Anzahl Schritte (48 ist eine Umdrehung)
        argsP.add(direction); //Nur 0 oder 1
        //StepperTurret stepperTurret = new StepperTurret("C:\\Users\\Severin\\Documents\\NetBeansProjects\\PythonPREN\\PeripherieAnsteuerung\\test2.py", argsP);
        StepperTurret stepperTurret = new StepperTurret("../PeripherieAnsteuerung/Ready for Pi/Stepper_Drehturm_PI_FINAL.py", argsP);
        stepperTurret.runPythonScript();
        stepperTurret.stopPythonProcess();
    }

    /**
     * Checks the Ultrasonic output and converts the difference to steps
     * @return steps
     */
    private int getUltrasonicSteps() {
        int steps = 0;
        double ultrasonicDiff=0;

        UltrasonicHandler ultrasonicHandler = new UltrasonicHandler("../PeripherieAnsteuerung/Ready for Pi/ultrasonic_PI_FINAL.py", new ArrayList<String>());
        ultrasonicHandler.runPythonScript();
        String diff=ultrasonicHandler.evaluateScriptOutput();
        ultrasonicHandler.stopPythonProcess();
        
        ultrasonicDiff = Double.parseDouble(diff);
        if(abs(ultrasonicDiff)>1){
            steps=(int) (ultrasonicDiff/MM_TO_STEP_CONVERSION);
        }
        return steps;
    }
    
    
    private void startDCEngine() throws InterruptedException {
        ArrayList<String> argsP = new ArrayList<>();
        argsP.add(_dcSPEED); // max 170
        //DCEngineHandler dcEngineHandler = new DCEngineHandler("C:\\Users\\Severin\\Documents\\NetBeansProjects\\PythonPREN\\PeripherieAnsteuerung\\test2.py", argsP);
        DCEngineHandler dcEngineHandler = new DCEngineHandler("../PeripherieAnsteuerung/Ready for Pi/UART_PI_FINAL.py", argsP);
        dcEngineHandler.runPythonScript();
        dcEngineHandler.stopPythonProcess();
    }    

    private void dcEngineStop() {
        ArrayList<String> argsP = new ArrayList<>();
        argsP.add(DC_STOP_SIGNAL);
        //DCEngineHandler dcEngineHandler = new DCEngineHandler("C:\\Users\\Severin\\Documents\\NetBeansProjects\\PythonPREN\\PeripherieAnsteuerung\\test2.py", argsP);
        DCEngineHandler dcEngineHandler = new DCEngineHandler("../PeripherieAnsteuerung/Ready for Pi/UART_PI_FINAL.py", argsP);
        dcEngineHandler.runPythonScript();
        dcEngineHandler.stopPythonProcess();
    }

    private void releaseBalls() {
        ArrayList<String> argsP = new ArrayList<>();
        argsP.add("1"); //wird * 100 gerechnet
        argsP.add("0"); //Nur 0 oder 1
        //StepperFeedingBalls stepperFeedingBalls = new StepperFeedingBalls("C:\\Users\\Severin\\Documents\\NetBeansProjects\\PythonPREN\\PeripherieAnsteuerung\\test2.py", argsP);
        StepperFeedingBalls stepperFeedingBalls = new StepperFeedingBalls("../PeripherieAnsteuerung/Ready for Pi/Stepper_Zufuerung_PI_FINAL.py", argsP);
        stepperFeedingBalls.runPythonScript();
        stepperFeedingBalls.stopPythonProcess();
    }
    
}
