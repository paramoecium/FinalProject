package com.ticket;

import java.util.Date;

public class Ticket {
	
	private String ticketID = null; //unique ticket number (format: xxxxxx-xxxx-xxx)


	
	//TODO Any instances here
	
	
	//TODO: fill in the constructor
	public Ticket(String ticketID){
		
	}
	
	//TODO: fill in the constructor
	public Ticket(String studentID, Date date, int startStation, int endStation, 
			int trainID){
		
	}
	
	
	//TODO: create the ticket and store the attributes.
	public void create(String studentID, Date date, int startStation, int endStation, 
			int trainID){
		
		
	}
	
	//TODO: please release the seat here.
	public boolean cancel(){
		
		return false;
		
	}
	
	
	//TODO: you should assign the seat for this ticket 
	public void setSeat(){
		
	}
	
	
	public void printTicket(){

		
		/*
		 * TODO:Please print the
		 * 1) Date
		 * 2) Train Type
		 * 3) Train No
		 * 4) Dep Time
		 * 5) Arr Time
		 * 6) Seat Number
		 */
		
	
		
	}
	
	public Seat getSeat(){
		//TODO: return the seat object
		
		return null;
	}
	
	/*
	 * There are only four cars in each train.
	 * And in each car, there are 52seats.
	 * 
	 * The sample seat number is  Car 1-03
	 */
	
	class Seat {
		
		//TODO Any instances here
		
		
		//TODO Any constructor here
		
		
		@Override
		public String toString(){
			//TODO Please return seat information as format "{\2d-\2d}" (e.g., 1-03, 12-03, 10-32)  
			return null;
		}
		
	}
}
