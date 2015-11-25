
package Behaviors;

import jade.core.AID;
import jade.core.Agent;
import jade.lang.acl.ACLMessage;
import jade.proto.FIPAProtocolNames;
import jade.proto.SimpleAchieveREInitiator;

/**
 * Request abstract class
 */
public abstract class Request extends SimpleAchieveREInitiator {
    
    protected ACLMessage request;
    
    public Request(Agent a, String to) {
        super(a, null);
        this.request = new ACLMessage(ACLMessage.REQUEST);
        this.request.setProtocol(FIPAProtocolNames.FIPA_REQUEST);
        this.request.addReceiver(new AID(to, AID.ISLOCALNAME));
    }

    @Override
    protected ACLMessage prepareRequest(ACLMessage msg) {
        return super.prepareRequest(this.request);
    }
    
    
    
}
