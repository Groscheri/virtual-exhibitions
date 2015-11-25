package Behaviors;

import jade.core.Agent;
import jade.lang.acl.ACLMessage;

/**
 * RequestCurator class
 */
public class RequestCurator extends Request {
    
    public RequestCurator(Agent a, String to) {
        super(a, to);
    }

    @Override
    protected void handleInform(ACLMessage msg) {
        System.out.println("Protocol finished. Rational Effect achieved. Received the following message: " + msg);
    }

    @Override
    protected void handleFailure(ACLMessage msg) {
        System.out.println("Protocol failed. Received the following message: " + msg);
    }
}
