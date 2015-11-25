
package Behaviors;

import jade.core.Agent;
import jade.domain.FIPAAgentManagement.NotUnderstoodException;
import jade.domain.FIPAAgentManagement.RefuseException;
import jade.lang.acl.ACLMessage;

/**
 * ListenProfiler class
 */
public class ListenProfiler extends Listen {
    
    public ListenProfiler(Agent a) {
        super(a);
    }

    @Override
    protected ACLMessage prepareResponse(ACLMessage request) throws NotUnderstoodException, RefuseException {
        // TODO implement this function according to scenario
        return super.prepareResponse(request); //To change body of generated methods, choose Tools | Templates.
    }
    
    @Override
    protected ACLMessage prepareResultNotification(ACLMessage request, ACLMessage response) {
        // TODO implement this function according to scenario
        return super.prepareResultNotification(request, response); //To change body of generated methods, choose Tools | Templates.
    }
    
}
