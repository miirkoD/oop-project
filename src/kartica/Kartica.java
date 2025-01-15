package kartica;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Scanner;
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
	
	private List<Najam> istorijaIznajmljivanja;
	
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
	
	public int generisiIDKartice() {
		int id;
		do {
			id=random.nextInt(10000);
		}while(iskorisceniIDovi.contains(id));
		iskorisceniIDovi.add(id);
		return id;
	}

	public void setIstorijaIznajmljivanja(List<Najam> istorijaIznajmljivanja) {
		this.istorijaIznajmljivanja = istorijaIznajmljivanja;
	}

	public LocalDateTime KreirajDatumOd() {
		LocalDateTime danas=LocalDateTime.now();
		datumOd = danas;
		datumDo=danas.plusYears(1);
		return datumOd;
	}
	public LocalDateTime KreirajDatumDo() {
		LocalDateTime danas=LocalDateTime.now();
		datumDo=danas.plusYears(1);
		return datumDo;
	}
	
	
	
	public int getId() {
		return id;
	}
	public boolean jeAktivna() { //metoda koja vraca da li je kartica aktivna
		DateTimeFormatter formatter= DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");
		try {
			LocalDate datumDoLocalDate=LocalDate.parse(datumDo.toString(),formatter);
			return LocalDate.now().isBefore(datumDoLocalDate);
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
		//Scanner vratiInput=new Scanner(System.in);
		if(this.voziloAktivno==null) {
			System.out.println("Prvo iznajmljivanje vozila na ovom nalogu");
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
	
	//metoda za vracanje sredstava stavljena u klasu kartica
	public double setSredstava(double sredstva) {
		return this.raspolozivaSredstva+=sredstva;
	}

	
}
