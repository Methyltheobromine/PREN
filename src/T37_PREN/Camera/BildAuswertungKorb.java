/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package T37_PREN.Camera;

import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.highgui.Highgui;
import org.opencv.imgproc.Imgproc;

/**
 *
 * @author Severin
 */
public class BildAuswertungKorb {

    static {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
    }

    public void bildAuswerten(String speicherort) {

//        String inFile = "C:/Users/Severin/Documents/NetBeansProjects/PREN_Bildauswertung/src/pren_bildauswertung/bild1.jpg";
//        String templateFile = "C:/Users/Severin/Documents/NetBeansProjects/PREN_Bildauswertung/src/pren_bildauswertung/bild11.jpg";
//        String outFile = "C:/Users/Severin/Documents/NetBeansProjects/PREN_Bildauswertung/src/pren_bildauswertung/bild111.jpg";
        
        //Bild in dem gesucht werden soll
        String inFile = "/home/pi/PREN/Bilder/test4.jpg"; //hier ersetzen durch speicherort
        //das Bild dass im infile gesucht wird
        String templateFile = "/home/pi/PREN/Bilder/korb.jpg";
        //Lösung wird in diesem Bild präsentiert
        String outFile = "/home/pi/PREN/LoesungsBild.jpg";
        //Überprüfungswert wird gesetzt
        int match_method = Imgproc.TM_CCOEFF_NORMED;

        //das original Bild und das zu suchende werden geladen
        Mat img = Highgui.imread(inFile, Highgui.CV_LOAD_IMAGE_COLOR);
        Mat templ = Highgui.imread(templateFile, Highgui.CV_LOAD_IMAGE_COLOR);

        // Lösungsmatrix generieren
        int result_cols = img.cols() - templ.cols() + 1;
        int result_rows = img.rows() - templ.rows() + 1;
        Mat result = new Mat(result_rows, result_cols, CvType.CV_32FC1);
        
        // Suchen und normalisieren
        Imgproc.matchTemplate(img, templ, result, match_method);
        Core.normalize(result, result, 0, 1, Core.NORM_MINMAX, -1, new Mat());

        // Mit MinMax Logik wird der beste "Match" gesucht
        Core.MinMaxLocResult mmr = Core.minMaxLoc(result);

        Point matchLoc;
        if (match_method == Imgproc.TM_SQDIFF || match_method == Imgproc.TM_SQDIFF_NORMED) {
            matchLoc = mmr.minLoc;
        } else {
            matchLoc = mmr.maxLoc;
        }

        // Darstellen
        Core.rectangle(img, matchLoc, new Point(matchLoc.x + templ.cols(),
                matchLoc.y + templ.rows()), new Scalar(0, 255, 0), 10);

        // Alle 4 Eckpunkte speichern
        Point topLeft = new Point(matchLoc.x, matchLoc.y);
        System.out.println("Punkt oben Links:(X,Y) " + topLeft);
        
        Point topRight = new Point(matchLoc.x+templ.cols(), matchLoc.y);
        System.out.println("Punkt oben Rechts:(X,Y) " + topRight);
        
        Point downLeft = new Point(matchLoc.x, matchLoc.y+templ.rows());
        System.out.println("Punkt unten Links:(X,Y) " + downLeft); 
        
        Point downRight = new Point(matchLoc.x+templ.cols(), matchLoc.y+templ.rows());
        System.out.println("Punkt unten Rechts:(X,Y) " + downRight);

// Lösungsbild speichern
        System.out.println("Writing " + outFile);
        Highgui.imwrite(outFile, img);

        TurmAusrichten myWurfBot300DrehtSich = new TurmAusrichten(outFile, topLeft, topRight, downLeft, downRight);
        myWurfBot300DrehtSich.berechneAusrichtung();
    }

}
