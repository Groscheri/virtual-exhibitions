/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Agents;

import jade.core.AID;
import jade.core.Agent;
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
    
    @Override
    protected void setup(){
        AMSAgentDescription [] agents = null;
        curators = new ArrayList<AID>();
        
        /*  SearchConstraints c = new SearchConstraints();
        c.setMaxResults ( new Long(-1) );
        try {
            agents = AMSService.search(this, new AMSAgentDescription (), c );
            System.out.println("------");
            for(int i=0; i<agents.length; i++){
                System.out.println(agents[i].getName());
            }
        } catch (FIPAException ex) {
            ex.printStackTrace();
        }*/
        DFAgentDescription template = new DFAgentDescription();
            ServiceDescription sd = new ServiceDescription();
            sd.setName("Obj-complementary-info");
            template.addServices(sd);
            SearchConstraints sc = new SearchConstraints();
            sc.setMaxResults(new Long(1));

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
                    } catch (FIPAException ex) {
                        ex.printStackTrace();
                    }

                }
            });
    }
    
    protected void takeDown(){
        
    }
}
