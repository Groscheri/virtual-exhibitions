package Agents;
import Behaviors.BuildVirtualTour;
import Behaviors.RequestCurator;
import jade.core.Agent;

/**
 * ProfilerAgent class
 * In order to run this agent, you have to create a new agent in the Jade GUI
 * specifying the className of the agent `ProfilerAgent`
 * 1- Ask `VirtualTourAgent` for a personalized virtual tour
 * 2- Ask `CuratorAgent` for details about items in the virtual tour
 */
public class ProfilerAgent extends Agent {
    @Override
    protected void setup() {
        // init
        System.out.println("Hello, I'm a profiler agent ! "
                + "My name is " + this.getAID().getName() + "!");
        
        /*
        // some tests to remove
        Object[] args = this.getArguments(); // separated by a coma ","
        if (args != null) {
            System.out.println("Received params:");
            for (int i = 0; i < args.length; ++i) {
                System.out.println("- " + (String)args[i]);
            }
        }*/
        
        Object[] args = this.getArguments();
        String curatorName = "cucu";
        if (args != null && args.length > 0) {
            curatorName = (String)args[0]; // custom curator name
        }
        
        // some test to remove
        this.addBehaviour(new BuildVirtualTour(this.getLocalName()));
        
        this.requestCurator(curatorName);
        
    }
    
    @Override
    protected void takeDown() {
        // ending
        System.out.println("Ending " + this.getAID().getName() + "!");
    }
    
    protected void requestCurator(String name) {
        this.addBehaviour(new RequestCurator(this, name));
    }
}
