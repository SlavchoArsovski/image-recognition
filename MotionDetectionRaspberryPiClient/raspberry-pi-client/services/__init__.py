import requests
from config import getConfig

config = getConfig()

def postImage(imagePath):
    url = config['server']['url']
    headers = config['server']['headers']
    image = readImageAsByte(imagePath)
   
    files = {'image': (imagePath, image, headers)}
 
    try:
        r = requests.post(url, files=files)
        r.text
    except Exception as e: 
        print e

def readImageAsByte(target):
    with open(target) as im:
        image = im.read()
    return image