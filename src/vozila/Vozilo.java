package vozila;


import Users.Vlasnik;
import enumi.Stanje;

abstract public class Vozilo {
	private int id;
	private Vlasnik vlasnik;
	private double cenaPoSatu;
	private double maxTezina;
	private Stanje stanje;
	private boolean zauzeto;

	public Vozilo(int id, Vlasnik vlasnik, double cenaPoSatu, double maxTezina, Stanje stanje) {
		this.id = id;
		this.vlasnik = vlasnik;
		this.cenaPoSatu = cenaPoSatu;
		this.maxTezina = maxTezina;
		this.stanje = stanje;
		this.zauzeto = false;
	}

	public int getId() {
		return this.id;
	}

	public String tipVozila() {
		if (this instanceof Bicikl) {
			return "bicikl";
		} else if (this instanceof Trotinet) {
			return "trotinet";
		} else {
			return "Nepoznato vozilo";
		}
	}

	public Stanje getStanje() {
		return stanje;
	}

	public void setZauzeto(boolean zauzeto) {
		this.zauzeto = zauzeto;
		UpravljanjeVozilima upravljanjeVozilima=new UpravljanjeVozilima();
		upravljanjeVozilima.voziloSlobodno(this);
	}

	public boolean isZauzeto() {
		return this.zauzeto;
	}

	public double getCenaPoSatu() {
		return cenaPoSatu;
	}

	public double getMaxTezina() {
		return maxTezina;
	}

	public Vlasnik getVlasnik() {
		return vlasnik;
	}


	// pretraga se moze vrsiti preko tipa, zauzetorsti, ili po servisu

}
