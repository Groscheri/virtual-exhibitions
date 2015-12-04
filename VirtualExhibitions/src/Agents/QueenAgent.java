
package Agents;

import Model.Chest;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.Behaviour;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.SearchConstraints;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAException;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.proto.SubscriptionInitiator;

/**
 * QueenAgent class
 */
public class QueenAgent extends Agent {

    protected AID previousQueen;
    protected AID nextQueen;
    protected Integer id;
    protected Integer n;
    
    protected int step = 0;
    
    @Override
    protected void setup() {
        // retrieve ID
        Object[] args = this.getArguments();
        if (args != null && args.length == 2) {
            try {
                this.id = Integer.parseInt((String)args[0]);
                this.n = Integer.parseInt((String)args[1]);
            } catch (Exception e1) {
                try {
                    this.id = (Integer)args[0];
                    this.n = (Integer)args[1];
                } catch (Exception e2) {
                    // wrong parameters (not Integers)
                    this.display_error("Wrong parameters", true);
                }
            }
        }
        else {
            this.display_error("2 paramaters needed", true);
        }
        
        //this.display("Parameters received: [id=" + this.id + ", n=" + this.n + "]");
        
        // register agent as alive to DF
        if (!this.registerToDF()) {
            this.display_error("Impossible to register to DF", true);
        }
        
        this.doWait(500L); // tempo

        // subscribe to DF to get next & previous queens' AIDs
        // look for previous queen
        if (!this.isFirst()) {
            this.subscribe("" + this.getPreviousQueen(), true);
        }
        else {
            step++;
        }
       
        // look for next queen
        if (!this.isLast()) {
            this.subscribe("" + this.getNextQueen(), false);
        }
        else {
            step++;
        }
        
        // wait for previous & next to be ready and launch solver
        this.addBehaviour(new Behaviour() {
            @Override
            public void action() {
                this.block(100L); // wait 100ms to limit CPU utilization
            }

            @Override
            public boolean done() {
                if (step == 2) {
                    display("Previous: " + ((previousQueen != null) ? previousQueen.getName() : "-") + " / Next: " + ((nextQueen != null) ? nextQueen.getName() : "-"));
                    this.myAgent.addBehaviour(new SolveBehavior()); // solver
                    return true;
                }
                return false;
            }
        });
    }
    
    private void subscribe(String name, final boolean previous) {
        DFAgentDescription template = new DFAgentDescription();
        ServiceDescription sd = new ServiceDescription();
        sd.setType("queen");
        sd.setName(name);
        template.addServices(sd);
        SearchConstraints sc = new SearchConstraints();
        sc.setMaxResults(new Long(1));
        
        this.addBehaviour(new SubscriptionInitiator(this, DFService.createSubscriptionMessage(this, this.getDefaultDF(), template, sc)) {
            @Override
            protected void handleInform(ACLMessage inform) {
                try {
                    DFAgentDescription[] dfds =
                            DFService.decodeNotification(inform.getContent());
                    if(dfds.length > 0){
                        AID aid = dfds[0].getName();
                        if (previous) {
                            previousQueen = aid;
                            step++;
                        }
                        else {
                            nextQueen = aid;
                            step++;
                        }
                    }
                } catch (FIPAException ex) {
                    ex.printStackTrace();
                }
            }
        });
    }

    @Override
    protected void takeDown() {
        // unregister
        try {
            DFService.deregister(this);
        }
        catch(FIPAException fe){
            fe.printStackTrace();
        }
        // ends
        this.display("Ending.");
    }
    
    private void display(String message) {
        System.out.println("[" + this.getLocalName() + "] " + message);
    }
    
    private void display_error(String message, boolean kill) {
        System.err.println("[" + this.getLocalName() + "] " + message);
        if (kill) {
            this.doDelete();
        }
    }
    
    private boolean registerToDF() {
        DFAgentDescription dfd = new DFAgentDescription();
        dfd.setName(this.getAID());
        ServiceDescription sd = new ServiceDescription();
        sd.setName(this.id + "");
        sd.setType("queen");
        dfd.addServices(sd);
        try {
            DFService.register(this, dfd);
            return true;
        } catch (FIPAException e) {
            return false;
        }
    }
    
    protected int getPreviousQueen() {
        return (this.id - 1);
    }
    
    protected int getNextQueen() {
        return (this.id + 1);
    }
    
    protected boolean isFirst() {
        return (this.id == 0);
    }
    
    protected boolean isLast() {
        return (this.id == this.n - 1);
    }
    
    /**
     * SolveBehavior private class
     */
    private class SolveBehavior extends Behaviour {
        
        /**
         * steps:
         * - 0 ==> next and previous queens missing
         * - 1 ==> next or previous queen missing
         * - 2 ==> init protocol
         * - 3 ==> wait for previous queen ACLMessage
         * - 4 ==> wait for next queen ACLMessage
         * - 5 ==> end protocol
         */
        
        // TODO refactor and make some more useful function to communicate (if 
        // possible)
        
        protected Chest c;
        protected int lastPosition = Chest.QUEEN_NOT_PLACED;

        @Override
        public void action() {
            switch (step) {
                case 2:
                {
                    if (isFirst()) {
                        display("Starting solver");
                        
                        ACLMessage message = new ACLMessage(ACLMessage.INFORM);
                        message.addReceiver(nextQueen); // send to next
                        message.setConversationId("place-queen");
                        
                        // create a chest
                        c = new Chest(n);
                        // find a place
                        int place = c.findPlace(id);
                        // set queen
                        c.setQueen(id, place);
                        lastPosition = place; // save last place
                        
                        // add chest to content
                        message.setContent(c.serialize());

                        this.myAgent.send(message);
                        step = 4; // wait for next queen to ask for a move
                    }
                    else {
                        // other
                        step = 3;
                    }
                }
                break;
                
                case 3:
                { // ASK TO PLACE
                    // wait for previous queen ACLMessage
                    //display("Waiting for previous queen message");
                    ACLMessage inform = this.myAgent.blockingReceive(MessageTemplate.and(
                            MessageTemplate.MatchConversationId("place-queen"), 
                            MessageTemplate.MatchPerformative(ACLMessage.INFORM)
                    ));
                    display("Received \"place-queen\"");
                    
                    // retrieve serial
                    String serial = inform.getContent();
                    
                    // build chest
                    c = Chest.fromSerial(serial, n);
                    
                    // find place
                    int place = c.findPlace(id);
                    
                    if (place == Chest.QUEEN_NOT_PLACED) { // impossible to be the first
                        display("No place available");
                        // if no position available send message to previous 
                        // agent and listen previous agent (step 3)
                        ACLMessage message = new ACLMessage(ACLMessage.SUBSCRIBE);
                        message.addReceiver(previousQueen);
                        message.setConversationId("move-queen");
                        message.setContent(c.serialize());
                        this.myAgent.send(message);
                        //step = 3;// step doesn't change
                    }
                    else {
                        display("Placing queen in " + place);
                        
                        // set queen
                        c.setQueen(id, place);
                        lastPosition = place; // save last place
                        
                        if (isLast()) {
                            // end
                            // TODO store the solution and continue as if no 
                            // solution was found
                            step = 5;
                        }
                        else {
                            // send to next queen
                            ACLMessage message = new ACLMessage(ACLMessage.INFORM);
                            message.addReceiver(nextQueen);
                            message.setConversationId("place-queen");

                            // add chest to content
                            message.setContent(c.serialize());
                            this.myAgent.send(message);
                            step = 4; // wait for next queen to ask for a move
                        }
                    }
                }    
                break;
                
                case 4:
                { // ASK TO MOVE
                    // wait for next queen ACLMessage
                    //display("Waiting for next queen message");
                    ACLMessage subscribe = this.myAgent.blockingReceive(MessageTemplate.and(
                            MessageTemplate.MatchConversationId("move-queen"), 
                            MessageTemplate.MatchPerformative(ACLMessage.SUBSCRIBE)
                    ));
                    display("Received \"move-queen\"");
                    
                    // retrieve serial
                    String serial = subscribe.getContent();
                    
                    // build chest
                    c = Chest.fromSerial(serial, n);
                    
                    // find place without taking precious queen into account (but without taking her again)
                    // TODO: list of tested queens
                    //display("LAST POSITION = " + lastPosition);
                    int place = c.findPlaceFromPrevious(id, lastPosition);
                    
                    // 2 options:
                    // - try other position and send to next queen, then listen to next queen (step 4)
                    // - if no position available send message to previous agent and listen previous agent (step 3) => think of removing queen
                    
                    if (place == Chest.QUEEN_NOT_PLACED) {
                        display("No place available");
                        // if no position available send message to previous agent and listen previous agent (step 3)
                        
                        if (isFirst()) {
                            // no solution any more...
                            step = 6;
                        }
                        else {
                            ACLMessage message = new ACLMessage(ACLMessage.SUBSCRIBE);
                            message.addReceiver(previousQueen);
                            message.setConversationId("move-queen");
                            message.setContent(c.serialize());
                            this.myAgent.send(message);
                            step = 3; // wait for previous queen to place queen
                        }
                    }
                    else {
                        // TODO reuse function from step2
                        display("Placing queen in " + place);
                        
                        // set queen
                        c.setQueen(id, place);
                        lastPosition = place; // TODO à voir si ça va là
                        
                        // send to next queen
                        ACLMessage message = new ACLMessage(ACLMessage.INFORM);
                        message.addReceiver(nextQueen);
                        message.setConversationId("place-queen");
                        
                        // add chest to content
                        message.setContent(c.serialize());
                        this.myAgent.send(message);
                        //step = 4; // step doesn't change
                    }
                }    
                break;
            }
        }

        @Override
        public boolean done() {
            if (step == 5) {
                // TODO remove this step, instead store solution and keep going
                // as if no solution was found !
                display("End of solver !");
                display("Solution:");
                c.display();
                return true;
            }
            else if (step == 6) {
                // TODO inform other queens that we're done !
                // TODO gather all solutions found and display them !
                display_error("No solution anymore", false);
                return true;
            }
            return false;
        }
        
    }
    
}
