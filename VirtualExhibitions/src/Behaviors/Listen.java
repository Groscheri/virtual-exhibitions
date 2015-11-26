
package Behaviors;

import jade.core.Agent;
import jade.domain.FIPAAgentManagement.NotUnderstoodException;
import jade.domain.FIPAAgentManagement.RefuseException;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.proto.SimpleAchieveREResponder;

/**
 * Listen abstract class
 */
public abstract class Listen extends SimpleAchieveREResponder {
    public Listen(Agent a, String conversationId) {
        super(a, null);
        MessageTemplate mt = MessageTemplate.and(MessageTemplate.MatchPerformative(ACLMessage.SUBSCRIBE), MessageTemplate.MatchConversationId(conversationId));
        this.reset(mt);
    }

    @Override
    protected ACLMessage prepareResponse(ACLMessage request) throws NotUnderstoodException, RefuseException {
        return super.prepareResponse(request);
    }
    
    @Override
    protected ACLMessage prepareResultNotification(ACLMessage request, ACLMessage response) {
        System.out.println("Responder has received the following message: " + request);
        ACLMessage informDone = request.createReply();
        informDone.setPerformative(ACLMessage.INFORM);
        informDone.setContent("inform done");
        return informDone;
}
    
}
