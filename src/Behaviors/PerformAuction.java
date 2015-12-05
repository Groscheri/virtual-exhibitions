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
import jade.wrapper.ControllerException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Laurentiu
 */
public class PerformAuction extends Behaviour {
    
    private List<AID> curators;
    private int step = 0;
    private int value;
    private int stepValue;
    private int minValue;
    private Boolean priceSuggested;
    private final long TIMEOUT = 3000;
    
    public PerformAuction(Agent a, List<AID> c, int value, int stepValue, int minValue){
        super();
        this.setAgent(a);
        curators = c;
        this.value = value;
        this.stepValue = stepValue;
        this.minValue = minValue;
        priceSuggested = false;
    }
    
    @Override
    public void action() {
        switch(step){
            case 0:
                //Send cfp with value to all curators
                if(priceSuggested){
                    //Decrease auction
                    value -= stepValue;
                    //Check if value is not under the minimal prise
                    if(value < minValue){
                        step = 2;
                        
                        try {
                            System.out.println("[AUCTION-"+this.myAgent.getContainerController().getContainerName()+"] Auction finished without selling!");
                        } catch (ControllerException ex) {
                            ex.printStackTrace();
                        }
                        break;
                    }
                }
                ACLMessage cfp = new ACLMessage(ACLMessage.CFP);
                for (int i = 0; i < curators.size(); ++i) {
                cfp.addReceiver(curators.get(i));
                }
                cfp.setContent(""+value);
                cfp.setConversationId("auction");
                cfp.setReplyWith("cfp"+System.currentTimeMillis());
                this.myAgent.send(cfp);
                {
                    try {
                        System.out.println("[AUCTION-"+this.myAgent.getContainerController().getContainerName()+"] Offer sent for "+value);
                    } catch (ControllerException ex) {
                        ex.printStackTrace();
                    }
                }
                priceSuggested = true;
                step = 1;
                break;
            case 1:
                //Receive acceptance
                MessageTemplate template = MessageTemplate.MatchPerformative(ACLMessage.ACCEPT_PROPOSAL);
                ACLMessage reply = this.myAgent.blockingReceive(template, TIMEOUT);
                if(reply != null){
                    try {
                        //a curator accepted the offer
                        System.out.println("[AUCTION-"+this.myAgent.getContainerController().getContainerName()+"] Auction finished! Winner is " + reply.getSender().getLocalName());
                    } catch (ControllerException ex) {
                        ex.printStackTrace();
                    }
                    //send cancel message to all the others
                    ACLMessage endAuction = new ACLMessage(ACLMessage.CANCEL);
                    for (int i = 0; i < curators.size(); ++i) {
                        if(!curators.get(i).getLocalName().equals(reply.getSender().getLocalName())){
                            endAuction.addReceiver(curators.get(i));
                        }
                    }
                    endAuction.setContent(""+value);
                    endAuction.setConversationId("auction");
                    endAuction.setReplyWith("cfp"+System.currentTimeMillis());
                    this.myAgent.send(endAuction);
                    //send confirmation message to the buyer
                    ACLMessage confirmAuction = new ACLMessage(ACLMessage.CONFIRM);
                    confirmAuction.addReceiver(new AID ("art", AID.ISLOCALNAME));
                    confirmAuction.setContent(""+value);
                    confirmAuction.setConversationId("auction");
                    confirmAuction.setReplyWith("cfp"+System.currentTimeMillis());
                    this.myAgent.send(confirmAuction);
                    step = 2;
                } else {
                    try {
                        System.out.println("[AUCTION-"+this.myAgent.getContainerController().getContainerName()+"] No curator auctioned for "+value);
                    } catch (ControllerException ex) {
                        Logger.getLogger(PerformAuction.class.getName()).log(Level.SEVERE, null, ex);
                    }
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
