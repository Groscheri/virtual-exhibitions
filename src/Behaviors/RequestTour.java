
package Behaviors;

import jade.core.Agent;
import jade.lang.acl.ACLMessage;
import java.util.Arrays;

/**
 * RequestTour class
 */
public class RequestTour extends Request {
    
    protected String[] interests;
    
    public RequestTour(Agent a, String to, String[] interests) {
        super(a, to);
        this.interests = interests;
        this.request.setConversationId("tour");
    }

    @Override
    protected ACLMessage prepareRequest(ACLMessage msg) {
        String content = "";
        for (int i = 0; i < this.interests.length; ++i) {
            content += this.interests[i] + ", ";
        }
        if (content.length() >= 2) {
            content = content.substring(0, content.length() -2);
        }
        this.request.setContent(content);
        System.out.println("[PRO] Send build-tour request");
        return super.prepareRequest(msg);
    }

    @Override
    protected void handleInform(ACLMessage msg) {
        // TODO create a requestInfo (curator) and ask him information about 
        // items listed in the virutal tour received
        String content = msg.getContent();
        String[] ids_str = content.split(", ");
        int size = ids_str.length;
        Long[] ids = new Long[size];
        for (int i = 0; i < size; ++i) {
            try {
                ids[i] = Long.parseLong(ids_str[i]);
            }
            catch (Exception e) {
                // ignore - wrong format
            }
        }
        System.out.println("[PRO] Received virtual tour => asking for information to Curator");
        this.myAgent.addBehaviour(new RequestInfo(this.myAgent, "cucu", ids));
    }
    
    
    
}
