# Measure distance using an ultrasonic module
# and gives back the value of each sensor and
# the difference
#
# Author : Team 37
# -----------------------
# -----------------------
# Import required Python libraries

import time
import RPi.GPIO as GPIO
import os

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

#print "Ultrasonic Measurement"

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

i = 0
n = 10
averageOverTenTimes = 0

while i < n:
	distanceR = measure_averageR()
	distanceL = measure_averageL()
	#s = str(distanceL) + ';' + str(distanceR) + ';' + str(distanceL - distanceR)
	#print s
	averageOverTenTimes = averageOverTenTimes + (distanceL - distanceR)
	#print averageOverTenTimes
	i = i + 1
	time.sleep(0.05)

averageOverTenTimes = averageOverTenTimes / 10
print "1;1;" + str(averageOverTenTimes)
GPIO.cleanup()
