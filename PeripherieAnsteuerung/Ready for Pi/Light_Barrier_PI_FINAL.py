# This Script is responsable for the Initialization
#
# Author : Team 37
# -----------------------
# -----------------------
# Import required Python libraries

import time
import sys
import RPi.GPIO as GPIO 
  
GPIO.setmode(GPIO.BCM)  	
 
DIR = 19
STEP = 26
  
GPIO.setup(DIR, GPIO.OUT)
GPIO.setup(STEP, GPIO.OUT) 
 
GPIO.setup(5, GPIO.IN, pull_up_down=GPIO.PUD_UP)#evt Up durch Down oder Up ersetzen

GPIO.output(DIR, False) #evt durch False ersetzen gibt nur die richtung an

while GPIO.input(5):
  GPIO.output(STEP, True)
  time.sleep(0.005)
  GPIO.output(STEP, False)
  time.sleep(0.005)
print 'Ready'
GPIO.cleanup()


                        
