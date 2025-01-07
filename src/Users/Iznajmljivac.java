package Users;

import kartica.Kartica;

public class Iznajmljivac extends Korisnik {
	private Kartica nsgoKartica;
	
	public Iznajmljivac(String username,String password,Kartica nsgoKartica) {
		super(username,password);
		this.nsgoKartica=nsgoKartica;
	}
}
