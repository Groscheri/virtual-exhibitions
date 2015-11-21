package Agents;

import Behaviors.BuildVirtualTour;
import Behaviors.RequestCurator;
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
        
        Object[] args = this.getArguments();
        
        String curatorName = "cucu";
        if (args != null && args.length > 0) {
            curatorName = (String)args[0]; // custom curator name
        }
        requestCurator(this.getLocalName());
        DFAgentDescription dfd = new DFAgentDescription();
        dfd.setName(getAID());
        ServiceDescription sd = new ServiceDescription();
        sd.setType("build-tour");
        sd.setName("Building-virtual-tour");
        dfd.addServices(sd);
        try{
            DFService.register(this, dfd);
        } catch (FIPAException fe){
            fe.printStackTrace();
        }
        // TODO add behavior which wait for requests from ProfilerAgent
    }
    
    protected void buildVirtualTour(String name, String[] interests) {
        //TODO add interests
        this.addBehaviour(new BuildVirtualTour(name, interests));
    }
    
    protected void requestCurator(String name) {
        this.addBehaviour(new RequestCurator(this, name));
    }
    
    protected void takeDown(){
        try{
            DFService.deregister(this);
        }
        catch(FIPAException fe){
            fe.printStackTrace();
        }
    }
}
