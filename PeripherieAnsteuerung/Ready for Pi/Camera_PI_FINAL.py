# Camera.py
#
# Author : Severin Eggermann
# Date   : 08.03.2015
# -----------------------
# -----------------------
# Import required picamera libraries

import picamera

camera = picamera.PiCamera()
#camera.resolution(640,480)
camera.capture('/home/pi/git/PREN/camera.jpg')

