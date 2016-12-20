# Project 3 for OMS6250
#
# This defines a DistanceVector (specialization of the Node class)
# that can run the Bellman-Ford algorithm. The TODOs are all related 
# to implementing BF. Students should modify this file as necessary,
# guided by the TODO comments and the assignment instructions. This 
# is the only file that needs to be modified to complete the project.
#
# Copyright 2015 Dave Lillethun & Sean Donovan
# Modified Fall 2016 by Jeffrey Randow to allow negative link weights
# Robert Skelton 
	    					
from Node import *
from helpers import *

# Robert notes
# max num iterations  = nodes - 1

# can use functions from Node
# weight[i] + d[i, i] = weight[i] + 0 = weight[i]

# set infinity to: inf_num = float('inf')
#  use Node.links and Node.neighbor_names to get the values
# or possibly self.links, like so:
# for neighbor in self.links:
#   print neighbor.name, "has", neighbor.weight
# Weights will be whole numbers only.

# In DistanceVector(Node):
# self.queue_msg() <--- used internally. It's wrapped by send_msg with some error checking.
# __str__(self) <--- optional depending on how you implement it
 
# In Node.py:
# __repr__(self) <--- basically the same as str
# verify_neighbors(self) <--- internal integrity checks to make sure your provided topology is right
 
# Basically the only one of those you should worry about (if at all) is __str__ under DistanceVector.

#use add_entry function to print

# send message from parent class has params

#As I understand, you want each instance to have it's own dictionary because each node will have different distances. 
#I created dictionaries for each DV instance and populate each with node information as messages come in.

#A node starts with the known distance vectors to its direct neighbors, and sends it's distance vectors to all neighbors. 
#Every time it gets a message from one of it's neighbors, it updates the distance to any new node in that message, and any nodes where it has found a shorter path. 
#If it updated it's DV due to new information, it re-sends it out to all neighbors.
dicto = {} # dict to store distances to each neighbor node


class DistanceVector(Node):
    #TODO: Declare any necessary data structure(s) to contain the Node's internal state / distance vector data
    
    IV = float('inf') # initial max value set to infinity

    # to add to or update dict
    #dict['key'] = "value"

    def __init__(self, name, topolink,neighbors):
        ''' Constructor. This is run once when the DistanceVector object is
        created at the beginning of the simulation. Initializing data structure(s)
        specific to a DV node is done here.'''

        super(DistanceVector, self).__init__(name, topolink, neighbors)

        messageMaster = []


    def __str__(self):
        ''' Returns a string representation of the Distance Vector node. '''

        #TODO: (optional) You may want to modify this to print your data structure(s)
        retstr = self.name + " : links ( "
        for neighbor in self.links:
            retstr = retstr + neighbor.name + neighbor.weight + " "
        return retstr + ")"


    def send_initial_messages(self):
        ''' This is run once at the beginning of the simulation, after all
        DistanceVector objects are created and their links to each other are
        established, but before any of the rest of the simulation begins. You
        can have nodes send out their initial DV advertisements here. 

        Remember that links points to a list of Neighbor data structure.  Access
        the elements with .name or .weight '''

        # use tuple (node sending message, distance to that node) as message
        for m in self.links:
            thisMessage = (m, self.topology)
            self.messages.append(thisMessage)


        #self.verify_neighbors(self)
        # message = []
        # for n in self.neighbor_names:
        #     #Node.verify_neighbors(self)
        #     #message.append(Node.send_msg(self, 0, self.get_neighbor_weight(n)))
        #     pass



        # TODO - Each node needs to build a message and send it to each of it's neighbors
        # HINT: Take a look at the skeleton methods provided for you in Node.py
        # get_neighbor_weight
        # verify_neighbors
        # send_msg
        # queue_msg 
        # also fields of
        #  self.name = name
        # self.links = neighbors
        # self.neighbor_names = []
        # self.topology = topolink
        # self.messages = []


    def process_BF(self):
        ''' This is run continuously (repeatedly) during the simulation. DV
        messages from other nodes are received here, processed, and any new DV
        messages that need to be sent to other nodes as a result are sent. '''

        # Implement the Bellman-Ford algorithm here.  It must accomplish two tasks below:
        # Process queued messages       
        for msg in self.messages:
            print "Dicto is %s" % dicto[msg] 
            print "Msg is %s" % msg         
            # if dicto[msg] > msg: #start BF
            #     dicto[msg] = msg   
            pass
        
        # Empty queue
        self.messages = []

        # Send neighbors updated distances
        for msg in self.messages:
            messages.add(msg)
        pass


    def log_distances(self):
        ''' This function is called immedately after process_BF each round.  It 
        prints distances to the console and the log file in the following format (no whitespace either end):
        
        A:A0,B1,C2
        
        Where:
        A is the node currently doing the logging (self),
        B and C are neighbors, with vector weights 1 and 2 respectively
        NOTE: A0 shows that the distance to self is 0 '''
        
        # TODO: Use the provided helper function add_entry() to accomplish this task (see helpers.py).
        # An example call that which prints the format example text above (hardcoded) is provided.        
        #add_entry("A", "A0,B1,C2")

        for neighbor in self.links:
            #print neighbor.name, "has", neighbor.weight
            add_entry(self.name, "0")
            add_entry(neighbor.name, neighbor.weight)
            pass


        #Singlelooptopo
        # A:A0,B2,C12,D4,E3
        # B:A2,B0,C10,D2,E1
        # C:A12,B10,C0,D12,E11
        # D:A4,B2,C12,D0,E1
        # E:A3,B1,C11,D1,E0



        finish_round()
        pass
