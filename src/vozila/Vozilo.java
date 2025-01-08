package vozila;

import Users.Vlasnik;
import enumi.Stanje;

abstract public class Vozilo {
	private int id;
	private  Vlasnik vlasnik;
	private double cenaPoSatu;
	private double maxTezina;
	private Stanje stanje;
	private boolean zauzeto;
	
	public Vozilo(int id, Vlasnik vlasnik, double cenaPoSatu, double maxTezina,Stanje stanje, boolean zauzeto) {
		this.id=id;
		this.vlasnik=vlasnik;
		this.cenaPoSatu=cenaPoSatu;
		this.maxTezina=maxTezina;
		this.stanje=stanje;
		this.zauzeto=zauzeto;
	}
}
