package com.sandeepthukral.tutorial.util;

import java.util.ArrayList;
import java.util.Date;
import java.util.TimeZone;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.text.ParseException;

import android.util.Log;

import com.sandeepthukral.tutorial.VertrekkendeTrein;

public class Utilities {
	
	private static final String TAG = "TreinAlert-Utilities";
	
	public static ArrayList<VertrekkendeTrein> getDepartureDetails(String xml, int numberOfEntries){
		ArrayList<VertrekkendeTrein> arrayList = null;
		//Log.d(TAG, "calling getListOfTrains");
		return getListOfTrains(xml, numberOfEntries);
		
	}
	
	private static ArrayList<VertrekkendeTrein> getListOfTrains(String xml, int numberOfItems) {
		
		InputStream is = new ByteArrayInputStream(xml.getBytes());
		return getListOfTrains(is, numberOfItems);
	}
	
	public static ArrayList<VertrekkendeTrein> getListOfTrains(InputStream is, int numberOfItems) {
		
		ArrayList<VertrekkendeTrein> arrayList = new ArrayList<VertrekkendeTrein>();
		
		try {
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();

			Document doc = dBuilder.parse(is);
			doc.getDocumentElement().normalize();
	 
			NodeList nList = doc.getElementsByTagName("VertrekkendeTrein");
			Log.d(TAG, "got Note list. Size is " + nList.getLength());
			
			int maxItems = ((nList.getLength() >= numberOfItems) ? numberOfItems : nList.getLength());
	 
			for (int temp = 0; temp < maxItems; temp++) {
				Log.d(TAG, "Processing train " + temp);
			   Node nNode = nList.item(temp);
			   if (nNode.getNodeType() == Node.ELEMENT_NODE) {
	 
			      Element eElement = (Element) nNode;
			      
			      VertrekkendeTrein vTrein = new VertrekkendeTrein();
	 
			      vTrein.setRitNummer(getTagValue(VertrekkendeTrein.RITNUMMER, eElement));
			      vTrein.setDelay(getTagValue(VertrekkendeTrein.VERTREKVERTRAGINGTEKST, eElement));
			      vTrein.setVertrekTijd(getTagValue(VertrekkendeTrein.VERTREKTIJD, eElement));
			      vTrein.setEindBestemming(getTagValue(VertrekkendeTrein.EINDBESTEMMING, eElement));
			      vTrein.setTreinSoort(getTagValue(VertrekkendeTrein.TREINSOORT, eElement));
			      vTrein.setVertrekSpoor(getTagValue(VertrekkendeTrein.VERTREKSPOOR, eElement));
			      vTrein.setRouteTekst(getTagValue(VertrekkendeTrein.ROUTETEKST, eElement));
			      
			      //TODO add Opmerkings
			      
			      arrayList.add(vTrein);
	 
			   }
			}
		  } catch (Exception e) {
			e.printStackTrace();
		  }
		
		return arrayList;
	}
	
	private static String getTagValue(String sTag, Element eElement) {
		
		//Log.d(TAG, "Getting value of tag " + sTag);
		NodeList nodeList = eElement.getElementsByTagName(sTag);
		//Log.d(TAG, ""+nodeList.getLength());
		if (nodeList.getLength() != 0){		
			Node n = nodeList.item(0);
			if (n.hasChildNodes()){
				Node nValue = (Node) n.getChildNodes().item(0);
				return nValue.getNodeValue();
			} else 
				return "";
		} else 
			return "";
	}
	 
	public static Date parseDate(String stringDate){
		try{
			SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");
			format.setTimeZone(TimeZone.getTimeZone("Europe/Amsterdam"));
			return format.parse(stringDate);
		} catch (ParseException e){
			return null;
		}
	}
}
