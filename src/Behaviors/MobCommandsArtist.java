/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Behaviors;

import Agents.CuratorAgent;
import jade.content.Concept;
import jade.content.ContentElement;
import jade.content.lang.Codec;
import jade.content.onto.OntologyException;
import jade.content.onto.basic.Action;
import jade.core.Agent;
import jade.core.Location;
import jade.core.behaviours.CyclicBehaviour;
import jade.domain.mobility.CloneAction;
import jade.domain.mobility.MobilityOntology;
import jade.lang.acl.ACLCodec.CodecException;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import java.util.logging.Level;
import java.util.logging.Logger;
import jade.content.lang.*;
import jade.content.lang.sl.*;
import jade.core.AID;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.SearchConstraints;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAException;
import jade.proto.SubscriptionInitiator;
import java.util.ArrayList;

/**
 *
 * @author Laurentiu
 */
public class MobCommandsArtist extends CyclicBehaviour {
    
    private Location destination;
    ArrayList<AID> curators;
    
    private int value;
    private int step;
    private int minValue;
    
    public MobCommandsArtist(Agent a, Location dest, ArrayList<AID> curators, int val, int step, int minVal){
        super(a);
        destination = dest;
        this.curators = curators;
        value = val;
        this.step = step;
        minValue = minVal;
        
        //Register language and ontology
	myAgent.getContentManager().registerLanguage(new SLCodec());
	myAgent.getContentManager().registerOntology(MobilityOntology.getInstance());
    }
    
    @Override
    public void action() {
        MessageTemplate mt = MessageTemplate.and(MessageTemplate.MatchOntology(MobilityOntology.getInstance().getName())
                                                            , MessageTemplate.MatchPerformative(ACLMessage.REQUEST));
        ACLMessage msg = myAgent.receive(mt);
        
        if(msg != null){
            try {
                System.out.println("[AM] Message received from Controller");
                ContentElement content = myAgent.getContentManager().extractContent(msg);
                Concept concept = ((Action)content).getAction();
                
                if(concept instanceof CloneAction){
                    CloneAction ca = (CloneAction)concept;
                    String newName = ca.getNewName();
                    Location l = ca.getMobileAgentDescription().getDestination();
                    if (l != null) {
                        destination = l;
                    }
                    myAgent.doClone(destination, newName);
                    
                    //this.myAgent = new CuratorAgent(new AID(newName, AID.ISLOCALNAME));
                    
                    //Get curators on the container
                    DFAgentDescription template = new DFAgentDescription();
                    ServiceDescription sd = new ServiceDescription();
                    sd.setName("Obj-complementary-info");
                    sd.setOwnership(destination.getName());
                    template.addServices(sd);
                    SearchConstraints sc = new SearchConstraints();
                    sc.setMaxResults(new Long(1));
                    
                    this.myAgent.addBehaviour(new SubscriptionInitiator(this.myAgent, 
                    DFService.createSubscriptionMessage(this.myAgent, this.myAgent.getDefaultDF(), template, sc)){
                        @Override
                        protected void handleInform(ACLMessage inform) {
                            try {
                                DFAgentDescription[] dfds =
                                        DFService.decodeNotification(inform.getContent());
                                for(int i=0; i < dfds.length; i++){
                                    System.out.println("[AM-"+destination.getName()+"] Notification for artist manager after subscription: "+dfds[i].getName().getLocalName());
                                    curators.add(dfds[i].getName());
                                }

                                //start auction
                                this.myAgent.addBehaviour(new PerformAuction(this.myAgent, curators, value, step, minValue));
                            } catch (FIPAException ex) {
                                ex.printStackTrace();
                            }

                        }
                    });

                    
                    
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        } else {
            block();
        }
    }
    
}
