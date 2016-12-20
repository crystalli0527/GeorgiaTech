from mininet.topo import Topo
from mininet.net import Mininet
from mininet.link import TCLink
from mininet.util import custom
			      					
# Topology to be instantiated in Mininet
class ComplexTopo(Topo):
    "Mininet Complex Topology"

    def __init__(self, cpu=.1, max_queue_size=None, **params):

        # Initialize topo
        Topo.__init__(self, **params)

 # Start of rjs topo
 
	# Host configuration, from end of Step 6
        hostConfig = {'cpu': cpu}

	# specified network configs from specs
        linkConfig = {'bw': 15, 'delay': '1ms', 'loss': 0, 'max_queue_size': max_queue_size }
        linkConfigWifi = {'bw': 5, 'delay': '5ms', 'loss': 4, 'max_queue_size': max_queue_size }
        linkConfig3G = {'bw': 1, 'delay': '8ms', 'loss': 12, 'max_queue_size': max_queue_size }

        # 4 switches
        s1 = self.addSwitch('s1')
        s2 = self.addSwitch('s2')
        s3 = self.addSwitch('s3')
        s4 = self.addSwitch('s4')
        
	# 3 hosts
	h1 = self.addHost('h1', **hostConfig)
        h2 = self.addHost('h2', **hostConfig)
        h3 = self.addHost('h3', **hostConfig)

        # Wiring
        self.addLink(h1, s1, **linkConfig)
        self.addLink(s1, s2, **linkConfig)
        self.addLink(s2, s3, **linkConfig)
        self.addLink(s2, s4, **linkConfig)
       	self.addLink(s4, h3, **linkConfig3G)
        self.addLink(s3, h2, **linkConfigWifi)

