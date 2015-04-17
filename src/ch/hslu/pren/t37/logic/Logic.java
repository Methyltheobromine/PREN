package ch.hslu.pren.t37.logic;

import ch.hslu.pren.t37.camera.BildAuswertungKorb;
import ch.hslu.pren.t37.camera.BildVonWebcamAufnehmen;
import ch.hslu.pren.t37.pythoninterop.ASignalHandler;
import ch.hslu.pren.t37.pythoninterop.ISignalHandler;
import ch.hslu.pren.t37.logic.ReadPropertyFile;
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

    private static int TURRET_DIST_MIDDLE; // Wert bei Initialisierung um in die Mitte zu fahren
    private static double MM_TO_STEP_CONVERSION; // Dividend bei Millimeter zu Drehturmschritten 
    private static double PIXEL_TO_STEP_CONVERSION; // Dividend bei Pixel zu Drehturmschritten
    private static String DC_STOP_SIGNAL; // DC Motor Stop Signal
    private static int BALL_COUNTER; // Anzahl Bälle

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
        System.out.println("Folgende Werte wurden aus dem config.properties geladen: \n"
                + "TURRET_DIST_MIDDLE : " + TURRET_DIST_MIDDLE + "\n"
                + "MM_TO_STEP_CONVERSION : " + MM_TO_STEP_CONVERSION + "\n"
                + "PIXEL_TO_STEP_CONVERSION : " + PIXEL_TO_STEP_CONVERSION + "\n"
                + "DC_STOP_SIGNAL : " + DC_STOP_SIGNAL + "\n"
                + "BALL_COUNTER : " + BALL_COUNTER + "\n"
                + "dcSPEED : " + dcSPEED);
    }

    /**
     * moves the turret to its initial position.
     *
     * @throws java.io.IOException
     * @throws java.lang.InterruptedException
     */
    public final void moveToInitialPosition() throws IOException, InterruptedException {

        TurretPositionInitialization turretPositionInitialization = new TurretPositionInitialization("../PeripherieAnsteuerung/Ready for Pi/Turret_Position_Initialization_PI_FINAL.py", new ArrayList<String>());
        turretPositionInitialization.runPythonScript();
        String signal = turretPositionInitialization.evaluateScriptOutput();
        if (!signal.equals("Ready")) {
            System.out.println("Initialisieren fehlgeschlagen");
            throw new IOException("Initilization failed");
        }
        turretPositionInitialization.stopPythonProcess();

        Thread.sleep(250);

        // move to middle
        positionTurret(TURRET_DIST_MIDDLE, "1");
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
        System.out.println(camSteps);
        if (camSteps != 0) {
            System.out.println("Start ausrichtung");
            String direction = camSteps < 0 ? "0" : "1";
            System.out.println(direction);
            positionTurret(abs(camSteps), direction);
            //turnByUltrasonicInformation();
        } else {
            System.out.println("test");
            //turnByUltrasonicInformation();
        }
        System.out.println("start dc engine");
        startDCEngine();
        for (int i = 1; i <= BALL_COUNTER; i++) {
            releaseBalls();
            System.out.println("Ball " + i + " geschossen");
        }
        dcEngineStop();
    }

    /**
     * Turns the Turret on the Information getting by the Ultrasonic Sensors
     *
     * @throws IOException
     * @throws InterruptedException
     */
    private void turnByUltrasonicInformation() throws IOException, InterruptedException {
        String direction;
        int ultrasonicStep = getUltrasonicSteps();
        //while(ultrasonicStep!=0){
        if (ultrasonicStep != 0) {
            direction = ultrasonicStep < 0 ? "0" : "1";
            do {
                positionTurret(ultrasonicStep, direction);
                ultrasonicStep = getUltrasonicSteps();
            } while (ultrasonicStep != 0);
        }
    }

    /**
     * Gets the steps from the picture evaluation.
     *
     * @return steps
     */
    private int getCalculatedStepsFromCamera() throws IOException, InterruptedException {
        double steps = 0;
        //Foto aufnehmen
        System.out.println("Start CAM");
        BildVonWebcamAufnehmen pictureFromWebcam = new BildVonWebcamAufnehmen("../PeripherieAnsteuerung/Ready for Pi/Camera_PI_FINAL.py", new ArrayList<String>());
        pictureFromWebcam.runPythonScript();
        pictureFromWebcam.stopPythonProcess();
        System.out.println("Stop CAM");
        //Foto auswerten
        BildAuswertungKorb bildauswertung = new BildAuswertungKorb();
        int stepsInPixel = bildauswertung.bildAuswerten();
        System.out.println("Steps in Pixel: " + stepsInPixel);
        steps = stepsInPixel / PIXEL_TO_STEP_CONVERSION; // int / double --> double
        System.out.println("CAM: Nach Berechnung, also Anzahl Schritte" + steps);
        steps = (int) Math.round(steps);
        System.out.println("CAM: Nach Berechnung GERUNDET, also Anzahl Schritte" + steps);

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
        //System.out.println();
        StepperTurret stepperTurret = new StepperTurret("../PeripherieAnsteuerung/Ready for Pi/Stepper_Drehturm_PI_FINAL.py", argsP);
        System.out.println("start turmdreheung");
        stepperTurret.runPythonScript();
        stepperTurret.stopPythonProcess();
        System.out.println("stop turmdreheung");
    }

    /**
     * Checks the Ultrasonic output and converts the difference to steps
     *
     * @return steps
     */
    private int getUltrasonicSteps() throws IOException, InterruptedException {
        int steps = 0;
        double ultrasonicDiff = 0;

        UltrasonicHandler ultrasonicHandler = new UltrasonicHandler("../PeripherieAnsteuerung/Ready for Pi/ultrasonic_PI_FINAL.py", new ArrayList<String>());
        ultrasonicHandler.runPythonScript();
        String diff = ultrasonicHandler.evaluateScriptOutput();
        ultrasonicHandler.stopPythonProcess();

        ultrasonicDiff = Double.parseDouble(diff);
        if ((abs(ultrasonicDiff)) > 1) {
            steps = (int) (ultrasonicDiff / MM_TO_STEP_CONVERSION);
        }
        return steps;
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
        argsP.add(getDcSPEED()); // max 199
        System.out.println("getDCSPeed liefert: " + getDcSPEED());
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

    /**
     * Releases the Magazine.
     *
     * @throws IOException
     * @throws InterruptedException
     */
    private void releaseBalls() throws IOException, InterruptedException {
        ArrayList<String> argsP = new ArrayList<>();
        argsP.add("48"); // Eine halbe Umdrehung
        argsP.add("1");  // Vorwärts drehen
        StepperMagazine stepperFeedingBalls = new StepperMagazine("../PeripherieAnsteuerung/Ready for Pi/Stepper_Zufuerung_PI_FINAL.py", argsP);
        stepperFeedingBalls.runPythonScript();
        stepperFeedingBalls.stopPythonProcess();
        Thread.sleep(500);
    }
}
