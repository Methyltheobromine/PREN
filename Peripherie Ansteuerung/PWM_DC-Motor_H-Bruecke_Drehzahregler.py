# PWM_DC-Motor_H-Bruecke_Drehzahregler
#
# Author : David Huwyler
# Date   : 30.11.2014
# -----------------------
# -----------------------
# Import required Python libraries
import time
import sys
import RPi.GPIO as GPIO # always needed with RPi.GPIO  

# -----------------------
# Funktion getFrequenz
# ----------------------- 
def getFrequenz():
  if (GPIO.input(LS) == False):
    while (GPIO.input(LS) == False):
      a = 0  #No Operaton
    start = time.time()
    while (GPIO.input(LS) == True):
      a = 0  #No Operaton
    while (GPIO.input(LS) == False):
      a = 0  #No Operaton
    stop = time.time()
      
  if (GPIO.input(LS) == True):
    while (GPIO.input(LS) == True):
      a = 0  #No Operaton
    start = time.time()
    while (GPIO.input(LS) == False):
      a = 0  #No Operaton
    while (GPIO.input(LS) == True):
      a = 0  #No Operaton
    stop = time.time()
  elapsed = stop-start
  freq = 1/elapsed
  return freq


# -----------------------
# Main Script
# -----------------------
GPIO.setmode(GPIO.BCM)  # choose BCM or BOARD numbering schemes
  
GPIO.setup(18, GPIO.OUT)# set GPIO 25 as an output
  
freq = 2000  
p = GPIO.PWM(18, freq)    # create an object p for PWM
duty = 40
p.start(duty)
kp = 4
ki = 0.01
f_soll = input('Frequenz: ')

try:

  while True:
    #PI-Regler
    f_ist = getFrequenz()
    e = f_soll-f_ist   #Regelabweichung
    e_sum = e_sum + e  #integration des Fehlers

    y_p = kp*e
    y_i = ky*e_sum

    y= y_p + y_i
    
    duty = duty + y
    
    
    p.ChangeDutyCycle(duty)
    time.sleep(0.1)

except KeyboardInterrupt:
  # User pressed CTRL-C
  # Reset GPIO settings
  p.stop() 
  GPIO.cleanup()

                        
