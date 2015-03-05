# PWM.py
#
# Author : David Huwyler
# Date   : 21.11.2014
# -----------------------
# -----------------------
# Import required Python libraries

import time
import sys
import RPi.GPIO as GPIO # always needed with RPi.GPIO  
  
GPIO.setmode(GPIO.BCM)  # GPIO Nummer Verwenden!

DIR = 19
STEP = 26
  
GPIO.setup(DIR, GPIO.OUT)
GPIO.setup(STEP, GPIO.OUT)

try:

        stepps = str(sys.argv[1]) * 100
        direction = str(sys.argv[2]) #input('Direction: (1 oder 0)')
		
        if direction:
          GPIO.output(DIR, True)
          print('Direction is True')
        else:
          GPIO.output(DIR, False)
          print('Direction is False')
          
        for i in range(0,stepps):
          print(i)
          GPIO.output(STEP, True)
          time.sleep(0.01)
          GPIO.output(STEP, False)
          time.sleep(0.01)


except KeyboardInterrupt:
  # User pressed CTRL-C
  # Reset GPIO settings
  GPIO.cleanup()

                        
