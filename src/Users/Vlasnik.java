package Users;

import java.util.List;

import javax.management.modelmbean.XMLParseException;

import enumi.Stanje;
import vozila.UpravljanjeVozilima;
import vozila.Vozilo;

public class Vlasnik extends Korisnik{
	private List <Vozilo> vozila;
	
	public Vlasnik(String username,String password) {
		super(username,password);
		
	}
	public void vanUpotrebe() throws XMLParseException {
		UpravljanjeVozilima upravljanjeVozilima=new UpravljanjeVozilima();
		List<Vozilo>vlasnikovaVozila= upravljanjeVozilima.vlasnikovaVozila(getUsername(), getPassword());
		for(Vozilo vozilo: vlasnikovaVozila) {
			if(vozilo.getStanje()==Stanje.Neupotrebljivo) {
				upravljanjeVozilima.obrisiVozilo(vozilo.getId());
			}
		}
	}
	

	//stavljanje vozila van upotrebe
}
