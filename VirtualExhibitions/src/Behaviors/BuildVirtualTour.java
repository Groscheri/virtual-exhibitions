package Behaviors;
import jade.core.behaviours.OneShotBehaviour;
import java.util.Arrays;

/**
 * BuildVirtualTour behavior class
 */
public class BuildVirtualTour extends OneShotBehaviour {

    private String name;
    private String[] interests; 
    
    public BuildVirtualTour() {
        super();
        this.name = "";
    }

    public BuildVirtualTour(String name, String[] interests) {
        super();
        this.name = name;
        this.interests = new String[interests.length];
        for (int i=0; i<interests.length; i++){
            this.interests[i] = interests[i];
        }
    }
       
    @Override
    public void action() {
        System.out.println("Building virtual tour called " + this.name + "for the following " + Arrays.toString(interests));
    }
    
}
