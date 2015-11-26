
package Behaviors;

import jade.core.AID;
import jade.core.Agent;
import jade.domain.FIPAAgentManagement.NotUnderstoodException;
import jade.domain.FIPAAgentManagement.RefuseException;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

/**
 * ListenTour class
 */
public class ListenTour extends Listen {
    
    public ListenTour(Agent a) {
        super(a, "tour");
    }

    @Override
    protected ACLMessage prepareResponse(ACLMessage request) throws NotUnderstoodException, RefuseException {
        return super.prepareResponse(request);
    }

    @Override
    protected ACLMessage prepareResultNotification(ACLMessage request, ACLMessage response) {
        System.out.println("[TO] a Profiler asked for a virtual tour with these interests: " + request.getContent());
        ACLMessage informDone = request.createReply();
        informDone.setPerformative(ACLMessage.INFORM);
        
        ACLMessage message = new ACLMessage(ACLMessage.SUBSCRIBE);
        message.addReceiver(new AID("cucu", AID.ISLOCALNAME));
        message.setConversationId("info");
        message.setContent("1, 2, 5"); // ask info about these ids
        
        System.out.println("[TO] send info-request to Curator in order to build virtual tour.");
        this.myAgent.send(message); // send message
        
        // wait for answer
        MessageTemplate mt = MessageTemplate.and(MessageTemplate.MatchPerformative(ACLMessage.INFORM), MessageTemplate.MatchConversationId("info"));
        ACLMessage answer = this.myAgent.blockingReceive(mt); // wait until we receive information
        
        System.out.println("[TO] receive answer to info-request from Curator");
        
        String content = answer.getContent();
        String tour = this.buildTour(content); // with content, choose the best items !
       
        System.out.println("[TO] build virtual tour");
        
        informDone.setContent(tour);
        System.out.println("[TO] send virtual tour to Profiler");
        return informDone;
        
    }
    
    private String buildTour(String info) {
        return "1, 5";
    }
    
}
