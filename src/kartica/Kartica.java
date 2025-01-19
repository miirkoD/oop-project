package kartica;

import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import Users.Iznajmljivac;
import vozila.Vozilo;
import najmovi.Najam;

public class Kartica {
	private int id;
	private Iznajmljivac iznajmljivac;
	private LocalDateTime datumOd;
	private LocalDateTime datumDo;
	private double raspolozivaSredstva;
	private Vozilo voziloAktivno;
	
	private List<Najam> istorijaIznajmljivanja=new ArrayList<>();
	
	private static Set<Integer>iskorisceniIDovi=new HashSet<>();
	private static Random random=new Random();
	
	public Kartica(Iznajmljivac iznajmljivac) {
		generisiIDKartice();
		this.iznajmljivac=iznajmljivac;
		this.raspolozivaSredstva=0.0; 
		this.voziloAktivno=null;
		KreirajDatumOd();
		KreirajDatumDo();
	}
	public Kartica(int id, Iznajmljivac iznajmljivac, LocalDateTime datumOd,LocalDateTime datumDo, double raspolozivaSredstva,Vozilo voziloAktivno) {
		this.id=id;
		this.iznajmljivac=iznajmljivac;
		this.datumOd=datumOd;
		this.datumDo=datumDo;
		this.raspolozivaSredstva=raspolozivaSredstva;
		this.voziloAktivno=voziloAktivno;
	}
	
	public List<Najam> getIstorijaIznajmljivanja() {
		return istorijaIznajmljivanja;
	}
	
	private int generisiIDKartice() {
		int id;
		do {
			id=random.nextInt(10000);
		}while(iskorisceniIDovi.contains(id));
		iskorisceniIDovi.add(id);
		return id;
	}
	
	public void dodajUIstoriju(Najam najam) {
		istorijaIznajmljivanja.add(najam);
	}

	private LocalDateTime KreirajDatumOd() {
		LocalDateTime danas=LocalDateTime.now();
		datumOd = danas;
		datumDo=danas.plusYears(1);
		return datumOd;
	}
	private LocalDateTime KreirajDatumDo() {
		LocalDateTime danas=LocalDateTime.now();
		datumDo=danas.plusYears(1);
		return datumDo;
	}
	
	public int getId() {
		return id;
	}
	public boolean jeAktivna() { //metoda koja vraca da li je kartica aktivna
		try {
			return LocalDateTime.now().isBefore(datumDo);
		}catch(DateTimeParseException e) {
			System.out.println("Greska prilikom parsiranja datuma: "+ e.getMessage());
			return false;
		}
	}
	
	public LocalDateTime getDatumDo() {
		return datumDo;
	}
	
	@Override
	public String toString() {
		return "Kartica {Korisnik "+this.iznajmljivac.getUsername()+" Id kartice "+ this.id+"Vazi do "+this.datumDo;
	}
	
	public void isVoziloAktivno(Vozilo v) {
		if(this.voziloAktivno==null) {
			System.out.println("Novo iznajmljivanje vozila na ovom nalogu");
			this.voziloAktivno=v;
		}else {
			this.voziloAktivno=v;
		}
	}

	public void setVoziloAktivno(Vozilo vozilo) {
		this.voziloAktivno=vozilo;
	}

	public LocalDateTime getDatumOd() {
		return datumOd;
	}

	public double getRaspolozivaSredstva() {
		return raspolozivaSredstva;
	}
	
	public double setSredstava(double sredstva) {
		return this.raspolozivaSredstva+=sredstva;
		
	}

	
}
