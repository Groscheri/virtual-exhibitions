package Agents;

import Behaviors.ListenArtistManager;
import Behaviors.ListenInfo;
import Behaviors.ListenTour;
import Behaviors.MobCommandsCurator;
import Behaviors.UpdateGallery;
import Model.AuctionStrategy;
import Model.Item;
import jade.core.AID;
import jade.core.Agent;
import jade.core.Location;
import jade.core.behaviours.ParallelBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAException;
import jade.wrapper.ControllerException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * CuratorAgent class
 * Monitor art gallery (database) : check update
 * - answer `ProfilerAgent` with details about items stored in the gallery
 * - answer `TourGuideAgent` with details about items in order to build virtualTour
 */
public class CuratorAgent extends Agent {
    protected ArrayList<Item> galleryItems;
    
    private AuctionStrategy strategy;
    
    Location dest;
    
    @Override
    protected void setup() {
        // get value to spend in auction
        Object[] args = this.getArguments();
        if (args != null && args.length > 0) {
            try{
                //get id value and strategies
                strategy = selectStrategy(args);
            } catch(Exception e){
                e.printStackTrace();
            }
        } else {
            strategy = new AuctionStrategy(3);
            strategy.setValToSpend(400);
        }
        

        // create an update behavior to take gallery up to date
        //this.addBehaviour(new UpdateGallery(this, 5000));
        
        dest = here();
        
        this.addBehaviour(new MobCommandsCurator(this, dest));
        System.out.println("[CU] Listening to Controller");       
        //TODO - get max value in param
        this.addBehaviour(new ListenArtistManager(this, strategy));
        System.out.println("[CU] Listening to Artist Manager");
        this.addBehaviour(new ListenInfo(this));
        System.out.println("[CU] Listening to TourGuide & Profiler");
    }
    
    /**
     * Gallery represented as a list of `Item` objects
     * Modeled as a singleton
     * @return artifacts list
     */
    private ArrayList<Item> getItems() {
        if (this.galleryItems == null) {
            ArrayList<Item> list = new ArrayList<>();
            list.add(new Item(1L, "La Joconde", "Da Vinci", "Painting"));
            list.add(new Item(2L, "Imagine", "John Lenon", "Music"));
            list.add(new Item(3L, "Guernica", "Picasso", "Painting"));
            list.add(new Item(4L, "Wheatfield with Crows", "Van Gogh", "Painting"));
            list.add(new Item(5L, "A beautiful mind", "Unknown", "Movie"));
            this.galleryItems = list;
        }
        return this.galleryItems;
    }
    
    public Item getItemById(Long id) {
        ArrayList<Item> items = this.getItems();
        for (Item i : items) {
            if (i.getId() == id) {
                return i;
            }
        }
        return null;
    }
    
    private AuctionStrategy selectStrategy(Object[] args){
        AuctionStrategy str = new AuctionStrategy(Integer.parseInt((String) args[0]));
        switch(str.getIdStrategy()){
            //wait for number of offers (nbOffers) than buy
            case 1:
                str.setNbOffers(Integer.parseInt((String) args[1]));
                break;
            //certain percent of first price
            case 2:
                str.setPercentage(Integer.parseInt((String) args[1]));
                break;
            //
            case 3:
                str.setValToSpend(Integer.parseInt((String) args[1]));
                break;
            case 4:
                int valToSpend = Integer.parseInt((String) args[1]);
                int percentage = Integer.parseInt((String) args[2]);
                valToSpend = valToSpend * percentage / 100;
                str.setValToSpend(valToSpend);
                break;
        }
        return str;
    }
    
    protected void takeDown(){
        // unregister service from this agent
        try {
            DFService.deregister(this);
        }
        catch(FIPAException fe){
            fe.printStackTrace();
        }
    }
}
