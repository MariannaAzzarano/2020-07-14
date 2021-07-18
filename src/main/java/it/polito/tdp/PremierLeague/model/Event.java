package it.polito.tdp.PremierLeague.model;

import java.util.Date;

public class Event implements Comparable<Event>{
	
    public enum EventType{
    	PAREGGIO, 
    	VITTORIA
	}
    private Match match;
    private EventType type;
    
    
    
	@Override
	public int compareTo(Event o) {
		return this.match.getDate().compareTo(o.match.getDate());
	}
	
	
	
    

	

}
