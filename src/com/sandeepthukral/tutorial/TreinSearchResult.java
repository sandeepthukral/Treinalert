package com.sandeepthukral.tutorial;

import java.util.ArrayList;

public class TreinSearchResult {
	
	private String stationName;
	private long searchDate;
	private ArrayList<VertrekkendeTrein> treins;
	
	
	public TreinSearchResult() {
		super();
		treins=new ArrayList<VertrekkendeTrein>();
	}


	public String getStationName() {
		return stationName;
	}


	public void setStationName(String stationName) {
		this.stationName = stationName;
	}


	public long getSearchDate() {
		return searchDate;
	}


	public void setSearchDate(long searchDate) {
		this.searchDate = searchDate;
	}


	public ArrayList<VertrekkendeTrein> getTreins() {
		return treins;
	}


	public void setTreins(ArrayList<VertrekkendeTrein> treins) {
		this.treins = treins;
	}
	
}
