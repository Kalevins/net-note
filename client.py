from imutils.video import VideoStream
import imagezmq
import argparse
import socket
import time

ap = argparse.ArgumentParser()
ap.add_argument("-s", "--server-ip", required=True,
	help="ip address of the server to which the client will connect")
args = vars(ap.parse_args())

rpiName = socket.gethostname()
vs = VideoStream(src=0).start()
time.sleep(2.0)
 
while True: