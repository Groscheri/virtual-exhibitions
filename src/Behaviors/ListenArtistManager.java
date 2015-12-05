/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Behaviors;

import Model.AuctionStrategy;
import jade.core.Agent;
import jade.core.behaviours.Behaviour;
import jade.core.behaviours.CyclicBehaviour;
import jade.domain.FIPAAgentManagement.NotUnderstoodException;
import jade.domain.FIPAAgentManagement.RefuseException;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.wrapper.ControllerException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Laurentiu
 */
public class ListenArtistManager extends CyclicBehaviour {
    
    private AuctionStrategy strategy;
    private int first;
    private int nbTimes;
    
    public ListenArtistManager(Agent a, AuctionStrategy str) {
        super(a);
        strategy = str;
        first = 0;
        nbTimes = 0;
    }
    
    public void action(){
        ACLMessage msg = myAgent.receive(MessageTemplate.MatchConversationId("auction"));
        if(msg != null){
            if(msg.getPerformative() == ACLMessage.CFP){
                int auctionValue = Integer.parseInt(msg.getContent());
                if(first == 0){
                    first = auctionValue;
                }
                if(strategy.applyStrategy(auctionValue, first, nbTimes)){
                //if(auctionValue <= value){
                    //Accept offer
                    ACLMessage reply = msg.createReply();
                    reply.setPerformative(ACLMessage.ACCEPT_PROPOSAL);
                    reply.setContent("buy-product");
                    this.myAgent.send(reply);
                    nbTimes = 0;
                } else {
                    nbTimes ++;
                    block();
                }
            } else if(msg.getPerformative() == ACLMessage.CANCEL) {
                nbTimes = 0;
            } else if(msg.getPerformative() == ACLMessage.CONFIRM){
                nbTimes = 0;
                try {
                    System.out.println("[AUCTION-"+this.myAgent.getContainerController().getContainerName()+"] Confirmation for auction received by "+this.myAgent.getLocalName());
                } catch (ControllerException ex) {
                    Logger.getLogger(ListenArtistManager.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        } else{
            block();
        }
    }
}
