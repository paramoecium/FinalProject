package com.service;
import java.util.ArrayList;
import java.util.List;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;



public class TrainInfo {
	
	private Integer        trainID = null;
	private String        trainType = null;
	private List<TimeInfo> tables = new ArrayList<TimeInfo>();
			
	public TrainInfo(Node item){
		
		Element eElement = (Element) item;
		trainType = eElement.getAttribute("CarClass");
		trainID = new Integer(eElement.getAttribute("Train"));
		NodeList timeinfos = item.getChildNodes();
		
		for (int temp = 0; temp < timeinfos.getLength(); temp++) {
			
			Node nNode = timeinfos.item(temp);
			
			tables.add(new TimeInfo(nNode));
			
		}
	}

	public Integer getTrainID() {
		return trainID;
	}

	public String getTrainType() {
		return trainType;
	}
	public TimeInfo getTimeInfo(int stationOrder){
		for(TimeInfo t : tables){
			if(t.order==stationOrder) return new TimeInfo(t);
		}
		return null;
	}

	
	public class TimeInfo{
		
		private String arrtime = null;
		private String deptime = null;
		public String getArrtime() {
			return arrtime;
		}

		public String getDeptime() {
			return deptime;
		}

		public String getStationID() {
			return stationID;
		}

		private int order = 0;
		private String stationID = null;
		
		public TimeInfo(Node item){
			Element eElement = (Element) item;
			this.arrtime   = eElement.getAttribute("ARRTime");
			this.deptime   = eElement.getAttribute("DEPTime");
			this.order     = Integer.parseInt(eElement.getAttribute("Order"));
			this.stationID = eElement.getAttribute("Station");
		}

		public TimeInfo(TimeInfo t) {
			this.arrtime   = t.arrtime;
			this.deptime   = t.deptime;
			this.order     = t.order;
			this.stationID = t.stationID;
		}
		
		
	}
	//TODO Any method you'd like to 
	public int getOrder(int station){
		int order = 0;
		for (TimeInfo i : tables){
			if(new Integer(i.stationID) == station){order = i.order;}
		}
		return order;
	}
}
