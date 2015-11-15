package Behaviors;
import jade.core.behaviours.OneShotBehaviour;

/**
 * BuildVirtualTour behavior class
 */
public class BuildVirtualTour extends OneShotBehaviour {

    protected String name;
    
    public BuildVirtualTour() {
        super();
        this.name = "";
    }

    public BuildVirtualTour(String name) {
        super();
        this.name = name;
    }
       
    @Override
    public void action() {
        System.out.println("Building virtual tour called " + this.name + "!");
    }
    
}
