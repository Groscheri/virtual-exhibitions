package Agents;

import Behaviors.BuildVirtualTour;
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
        
        // TODO add behavior which ask information from CuratorAgent
        // TODO add behavior which wait for requests from ProfilerAgent
    }
    
    protected void buildVirtualTour(String name) {
        this.addBehaviour(new BuildVirtualTour(name));
    }
}
