package Behaviors;

import jade.core.Agent;
import jade.lang.acl.ACLMessage;
import java.util.Arrays;

/**
 * RequestInfo class
 */
public class RequestInfo extends Request {
    
    protected Long[] ids;
    
    public RequestInfo(Agent a, String to, Long[] ids) {
        super(a, to);
        this.ids = ids;
        this.request.setConversationId("info");
    }

    @Override
    protected ACLMessage prepareRequest(ACLMessage msg) {
        String content = "";
        for (int i = 0; i < this.ids.length; ++i) {
            content += this.ids[i] + ", ";
        }
        if (content.length() >= 2) {
            content = content.substring(0, content.length() -2);
        }
        this.request.setContent(content);
        return super.prepareRequest(msg);
    }

    @Override
    protected void handleInform(ACLMessage msg) {
        String content = msg.getContent();
        System.out.println("Content about items requested:");
        System.out.println(content);
    }

    @Override
    protected void handleFailure(ACLMessage msg) {
        System.out.println("Impossible to get information about items requested."
                + " Received the following message: " + msg);
    }
}
