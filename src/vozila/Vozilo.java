package vozila;

import Users.Vlasnik;

abstract public class Vozilo {
	private int id;
	private  Vlasnik vlasnik;
	private int cenaPoSatu;
	private int maxTezina;
	private Stanje stanje;
	private boolean zauzeto;
}
