package Agents;
import Behaviors.RequestCurator;
import Behaviors.SearchAllServices;
import jade.core.Agent;
import jade.core.behaviours.Behaviour;
import jade.core.behaviours.OneShotBehaviour;
import jade.core.behaviours.SequentialBehaviour;
import jade.core.behaviours.WakerBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.SearchConstraints;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAException;
import jade.lang.acl.ACLMessage;
import jade.proto.SubscriptionInitiator;
import java.util.Arrays;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * ProfilerAgent class
 * In order to run this agent, you have to create a new agent in the Jade GUI
 * specifying the className of the agent `ProfilerAgent`
 * 1- Ask `VirtualTourAgent` for a personalized virtual tour
 * 2- Ask `CuratorAgent` for details about items in the virtual tour
 */
public class ProfilerAgent extends Agent {
    
    // default interests
    private String[] interests = {"art", "Picasso", "Van Gogh"};
    
    @Override
    protected void setup() {
        // init
        
        // Load application (this task is loaded after 5000ms)
        this.addBehaviour(new WakerBehaviour(this, 5000) {
            @Override
            protected void onWake() {
                System.out.println("Application loaded after "+ new Date(this.getWakeupTime()));
                
                // check args for interests of the user
                Object[] args = this.myAgent.getArguments();
                if (args != null && args.length > 0) {
                    interests = new String[args.length];
                    for(int i=0; i<args.length; i++){
                        interests[i] = (String)args[i];
                    }
                }

                // look for services
                this.myAgent.addBehaviour(new SearchAllServices(this.getAgent()));
                
                /* 
                Sequential behavior:
                1- display interests of the user
                2- display button to ask for a virtual tour
                3- display button to ask for information about contact
                */
                SequentialBehaviour seq = new SequentialBehaviour();
                seq.addSubBehaviour(new OneShotBehaviour(this.myAgent){
                    @Override
                    public void action() {
                        System.out.println("The profiler has the following interests: "+Arrays.toString(interests));
                    }
                });
                seq.addSubBehaviour(new OneShotBehaviour(this.myAgent){
                    @Override
                    public void action() {
                        System.out.println("Display button to ask virtual tour.");
                    }
                });
                seq.addSubBehaviour(new OneShotBehaviour(this.myAgent){
                    @Override
                    public void action() {
                        System.out.println("Display \"Contact\" button information.");
                    }
                });
                
                this.myAgent.addBehaviour(seq);
                
                /*
                Add Subscription behavior:
                1- create subscription message
                2- handle inform message
                3- decode information
                */
                DFAgentDescription template = new DFAgentDescription();
                ServiceDescription sd = new ServiceDescription();
                sd.setType("build-tour");
                template.addServices(sd);
                SearchConstraints sc = new SearchConstraints();
                sc.setMaxResults(new Long(1));
                
                this.myAgent.addBehaviour(new SubscriptionInitiator(this.myAgent, 
                        DFService.createSubscriptionMessage(myAgent, getDefaultDF(), template, sc)){
                    @Override
                    protected void handleInform(ACLMessage inform) {
                        try {
                            DFAgentDescription[] dfds =
                                    DFService.decodeNotification(inform.getContent());
                            if(dfds.length > 0){
                                System.out.println("Notification for profiler after subscription: "+dfds[0].getName());
                            }
                        } catch (FIPAException ex) {
                            ex.printStackTrace();
                        }
                        
                    }
                });
                System.out.println("Subscription done !");
            }
          
        });
    }
    
    @Override
    protected void takeDown() {
        // ending
        System.out.println("Ending " + this.getAID().getName() + "!");
    }
    
    /**
     * Request Curator
     * @param name name of the curator
     */
    public void requestCurator(String name) {
        this.addBehaviour(new RequestCurator(this, name));
    }
}
