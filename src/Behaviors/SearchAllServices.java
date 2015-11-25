package Behaviors;

import jade.core.Agent;
import jade.core.behaviours.OneShotBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Iterator;

/**
 * SearchAllServices class
 */
public class SearchAllServices extends OneShotBehaviour {
    
    public SearchAllServices(Agent a){
        super();
        this.setAgent(a);
    }
    
    @Override
    public void action(){
        try {
            // search for services
            DFAgentDescription[] result = DFService.search(this.myAgent, null);
            System.out.println("There are " + result.length + " agents found by DF.");
            int nbServices = 0;
            for(int i = 0; i < result.length; i++){
                Iterator it = result[i].getAllServices();
                while(it.hasNext()){
                    nbServices++;
                    ServiceDescription s = (ServiceDescription) it.next();
                    System.out.println("Service "+nbServices+": " + s.getName());
                }
            }
            if(nbServices > 0){
                System.out.println("Enter number of service:");
                try {
                    BufferedReader bufferRead = new BufferedReader(new InputStreamReader(System.in));
                    String s = bufferRead.readLine();
                    int nbChosen = Integer.parseInt(s);
                    int nb = 0;
                    for(int i = 0; i < result.length; i++) {
                        Iterator it = result[i].getAllServices();
                        while(it.hasNext()) {
                            nb++;
                            if(nb == nbChosen){
                                ServiceDescription a = (ServiceDescription) it.next();
                                System.out.println("Information for service "+nb+":\n\t- type: "+a.getType()+"\n\t- name: "+a.getName()+"\n\t- languages: "+ a.getAllLanguages().toString());
                            } else{
                                it.next();
                            }
                        }
                    }
                } catch(IOException e){
                    e.printStackTrace();
                }
            }
            
            
        } catch(FIPAException fe){
            fe.printStackTrace();
        }
    }
}
