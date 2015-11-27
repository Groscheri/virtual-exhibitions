/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Behaviors;

import jade.core.Agent;
import jade.core.behaviours.Behaviour;
import jade.core.behaviours.CyclicBehaviour;
import jade.domain.FIPAAgentManagement.NotUnderstoodException;
import jade.domain.FIPAAgentManagement.RefuseException;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

/**
 *
 * @author Laurentiu
 */
public class ListenArtistManager extends CyclicBehaviour {
    
    private int value;
    
    public ListenArtistManager(Agent a, int val) {
        super(a);
        value = val;
    }
    
    public void action(){
        ACLMessage msg = myAgent.receive(MessageTemplate.MatchConversationId("auction"));
        if(msg != null){
            int auctionValue = Integer.parseInt(msg.getContent());
            if(auctionValue <= value){
                //Accept offer
                ACLMessage reply = msg.createReply();
                reply.setPerformative(ACLMessage.ACCEPT_PROPOSAL);
                reply.setContent("buy-product");
                this.myAgent.send(reply);
            } else {
                block();
            }
        } else{
            block();
        }
    }
}
