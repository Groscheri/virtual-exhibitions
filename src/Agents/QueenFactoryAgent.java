
package Agents;

import jade.core.AID;
import jade.core.Agent;
import jade.wrapper.AgentController;
import jade.wrapper.ContainerController;
import jade.wrapper.StaleProxyException;
/**
 * QuenFactoryAgent class
 */
public class QueenFactoryAgent extends Agent {
    
    protected final static Integer DEFAUT_VALUE = 8;
    protected Integer n;
    protected AID[] queenAgents;
    protected AgentController[] queenControleurs;

    @Override
    protected void setup() {
        System.out.println("[FACTORY] Begin.");
        
        // retrieve number of queens to create in order to solve problem
        Object[] args = this.getArguments();
        if (args != null && args.length > 0) {
            try {
                this.n = Integer.parseInt((String)args[0]);
            } catch (Exception e) {
                // wrong parameters (not an Integer)
                this.n = QueenFactoryAgent.DEFAUT_VALUE;
                System.err.println("[FACTORY] Erreur with inserted parameter "
                        + "'n': " + (String)args[0] +  ". It must be an "
                        + "integer. Default value chosen: "
                        + QueenFactoryAgent.DEFAUT_VALUE + ".");
            }
        }
        else {
            this.n = QueenFactoryAgent.DEFAUT_VALUE;
            System.err.println("[FACTORY] No parameter found. Default value chosen: "
                        + QueenFactoryAgent.DEFAUT_VALUE + ".");
        }
        
        // check parameter
        if (this.n <= 3) {
            System.out.println("[FACTORY] Impossible to deal with n = " 
                    + this.n + ".");
            this.doDelete(); // kill itself
        }
        
        System.out.println("[FACTORY] Init problem with n = " + this.n + ".");
        
        // retrieve agent factory to create queen agents
        ContainerController agentFactory = getContainerController();
        
        // init AIDs table
        this.queenAgents = new AID[this.n];
        this.queenControleurs = new AgentController[this.n];
        
        // create queens
        for (int i = 0; i < this.n; ++i) {
            // init queen param
            String name = QueenFactoryAgent.getName(i); // name
            Object[] arguments = {i, this.n};
            try {
                // create agent
                this.queenControleurs[i] = agentFactory.createNewAgent(name, QueenAgent.class.getName(),arguments);
            } catch (StaleProxyException ex) {
                System.err.println("[FACTORY] Impossible to create " + name);
                this.kill(i); // end agents
            }
            this.queenAgents[i] = new AID(name, AID.ISLOCALNAME); // save AID
            try {
                this.queenControleurs[i].start(); // launch queen
            } catch (StaleProxyException ex) {
                System.err.println("[FACTORY] Impossible to start " + name);
                this.kill(i); // end agents
            }
            System.out.println("[FACTORY] " + name + " started.");
        }
    }
    
    private void kill(int index_queens_created) {
        for (int i = 0; i < index_queens_created; ++i) {
            this.killQueen(i); // kill already created queens
        }
        this.doDelete(); // kill itself
    }
    
    private boolean killQueen(int index) {
        try {
            this.queenControleurs[index].kill();
            return true;
        } catch (Exception e) {
            System.err.println("[FACTORY] Impossible to kill " + this.queenAgents[index].getLocalName());
            return false;
        }
    }

    @Override
    protected void takeDown() {
        System.out.println("[FACTORY] End.");
    }
    
    public static String getName(int id) {
        return "queen#" + id;
    }
    
    
    
}
