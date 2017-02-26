# import the necessary packages
from pyimagesearch.tempimage import TempImage
from picamera.array import PiRGBArray
from picamera import PiCamera
from services import postImage
from config import getConfig
import argparse
import warnings
import datetime
import imutils
import json
import time
import cv2


warnings.filterwarnings("ignore")
conf = getConfig()
client = None

# Initialize the camera object
camera = PiCamera()
camera.resolution = (conf["resolution"]["width"], conf["resolution"]["height"])
camera.framerate = conf["fps"]
# Get a reference to the 3-dimensional matrix (each dimension represents a single basic color)
rawCapture = PiRGBArray(camera, (conf["resolution"]["width"], conf["resolution"]["height"]))

# allow the camera to warmup, because it needs some time to physically adjust focus
print "[INFO] Camera warm up..."
time.sleep(conf["camera_warmup_time"])

# Average frame, last uploaded time and motion counter initialization
avg = None
lastUploaded = datetime.datetime.now()
motionCounter = 0

# Start capturing frames with the camera
for f in camera.capture_continuous(rawCapture, format="bgr", use_video_port=True):

	# Get the current array that represents the image
	frame = f.array

	# Get the current timestamp and set the text that will be displayed
	timestamp = datetime.datetime.now()
	text = "No new motion detected"

	# Resize the frame, convert it to grayscale, and blur it to increase performances
	frame = imutils.resize(frame, width=500)
	gray = cv2.cvtColor(frame, cv2.COLOR_BGR2GRAY)
	gray = cv2.GaussianBlur(gray, (21, 21), 0)

	# Initialize the average frame
	if avg is None:
		print "[INFO] Initializing the average frame..."
		avg = gray.copy().astype("float")
		rawCapture.truncate(0)
		continue

	# Accumulate the weighted average between the current and previous frames
	cv2.accumulateWeighted(gray, avg, 0.5)
	# Then compute the difference between the current frame and the average
	frameDelta = cv2.absdiff(gray, cv2.convertScaleAbs(avg))

	# Threshold the motion sensitivity. The lesser the value, the higher the sensitivity
	# in the difference between pixel color
	thresh = cv2.threshold(frameDelta, conf["delta_thresh"], 255, cv2.THRESH_BINARY)[1]
	# Expand/Dilate the image, fill in all holes
	thresh = cv2.dilate(thresh, None, iterations=2)
	# Find the contours that are different
	(contours, _) = cv2.findContours(thresh.copy(), cv2.RETR_EXTERNAL, cv2.CHAIN_APPROX_SIMPLE)

	# Go through each of the contours in order to detect motion
	for c in contours:
		# if the contour is too small, ignore it
		if cv2.contourArea(c) < conf["min_area"]:
			continue

		# Calculate the bounding box for the contour
		(x, y, w, h) = cv2.boundingRect(c)
		# Draw the rectange on the frame
		cv2.rectangle(frame, (x, y), (x + w, y + h), (0, 255, 0), 2)
		# Update the text to signal motion
		text = "Motion detected"

	# Format the timestamp
	ts = timestamp.strftime("%A %d %B %Y %I:%M:%S%p")

	statusColor = (155, 205, 75)
	if text == "Motion detected":
		statusColor = (75, 75, 205)
	
	# Draw the text and timestamp on the frame
	cv2.putText(
                frame,
                "Status: {}".format(text),
                (15, 20),
		cv2.FONT_HERSHEY_SIMPLEX,
                0.7,
                statusColor,
                2)
	
	cv2.putText(
                frame,
                ts,
                (10, frame.shape[0] - 10),
                cv2.FONT_HERSHEY_SIMPLEX,
		0.35,
                (205, 155, 75),
                1)

	# Check to see if there was new motion detected
	if text == "Motion detected":
		# Check if enough time has elapsed since the last upload
		if (timestamp - lastUploaded).seconds >= conf["min_upload_seconds"]:

			# Increment the motion counter
			motionCounter += 1

			# Check to see if the number of frames with consistent motion is high enough
			if motionCounter >= conf["min_motion_frames"]:
				t = TempImage(conf["storage_base_path"])
				cv2.imwrite(t.path, frame)
				print "[INFO] Uploading image. Path: {path}".format(path=t.path)
				postImage(t.path)
				t.cleanup()
				
				# Upload the last uploaded timestamp
				lastUploaded = timestamp
				# Reset the motion counter for the next capture
				motionCounter = 0

	# Otherwise: No new motion has been detected
	else:
		motionCounter = 0
 
	if conf["show_video"]:
		# Display the security feed
		cv2.imshow("Camera Feed", frame)
		key = cv2.waitKey(1) & 0xFF

		# Break the loop when the user presses 'q'
		if key == ord("q"):
			break

	# The stream must be cleared before starting a new capture
	rawCapture.truncate(0)
