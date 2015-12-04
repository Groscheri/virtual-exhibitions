/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Agents;


import Behaviors.PerformAuction;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.SequentialBehaviour;
import jade.domain.AMSService;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.AMSAgentDescription;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.SearchConstraints;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAException;
import jade.lang.acl.ACLMessage;
import jade.proto.SubscriptionInitiator;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Laurentiu
 */
public class ArtistManagerAgent extends Agent {
    
    ArrayList<AID> curators;
    private int value;
    private int step;
    private int minValue;
    
    @Override
    protected void setup(){
         // get value of product, step and minimal value
         
        Object[] args = this.getArguments();
        if (args != null && args.length > 2) {
            try{
                value = Integer.parseInt((String) args[0]);
                step = Integer.parseInt((String) args[1]);
                minValue = Integer.parseInt((String) args[2]);
            } catch(Exception e){
                e.printStackTrace();
            }
        } else {
            //default values
            value = 1000;
            step = 100;
            minValue = 300;
        }
        AMSAgentDescription [] agents = null;
        curators = new ArrayList<AID>();
        
        DFAgentDescription template = new DFAgentDescription();
            ServiceDescription sd = new ServiceDescription();
            sd.setName("Obj-complementary-info");
            template.addServices(sd);
            SearchConstraints sc = new SearchConstraints();
            sc.setMaxResults(new Long(1));
        
        //SequentialBehaviour seq = new SequentialBehaviour();
        this.addBehaviour(new SubscriptionInitiator(this, 
                DFService.createSubscriptionMessage(this, getDefaultDF(), template, sc)){
            @Override
            protected void handleInform(ACLMessage inform) {
                try {
                    DFAgentDescription[] dfds =
                            DFService.decodeNotification(inform.getContent());
                    for(int i=0; i < dfds.length; i++){
                        System.out.println("Notification for artist manager after subscription: "+dfds[i].getName());
                        curators.add(dfds[i].getName());
                        System.out.println(curators.size());
                    }
                    this.myAgent.addBehaviour(new PerformAuction(this.myAgent, curators, value, step, minValue));
                } catch (FIPAException ex) {
                    ex.printStackTrace();
                }

            }
        });
            
        //Start Dutch auction
        //System.out.println("This is "+curators.size()); //has 0 elems
        //seq.addSubBehaviour(new InformFromArtistManager(this, curators));
        //this.addBehaviour(seq);
        //Send offer
        /*
        ACLMessage cfp = new ACLMessage(ACLMessage.CFP);
        for (int i = 0; i < curators.size(); ++i){
            cfp.addReceiver(curators.get(i));
            System.out.println("OK");
        }
        cfp.setContent("An offer is sent");
        this.send(cfp);
        System.out.println("Offer sent");*/
    }
    
    protected void takeDown(){
        
    }
}
