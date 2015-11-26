/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Behaviors;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.Behaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import java.util.List;

/**
 *
 * @author Laurentiu
 */
public class PerformAuction extends Behaviour {
    
    private List<AID> curators;
    private int step = 0;
    private int value;
    private final long TIMEOUT = 5000;
    
    public PerformAuction(Agent a, List<AID> c){
        super();
        this.setAgent(a);
        curators = c;
        value = 1000;
    }
    
    @Override
    public void action() {
        switch(step){
            case 0:
                //Send cfp with value to all curators
                ACLMessage cfp = new ACLMessage(ACLMessage.CFP);
                for (int i = 0; i < curators.size(); ++i) {
                cfp.addReceiver(curators.get(i));
                }
                cfp.setContent(""+value);
                cfp.setConversationId("auction-from-"+this.myAgent.getLocalName());
                cfp.setReplyWith("cfp"+System.currentTimeMillis());
                this.myAgent.send(cfp);
                System.out.println("Offer sent for "+value);
                step = 1;
                break;
            case 1:
                //Receive acceptance
                MessageTemplate template = MessageTemplate.MatchPerformative(ACLMessage.ACCEPT_PROPOSAL);
                ACLMessage reply = this.myAgent.blockingReceive(template, TIMEOUT);
                if(reply != null){
                    //a curator accepted the offer
                    System.out.println("Auction finished! Product goes to " + reply.getSender().getLocalName());
                    step = 2;
                } else {
                    step = 0;
                }
                break;
                
        }
    }

    @Override
    public boolean done() {
        return (step == 2);
    }
    
}
