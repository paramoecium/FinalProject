package com.ticket;

import java.util.Date;
import java.util.Scanner;
import java.text.SimpleDateFormat;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.FileOutputStream;
import java.text.DecimalFormat;

import com.service.TrainInfo;

import org.json.*;

public class Ticket {
	private static long newID = 1;
	private String ticketID = null; //unique ticket number (format: xxxxxx-xxxx-xxx)
	private int trainID;
	private String trainType;
	private String date;
	private String deptime;
	private String arrtime;
	private String seat;
	//TODO Any instances here
	
	
	//TODO: fill in the constructor
	public Ticket(String ticketID){
		this(readTicket(ticketID));
	}	
	public Ticket(Ticket ticket){
		deptime = ticket.deptime;
		arrtime = ticket.arrtime;
		trainID = ticket.trainID;
		trainType = ticket.trainType;
		date = ticket.date;
		ticketID = ticket.ticketID;
		seat = ticket.seat;
	}
	public Ticket(Date date,TrainInfo train,int startStationOrder,int endStationOrder){
		deptime = train.getTimeInfo(startStationOrder).getDeptime();
		arrtime = train.getTimeInfo(endStationOrder).getArrtime();
		trainID = train.getTrainID();
		trainType = train.getTrainType();
		SimpleDateFormat sdFormat = new SimpleDateFormat("yyyy-MM-dd");
		this.date = sdFormat.format(date);
	}
	//TODO: fill in the constructor
//	public Ticket(String studentID, Date date, int startStation, int endStation, 
//			int trainID){
//		ticketID = new String(""+newID/10000000+"-"+(newID%10000000)/1000+"-"+(newID%1000));
//		newID = (newID+1)%10000000000000L;
//	}
	public Ticket(JSONObject jsonObj){
		deptime = (String)jsonObj.get("deptime");
		arrtime = (String)jsonObj.get("arrtime");
		trainID = (int) jsonObj.get("trainID");
		trainType = (String)jsonObj.get("trainType");
		date = (String)jsonObj.get("date");
		ticketID = (String)jsonObj.get("ticketID");
		seat = (String)jsonObj.get("seat");
	}
	
	//TODO: create the ticket and store the attributes.
//	public void create(String studentID, Date date, int startStation, int endStation, 
//			int trainID){
//		
//		
//	}

	public String getTicketID() {
		return ticketID;
	}
	//TODO: please release the seat here.
	public boolean cancel(){
		String filePath = "./data/ticketList/"+ticketID+".txt";
		try{
			File path = new File(filePath);
			path.delete();
			return true;
		}
		catch(Exception e) {
			return false;
		}
	}
	
	
	//TODO: you should assign the seat for this ticket 
	public void setSeat(int car,int seatNum){
		seat = new Seat(car,seatNum).toString();
		DecimalFormat df0 = new DecimalFormat("000000");
		DecimalFormat df1 = new DecimalFormat("0000");
		DecimalFormat df2 = new DecimalFormat("000");
		ticketID = new String(""+df0.format(newID/10000000)+"-"+df1.format((newID%10000000)/1000)+"-"+df2.format(newID%1000));
		newID = (newID+1)%10000000000000L;
		writeTicket(this);
	}
	@Override
//	public boolean equals(Object obj) {
//		Ticket ticket = (Ticket)obj;
//		return 	(deptime == ticket.deptime)&&
//				(arrtime == ticket.arrtime)&&
//				(trainID == ticket.trainID)&&
//				(trainType == ticket.trainType)&&
//				(date == ticket.date)&&
//				(ticketID == ticket.ticketID)&&
//				(seat == ticket.seat);
//	}
	public boolean equals(Object obj) {
		Ticket ticket = (Ticket)obj;
		 	if(!deptime.equals(ticket.deptime))return false;
		 	if(!arrtime.equals(ticket.arrtime))return false;
		 	if(trainID != ticket.trainID)return false;
		 	if(!trainType.equals(ticket.trainType))return false;
		 	if(!date.equals(ticket.date))return false;
		 	if(!ticketID.equals(ticket.ticketID))return false;
		 	if(!seat.equals(ticket.seat))return false;
		 	
		 return true;	
	}

//	public boolean equals(Ticket ticket){
//		return 	(deptime == ticket.deptime)&&
//				(arrtime == ticket.arrtime)&&
//				(trainID == ticket.trainID)&&
//				(trainType == ticket.trainType)&&
//				(date == ticket.date)&&
//				(ticketID == ticket.ticketID)&&
//				(seat == ticket.seat);
//	}
	
	public void printTicket(){
		String filePath = "./data/ticketList/"+ticketID+".txt";
		File path = new File(filePath);
		if(path.exists()){
			/*
			 * TODO:Please print the
			 * 1) Date
			 * 2) Train Type
			 * 3) Train No
			 * 4) Dep Time
			 * 5) Arr Time
			 * 6) Seat Number
			 */
			System.out.println("¢z¢w¢w¢w¢w¢w¢w¢w¢w¢w¢w¢w¢w¢w¢w¢w¢w¢w¢w¢w¢w¢{");
			System.out.println("¢xDate: "+ date+ "    ¢x");
			System.out.println("¢xTrain Type: "+trainType+"    ¢x");
			System.out.println("¢xTrain No: "+trainID+"      ¢x");
			System.out.println("¢xDep Time: "+deptime+"  ¢x");
			System.out.println("¢xArr Time: "+arrtime+"  ¢x");
			System.out.println("¢xSeatr: "+seat+"     ¢x");
			System.out.println("¢x     " + ticketID +"¢x");
			System.out.println("¢|¢w¢w¢w¢w¢w¢w¢w¢w¢w¢w¢w¢w¢w¢w¢w¢w¢w¢w¢w¢w¢}");
		}
		else{
			System.out.println("null");
		}		
	}
	public static boolean writeTicket(Ticket ticket){
		String dirPath = "./data/ticketList";
		String filePath = dirPath+"/"+ticket.ticketID+".txt";
		
		File path = new File(dirPath);
		if (!path.exists()) {
			if (path.mkdirs()) {
				System.out.println("creating directories: "+dirPath);
			} 
			else {
				System.out.println("Failed to create directories:"+dirPath);
			}
		}
		try{
			PrintWriter outputStreamName = new PrintWriter(new FileOutputStream(filePath));
			JSONObject jsonTicket = new JSONObject();
			jsonTicket.put("seat", ticket.seat);
			jsonTicket.put("arrtime", ticket.arrtime);
			jsonTicket.put("deptime", ticket.deptime);
			jsonTicket.put("trainID", ticket.trainID);
			jsonTicket.put("trainType", ticket.trainType);
			jsonTicket.put("date", ticket.date);
			jsonTicket.put("ticketID", ticket.ticketID);
			
			outputStreamName.println(jsonTicket.toString());
			outputStreamName.close();
			return true;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return false;
	}

	public static Ticket readTicket(String ticketID){
		String filePath = "./data/ticketList/"+ticketID+".txt";
		Scanner inputStream = null;
		try{
			inputStream = new Scanner(new FileInputStream(filePath));
		}catch(IOException e){
			e.printStackTrace();
			return null;
		}
		String jsonStr = inputStream.nextLine();
		inputStream.close();
		JSONObject jsonTicket = new JSONObject(jsonStr);
		return new Ticket(jsonTicket);
	}
	
//	public Seat getSeat(){
//		//TODO: return the seat object
//		
//		
//	}
	
	/*
	 * There are only four cars in each train.
	 * And in each car, there are 52seats.
	 * 
	 * The sample seat number is  Car 1-03
	 */
	
	class Seat {
		
		//TODO Any instances here
		private int car;
		private int seat;
		
		//TODO Any constructor here
		
		public Seat(int car,int seat){
			this.car = car;
			this.seat = seat;
		}
		@Override
		public String toString(){
			//TODO Please return seat information as format "{\2d-\2d}" (e.g., 1-03, 12-03, 10-32)  
			return "Car "+car+"-"+seat/10+seat%10;
		}
		
	}
}
