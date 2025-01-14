package najmovi;

//import java.time.LocalDate;
import java.time.LocalDateTime;
//import java.util.Date;
import java.util.List;

import Users.Iznajmljivac;
import vozila.Vozilo;
import enumi.DodatnaOprema;

public class Najam {
	private int id;
	private LocalDateTime datumPocetka;
	private LocalDateTime datumKraja;
	private Iznajmljivac iznajmljivac;
	private Vozilo vozilo;
	//dodatna oprema
	private List<DodatnaOprema> dodatneOprema;
	private boolean servis;
	
	public Najam(int id, LocalDateTime datumPocetka,LocalDateTime datumKraja, Iznajmljivac iznajmljivac, Vozilo vozilo,boolean servis) {
		this.id=id;
		this.datumPocetka=datumPocetka;
		this.datumKraja=datumKraja;
		this.iznajmljivac=iznajmljivac;
		this.vozilo=vozilo;
		this.servis=servis; //po potrebi vratiti
	}

	public Vozilo getVozilo() {
		return vozilo;
	}

	public LocalDateTime getDatumPocetka() {
		return datumPocetka;
	}

	public LocalDateTime getDatumKraja() {
		return datumKraja;
	}

	public Iznajmljivac getIznajmljivac() {
		return iznajmljivac;
	}
	
}