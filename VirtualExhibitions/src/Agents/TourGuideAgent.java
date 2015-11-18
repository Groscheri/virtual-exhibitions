package Agents;

import Behaviors.BuildVirtualTour;
import Behaviors.RequestCurator;
import jade.core.Agent;

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
        
        // TODO add behavior which ask information from CuratorAgent
        this.requestCurator(curatorName);
        // TODO add behavior which wait for requests from ProfilerAgent
    }
    
    protected void buildVirtualTour(String name) {
        this.addBehaviour(new BuildVirtualTour(name));
    }
    
    protected void requestCurator(String name) {
        this.addBehaviour(new RequestCurator(this, name));
    }
}
