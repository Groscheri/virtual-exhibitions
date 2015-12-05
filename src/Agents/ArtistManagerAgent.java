/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Agents;


import Behaviors.MobCommandsArtist;
import Behaviors.PerformAuction;
import jade.content.lang.sl.SLCodec;
import jade.content.onto.basic.Action;
import jade.core.AID;
import jade.core.Agent;
import jade.core.Location;
import jade.core.behaviours.Behaviour;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.SequentialBehaviour;
import jade.core.behaviours.WakerBehaviour;
import jade.domain.AMSService;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.AMSAgentDescription;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.SearchConstraints;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAException;
import jade.domain.mobility.CloneAction;
import jade.domain.mobility.MobileAgentDescription;
import jade.domain.mobility.MobilityOntology;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.proto.SubscriptionInitiator;
import jade.wrapper.ControllerException;
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
    
    private int bestBid;
    private String bidder;
    
    private Location dest;
    
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
        
        dest = here();
        
        //register auction service
        DFAgentDescription dfd = new DFAgentDescription();
        dfd.setName(this.getAID());
        ServiceDescription sd_register = new ServiceDescription();
        sd_register.setType("auction");
        sd_register.setName("Auction-auctioneer");
        dfd.addServices(sd_register);
        try{
            System.out.println("[AM] Service Auction-auctioneer registered");
            DFService.register(this, dfd);
        } catch (FIPAException fe){
            fe.printStackTrace();
        }
        
        curators = new ArrayList<AID>();
        //SequentialBehaviour seq = new SequentialBehaviour();
        this.addBehaviour(new MobCommandsArtist(this, dest));
        this.addBehaviour(new CyclicBehaviour(this){   
            
            private ArrayList<Integer> offers;
            
            @Override
            public void action() {
                ACLMessage msg = myAgent.receive(MessageTemplate.and(MessageTemplate.MatchConversationId("auction"), MessageTemplate.MatchPerformative(ACLMessage.CONFIRM)));
                if(msg != null){
                    try {
                        int localBestBid = Integer.parseInt(msg.getContent());
                        if(localBestBid > bestBid){
                            bestBid = localBestBid;
                            bidder = msg.getSender().getLocalName();
                        }
                        System.out.println("[AUCTION] Best offer of "+bestBid);
                    } catch(Exception e){
                        e.printStackTrace();
                    }
                } else
                {
                    block();
                }
            }
        });
        
        //this.addBehaviour(seq);
    }
    
    protected void afterClone() {
        //Get curators on the container
        DFAgentDescription template = new DFAgentDescription();
        ServiceDescription sd = new ServiceDescription();
        sd.setName("Obj-complementary-info");
        try {
            sd.setOwnership(this.getContainerController().getContainerName());
        } catch (ControllerException ex) {
            ex.printStackTrace();
        }
        template.addServices(sd);
        SearchConstraints sc = new SearchConstraints();
        sc.setMaxResults(new Long(1));

        this.addBehaviour(new SubscriptionInitiator(this, 
        DFService.createSubscriptionMessage(this, this.getDefaultDF(), template, sc)){
            @Override
            protected void handleInform(ACLMessage inform) {
                try {
                    DFAgentDescription[] dfds =
                            DFService.decodeNotification(inform.getContent());
                    for(int i=0; i < dfds.length; i++){
                        System.out.println("[AM-"+this.myAgent.getContainerController().getContainerName()+"] Notification for artist manager after subscription: "+dfds[i].getName().getLocalName());
                        curators.add(dfds[i].getName());
                    }

                    //start auction
                    this.myAgent.addBehaviour(new PerformAuction(this.myAgent, curators, value, step, minValue));
                } catch (FIPAException ex) {
                    ex.printStackTrace();
                } catch (ControllerException ex) {
                    ex.printStackTrace();
                }

            }
        });
   }

    
    protected void takeDown(){
        try {
            DFService.deregister(this);
        } catch (FIPAException ex) {
            Logger.getLogger(ArtistManagerAgent.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
