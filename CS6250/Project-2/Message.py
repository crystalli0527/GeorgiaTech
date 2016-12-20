# Project 2 for OMS6250
#
# This defines a Message sent from one node to another using Spanning Tree Protocol.
# Students should not modify this file.
#
# Copyright 2016 Michael Brown
#           Based on prior work by Sean Donovan, 2015

class Message(object):

    def __init__(self, claimedRoot, distanceToRoot, originID, destinationID):
    # root = id of the switch thought to be the root by the origin switch
    # distance = the distance from the origin to the root node
    # origin =  the ID of the origin switch 
    # destination = the ID of the destination switch 
        self.root = claimedRoot
        self.distance = distanceToRoot
        self.origin = originID
        self.destination = destinationID

    
    
