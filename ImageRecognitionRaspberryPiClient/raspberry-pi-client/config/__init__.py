import os
import yaml

__config = None 

def getConfig():
    global __config
    if __config is None:
        __loadConfiguration()
    return __config

def __loadConfiguration():
    global __config
    (this_dir, this_filename) = os.path.split(__file__)
    configPath = os.path.join(this_dir, "config.yml")
    
    with open(configPath, 'r') as config:
        __config = yaml.load(config)
