
package Behaviors;

import jade.core.Agent;
import jade.domain.FIPAAgentManagement.NotUnderstoodException;
import jade.domain.FIPAAgentManagement.RefuseException;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.proto.FIPAProtocolNames;
import jade.proto.SimpleAchieveREResponder;

/**
 * Listen abstract class
 */
public abstract class Listen extends SimpleAchieveREResponder {
    public Listen(Agent a) {
        super(a, null);
        MessageTemplate mt = SimpleAchieveREResponder.createMessageTemplate(FIPAProtocolNames.FIPA_REQUEST);
        this.reset(mt);
    }

    @Override
    protected ACLMessage prepareResponse(ACLMessage request) throws NotUnderstoodException, RefuseException {
        System.out.println("Preparing response !");
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
