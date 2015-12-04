This README gives information about how to use virtual exhibitions agents which 
have been developed.

# Virtual exhibitions

Distributed Artifical Intelligence & Intelligent Agents - Homework 3
Group 5: Laurentiu CAPATINA & Quentin LEMAIRE

## Assumptions

In order to test the different use cases described below and in the report 
called **homework3.pdf** (PDF format), it is assumed that JADE framework is 
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

#### Task 1: N queens

Use case:
Create a QueenFactoryAgent called "main" with 1 argument, an integer N which 
represents the number of queens. Default value (if parameter not provided) is 8.

All queens will be created automatically and the problem will be solved as fast 
as possible using backtracking. First solution found is displayed on the screen 
and solver is stopped.

#### Task 2: Multiple containers & cloning

TODO
