package vozila;

import Users.Vlasnik;
import enumi.Stanje;

public class Trotinet extends Vozilo{
	private int maxBrzina;
	private int trajanjeBaterije;
	
	public Trotinet(int id, Vlasnik vlasnik, double cenaPoSatu, double maxTezina,Stanje stanje, boolean zauzeto,int maxBrzina,int trajanjeBaterije) {
		super(id,vlasnik,cenaPoSatu,maxTezina,stanje,zauzeto);
		this.maxBrzina=maxBrzina;
		this.trajanjeBaterije=trajanjeBaterije;
	}
}
