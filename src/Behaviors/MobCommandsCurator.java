/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Behaviors;

import Agents.CuratorAgent;
import jade.content.Concept;
import jade.content.ContentElement;
import jade.content.lang.sl.SLCodec;
import jade.content.onto.basic.Action;
import jade.core.Agent;
import jade.core.Location;
import jade.core.behaviours.CyclicBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAException;
import jade.domain.mobility.CloneAction;
import jade.domain.mobility.MobilityOntology;
import jade.domain.mobility.MoveAction;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.wrapper.ControllerException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Laurentiu
 */
public class MobCommandsCurator extends CyclicBehaviour {
    
    private Location destination;
    
    public MobCommandsCurator(Agent a, Location dest){
        super(a);
        destination = dest;
        
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
                System.out.println("[CA] Message received from Controller");
                ContentElement content = myAgent.getContentManager().extractContent(msg);
                Concept concept = ((Action)content).getAction();
                
                if(concept instanceof MoveAction){
                    MoveAction ma = (MoveAction)concept;
                    Location l = ma.getMobileAgentDescription().getDestination();
                    if (l != null) {
                        destination = l;
                    }
                    myAgent.doMove(destination);
                    System.out.println("[CA] Curator "+myAgent.getLocalName()+" moved!");
                    
                    // create & register service for complementary information
                    DFAgentDescription dfd = new DFAgentDescription();
                    dfd.setName(this.myAgent.getAID());
                    ServiceDescription sd = new ServiceDescription();
                    sd.setType("object-info");
                    sd.setOwnership(destination.getName());
                    sd.setName("Obj-complementary-info");
                    dfd.addServices(sd);
                    try{
                        DFService.register(this.myAgent, dfd);
                    } catch (FIPAException fe){
                        fe.printStackTrace();
                    }
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        } else {
            block();
        }
    }
    
}
