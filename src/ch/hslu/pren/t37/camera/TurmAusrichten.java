/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ch.hslu.pren.t37.camera;

import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.highgui.Highgui;

/**
 *
 * @author Severin
 */
public class TurmAusrichten {

    private Point topLeft;
    private Point topRight;
    private Point downLeft;
    private Point downRight;
    private String picture;

    public TurmAusrichten(String picture, Point topLeft, Point topRight, Point downLeft, Point downRight) {
        this.picture = picture;
        this.topLeft = topLeft;
        this.topRight = topRight;
        this.downLeft = downLeft;
        this.downRight = downRight;
    }
    
    public int berechneAusrichtung(){
        double mittePicture;
        double mitteKorb;
        
        Mat img = Highgui.imread(picture, Highgui.CV_LOAD_IMAGE_COLOR);
         
        mittePicture = img.width() / 2;
        mitteKorb = (topRight.x - topLeft.x) / 2;
        mitteKorb = topLeft.x + mitteKorb;
        
        System.out.println("Mitte Korb: " + mitteKorb);
        System.out.println("Mitte Bild: " + mittePicture);
        System.out.println(mitteKorb - mittePicture);
        System.out.println("TEST");
        return (int)(mitteKorb - mittePicture); 
        
//        if(mitteKorb != mittePicture){
//            if(mitteKorb < mittePicture){
//                //kleiner bedeutet NACH LINKS DREHEN
//                /*
//                Schrittmotor ansteuern und je nach Pixel die verschoben werden müssen Anzahl Schritte
//                durchführen. Abfangen dass ein Unterschied von -+ 15 Pixel (oder so kei ahnig) ok ist.
//                */
//                System.out.println("Nach links drehen");
//                
//            }else{
//                //kleiner bedeutet NACH RECHTS DREHEN
//                System.out.println("Nach rechts drehen");
//                
//            }
//        }else{
//            System.out.println("Korb ist bereits eingemittet");
//        }
        
        
        // von vorne beginnen, also erneut ein Bild einlesen um zu bestätigen dass man effektiv gerade vor dem Korb steht.
    }


    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
}
