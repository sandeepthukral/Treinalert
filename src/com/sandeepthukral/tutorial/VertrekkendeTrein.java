package com.sandeepthukral.tutorial;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.sandeepthukral.tutorial.util.Utilities;

public class VertrekkendeTrein {
	
	private String ritNummer;
	private Date vertrekTijd;
	private String eindBestemming;
	private String treinSoort;
	private String routeTekst;
	private String VertrekSpoor;
	private boolean wijziging;
	private String delay ;
	
	public static final String RITNUMMER = "RitNummer";
	public static final String VERTREKTIJD = "VertrekTijd";
	public static final String EINDBESTEMMING = "EindBestemming";
	public static final String TREINSOORT = "TreinSoort";
	public static final String ROUTETEKST = "RouteTekst";
	public static final String VERVOERDER = "Vervoerder";
	public static final String VERTREKSPOOR = "VertrekSpoor";
	public static final String WIJZIGING = "wijziging";
	public static final String VERTREKVERTRAGINGTEKST = "VertrekVertragingTekst";
	public static final String OPMERKINGEN = "Opmerkingen";
	public static final String OPMERKING = "Opmerking";
	
	
	public VertrekkendeTrein (){	
	}

	public VertrekkendeTrein(String ritNummer, String vertrekTijd,
			String eindBestemming, String treinSoort, String routeTekst,
			String vertrekSpoor, boolean wijziging) {
		super();
		this.ritNummer = ritNummer;
		if (Utilities.parseDate(vertrekTijd) != null){
			this.vertrekTijd = Utilities.parseDate(vertrekTijd);
		}
		this.eindBestemming = eindBestemming;
		this.treinSoort = treinSoort;
		this.routeTekst = routeTekst;
		VertrekSpoor = vertrekSpoor;
		this.wijziging = wijziging;
	}

	public String getRitNummer() {
		return ritNummer;
	}

	public void setRitNummer(String ritNummer) {
		this.ritNummer = ritNummer;
	}

	public String getVertrekTijd() {
		return vertrekTijd.toLocaleString();
	}
	
	public String getVertrekTijdShort(){
		SimpleDateFormat format = new SimpleDateFormat("kk:mm");
		return format.format(this.vertrekTijd);
	}

	public void setVertrekTijd(String vertrekTijd) {
		if (Utilities.parseDate(vertrekTijd) != null){
			this.vertrekTijd = Utilities.parseDate(vertrekTijd);
		}
	}

	public String getEindBestemming() {
		return eindBestemming;
	}

	public void setEindBestemming(String eindBestemming) {
		this.eindBestemming = eindBestemming;
	}

	public String getTreinSoort() {
		return treinSoort;
	}

	public void setTreinSoort(String treinSoort) {
		this.treinSoort = treinSoort;
	}

	public String getRouteTekst() {
		return routeTekst;
	}

	public void setRouteTekst(String routeTekst) {
		this.routeTekst = routeTekst;
	}

	public String getVertrekSpoor() {
		return VertrekSpoor;
	}

	public void setVertrekSpoor(String vertrekSpoor) {
		VertrekSpoor = vertrekSpoor;
	}

	public boolean isWijziging() {
		return wijziging;
	}

	public void setWijziging(boolean wijziging) {
		this.wijziging = wijziging;
	}

	public String getDelay() {
		return delay;
	}

	public void setDelay(String delay) {
		String[] strings=delay.split(" ");
		this.delay = strings[0];
	}
}
