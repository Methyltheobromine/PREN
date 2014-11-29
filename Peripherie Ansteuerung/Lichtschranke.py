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

    start = time.time()
  
    while GPIO.input(GPIO_ECHO_R)==0:
      start = time.time()

    while GPIO.input(GPIO_ECHO_R)==1:
      stop = time.time()

    elapsed = stop-start



except KeyboardInterrupt:
  # User pressed CTRL-C
  # Reset GPIO settings
  GPIO.cleanup()
