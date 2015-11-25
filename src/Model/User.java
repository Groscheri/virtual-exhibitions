package Model;

import java.util.ArrayList;

/**
 * User model class
 */
public class User {
    
    public final String MALE = "M";
    public final String FEMALE = "F";
    
    protected int age;
    protected String occupation;
    protected String gender;
    protected ArrayList<String> interest;
    protected ArrayList<Item> visitedItems;
    
    User() {
       this.age = 22;
       this.occupation = "Student";
       this.gender = this.MALE;
       this.interest = new ArrayList<>();
       this.visitedItems = new ArrayList<>();
    }
    
    User(int age, String occupation, String gender) {
        this.age = age;
        
        if (!checkGender(gender)) {
            this.gender = this.MALE;
        } else {
            this.gender = gender;
        }
        
        this.occupation = occupation;
        this.interest = new ArrayList<>();
        this.visitedItems = new ArrayList<>();
    }
    
    public boolean addInterest(String i) {
        return this.interest.add(i);
    }
    
    public boolean removeInterest(String i) {
        return this.interest.remove(i);
    }
    
    public boolean addVisitedItem(Item i) {
        return this.visitedItems.add(i);
    }
    
    private boolean checkGender(String gender) {
        if (gender.equals(this.MALE) || gender.equals(this.FEMALE)) {
            return true;
        }
        return false;
    }
}
