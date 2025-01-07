package Users;

import vozila.Vozilo;

public class Vlasnik extends Korisnik{
	private Vozilo vozila;
	
	public Vlasnik(String username,String password,Vozilo vozila) {
		super(username,password);
		this.vozila=vozila;
	}
}
