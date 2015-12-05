/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

import java.io.Serializable;

/**
 *
 * @author Laurentiu
 */
public class AuctionStrategy implements Serializable {
        private int valToSpend;
        private int idStrategy;
        private int nbOffers;
        private int percentage;
        
        public AuctionStrategy(){
            
        }
        
        public AuctionStrategy(int id){
            idStrategy = id;
        }
        
        public AuctionStrategy(int id, int val, int offers, int perc){
            idStrategy = id;
            valToSpend = val;
            nbOffers = offers;
            percentage = perc;
        }
        
        public Boolean applyStrategy(int value, int first, int nbTimes){
            switch(idStrategy){
                case 1:
                    return nbTimes >= nbOffers;
                case 2:
                    return ((value / (float)first * 100) - percentage)<= 0.001;
                case 3:
                    return value <= valToSpend;
                case 4:
                    return value <= valToSpend;
                    
            }
            return false;
        }
        
        public void setValToSpend(int val){
            valToSpend = val;
        }
        
        public void setNbOffers(int val){
            nbOffers = val;
        }
        
        public void setPercentage(int val){
            percentage = val;
        }
        
        public int getIdStrategy(){
            return idStrategy;
        }
    
}
