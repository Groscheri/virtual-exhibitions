package Agents;

import Behaviors.BuildVirtualTour;
import Behaviors.ListenTour;
import Behaviors.RequestInfo;
import jade.core.Agent;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAException;

/**
 * TourGuideAgent class
 * - get information from `CuratorAgent`
 * - build VirtualTour upon requests from `ProfilerAgent`
 */
public class TourGuideAgent extends Agent {
    @Override
    protected void setup() {
        // init
        
        // retrieve curator's name before requesting him (from arguments)
        Object[] args = this.getArguments();
        String curatorName = "cucu";
        if (args != null && args.length > 0) {
            curatorName = (String)args[0]; // custom curator name
        }
        
        // create & register service to build a virtual tour
        DFAgentDescription dfd = new DFAgentDescription();
        dfd.setName(this.getAID());
        ServiceDescription sd = new ServiceDescription();
        sd.setType("build-tour");
        sd.setName("Building-virtual-tour");
        dfd.addServices(sd);
        try {
            //System.out.println("[TO] Service registered");
            DFService.register(this, dfd);
        } catch (FIPAException fe){
            fe.printStackTrace();
        }
        
        // listen for build-tour requests
        this.addBehaviour(new ListenTour(this));
        System.out.println("[TO] Listening to Profiler to build virtual tour.");
        
    }
    
    /**
     * Build virtual tour
     * @param name name of the virtual tour
     * @param interests interests of the user in order to build an adapted 
     * virtual tour
     */
    protected void buildVirtualTour(String name, String[] interests) {
        //TODO add interests
        this.addBehaviour(new BuildVirtualTour(name, interests));
    }
    
    
    protected void takeDown(){
        // ending
        try {
            //System.out.println("[TO] Service deregistered");
            DFService.deregister(this); // unregister
        }
        catch(FIPAException fe){
            fe.printStackTrace();
        }
    }
}
