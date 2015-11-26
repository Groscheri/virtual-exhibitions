This README gives information about how to use virtual exhibitions agents which 
have been developed.

# Virtual exhibitions

Distributed Artifical Intelligence & Intelligent Agents - Homework 1
Group 5: Laurentiu CAPATINA & Quentin LEMAIRE

## Assumptions

In order to test the different use cases described below and in the report 
called **homework1.pdf** (PDF format), it is assumed that JADE framework is 
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

Create different agents in this order to test different scenarios:

1. Create a ProfilerAgent called "pro" without any argument
Wait at least 5 seconds, the ProfilerAgent will wake up and subscribe to a 
service. It will also display information on the "mobile applcation screen".

2. Create a CuratorAgent called "cucu" without any argument
CuratorAgent will start listening to Tour Guide & Profiler agents. It will also 
register a service to DF. Then, wait at least 5 seconds, the gallery will be 
updated by the agent. This action will then be repeated each 5 seconds.

3. Create a TourGuideAgent called "to" without any argument
Several actions will take place:

- TourGuideAgent will register one service
- ProfilerAgent will be notified by this registration
- TourGuideAgent will send a message (ACLMessage) to CuratorAgent "cucu"
- CuratorAgent, still listening, will answer to the message which is a request 
(ACLMessage)

4. Create a ProfilerAgent called "pro2" without any argument
Wait 5 seconds again. This time 2 services will be found by the research. 
Choose one of the 2 services typing "1" or "2" in the command line and then 
pressing "enter" key. Information about the chosen service will be displayed.

>>>>>>> refs/remotes/origin/master
