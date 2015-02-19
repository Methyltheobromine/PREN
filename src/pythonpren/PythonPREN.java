/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package pythonpren;

import T37_PREN.Camera.BildAuswertungKorb;
import T37_PREN.Camera.BildVonWebcamAufnehmen;

/**
 *
 * @author Severin
 */
public class PythonPREN {

    private static String speicherort = "";
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        //Foto aufnehmen
        BildVonWebcamAufnehmen myPictureFromWebcam = new BildVonWebcamAufnehmen();
        speicherort = myPictureFromWebcam.takeAPicture();
        
        //Foto auswerten
        BildAuswertungKorb myBildauswertung = new BildAuswertungKorb();
        myBildauswertung.bildAuswerten(speicherort);
        
        //Berechnen der Drehung des Turmes
        //erfolgt in der BildAuswertungsKlasse
    }
    
}
