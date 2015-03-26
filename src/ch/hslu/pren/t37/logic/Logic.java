package ch.hslu.pren.t37.logic;

import ch.hslu.pren.t37.camera.BildAuswertungKorb;
import ch.hslu.pren.t37.camera.BildVonWebcamAufnehmen;
import static java.lang.Math.abs;
import java.util.ArrayList;

/**
 * Executes the main controller logic:
 * @author Team 37
 */
public class Logic {

    private static final int TURRET_DIST_MIDDLE = 200;
    private static final int TURRET_MAX_LEFT = 200;
    private static final int MM_TO_STEP_CONVERSION = 20;
    private static final String DC_STOP_SIGNAL = "000";
    private static final int BALL_COUNTER=5;    
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

    /**
     * Constructor.
     */
    public Logic() {
        this._dcSPEED = "170";
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

    /**
     * Executes the main logic.
     * 1. takes the picture
     * 2. evaluates the template matching
     * 3. executes the turret stepper.
     * 4. starts the engine
     * 5. releases the magazine
     * @throws InterruptedException 
     */
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
        for(int i=1;i<BALL_COUNTER;i++){
            releaseBalls();
        }
        dcEngineStop();
    }

    /**
     * Gets the steps from the picture evaluation.
     * @return steps
     */
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

    /**
     * Positions the Turret.
     * @param camSteps
     * @param direction 
     */
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
    
    
    /**
     * Starts the DC Engine.
     * @throws InterruptedException 
     */
    private void startDCEngine() throws InterruptedException {
        ArrayList<String> argsP = new ArrayList<>();
        argsP.add(_dcSPEED); // max 170
        //DCEngineHandler dcEngineHandler = new DCEngineHandler("C:\\Users\\Severin\\Documents\\NetBeansProjects\\PythonPREN\\PeripherieAnsteuerung\\test2.py", argsP);
        DCEngineHandler dcEngineHandler = new DCEngineHandler("../PeripherieAnsteuerung/Ready for Pi/UART_PI_FINAL.py", argsP);
        dcEngineHandler.runPythonScript();
        dcEngineHandler.stopPythonProcess();
    }    

    /**
     * Stops the DC Engine.
     */
    private void dcEngineStop() {
        ArrayList<String> argsP = new ArrayList<>();
        argsP.add(DC_STOP_SIGNAL);
        //DCEngineHandler dcEngineHandler = new DCEngineHandler("C:\\Users\\Severin\\Documents\\NetBeansProjects\\PythonPREN\\PeripherieAnsteuerung\\test2.py", argsP);
        DCEngineHandler dcEngineHandler = new DCEngineHandler("../PeripherieAnsteuerung/Ready for Pi/UART_PI_FINAL.py", argsP);
        dcEngineHandler.runPythonScript();
        dcEngineHandler.stopPythonProcess();
    }

    /**
     * Releases the Magazine.
     */
    private void releaseBalls() {
        ArrayList<String> argsP = new ArrayList<>();
        argsP.add("1"); //wird * 100 gerechnet
        argsP.add("0"); //Nur 0 oder 1
        //StepperFeedingBalls stepperFeedingBalls = new StepperMagazine("C:\\Users\\Severin\\Documents\\NetBeansProjects\\PythonPREN\\PeripherieAnsteuerung\\test2.py", argsP);
        StepperMagazine stepperFeedingBalls = new StepperMagazine("../PeripherieAnsteuerung/Ready for Pi/Stepper_Zufuerung_PI_FINAL.py", argsP);
        stepperFeedingBalls.runPythonScript();
        stepperFeedingBalls.stopPythonProcess();
    }
    
}
