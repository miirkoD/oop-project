package Users;

import java.util.List;

import enumi.Stanje;
import vozila.UpravljanjeVozilima;
import vozila.Vozilo;

public class Vlasnik extends Korisnik{
	private List <Vozilo> vozila;
	
	public Vlasnik(String username,String password) {
		super(username,password);
		
	}
	public void vanUpotrebe() {
		UpravljanjeVozilima upravljanjeVozilima=new UpravljanjeVozilima();
		List<Vozilo>vlasnikovaVozila= upravljanjeVozilima.vlasnikovaVozila(getUsername(), getPassword());
		for(Vozilo vozilo: vlasnikovaVozila) {
			if(vozilo.getStanje()==Stanje.Neupotrebljivo) {
				// treba da se obrise
			}
		}
	}
	//stavljanje vozila van upotrebe
}
