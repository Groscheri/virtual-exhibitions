package Agents;

import Model.Item;
import jade.core.Agent;
import jade.core.behaviours.Behaviour;
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
        Behaviour listenProfiler = new Behaviour() {
            @Override
            public void action() {
                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }

            @Override
            public boolean done() {
                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }
        };
        
        Behaviour listenTourGuide = new Behaviour() {
            @Override
            public void action() {
                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }

            @Override
            public boolean done() {
                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }
        };
        parallel.addSubBehaviour(listenProfiler);
        parallel.addSubBehaviour(listenTourGuide);
        
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
        if (galleryItems == null) {
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
