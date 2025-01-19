package najmovi;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import Users.Iznajmljivac;
import vozila.Vozilo;
import enumi.DodatnaOprema;

public class Najam {
	private int id;
	private LocalDateTime datumPocetka;
	private LocalDateTime datumKraja;
	private Iznajmljivac iznajmljivac;
	private Vozilo vozilo;
	private List<DodatnaOprema> dodatneOprema;
	private boolean servis;
	
	private static Set<Integer>iskorisceniIDovi=new HashSet<>();
	private static Random random=new Random();
	
	public Najam(LocalDateTime datumPocetka,LocalDateTime datumKraja, Iznajmljivac iznajmljivac, Vozilo vozilo,boolean servis) {
		generisiIDNajma();
		this.datumPocetka=datumPocetka;
		this.datumKraja=datumKraja;
		this.iznajmljivac=iznajmljivac;
		this.vozilo=vozilo;
		this.servis=servis; //po potrebi vratiti
	}
	
	public int generisiIDNajma() {
		int id;
		do {
			id=random.nextInt(10000);
		}while(iskorisceniIDovi.contains(id));
		iskorisceniIDovi.add(id);
		return id;
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

	public int getId() {
		return this.id;
	}
	
}