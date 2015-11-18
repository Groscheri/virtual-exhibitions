package Agents;

import Behaviors.ListenProfiler;
import Behaviors.ListenTourGuide;
import Model.Item;
import jade.core.Agent;
import jade.core.behaviours.ParallelBehaviour;
import jade.proto.states.MsgReceiver;
import java.util.ArrayList;

/**
 * CuratorAgent class
 * Monitor art gallery
 * - answer `ProfilerAgent` with details about items stored in the gallery
 * - answer `TourGuideAgent` with details about items in order to build virtualTour
 */
public class CuratorAgent extends Agent {
    protected ArrayList<Item> galleryItems;
    
    @Override
    protected void setup() {
        // init
        
        // TODO : add 2 behaviors to listen to incoming messages from ProfilerAgent and TourGuideAgent
        ParallelBehaviour parallel = new ParallelBehaviour();
        parallel.addSubBehaviour(new ListenProfiler(this));
        parallel.addSubBehaviour(new ListenTourGuide(this));
        
        this.addBehaviour(parallel);
        
        // TODO
        //new MsgReceiver(this, mt, D_ACTIVE, s, this)
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
}
