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
import jade.core.behaviours.SequentialBehaviour;
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
        
        //Find all curators
        AMSAgentDescription [] agents = null;
        curators = new ArrayList<AID>();
        
        this.addBehaviour(new MobCommandsArtist(this, dest, curators, value, step, minValue));    
    }
    
    protected void takeDown(){
        
    }
    
}
