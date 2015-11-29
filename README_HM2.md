This README gives information about how to use virtual exhibitions agents which 
have been developed.

# Virtual exhibitions

Distributed Artifical Intelligence & Intelligent Agents - Homework 2
Group 5: Laurentiu CAPATINA & Quentin LEMAIRE

## Assumptions

In order to test the different use cases described below and in the report 
called **homework2.pdf** (PDF format), it is assumed that JADE framework is 
already installed and configured on the machine.

Notice that you don't need to use any of the common Java IDE in order to test 
these different use cases. It is possible to launch JADE with the command line 
using: `java jade.Boot -gui agent_name:package.agentClass`. JAR executables of 
Jade and commons codec must be added in CLASSPATH in order to execute Jade.

## Use cases

### Initialization

1. Change directory in "src/" folder `cd src`
2. Launch JADE `java jade.Boot -gui` (CLASSPATH must have been set)

In order to create a new agent with the UI, double click on "AgentPlatforms" on 
the top left of the window. Then double click again on the folder which 
appeared. "Main-Container" should appear. Right clic on it and then click on 
"Start New Agent". This will open a small window box asking for information 
about the agent.

### Tests

#### Task 1

Create different agents in this order to test communication scenario:
1. Create a CuratorAgent called "cucu" without any arguments
CuratorAgent will start listening to TourGuide & Profiler agents (info requests)

2. Create a TourGuideAgent called "toto" without any arguments
TourGuideAgent will start listening to Profiler agent (build tour requests)

3. Create a ProfilerAgent called "pro" without any arguments
Wait at least 2 seconds, the ProfilerAgent will wake up and send a "build tour" 
request to TourGuideAgent. Then, the TourGuide agent will make an info request 
to CuratorAgent in order to get information about artifacts in the virtual 
museum. When it receives information about artifacts, it builds the virtual tour 
and answers the ProfilerAgent.
Finally, the ProfilerAgent send an info request to the Curator agent to get 
information about items it received (from TourGuide agent) in the virtual tour.

####Task 2

Create a curator "cuuu" with the arguments "1,5" and an artist manager with arguments "1000,100,500"
The winner will be "cuuu" because his competitor has the default strategy "3,400".
Create the following curators and after each creation an new auction for the same object will be launched.
Curator "cucu1" with the arguments "2,60"
Curator "cucu2" with the arguments "3,700"
Curator "cucu3" with the arguments "4,1000,80"
The winners will always be the last created curator because it has the highest price to offer
according to their strategy.


