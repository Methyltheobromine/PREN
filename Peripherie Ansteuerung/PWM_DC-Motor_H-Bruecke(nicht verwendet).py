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
  
GPIO.setup(18, GPIO.OUT)# set GPIO 25 as an output
  
freq = 2000  
p = GPIO.PWM(18, freq)    # create an object p for PWM
duty = input('Duty: ')
p.start(duty)


try:

  while True:
	duty = input('Duty: ')
	p.ChangeDutyCycle(duty)   # change the duty cycle to 90%  
	time.sleep(1)

except KeyboardInterrupt:
  # User pressed CTRL-C
  # Reset GPIO settings
  p.stop() 
  GPIO.cleanup()

                        
