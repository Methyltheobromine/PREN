# ultrasonic.py
# Measure distance using an ultrasonic module
# in a loop.
#
# Author : David Huwyler
# Date   : 22.10.2014
# -----------------------
# -----------------------
# Import required Python libraries
# -----------------------
import time
import RPi.GPIO as GPIO

# -----------------------
# Define some functions
# -----------------------

def measureR():
  # This function measures a distance

  GPIO.output(GPIO_TRIGGER_R, True)
  time.sleep(0.00001)
  GPIO.output(GPIO_TRIGGER_R, False)
  start = time.time()
  
  while GPIO.input(GPIO_ECHO_R)==0:
    start = time.time()

  while GPIO.input(GPIO_ECHO_R)==1:
    stop = time.time()

  elapsed = stop-start
  distance = (elapsed * 34300)/2

  return distance

def measureL():
  # This function measures a distance

  GPIO.output(GPIO_TRIGGER_L, True)
  time.sleep(0.00001)
  GPIO.output(GPIO_TRIGGER_L, False)
  start = time.time()
  while GPIO.input(GPIO_ECHO_L)==0:
    start = time.time()

  while GPIO.input(GPIO_ECHO_L)==1:
    stop = time.time()

  elapsed = stop-start
  distance = (elapsed * 34300)/2
  return distance
  
def measure_averageR():
  # This function takes 3 measurements and
  # returns the average.

  distance1=measureR()
  time.sleep(0.05)
  distance2=measureR()
  time.sleep(0.05)
  distance3=measureR()
  distance = distance1 + distance2 + distance3
  distance = distance / 3
  return distance

def measure_averageL():
  # This function takes 3 measurements and
  # returns the average.
  
  time.sleep(0.05)
  distance1=measureL()
  time.sleep(0.05)
  distance2=measureL()
  time.sleep(0.05)
  distance3=measureL()
  distance = distance1 + distance2 + distance3
  distance = distance / 3
  return distance
  
# -----------------------
# Main Script
# -----------------------

# Use BCM GPIO references
# instead of physical pin numbers
GPIO.setmode(GPIO.BCM)

# Define GPIO to use on Pi
GPIO_TRIGGER_R = 23
GPIO_ECHO_R    = 24
GPIO_TRIGGER_L = 17
GPIO_ECHO_L    = 27

print "Ultrasonic Measurement"

# Set pins as output and input
GPIO.setup(GPIO_TRIGGER_R,GPIO.OUT)  # Trigger R
GPIO.setup(GPIO_ECHO_R,GPIO.IN)      # Echo R
GPIO.setup(GPIO_TRIGGER_L,GPIO.OUT)  # Trigger L
GPIO.setup(GPIO_ECHO_L,GPIO.IN)      # Echo L

# Set trigger to False (Low)
GPIO.output(GPIO_TRIGGER_R, False)
GPIO.output(GPIO_TRIGGER_L, False)

# Wrap main content in a try block so we can
# catch the user pressing CTRL-C and run the
# GPIO cleanup function. This will also prevent
# the user seeing lots of unnecessary error
# messages.
try:

  while True:

    distanceR = measure_averageR()
    distanceL = measure_averageL()
    s = 'L: ' +str(distanceL)+ '   R: ' +str(distanceR)+ '   Differenz: ' +str(abs(distanceL - distanceR))+'\n'
    print s
    time.sleep(0.1)

except KeyboardInterrupt:
  # User pressed CTRL-C
  # Reset GPIO settings
  GPIO.cleanup()