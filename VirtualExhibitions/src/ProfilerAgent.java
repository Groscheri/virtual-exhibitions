
import jade.core.Agent;

/**
 * ProfilerAgent class
 * In order to run this agent, you have to create a new agent in the Jade GUI
 * specifying the className of the agent `ProfilerAgent`
 */
public class ProfilerAgent extends Agent {
    @Override
    protected void setup() {
        System.out.println("Hello, I'm a profiler agent ! "
                + "My name is " + this.getAID().getName() + " !");
    }
}
