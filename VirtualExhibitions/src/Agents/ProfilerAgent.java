package Agents;
import Behaviors.BuildVirtualTour;
import Behaviors.RequestCurator;
import Behaviors.SearchAllServices;
import Behaviors.UpdateGallery;
import jade.core.Agent;
import jade.core.behaviours.Behaviour;
import jade.core.behaviours.SequentialBehaviour;
import jade.core.behaviours.WakerBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAException;
import java.util.Arrays;
import java.util.Date;

/**
 * ProfilerAgent class
 * In order to run this agent, you have to create a new agent in the Jade GUI
 * specifying the className of the agent `ProfilerAgent`
 * 1- Ask `VirtualTourAgent` for a personalized virtual tour
 * 2- Ask `CuratorAgent` for details about items in the virtual tour
 */
public class ProfilerAgent extends Agent {
    
    private String[] interests;
    
    @Override
    protected void setup() {
        // init
        this.addBehaviour(new WakerBehaviour(this, 5000) {
            @Override
            protected void onWake() {
                System.out.println("Applicaiton loaded after "+ new Date(this.getWakeupTime()));
                Object[] args = this.myAgent.getArguments();
                if (args != null && args.length > 0) {
                    interests = new String[args.length];
                    for(int i=0; i<args.length; i++){
                        interests[i] = (String)args[i];
                    }
                } else {
                    interests = new String[3];
                    interests[0] = "art";
                    interests[1] = "picasso";
                    interests[2] = "Van Gogh";
                }

                this.myAgent.addBehaviour(new SearchAllServices(this.myAgent));
                
                SequentialBehaviour seq = new SequentialBehaviour();
                seq.addSubBehaviour(new Behaviour(this.myAgent){
                    @Override
                    public void action() {
                        System.out.println("The profiler has the following interests: "+Arrays.toString(interests));
                    }

                    @Override
                    public boolean done() {
                        return true;
                    }
                    
                });
                seq.addSubBehaviour(new Behaviour(this.myAgent){
                    @Override
                    public void action() {
                        System.out.println("Display button to ask virtual tour.");
                    }

                    @Override
                    public boolean done() {
                        return true;
                    }
                
                });
                seq.addSubBehaviour(new Behaviour(this.myAgent){
                    @Override
                    public void action() {
                        System.out.println("Display Contact Button Information.");
                    }

                    @Override
                    public boolean done() {
                        return true;
                    }
                
                });
                
                this.myAgent.addBehaviour(seq);
            }
          
        });
    }
    
    @Override
    protected void takeDown() {
        // ending
        System.out.println("Ending " + this.getAID().getName() + "!");
    }
    
    public void requestCurator(String name) {
        this.addBehaviour(new RequestCurator(this, name));
    }
}
