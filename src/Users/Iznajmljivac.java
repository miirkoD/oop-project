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
	//kreirati metode koje su vezane za iznajmljivaca 
	
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
		
	}
	//kreirati funkciju iznajmljivanje vozila
	//public void iznajmiVozilo(List<Vozilo>slobodnaVozila){   izvuci kod iz funkcije slobodnaVozila 
	//ako je vozilo slobodno ispisati ga- podesiti da kako ce se vozila ispisivati(id ,ime vozila, max brzina/broj brzina, max tezina)
	//Scanner odabirVozila = new Scanner(System.in);
	//int idVozila=odabirVozila.nextLine();
	//
	//}
			
	//kreirati funkciju za vracanje vozila
			
	//dopuna sredstava na kartici
			
			//prikaz istorije iznajmljivanja
}
