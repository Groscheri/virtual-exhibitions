
package Behaviors;

import Agents.CuratorAgent;
import Model.Item;
import jade.core.Agent;
import jade.domain.FIPAAgentManagement.NotUnderstoodException;
import jade.domain.FIPAAgentManagement.RefuseException;
import jade.lang.acl.ACLMessage;

/**
 * ListenInfo class
 */
public class ListenInfo extends Listen {
    
    public ListenInfo(Agent a) {
        super(a, "info");
    }

    @Override
    protected ACLMessage prepareResponse(ACLMessage request) throws NotUnderstoodException, RefuseException {
        return super.prepareResponse(request);
    }
    
    @Override
    protected ACLMessage prepareResultNotification(ACLMessage request, ACLMessage response) {
        System.out.println("[CU] Info-request received.");
        CuratorAgent cu = (CuratorAgent)(this.myAgent);
        String content = request.getContent();
        String[] ids_str = content.split(", ");
        int size = ids_str.length;
        
        String info = "";
        for (int i = 0; i < size; ++i) {
            try {
                Long id = Long.parseLong(ids_str[i]);
                Item it = cu.getItemById(id);
                info += "Item " + id + ": " + it.getName() + " created by " + it.getCreator() + " (genre: " + it.getGenre() + ")\n";
            } catch (Exception e) {
                // ignore
            }
        }
        
        ACLMessage answer = request.createReply();
        answer.setPerformative(ACLMessage.INFORM);
        
        answer.setContent(info);
        System.out.println("[CU] Answer info-request");
        return answer;
    }
    
}
