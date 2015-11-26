package Agents;

import Behaviors.ListenArtistManager;
import Behaviors.ListenProfiler;
import Behaviors.ListenTourGuide;
import Behaviors.UpdateGallery;
import Model.Item;
import jade.core.Agent;
import jade.core.behaviours.ParallelBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAException;
import java.util.ArrayList;

/**
 * CuratorAgent class
 * Monitor art gallery (database) : check update
 * - answer `ProfilerAgent` with details about items stored in the gallery
 * - answer `TourGuideAgent` with details about items in order to build virtualTour
 */
public class CuratorAgent extends Agent {
    protected ArrayList<Item> galleryItems;
    private int valToSpend;
    
    
    @Override
    protected void setup() {
        // get value to spend in auction
        Object[] args = this.getArguments();
        if (args != null && args.length > 0) {
            try{
                valToSpend = Integer.parseInt((String) args[0]);
            } catch(Exception e){
                e.printStackTrace();
            }
        } else {
            valToSpend = 900;
        }
        
        // create parallel behavior to listen to requests
        ParallelBehaviour parallel = new ParallelBehaviour();
        parallel.addSubBehaviour(new ListenProfiler(this));
        parallel.addSubBehaviour(new ListenTourGuide(this));
       // parallel.addSubBehaviour(new ListenArtistManager(this));
        
        // create an update behavior to take gallery up to date
        this.addBehaviour(new UpdateGallery(this, 5000));
        
        // create & register service for complementary information
        DFAgentDescription dfd = new DFAgentDescription();
        dfd.setName(this.getAID());
        ServiceDescription sd = new ServiceDescription();
        sd.setType("object-info");
        sd.setName("Obj-complementary-info");
        dfd.addServices(sd);
        try{
            System.out.println("[CU] Service registered");
            DFService.register(this, dfd);
        } catch (FIPAException fe){
            fe.printStackTrace();
        }
        
        //this.addBehaviour(parallel);
        //TODO - get max value in param
        this.addBehaviour(new ListenArtistManager(this, valToSpend));
        System.out.println("Listening to TourGuide & Profiler");
    }
    
    /**
     * Gallery represented as a list of `Item` objects
     * Modeled as a singleton
     * @return artifacts list
     */
    private ArrayList<Item> getItems() {
        if (this.galleryItems == null) {
            ArrayList<Item> list = new ArrayList<>();
            list.add(new Item("La Joconde", "Da Vinci", "Painting"));
            list.add(new Item("Imagine", "John Lenon", "Music"));
            list.add(new Item("Guernica", "Picasso", "Painting"));
            list.add(new Item("Wheatfield with Crows", "Van Gogh", "Painting"));
            list.add(new Item("A beautiful mind", "Unknown", "Movie"));
            this.galleryItems = list;
        }
        return this.galleryItems;
    }
    
    protected void takeDown(){
        // unregister service from this agent
        try {
            System.out.println("[CU] Service deregistered");
            DFService.deregister(this);
        }
        catch(FIPAException fe){
            fe.printStackTrace();
        }
    }
}
