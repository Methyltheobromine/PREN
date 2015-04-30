# Create an UART-Connection and sends the value between 000 and 170
# to speed up the fly wheel
#
# Author : Team 37
# -----------------------
# -----------------------
# Import required Python libraries

import serial
import sys
import RPi.GPIO as GPIO # always needed with RPi.GPIO  
  
GPIO.cleanup()

