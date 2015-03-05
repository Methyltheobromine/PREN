# Strellt eine UART Verbindung her und
# Sendet die den Gewuenschten String
# Author : David Huwyler
# Date   : 28.11.2014
# -----------------------
# -----------------------
# Import required Python libraries
# -----------------------
import serial
import sys
  
# -----------------------
# Main Script
# -----------------------
UART = serial.Serial("/dev/ttyAMA0", 38400)

try:


    string = str(sys.argv[1])
    UART.open()
    UART.write(string)
    UART.close()
	
except KeyboardInterrupt:
  # User pressed CTRL-C
  # Reset GPIO settings
  GPIO.cleanup()
