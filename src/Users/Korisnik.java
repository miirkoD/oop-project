package Users;

public abstract class Korisnik {

	private String username;
	private String password;
	
	public Korisnik( String username, String password) {
		this.username=username;
		this.password=password;
	}
	
}
