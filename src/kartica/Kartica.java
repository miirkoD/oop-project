package kartica;

import java.util.Date;
import java.util.List;

import Users.Iznajmljivac;
import vozila.Vozilo;
import najmovi.Najam;

public class Kartica {
	private int id;
	private Iznajmljivac iznajmljivac;
	private Date datumOd;
	private Date datumDo;
	private double raspolozivaSredstva;
	private Vozilo voziloAktivno;
	private List<Najam> istrorijaIznajmljivanja;
	
	
	public Kartica(int id, Iznajmljivac iznajmljivac,Date datumOd,Date datumDo, double raspolozivaSredstva) {
		this.id=id;
		this.iznajmljivac=iznajmljivac;
		this.datumOd=datumOd;
		this.datumDo=datumDo;
		this.raspolozivaSredstva=raspolozivaSredstva; 
	}
}
