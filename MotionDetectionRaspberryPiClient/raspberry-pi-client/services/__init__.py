import requests
from config import getConfig

config = getConfig()

def postImage(imagePath):
    url = config['server']['upload_url']
    headers = config['server']['headers']
    image = readImageAsByte(imagePath)
    files = {'image': (imagePath, image, headers)}

    try:
        r = requests.post(url, files=files, data=[('clientId', config['clientId'])])
        r.text
    except Exception as e:
        print e

def getConfigFromServer():
    url = config['server']['config_url']
    print "Fetching config from url:", url
    try:
        r = requests.get(url)
        return r.json()
    except Exception as e:
        print e

def readImageAsByte(target):
    with open(target) as im:
        image = im.read()
    return image
