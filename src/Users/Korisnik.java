package Users;

public abstract class Korisnik {
	private String username;
	private String password;
	
	public Korisnik( String username, String password) {
		this.username=username;
		this.password=password;
	}

	public String getUsername() {
		return username;
	}

	public String getPassword() {
		return password;
	}
	
	
	
	//pretraga vozila po tipu, po zauzetosti
	
}
