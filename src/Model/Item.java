/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

import java.util.Date;

/**
 * Item model class
 * Artifact
 */
public class Item {
    
    protected Long id;
    protected String name;
    protected String creator;
    protected Date creation;
    protected String place;
    protected String genre;
    
    public Item() {
        this.id = 0L; // TODO generate random uuid4
        this.name = "";
        this.creator = "";
        this.creation = new Date();
        this.place = "";
        this.genre = "";
    }
    
    public Item(String name, String genre) {
        this.id = 0L; // TODO generate random uuid4
        this.name = name;
        this.creator = "";
        this.creation = new Date();
        this.place = "";
        this.genre = genre;
    }
    
    public Item(Long id, String name, String creator, String genre) {
        this.id = id;
        this.name = name;
        this.creator = creator;
        this.creation = new Date();
        this.place = "";
        this.genre = genre;
    }

    public long getId() {
        return this.id;
    }
    
    public String getName() {
        return this.name;
    }

    public String getCreator() {
        return this.creator;
    }

    public Date getCreation() {
        return this.creation;
    }

    public String getPlace() {
        return this.place;
    }

    public String getGenre() {
        return this.genre;
    }
    
    
    
}
