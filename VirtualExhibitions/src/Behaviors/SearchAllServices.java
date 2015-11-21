/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Behaviors;

import jade.core.Agent;
import jade.core.Service;
import jade.core.behaviours.Behaviour;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import static java.lang.System.in;
import java.util.Iterator;

/**
 *
 * @author Laurentiu
 */
public class SearchAllServices extends Behaviour {
    
    public SearchAllServices(Agent a){
        super();
        this.myAgent = a;
    }
    
    @Override
    public void action(){
        ServiceDescription sd = new ServiceDescription();
        try{
            DFAgentDescription[] result = DFService.search(this.myAgent, null);
            System.out.println("There are "+result.length+" agents found by DF");
            int nbServices = 0;
            for (int i=0; i < result.length; i++){
                Iterator it = result[i].getAllServices();
                while(it.hasNext()){
                    nbServices++;
                    ServiceDescription s = (ServiceDescription)  it.next();
                    System.out.println("Service "+nbServices+": "+s.getName());
                }
            }
            
            System.out.println("Enter number of service:");
            try {
                BufferedReader bufferRead = new BufferedReader(new InputStreamReader(System.in));
                String s = bufferRead.readLine();
                int nbChosen = Integer.parseInt(s);
                int nb = 0;
                for (int i=0; i < result.length; i++){
                    Iterator it = result[i].getAllServices();
                    while(it.hasNext()){
                        nb++;
                        if(nb == nbChosen){
                            ServiceDescription a = (ServiceDescription)  it.next();
                            System.out.println("Information for service "+nb+": type - "+a.getType()+", name - "+a.getName()+", languages "+a.getAllLanguages());
                        } else{
                            it.next();
                        }
                    }
                }
            } catch(IOException e){
                e.printStackTrace();
            }
            
        } catch(FIPAException fe){
            fe.printStackTrace();
        }
    }

    @Override
    public boolean done() {
        return true;
    }
    
    
}
