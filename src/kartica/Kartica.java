package kartica;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Date;
import java.util.List;

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
	private List<Najam> istrorijaIznajmljivanja;
	
	public Kartica(int id, Iznajmljivac iznajmljivac, double raspolozivaSredstva) {
		this.id=id;
		this.iznajmljivac=iznajmljivac;
		this.raspolozivaSredstva=raspolozivaSredstva; 
		KreirajDatume();
	}
	
	public void KreirajDatume() {
		LocalDate danas=LocalDate.now();
		datumOd = danas;
		datumDo=danas.plusMonths(1);
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
	
	public void setVoziloAktivno(Vozilo vozilo) {
		this.voziloAktivno=vozilo;
	}

	public double getRaspolozivaSredstva() {
		return raspolozivaSredstva;
	}

	
}
