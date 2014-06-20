package com.service;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
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
	
	//TODO Any instances here
	List<TrainInfo> trains = new ArrayList<TrainInfo>();
	
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
		
		
		boolean condition1 = true;
		
		//TODO if the tickets are not available or sold out. Please throw the related exception.
		
		if(condition1 == true){
			throw new  TicketOrderException(ExceptionTYPE.BOOKINGISFULL);	
		}else if(condition1 == true){
			throw new  TicketOrderException(ExceptionTYPE.STATATIONID_NOFOUND);
		}
		
		// .... more exceptions
		
		return null;
	}
	
	
	/*
	 * Return ExceptionTYPE. Null means the tickets of the given conditions are available. 
	 */
	
	public ExceptionTYPE checkAvailable(String studentID, Date date, int startStation, int endStation, 
			int trainID, int count) {
		
		//TODO
		
		return null;
	}
	
	
	
	
	
}
