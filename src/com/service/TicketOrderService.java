package com.service;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.FileOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import java.util.ArrayList;
import java.util.Date;
import java.util.Scanner;
import java.text.SimpleDateFormat;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import com.service.TicketOrderException.ExceptionTYPE;
import com.ticket.Ticket;

//These packages are for xml parser
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;


public class TicketOrderService {
	private final int CAR_NUM_PER_TRAIN = 4; 
	private final int SEAT_NUM_PER_CAR = 52; 
	//TODO Any instances here
	List<TrainInfo> trains = new ArrayList<TrainInfo>();
	Ticket newTicket = null;
	
	protected TicketOrderService(){
		
		/* 
		 *  1) Read the information from time table(timetable.xml) 
		 *  2) Save the information in the  {@code TicketOrderService} 
		 *  
		 */ 
		try {
			File fXmlFile = new File("./data/20140524.xml");
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(fXmlFile);
			
			NodeList nList = doc.getElementsByTagName("TrainInfo");
			System.out.println("# of train:" + nList.getLength());
			
			for (int temp = 0; temp < nList.getLength(); temp++) {
				
				Node nNode = nList.item(temp);
				trains.add(new TrainInfo(nNode));
				
			}
			
		}catch(Exception e){
			
		}
	}
	
	
	/*
	 * You have to use the Real-Time Table
	 */
	
	public List<Ticket> order(String studentID, Date date, String startStation, String endStation, 
			int trainID, int count) throws TicketOrderException {
		List<Ticket> tickets = new ArrayList<Ticket>();
		int[][][] seatTable = null;
		boolean condition1 = true;
		int startStationOrder = 0;
		int endStationOrder = 0;
		SimpleDateFormat sdFormat = new SimpleDateFormat("yyyy-MM-dd");
		//System.out.println(sdFormat.format(date));
		
		//TODO if the tickets are not available or sold out. Please throw the related exception.
		seatTable = checkAvailable(date, new Integer(startStation), new Integer(endStation), trainID, count);
		startStationOrder = seatTable[0][0][0];
		endStationOrder = seatTable[0][0][1];
		int[] i = new int[count];
		int[] j = new int[count];
		int n=0;
		for( ;n<count;n++){
			boolean full = true;
			i[n]=0;
			j[n]=0;
			int k=0;
			OuterLoop:
			for(i[n]=0;i[n]<CAR_NUM_PER_TRAIN;i[n]++){
				for(j[n]=0;j[n]<SEAT_NUM_PER_CAR;j[n]++){
					InnerLoop:
					for(k=1;k<1+endStationOrder-startStationOrder;k++){
						if( ((n>0) && (52*i[n-1]+j[n-1]>=52*i[n]+j[n])) ||
						    (seatTable[k][i[n]][j[n]]!=0)) break InnerLoop;
					}
					if(k==1+endStationOrder-startStationOrder){
						full = false;
						break OuterLoop;
					}
				}
			}
			if(full==true)break;
		}
		if(n==count){
			n = 0;
			int k;
			for( ;n<count;n++){
				for(k=1;k<1+endStationOrder-startStationOrder;k++){
					seatTable[k][i[n]][j[n]] = 1;
				}
				
				newTicket.setSeat(i[n]+1, j[n]+1);
				tickets.add(new Ticket(newTicket));
			}
		}
		else throw new TicketOrderException(ExceptionTYPE.BOOKINGISFULL);
		
		for(int m=startStationOrder;m<endStationOrder;m++)
			writeSeatTable(date, m, trainID,seatTable[m-startStationOrder+1]);
//		if(condition1 == true){
//			throw new  TicketOrderException(ExceptionTYPE.BOOKINGISFULL);	
//		}else if(condition1 == true){
//			throw new  TicketOrderException(ExceptionTYPE.STATATIONID_NOFOUND);
//		}
		
		// .... more exceptions
		
		return tickets;
	}
	
	
	/*
	 * Return ExceptionTYPE. Null means the tickets of the given conditions are available. 
	 */
	
	public int[][][] checkAvailable(Date date, int startStation, int endStation, 
			int trainID, int count) throws TicketOrderException {
		//TODO
		TrainInfo train = null;
		int startStationOrder = 0;
		int endStationOrder = 0;
		int[][][] seatTable;
		for (TrainInfo i : trains) {
			//System.out.println(train);
			if(i.getTrainID() == trainID) {
				train = i;
				break;
			}
		}
		if(train == null) throw new TicketOrderException(ExceptionTYPE.TRAINID_NOFOUND);
		else{
			startStationOrder = train.getOrder(startStation);
			endStationOrder = train.getOrder(endStation);
			seatTable = new int[1+endStationOrder-startStationOrder][][];
			seatTable[0] = new int[1][2];
			seatTable[0][0][0] = startStationOrder;
			seatTable[0][0][1] = endStationOrder;
		}
		if((startStationOrder==0)||(endStationOrder==0)) throw new TicketOrderException(ExceptionTYPE.STATATIONID_NOFOUND);
		else{
			for(int i=startStationOrder;i<endStationOrder;i++)
				seatTable[i-startStationOrder+1] = readSeatTable(date, i, trainID);
			newTicket = new Ticket(date,train,startStationOrder,endStationOrder);
			return seatTable;
		}
	}
	public int[][] readSeatTable(Date date, int order, int trainID){
		int[][] seats = new int[CAR_NUM_PER_TRAIN][SEAT_NUM_PER_CAR];//initialize to false
		//TODO
		SimpleDateFormat sdFormat = new SimpleDateFormat("yyyy-MM-dd");
		//System.out.println(sdFormat.format(date));
		String dirPath = "./data/seatTable/"+trainID+"/"+sdFormat.format(date);
		String filePath = dirPath+"/"+order+".txt";
		
		File path = new File(dirPath);
		if (!path.exists()) {
			if (path.mkdirs()) {
				System.out.println("creating directories: "+dirPath);
			} 
			else {
				System.out.println("Failed to create directories:"+dirPath);
			}
		}
		File readSeatTable = new File(filePath);
		if (!readSeatTable.exists()) {
			System.out.println("creating seat table: " + filePath);
			try{
				readSeatTable.createNewFile();
				PrintWriter outputStreamName = new PrintWriter(new FileOutputStream(filePath));
				for(int i=0;i<CAR_NUM_PER_TRAIN*SEAT_NUM_PER_CAR/4;i++) outputStreamName.println("0 0 0 0");
				outputStreamName.close();
				System.out.println("seat table is created");  
		     } catch (IOException e) {
				e.printStackTrace();
		     }
		}
		Scanner inputStream = null;
		try{
			inputStream = new Scanner(new FileInputStream(filePath));
		}catch(IOException e){
			e.printStackTrace();
		}
		for(int i=0;i<CAR_NUM_PER_TRAIN;i++){
			for(int j=0;j<SEAT_NUM_PER_CAR;j++){
				seats[i][j] = inputStream.nextInt();
			}
		}
		inputStream.close();
		
		return seats;
	}
	public void writeSeatTable(Date date, int order, int trainID,int[][] seatTable){
		//TODO
		SimpleDateFormat sdFormat = new SimpleDateFormat("yyyy-MM-dd");
		//System.out.println(sdFormat.format(date));
		String dirPath = "./data/seatTable/"+trainID+"/"+sdFormat.format(date);
		String filePath = dirPath+"/"+order+".txt";

		try{
			PrintWriter outputStreamName = new PrintWriter(new FileOutputStream(filePath));
			int k = 0;
			for(int i=0;i<CAR_NUM_PER_TRAIN;i++){
				for(int j=0;j<SEAT_NUM_PER_CAR;j++){
					outputStreamName.print(seatTable[i][j]);
					k++;
					if(k%4 ==0) outputStreamName.print("\n");
					else outputStreamName.print(" ");
				}
			}
			outputStreamName.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
