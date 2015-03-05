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

DIR = 16
STEP = 20
  
GPIO.setup(DIR, GPIO.OUT)
GPIO.setup(STEP, GPIO.OUT)

try:

        stepps = str(sys.argv[1])
        direction = str(sys.argv[2]) #input('Direction: (1 oder 0)')
		
        if direction:
          GPIO.output(DIR, True)
        else:
          GPIO.output(DIR, False)
          
        for i in range(0,stepps):
          GPIO.output(STEP, True)
          wait = time.time()
          while(time.time() <= (wait+0.00009)):
            wait = wait
          GPIO.output(STEP, False)
          wait = time.time()
          while(time.time() <= (wait+0.00009)):
            wait = wait


except KeyboardInterrupt:
  # User pressed CTRL-C
  # Reset GPIO settings
  GPIO.cleanup()

                        