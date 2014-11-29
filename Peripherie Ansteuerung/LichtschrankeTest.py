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
    if (GPIO.input(LS) == True):
      print ("True")
    if (GPIO.input(LS) == False):
      print ("False")
    time.sleep(1)
	
except KeyboardInterrupt:
  # User pressed CTRL-C
  # Reset GPIO settings
  GPIO.cleanup()
