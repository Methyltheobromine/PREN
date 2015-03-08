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

/**
 *
 * @author Severin
 */
public class BildVonWebcamAufnehmen {

    private String speicherort = "/home/pi/PREN/camera.jpg";

    static {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
    }

    public String takeAPicture() throws InterruptedException {

        VideoCapture camera = new VideoCapture(0);
        
        camera.open(0);
        
        boolean wset = camera.set(Highgui.CV_CAP_PROP_FRAME_WIDTH, 640);
        boolean hset = camera.set(Highgui.CV_CAP_PROP_FRAME_HEIGHT, 480);

        //System.out.println(wset);
        //System.out.println(hset);

        Thread.sleep(1000);
        
        if(!camera.isOpened()){
            System.out.println("Camera Error");
        }
        else{
            System.out.println("Camera OK?");
        }
        
        Mat frame = new Mat();

        camera.read(frame);
        System.out.println("Foto geschossen");
        System.out.println("Abmessung (Breite/Höhe): " + frame.size() + "\nPixel: " + frame.total());

        Highgui.imwrite(speicherort, frame);
        System.out.println("Foto geschpeichert");

        camera.release();
        if (!camera.isOpened()) {
            System.out.println("Kamera geschlossen!");
        } else {
            System.out.println("Kamera Error!");
        }

        return speicherort;
    }

}
