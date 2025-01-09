package vozila;

import Users.Vlasnik;
import enumi.Stanje;

public class Bicikl extends Vozilo{
	private int brzine;
	private double visina;
	
	public Bicikl(int id, Vlasnik vlasnik, double cenaPoSatu, double maxTezina,Stanje stanje,int brzine,double visina) {
		super(id,vlasnik,cenaPoSatu,maxTezina,stanje);
		this.brzine=brzine;
		this.visina=visina;
	}
}
