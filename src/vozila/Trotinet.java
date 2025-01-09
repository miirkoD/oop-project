package vozila;

import Users.Vlasnik;
import enumi.Stanje;

public class Trotinet extends Vozilo{
	private int maxBrzina;
	private double trajanjeBaterije;
	
	public Trotinet(int id, Vlasnik vlasnik, double cenaPoSatu, double maxTezina,Stanje stanje,int maxBrzina,double trajanjeBaterije) {
		super(id,vlasnik,cenaPoSatu,maxTezina,stanje);
		this.maxBrzina=maxBrzina;
		this.trajanjeBaterije=trajanjeBaterije;
	}
}
