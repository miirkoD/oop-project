package kartica;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

import Users.Iznajmljivac;
import vozila.Vozilo;
import najmovi.Najam;

public class Kartica {
	private int id;
	private Iznajmljivac iznajmljivac;
	private LocalDate datumOd;
	private LocalDate datumDo;
	private double raspolozivaSredstva;
	private Vozilo voziloAktivno;
	private List<Najam> istorijaIznajmljivanja;
	
	public Kartica(int id, Iznajmljivac iznajmljivac) {
		this.id=id;
		this.iznajmljivac=iznajmljivac;
		this.raspolozivaSredstva=0.0; 
		this.voziloAktivno=null;
		KreirajDatume();
	}
	
	public List<Najam> getIstorijaIznajmljivanja() {
		return istorijaIznajmljivanja;
	}

	public void setIstorijaIznajmljivanja(List<Najam> istorijaIznajmljivanja) {
		this.istorijaIznajmljivanja = istorijaIznajmljivanja;
	}

	public void KreirajDatume() {
		LocalDate danas=LocalDate.now();
		datumOd = danas;
		//datumDo=danas.plusMonths(1);
	}
	
	public boolean jeAktivna() {
		DateTimeFormatter formatter= DateTimeFormatter.ofPattern("dd.mm.yyyy");
		try {
			//LocalDate datumOdLocalDate= LocalDate.parse(datumOd.toString(),formatter);
			LocalDate datumDoLocalDate=LocalDate.parse(datumDo.toString(),formatter);
			return LocalDate.now().isBefore(datumDoLocalDate);
		}catch(DateTimeParseException e) {
			System.out.println("Greska prilikom parsiranja datuma: "+ e.getMessage());
			return false;
		}
		
	}
	
	public void isVoziloAktivno(Vozilo v) {
		//Scanner vratiInput=new Scanner(System.in);
		if(this.voziloAktivno==null) {
			System.out.println("Nema aktivnih vozila na ovom nalogu");
			this.voziloAktivno=v;
		}else {
			System.out.println("Trenutno ne mozete iznajmiti vozilo jer niste vratili prethodno!");
			//System.out.println("Da li zelite da vratite vozilo? \n 1.Da \n 2.Ne");
		}
	}

	public void setVoziloAktivno(Vozilo vozilo) {
		this.voziloAktivno=vozilo;
	}

	public LocalDate getDatumOd() {
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
