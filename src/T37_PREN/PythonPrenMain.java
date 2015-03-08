/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package T37_PREN;

import T37_PREN.Camera.BildAuswertungKorb;
import T37_PREN.Camera.BildVonWebcamAufnehmen;
import T37_PREN.Logic.DCEngineHandler;
import T37_PREN.Logic.StepperFeedingBalls;
import T37_PREN.Logic.StepperTurret;
import T37_PREN.Logic.UltrasonicHandler;
import java.util.ArrayList;

public class PythonPrenMain {

    private static String speicherort = "";

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws Exception {
        cam();
        //ultraschall();
        //stepperDrehturm();
        //stepperFeedingBalls();
        //dcEngineStart();
        //dcEngineStop();
    }

    public static void cam() throws InterruptedException {
        //Foto aufnehmen
        BildVonWebcamAufnehmen myPictureFromWebcam = new BildVonWebcamAufnehmen("../PeripherieAnsteuerung/Ready for Pi/Camera_PI_FINAL.py", new ArrayList<String>());
        myPictureFromWebcam.runPythonScript();
        myPictureFromWebcam.stopPythonProcess();

        //Foto auswerten
        BildAuswertungKorb myBildauswertung = new BildAuswertungKorb();
        myBildauswertung.bildAuswerten();

        //Berechnen der Drehung des Turmes
        //erfolgt in der BildAuswertungsKlasse 
    }

    public static void ultraschall() {
        //UltrasonicHandler ultrasonicHandler = new UltrasonicHandler("C:\\Users\\Severin\\Documents\\NetBeansProjects\\PythonPREN\\PeripherieAnsteuerung\\test1.py", new ArrayList<String>());
        UltrasonicHandler ultrasonicHandler = new UltrasonicHandler("../PeripherieAnsteuerung/Ready for Pi/ultrasonic_PI_FINAL.py", new ArrayList<String>());
        ultrasonicHandler.runPythonScript();
        ultrasonicHandler.evaluateScriptOutput();
        ultrasonicHandler.stopPythonProcess();
    }

    public static void stepperDrehturm() {
        ArrayList<String> argsP = new ArrayList<>();
        argsP.add("10"); //Anzahl Schritte (48 ist eine Umdrehung)
        argsP.add("0"); //Nur 0 oder 1
        //StepperTurret stepperTurret = new StepperTurret("C:\\Users\\Severin\\Documents\\NetBeansProjects\\PythonPREN\\PeripherieAnsteuerung\\test2.py", argsP);
        StepperTurret stepperTurret = new StepperTurret("../PeripherieAnsteuerung/Ready for Pi/Stepper_Drehturm_PI_FINAL.py", argsP);
        stepperTurret.runPythonScript();
        stepperTurret.stopPythonProcess();
    }

    public static void stepperFeedingBalls() {
        ArrayList<String> argsP = new ArrayList<>();
        argsP.add("1"); //wird * 100 gerechnet
        argsP.add("0"); //Nur 0 oder 1
        //StepperFeedingBalls stepperFeedingBalls = new StepperFeedingBalls("C:\\Users\\Severin\\Documents\\NetBeansProjects\\PythonPREN\\PeripherieAnsteuerung\\test2.py", argsP);
        StepperFeedingBalls stepperFeedingBalls = new StepperFeedingBalls("../PeripherieAnsteuerung/Ready for Pi/Stepper_Zufuerung_PI_FINAL.py", argsP);
        stepperFeedingBalls.runPythonScript();
        stepperFeedingBalls.stopPythonProcess();
    }

    public static void dcEngineStart() throws InterruptedException {
        ArrayList<String> argsP = new ArrayList<>();
        argsP.add("010");
        //DCEngineHandler dcEngineHandler = new DCEngineHandler("C:\\Users\\Severin\\Documents\\NetBeansProjects\\PythonPREN\\PeripherieAnsteuerung\\test2.py", argsP);
        DCEngineHandler dcEngineHandler = new DCEngineHandler("../PeripherieAnsteuerung/Ready for Pi/UART_PI_FINAL.py", argsP);
        dcEngineHandler.runPythonScript();
        dcEngineHandler.stopPythonProcess();
    }

    public static void dcEngineStop() {
        ArrayList<String> argsP = new ArrayList<>();
        argsP.add("\"000\"");
        //DCEngineHandler dcEngineHandler = new DCEngineHandler("C:\\Users\\Severin\\Documents\\NetBeansProjects\\PythonPREN\\PeripherieAnsteuerung\\test2.py", argsP);
        DCEngineHandler dcEngineHandler = new DCEngineHandler("../PeripherieAnsteuerung/Ready for Pi/UART_PI_FINAL.py", argsP);
        dcEngineHandler.runPythonScript();
        dcEngineHandler.stopPythonProcess();
    }

}
