/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package T37_PREN.Camera;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.highgui.Highgui;
import org.opencv.highgui.VideoCapture;
import T37_PREN.PythonInterop.ASignalHandler;
import java.util.ArrayList;

/**
 *
 * @author Severin
 */
public class BildVonWebcamAufnehmen extends ASignalHandler {

    //private String speicherort = "../camera.jpg";
    public BildVonWebcamAufnehmen(String scriptPath, ArrayList<String> scriptArguments) {
        super.setPythonScriptPath(scriptPath);
        super.setScriptArguments(scriptArguments);
    }

    @Override
    public void evaluateScriptOutput() {

    }

    static {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
    }

    public void takeAPicture() throws InterruptedException {

//        VideoCapture camera = new VideoCapture(1);
//        if(!camera.isOpened()){
//            System.out.println("1Camera Error");
//        }
//        else{
//            System.out.println("1Camera OK?");
//        }
//        camera.open(1);
//        if(!camera.isOpened()){
//            System.out.println("2Camera Error");
//        }
//        else{
//            System.out.println("2Camera OK?");
//        }
//        boolean wset = camera.set(Highgui.CV_CAP_PROP_FRAME_WIDTH, 640);
//        boolean hset = camera.set(Highgui.CV_CAP_PROP_FRAME_HEIGHT, 480);
//
//        //System.out.println(wset);
//        //System.out.println(hset);
//
//        Thread.sleep(1000);
//        
//        Mat frame = new Mat();
//
//        camera.read(frame);
//        System.out.println("Foto geschossen");
//        System.out.println("Abmessung (Breite/Höhe): " + frame.size() + "\nPixel: " + frame.total());
//
//        Highgui.imwrite(speicherort, frame);
//        System.out.println("Foto geschpeichert");
//
//        camera.release();
//        if (!camera.isOpened()) {
//            System.out.println("Kamera geschlossen!");
//        } else {
//            System.out.println("Kamera Error!");
//        }
        // return speicherort;
    }

}
