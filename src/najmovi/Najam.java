package najmovi;

import java.util.Date;
import java.util.List;

import Users.Iznajmljivac;
import vozila.Vozilo;
import enumi.DodatnaOprema;

public class Najam {
	private int id;
	//datum i vreme 
	private Date datumPocetka;
	//datum i vreme kraja
	private Date datumKraja;
	private Iznajmljivac iznajmljivac;
	private Vozilo vozilo;
	//dodatna oprema
	private List<DodatnaOprema> dodatneOprema;
	private boolean servis;
	
	public Najam(int id, Date datumPocetka,Date datumKraja, Iznajmljivac iznajmljivac, Vozilo vozilo, boolean servis) {
		this.id=id;
		this.datumPocetka=datumPocetka;
		this.datumKraja=datumKraja;
		this.iznajmljivac=iznajmljivac;
		this.vozilo=vozilo;
		this.servis=servis;
	}
}