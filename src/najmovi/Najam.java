package najmovi;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

import Users.Iznajmljivac;
import vozila.Vozilo;
import enumi.DodatnaOprema;

public class Najam {
	private int id;
	private LocalDate datumPocetka;
	private LocalDate datumKraja;
	private Iznajmljivac iznajmljivac;
	private Vozilo vozilo;
	//dodatna oprema
	private List<DodatnaOprema> dodatneOprema;
	private boolean servis;
	
	public Najam(int id, LocalDate datumPocetka,LocalDate datumKraja, Iznajmljivac iznajmljivac, Vozilo vozilo,boolean servis) {
		this.id=id;
		this.datumPocetka=datumPocetka;
		this.datumKraja=datumKraja;
		this.iznajmljivac=iznajmljivac;
		this.vozilo=vozilo;
		this.servis=servis; //po potrebi vratiti
	}
}