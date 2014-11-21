# PWM.py
#
# Author : David Huwyler
# Date   : 25.10.2014
# -----------------------
# -----------------------
# Import required Python libraries
import time
import sys
import RPi.GPIO as GPIO # always needed with RPi.GPIO  
  
GPIO.setmode(GPIO.BCM)  # choose BCM or BOARD numbering schemes

DIR = 16
STEP = 20
  
GPIO.setup(DIR, GPIO.OUT)
GPIO.setup(STEP, GPIO.OUT)

try:

  while True:
        stepps = input('Stepps: ')
        direction = input('Direction: (1 oder 0)')
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

                        
