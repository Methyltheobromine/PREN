# Lichtschranke.py
# Misst die Drehzahl des DC motors
# 
# Author : David Huwyler
# Date   : 28.11.2014
# -----------------------
# -----------------------
# Import required Python libraries
# -----------------------
import time
import RPi.GPIO as GPIO

# -----------------------
# Define some functions
# -----------------------


# -----------------------
# Main Script
# -----------------------

GPIO.setmode(GPIO.BCM)

# Define GPIO to use on Pi
LS = 4   #Lichtschranken Eingang

# Set pins as output and input
GPIO.setup(LS,GPIO.IN)

try:

  while True:
      counter = 0
      start = time.time()
      while (time.time() < (start +1)):
        if (GPIO.input(LS) == False):
          while (GPIO.input(LS) == False):
            a = 0  #No Operaton
          counter = counter +1
          
      print "Frequenz :" + str(counter)
	
except KeyboardInterrupt:
  # User pressed CTRL-C
  # Reset GPIO settings
  GPIO.cleanup()
