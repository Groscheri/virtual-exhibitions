/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Agents;

import jade.content.ContentElement;
import jade.content.lang.sl.SLCodec;
import jade.content.onto.basic.Action;
import jade.content.onto.basic.Result;
import jade.core.AID;
import jade.core.Agent;
import jade.core.Location;
import jade.core.ProfileImpl;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.SearchConstraints;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAException;
import jade.domain.JADEAgentManagement.QueryPlatformLocationsAction;
import jade.domain.mobility.CloneAction;
import jade.domain.mobility.MobileAgentDescription;
import jade.domain.mobility.MobilityOntology;
import jade.domain.mobility.MoveAction;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.proto.SubscriptionInitiator;
import jade.wrapper.AgentController;
import jade.wrapper.ContainerController;
import jade.wrapper.StaleProxyException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Laurentiu
 */
public class ControllerAgent extends Agent {
    
    AID artist;
    private jade.wrapper.AgentContainer home;
    private jade.wrapper.AgentContainer[] container = null;
    private Map locations = new HashMap();
    private final int NB_SUB_CONTAINERS = 2;
    // Get a JADE Runtime instance
    jade.core.Runtime runtime = jade.core.Runtime.instance();
    
    @Override
    protected void setup() {
        
        // Register language and ontology
	  getContentManager().registerLanguage(new SLCodec());
	  getContentManager().registerOntology(MobilityOntology.getInstance());

      try {
         // Create the container objects
         container = new jade.wrapper.AgentContainer[3];
        for (int i = 0; i < 2; i++){
            container[0] = runtime.createAgentContainer(new ProfileImpl());
	}
	     doWait(2000);

	     // Get available locations with AMS
	     sendRequest(new Action(getAMS(), new QueryPlatformLocationsAction()));

	     //Receive response from AMS
         MessageTemplate mt = MessageTemplate.and(
			                  MessageTemplate.MatchSender(getAMS()),
			                  MessageTemplate.MatchPerformative(ACLMessage.INFORM));
         ACLMessage resp = blockingReceive(mt);
         ContentElement ce = getContentManager().extractContent(resp);
         Result result = (Result) ce;
         jade.util.leap.Iterator it = result.getItems().iterator();
         while (it.hasNext()) {
            Location loc = (Location)it.next();
            locations.put(loc.getName(), loc);
		 }
	}
	  catch (Exception e) { 
              e.printStackTrace(); 
          }
        
        DFAgentDescription template = new DFAgentDescription();
        ServiceDescription sd = new ServiceDescription();
        sd.setName("Auction-auctioneer");
        template.addServices(sd);
        SearchConstraints sc = new SearchConstraints();
        sc.setMaxResults(new Long(1));
        
        this.addBehaviour(new SubscriptionInitiator(this, 
                DFService.createSubscriptionMessage(this, getDefaultDF(), template, sc)){
            @Override
            @SuppressWarnings("empty-statement")
            protected void handleInform(ACLMessage inform) {
                 try {
                    DFAgentDescription[] dfds =
                            DFService.decodeNotification(inform.getContent());
                    if(dfds.length > 0){
                        artist = dfds[0].getName();
                        for(int i=1; i<= NB_SUB_CONTAINERS; i++){
                            cloneAgent(artist.getLocalName()+"-"+i,"Container-"+i);
                        }
                        
                        ContainerController agentFactory = getContainerController();
                        Object[][] args = {{"1", "5"}, {"2", "60"}, {"3", "700" }, {"4", "1000", "80"}};
                        String name = "cucu";
                        for(int i=0; i<args.length; i++){
                            String container = "Container-";
                            AgentController curator = agentFactory.createNewAgent(name+i, CuratorAgent.class.getName(), args[i]);
                            curator.start();
                            if(i%2==0){
                                container += "1";
                            } else {
                                container += "2";
                            }
                            moveAgent(new AID(name+i, AID.ISLOCALNAME), container);
                        }
                        
                        
                    }
                 } catch(FIPAException e){
                     e.printStackTrace();
                 } catch (StaleProxyException ex) {
                    Logger.getLogger(ControllerAgent.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
        
    }
    
    void cloneAgent(String agentNewName, String containerName){
        System.out.println("[CTA] Start cloning");
        Location dest = (Location)locations.get(containerName);
        MobileAgentDescription mad = new MobileAgentDescription();
        mad.setName(artist);
        mad.setDestination(dest);
        String newName = agentNewName;
        CloneAction ca = new CloneAction();
        ca.setNewName(newName);
        ca.setMobileAgentDescription(mad);
        sendRequest(new Action(artist, ca));
        System.out.println("[CTA] Cloning request sent to "+artist.getLocalName());
    }
    
    void moveAgent(AID aidOfAgent, String containerName){
        System.out.println("[CTA] Start moving");
        Location dest = (Location)locations.get(containerName);
        MobileAgentDescription mad = new MobileAgentDescription();
        mad.setName(aidOfAgent);
        mad.setDestination(dest);
        MoveAction ma = new MoveAction();
        ma.setMobileAgentDescription(mad);
        sendRequest(new Action(aidOfAgent, ma));
        System.out.println("[CTA] Moving request sent to "+aidOfAgent.getLocalName());
    }
    
     void sendRequest(Action action) {
        ACLMessage request = new ACLMessage(ACLMessage.REQUEST);
        request.setLanguage(new SLCodec().getName());
        request.setOntology(MobilityOntology.getInstance().getName());
        try {
            getContentManager().fillContent(request, action);
            request.addReceiver(action.getActor());
            send(request);
        }
        catch (Exception ex) {
            ex.printStackTrace(); 
        }
    }
}
