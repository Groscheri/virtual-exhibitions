
package Behaviors;

import jade.core.Agent;
import jade.core.behaviours.TickerBehaviour;

/**
 * UpdateGallery class
 */
public class UpdateGallery extends TickerBehaviour {
    private Agent myAgent;
    
    public UpdateGallery(Agent aid, long time){
        super(aid, time);
        myAgent = aid;
    }
    
    @Override
    protected void onTick(){
        System.out.println("Gallery database was updated");
    }
}
