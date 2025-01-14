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

	public int getBrzine() {
		return brzine;
	}

	public double getVisina() {
		return visina;
	}

	@Override
	public String toString() {
		return"Bicikl{"+
	"Id "+this.getId()+
	"\nCena po satu "+this.getCenaPoSatu()+"\nMaksimalna kilaza "+this.getMaxTezina()+"\nBroj brzina "+this.getBrzine()+"\nVisina bicikla "+this.getVisina()+"}";
	}
	
	
}
