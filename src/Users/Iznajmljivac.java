package Users;

import java.util.List;

import kartica.Kartica;
import najmovi.Najam;
import vozila.Vozilo;

public class Iznajmljivac extends Korisnik {
	private Kartica nsgoKartica;
	
	public Iznajmljivac(String username,String password,Kartica nsgoKartica) {
		super(username,password);
		this.nsgoKartica=nsgoKartica;
	}
	
	public boolean validnaKartica(Kartica kartica) {
		if(!kartica.jeAktivna()) {
			System.out.println("Kartica nije aktivna");
			return false;
		}else if(kartica.getRaspolozivaSredstva()<=0) {
			System.out.println("Na kartici nema dovoljno sredstava");
			return false;
		}else {
			return true;
		}
		
		//List<>
	}
}
