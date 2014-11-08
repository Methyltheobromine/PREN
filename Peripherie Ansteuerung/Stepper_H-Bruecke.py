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

IN_1 = 19
IN_2 = 16
IN_3 = 26
IN_4 = 20
  
  
GPIO.setup(IN_1, GPIO.OUT)
GPIO.setup(IN_2, GPIO.OUT)
GPIO.setup(IN_3, GPIO.OUT)
GPIO.setup(IN_4, GPIO.OUT)
  

try:

  while True:
	stepps = input('Stepps: ')
	for i in range(0,stepps):
	  GPIO.output(IN_1, False)
	  GPIO.output(IN_2, True)
	  GPIO.output(IN_3, True)
	  GPIO.output(IN_4, False)
	  time.sleep(0.01)
	  GPIO.output(IN_1, False)
	  GPIO.output(IN_2, True)
	  GPIO.output(IN_3, False)
	  GPIO.output(IN_4, True)
	  time.sleep(0.01)
	  GPIO.output(IN_1, True)
	  GPIO.output(IN_2, False)
	  GPIO.output(IN_3, False)
	  GPIO.output(IN_4, True)
	  time.sleep(0.01)
	  GPIO.output(IN_1, True)
	  GPIO.output(IN_2, False)
	  GPIO.output(IN_3, True)
	  GPIO.output(IN_4, False)
	  time.sleep(0.01)

except KeyboardInterrupt:
  # User pressed CTRL-C
  # Reset GPIO settings
  GPIO.cleanup()

                        
