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

    private String speicherort = "camera.jpg";
    
    static {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
    }

    public String takeAPicture() {

        VideoCapture camera = new VideoCapture(0);

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
