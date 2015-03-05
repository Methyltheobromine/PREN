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
//
//        /////CAMERA!!!
//            //Foto aufnehmen
//            BildVonWebcamAufnehmen myPictureFromWebcam = new BildVonWebcamAufnehmen();
//            speicherort = myPictureFromWebcam.takeAPicture();
//
//            //Foto auswerten
//            BildAuswertungKorb myBildauswertung = new BildAuswertungKorb();
//            myBildauswertung.bildAuswerten(speicherort);
//            //Berechnen der Drehung des Turmes
//            //erfolgt in der BildAuswertungsKlasse        
//        /////CAMERA!!!
//            

        /////Ultraschall!!!
//            UltrasonicHandler ultrasonicHandler = new UltrasonicHandler("C:\\Users\\Severin\\Documents\\NetBeansProjects\\PythonPREN\\PeripherieAnsteuerung\\test1.py", new ArrayList<String>());
//            UltrasonicHandler ultrasonicHandler = new UltrasonicHandler("ultrasonic_PI_FINAL.py", new ArrayList<String>());
//            ultrasonicHandler.runPythonScript();
//            ultrasonicHandler.evaluateScriptOutput();
//            ultrasonicHandler.stopPythonProcess();
        /////Ultraschall!!!

        
        /////Stepper Drehturm!!!
            ArrayList<String> argsP = new ArrayList<>();
            argsP.add("10"); //Anzahl Schritte
            argsP.add("0"); //Nur 0 oder 1
            //StepperTurret stepperTurret = new StepperTurret("C:\\Users\\Severin\\Documents\\NetBeansProjects\\PythonPREN\\PeripherieAnsteuerung\\test2.py", argsP);
            StepperTurret stepperTurret = new StepperTurret("Stepper_Drehturm_PI_FINAL.py", argsP);
            stepperTurret.runPythonScript();
            stepperTurret.stopPythonProcess();
        /////Stepper Drehturm!!!
            
            
        /////Stepper FeedingBalls!!!
            argsP = new ArrayList<>();
            argsP.add("1"); //wird * gerechnet
            argsP.add("0"); //Nur 0 oder 1
            //StepperFeedingBalls stepperFeedingBalls = new StepperFeedingBalls("C:\\Users\\Severin\\Documents\\NetBeansProjects\\PythonPREN\\PeripherieAnsteuerung\\test2.py", argsP);
            StepperFeedingBalls stepperFeedingBalls = new StepperFeedingBalls("Stepper_Zufuerung_PI_FINAL.py", argsP);
            stepperFeedingBalls.runPythonScript();
            stepperFeedingBalls.stopPythonProcess();
        /////Stepper FeedingBalls!!!
            
            
        /////Stepper DCEngineHandler!!!
            argsP = new ArrayList<>();
            argsP.add("\"060\"");
            //DCEngineHandler dcEngineHandler = new DCEngineHandler("C:\\Users\\Severin\\Documents\\NetBeansProjects\\PythonPREN\\PeripherieAnsteuerung\\test2.py", argsP);
            DCEngineHandler dcEngineHandler = new DCEngineHandler("UART_PI_FINAL.py", argsP);
            dcEngineHandler.runPythonScript();
            dcEngineHandler.stopPythonProcess();
            
            argsP = new ArrayList<>();
            argsP.add("\"000\"");
            //DCEngineHandler dcEngineHandler = new DCEngineHandler("C:\\Users\\Severin\\Documents\\NetBeansProjects\\PythonPREN\\PeripherieAnsteuerung\\test2.py", argsP);
            dcEngineHandler = new DCEngineHandler("UART_PI_FINAL.py", argsP);
            dcEngineHandler.runPythonScript();
            dcEngineHandler.stopPythonProcess(); 
        /////Stepper DCEngineHandler!!!
    }

}