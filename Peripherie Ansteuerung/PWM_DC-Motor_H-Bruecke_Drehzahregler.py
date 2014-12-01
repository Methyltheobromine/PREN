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
duty = 30
p.start(duty)
kp = 10   #Verstärkung Proportional-Anteil
ki = 0.5  #Verstärkung Integral-Anteil
kd = 0.1  #Verstärkung Differential-Anteil

e_sum = 0
e_alt = 0

delta_t = 0.1 #Abtastzeit (1/f)

f_soll = input('Frequenz (min 5): ')
if (f_soll < 5):
  f_soll = 5

try:

  while True:
    #PID-Regler
    f_ist = getFrequenz()
    
    e = f_soll-f_ist                #Regelabweichung
    e_sum = e_sum + e               #integration des Fehlers
    e_dif = (e - e_alt)/ delta_t    #differentiation des Fehlers
    e_alt = e
    if (e_sum > 1000000):
      e_sum = 1000000
    if (e_sum < -1000000):
      e_sum = -1000000

    y_p = kp*e
    y_i = ki*e_sum
    y_d = kd*e_dif

    y= y_p + y_i + y_d
    
    duty = duty + y
    if (duty > 100):
      duty = 100
    if (duty < 0):
      duty = 0
      
    print "Frequenz :" + str(freq) + "Duty :" +str(duty)
    
    p.ChangeDutyCycle(duty)
    time.sleep(delta_t)

except KeyboardInterrupt:
  # User pressed CTRL-C
  # Reset GPIO settings
  p.stop() 
  GPIO.cleanup()

                        
