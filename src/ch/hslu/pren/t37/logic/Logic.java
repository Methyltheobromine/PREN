package ch.hslu.pren.t37.logic;

import ch.hslu.pren.t37.Logger.PrenLogger;
import ch.hslu.pren.t37.camera.BildAuswertungKorb;
import ch.hslu.pren.t37.camera.BildVonWebcamAufnehmen;
import ch.hslu.pren.t37.pythoninterop.ASignalHandler;
import ch.hslu.pren.t37.pythoninterop.ISignalHandler;
import java.io.IOException;
import static java.lang.Math.abs;
import java.util.ArrayList;

/**
 * Executes the main controller logic:
 *
 * @author Team 37
 */
public class Logic {

    private ISignalHandler _sigHandler;
    private ASignalHandler _aSignalH;
    private DCEngineHandler _dcHandlerE;
    private BildVonWebcamAufnehmen _bvw;
    private BildAuswertungKorb _bak;
    private StepperTurret _st;
    private UltrasonicHandler _uh;
    private StepperMagazine _sM;
    private PrenLogger logger;

    private static int TURRET_DIST_MIDDLE; // Wert bei Initialisierung um in die Mitte zu fahren
    private static double MM_TO_STEP_CONVERSION; // Dividend bei Millimeter zu Drehturmschritten 
    private static double PIXEL_TO_STEP_CONVERSION; // Dividend bei Pixel zu Drehturmschritten
    private static String DC_STOP_SIGNAL; // DC Motor Stop Signal
    private static int BALL_COUNTER; // Anzahl Bälle
    private static String LOGLEVEL;

    /**
     * RPM Speed of DC-Engine 000 = Stop 199 = Max Speed
     */
    private static String dcSPEED;

    public String getDcSPEED() {
        return dcSPEED;
    }

    public void setDcSPEED(String _dcSPEED) {
        this.dcSPEED = _dcSPEED;
    }

    /**
     * Constructor.
     *
     * @throws IOException
     * @throws InterruptedException
     */
    public Logic() throws IOException, InterruptedException {
        loadVariableContent();
        moveToInitialPosition();
    }

    public void loadVariableContent() {
        TURRET_DIST_MIDDLE = Integer.parseInt(ReadPropertyFile.getProperties().getProperty("TURRET_DIST_MIDDLE"));
        MM_TO_STEP_CONVERSION = Double.parseDouble(ReadPropertyFile.getProperties().getProperty("MM_TO_STEP_CONVERSION"));
        PIXEL_TO_STEP_CONVERSION = Double.parseDouble(ReadPropertyFile.getProperties().getProperty("PIXEL_TO_STEP_CONVERSION"));
        DC_STOP_SIGNAL = ReadPropertyFile.getProperties().getProperty("DC_STOP_SIGNAL");
        BALL_COUNTER = Integer.parseInt(ReadPropertyFile.getProperties().getProperty("BALL_COUNTER"));
        dcSPEED = ReadPropertyFile.getProperties().getProperty("dcSPEED");
        LOGLEVEL = ReadPropertyFile.getProperties().getProperty("LogLevel");
        System.out.println(LOGLEVEL);
        PrenLogger.setCurrentLoglevel(PrenLogger.LogLevel.valueOf(LOGLEVEL));

        logger.log(PrenLogger.LogLevel.DEBUG, "Folgende Werte wurden aus dem config.properties geladen: \n"
                + "TURRET_DIST_MIDDLE : " + TURRET_DIST_MIDDLE + "\n"
                + "MM_TO_STEP_CONVERSION : " + MM_TO_STEP_CONVERSION + "\n"
                + "PIXEL_TO_STEP_CONVERSION : " + PIXEL_TO_STEP_CONVERSION + "\n"
                + "DC_STOP_SIGNAL : " + DC_STOP_SIGNAL + "\n"
                + "BALL_COUNTER : " + BALL_COUNTER + "\n"
                + "dcSPEED : " + dcSPEED + "\n"
                + "LogLevel : " + LOGLEVEL);
    }

    /**
     * moves the turret to its initial position.
     *
     * @throws java.io.IOException
     * @throws java.lang.InterruptedException
     */
    public void moveToInitialPosition() throws IOException, InterruptedException {

        TurretPositionInitialization turretPositionInitialization = new TurretPositionInitialization("../PeripherieAnsteuerung/Ready for Pi/Turret_Position_Initialization_PI_FINAL.py", new ArrayList<String>());
        turretPositionInitialization.runPythonScript();
        String signal = turretPositionInitialization.evaluateScriptOutput();
        if (!signal.equals("Ready")) {
            logger.log(PrenLogger.LogLevel.ERROR, "Initialisieren fehlgeschlagen");
            throw new IOException("Initilization failed");
        }
        turretPositionInitialization.stopPythonProcess();
        Thread.sleep(100);
        // move to middle
        positionTurret(TURRET_DIST_MIDDLE, "1");
        logger.log(PrenLogger.LogLevel.DEBUG, "Initialisierung fertig gestellt");
    }

    /**
     * Executes the main logic. 1. takes the picture 2. evaluates the template
     * matching 3. executes the turret stepper. 4. starts the engine 5. releases
     * the magazine
     *
     * @throws InterruptedException
     * @throws java.io.IOException
     */
    public void initialRun() throws InterruptedException, IOException {
        int camSteps = getCalculatedStepsFromCamera();
        if (camSteps != 0) {
            logger.log(PrenLogger.LogLevel.DEBUG, "Start ausrichtung");
            String direction = camSteps < 0 ? "0" : "1";
            logger.log(PrenLogger.LogLevel.DEBUG, "Richtung: " + direction);
            positionTurret(abs(camSteps), direction);
        }
        logger.log(PrenLogger.LogLevel.DEBUG, "Start DC Engine");
        startDCEngine();
        Thread.sleep(2000);
        for (int i = 1; i <= BALL_COUNTER; i++) {
            releaseBalls();
            logger.log(PrenLogger.LogLevel.DEBUG, "Ball " + i + " geschossen");
            Thread.sleep(1000);
        }
        dcEngineStop();
    }

    /**
     * Gets the steps from the picture evaluation.
     *
     * @return steps
     */
    private int getCalculatedStepsFromCamera() throws IOException, InterruptedException {
        double steps = 0;
        //Foto aufnehmen
        logger.log(PrenLogger.LogLevel.DEBUG, "Start Camera");
        BildVonWebcamAufnehmen pictureFromWebcam = new BildVonWebcamAufnehmen("../PeripherieAnsteuerung/Ready for Pi/Camera_PI_FINAL.py", new ArrayList<String>());
        pictureFromWebcam.runPythonScript();
        pictureFromWebcam.stopPythonProcess();
        logger.log(PrenLogger.LogLevel.DEBUG, "Stop Camera");
        //Foto auswerten
        BildAuswertungKorb bildauswertung = new BildAuswertungKorb();
        int stepsInPixel = bildauswertung.bildAuswerten();
        //System.out.println("Steps in Pixel: " + stepsInPixel);
        steps = stepsInPixel / PIXEL_TO_STEP_CONVERSION; // int / double --> double
        //System.out.println("CAM: Nach Berechnung, also Anzahl Schritte" + steps);
        steps = (int) Math.round(steps);
        //System.out.println("CAM: Nach Berechnung GERUNDET, also Anzahl Schritte" + steps);

        int stepsToTurn = (int) steps;
        //Berechnen der Drehung des Turmes
        //erfolgt in der BildAuswertungsKlasse 
        return stepsToTurn;
    }

    /**
     * Positions the Turret.
     *
     * @param camSteps
     * @param direction
     */
    private void positionTurret(int camSteps, String direction) throws IOException, InterruptedException {
        ArrayList<String> argsP = new ArrayList<>();
        argsP.add(Integer.toString(camSteps));
        argsP.add(direction);
        StepperTurret stepperTurret = new StepperTurret("../PeripherieAnsteuerung/Ready for Pi/Stepper_Drehturm_PI_FINAL.py", argsP);
        stepperTurret.runPythonScript();
        stepperTurret.stopPythonProcess();
    }

    /**
     * Starts the DC Engine.
     *
     * @throws InterruptedException
     * @throws IOException
     */
    private void startDCEngine() throws InterruptedException, IOException {
        ArrayList<String> argsP = new ArrayList<>();

        //Wie schnell muss der DC drehen?!?!?!?!?!?
        argsP.add(getDcSPEED());
        logger.log(PrenLogger.LogLevel.DEBUG, "getDCSPeed liefert: " + getDcSPEED());
        DCEngineHandler dcEngineHandler = new DCEngineHandler("../PeripherieAnsteuerung/Ready for Pi/UART_PI_FINAL.py", argsP);
        dcEngineHandler.runPythonScript();
        dcEngineHandler.stopPythonProcess();
    }

    /**
     * Stops the DC Engine.
     *
     * @throws IOException
     * @throws InterruptedException
     */
    private void dcEngineStop() throws IOException, InterruptedException {
        ArrayList<String> argsP = new ArrayList<>();
        argsP.add(DC_STOP_SIGNAL);
        DCEngineHandler dcEngineHandler = new DCEngineHandler("../PeripherieAnsteuerung/Ready for Pi/UART_PI_FINAL.py", argsP);
        dcEngineHandler.runPythonScript();
        dcEngineHandler.stopPythonProcess();
    }

//    /**
//     * Releases the Magazine.
//     *
//     * @throws IOException
//     * @throws InterruptedException
//     */
//    private void releaseBalls() throws IOException, InterruptedException {
//        ArrayList<String> argsP = new ArrayList<>();
//        argsP.add("48"); // Eine halbe Umdrehung
//        argsP.add("1");  // Vorwärts drehen
//        StepperMagazine stepperFeedingBalls = new StepperMagazine("../PeripherieAnsteuerung/Ready for Pi/Light_Barrier_PI_FINAL.py", argsP);
//        stepperFeedingBalls.runPythonScript();
//        stepperFeedingBalls.stopPythonProcess();
//        Thread.sleep(500);
//    }
    private void releaseBalls() throws IOException, InterruptedException {
        StepperMagazine stepperFeedingBalls = new StepperMagazine("../PeripherieAnsteuerung/Ready for Pi/Light_Barrier_PI_FINAL.py", new ArrayList<String>());
        stepperFeedingBalls.runPythonScript();

        String signal = stepperFeedingBalls.evaluateScriptOutput();
        if (!signal.equals("Ready")) {
            logger.log(PrenLogger.LogLevel.ERROR, "Initialisieren fehlgeschlagen");
            throw new IOException("Initilization failed");
        }
            
        stepperFeedingBalls.stopPythonProcess();
        Thread.sleep(500);
    }
}

