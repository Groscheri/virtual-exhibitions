/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Behaviors;

import jade.core.Agent;
import jade.core.behaviours.TickerBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAException;

/**
 *
 * @author Laurentiu
 */
public class UpdateGallery extends TickerBehaviour {
    private Agent myAgent;
    
    public UpdateGallery(Agent aid, long time){
        super(aid, time);
        myAgent = aid;
    }
    
    protected void onTick(){
        System.out.println("Gallery database was updated");
    }
}
