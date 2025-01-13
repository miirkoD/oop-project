package Users;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Scanner;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

import kartica.Kartica;
import najmovi.Najam;
//import najmovi.Najam;
import vozila.Iznajmljivo;
import vozila.Vozilo;

public class Iznajmljivac extends Korisnik implements Iznajmljivo {
	private Kartica nsgoKartica;

	public Iznajmljivac(String username, String password, Kartica nsgoKartica) {
		super(username, password);
		this.nsgoKartica = nsgoKartica;
	} 
	public Iznajmljivac(String username, String password) {
        super(username, password);
    }
	// kreirati metode koje su vezane za iznajmljivaca

	public boolean validnaKartica(Kartica kartica) {
		if (!kartica.jeAktivna()) {
			System.out.println("Kartica nije aktivna");
			return false;
		} else if (kartica.getRaspolozivaSredstva() <= 0) {
			System.out.println("Na kartici nema dovoljno sredstava");
			return false;
		} else {
			return true;
		}
	}

	@Override
	public Najam unajmi(List<Vozilo> slobodnaVozila) {// izvuci kod iz funkcije slobodnaVozila
		Scanner odabirVozila = new Scanner(System.in);
		Najam najam = null;
		int brojVozila = 0;

		if (slobodnaVozila.isEmpty()) {
			System.out.println("Trenutno nema slobodnih vozila!");
			odabirVozila.close();
			return null;
		}

		while (true) {
			System.out.println("Odaberite vozilo koje zelite da iznajmite");
			for (Vozilo v : slobodnaVozila) {
				brojVozila++;
				System.out.println(brojVozila + ". " + " Cena po satu " + v.getCenaPoSatu() + " Maksimalna tezina "
						+ v.getMaxTezina());
			}
			
			int odabranBroj = odabirVozila.nextInt();
			
			if (odabranBroj >= 1 && odabranBroj <= slobodnaVozila.size()) {
				Vozilo odabranoVozilo = slobodnaVozila.get(odabranBroj - 1);
				odabranoVozilo.postaviNaZauzeto();
				System.out.println("Odabrali ste vozilo: " + odabranoVozilo);
				this.nsgoKartica.isVoziloAktivno(odabranoVozilo);
				// popom kreirati novu metodu u kojoj bi prosledili najam i onda dodavali u
				// listu istorija iznajmljivanja
				najam = new Najam(odabranoVozilo.getId(), this.datumIVremeTrenutno(), null, this, odabranoVozilo,false);
				break;
			} else {
				System.out.println("Neispravan unos pokusajte opet!");
			}
		}
		odabirVozila.close();
		najamUDatoteku(najam);
		//dodati istoriju kasnije
		//this.nsgoKartica.
		odabirVozila.close();
		return najam;
	}
	
	public LocalDateTime datumIVremeTrenutno() {
		LocalDateTime sadaVreme= LocalDateTime.now();
		return sadaVreme;
	}
	
	public String FormatirajVreme(LocalDateTime vreme) {
		DateTimeFormatter formatter=DateTimeFormatter.ofPattern("dd.MM.yyyy HH.mm");
		String formiranoVreme=vreme.format(formatter);
		return formiranoVreme;
	}
	
	public void najamUDatoteku(Najam najam) {
		try {
			DocumentBuilderFactory dbFactory =DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder=dbFactory.newDocumentBuilder();
			Document doc;
			
			File file=new File("Data/najam.xml");
			if(file.exists()) {
				doc=dBuilder.parse(file);
			}else {
				doc=dBuilder.newDocument();
				Element rootElement=doc.createElement("najmovi");
				doc.appendChild(rootElement);
			}
			
			Element najamElement=doc.createElement("najam");
			
			doc.getDocumentElement().appendChild(najamElement);
			
			TransformerFactory transformerFactory=TransformerFactory.newInstance();
			Transformer transformer=transformerFactory.newTransformer();
			transformer.transform(new DOMSource(doc), new StreamResult(file));
		}catch(ParserConfigurationException | TransformerException| IOException| SAXException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public String toString() {
		return "Iznajmljivac {Korisnicko ime= '"+ getUsername()+ this.nsgoKartica.toString()+" }";
	}
	
	public String tip() {
		return "Iznajmljivac";
	}

	public void dodajUIstoriju(Najam n) {
		
	}

	// kreirati funkciju za vracanje vozila
	public void vrati(Vozilo v) {
		
	}

	// prikaz istorije iznajmljivanja
}
