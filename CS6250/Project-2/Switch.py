# Project 2 for OMS6250
#
# This defines a Switch that can can send and receive spanning tree 
# messages to converge on a final loop free forwarding topology.
#
# Copyright 2016 Michael Brown
#           Based on prior work by Sean Donovan, 2015
# Edited and implemented by Robert Skelton

from Message import *

def get_root(listIn):
	final_root = min(int(s) for s in listIn)
	#print "The final root is: %d" % final_root

class Switch(object):
	global linkData
	linkData = []


	def __init__(self, idNum, topolink, neighbors):
    # switchID = id of the switch (lowest value determines root switcha nd breaks ties.)
    # topology = backlink to the Topology class. Used for sending messages.
    #   as follows: self.topology.send_message(message)
    # links = a list of the switch IDs linked to this switch.
		self.switchID = idNum
		self.topology = topolink
		self.links = neighbors
		

		messageList = [idNum, topolink, neighbors]


	def verify_neighbors(self):
		''' Verify that all your neighbors have a backlink to you. '''
		for neighbor in self.links:
			if self.switchID not in self.topology.switches[neighbor].links:
				raise Exception(neighbor + " does not have link to " + self.switchID)
	



	def process_message(self, message):
		# This function needs to accept an incoming message and process it accordingly.
	    # This function is called every time the switch receives a new message.
		#print "\nRoot is %d" % message.root
		#print "Distance to root is %d" % message.distance
		#print "Origin is %d" % message.origin
		#print "Destination is %d" % message.destination
		
		#update_root(message.root)

		if message.root < message.origin:
			root = message.root
			linkData.append(root)
		else:
			root = message.origin
			linkData.append(root)

		get_root(linkData)







	def send_initial_messages(self):
		# 1. for loop over self.links
		# 2. create datagram
		# 3. send_message call
		for neighbor in self.links:
			datagram = Message(self.switchID, 0, self.switchID, neighbor)
			#print datagram
			#print  "\n%d sent a datagram to  %d" % (self.switchID, neighbor)
			self.topology.send_message(datagram) 


		


	