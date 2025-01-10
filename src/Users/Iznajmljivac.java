package Users;

import java.util.List;
import java.util.Scanner;

import kartica.Kartica;
import najmovi.Najam;
import vozila.Iznajmljivo;
import vozila.Vozilo;

public class Iznajmljivac extends Korisnik implements Iznajmljivo{
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
	
	@Override
	public void unajmi(List<Vozilo>slobodnaVozila){//izvuci kod iz funkcije slobodnaVozila 
		Scanner odabirVozila=new Scanner(System.in);
		int brojVozila=0;
		
		if(slobodnaVozila.isEmpty()) {
			System.out.println("Trenutno nema slobodnih vozila!");
			return;
		}
		
		System.out.println("Odaberite vozilo koje zelite da iznajmite");
		for(Vozilo v: slobodnaVozila) {
			brojVozila++;
			System.out.println(brojVozila +". "+v.getCenaPoSatu()+v.getMaxTezina());
		}
		int odabranBroj=odabirVozila.nextInt();
		
		if(odabranBroj >1 &&odabranBroj<=slobodnaVozila.size()) {
			Vozilo odabranoVozilo=slobodnaVozila.get(odabranBroj-1);
			odabranoVozilo.postaviNaZauzeto();
			System.out.println("Odabrali ste vozilo: "+odabranoVozilo);
			this.nsgoKartica.isVoziloAktivno(odabranoVozilo);
		}else {
			System.out.println("Neispravan unos pokusajte opet!");
			unajmi(slobodnaVozila);
		}
}
	
	public boolean isActive() {
		return false;
	}
	
	
	public void vrati() {
		
	}
			
	//kreirati funkciju za vracanje vozila
			
	//omoguciti da korisnik koji je iznajmljivac moze da dodaje sredstva u metodi meni za korisnike
			
	//prikaz istorije iznajmljivanja
}
