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
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import kartica.Kartica;
import najmovi.Najam;
//import najmovi.Najam;
import vozila.Iznajmljivo;
import vozila.UpravljanjeVozilima;
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

	public LocalDateTime datumIVremeTrenutno() {
		LocalDateTime sadaVreme = LocalDateTime.now();
		return sadaVreme;
	}

	public String formatirajVreme(LocalDateTime vreme) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");
		String formiranoVreme = vreme.format(formatter);
		return formiranoVreme;
	}

	@Override
	public Najam unajmi(List<Vozilo> slobodnaVozila) {
		Scanner odabirVozila = new Scanner(System.in);
		Najam najam = null;
		int brojVozila = 0;
		UpravljanjeVozilima upravljanjeVozilima = new UpravljanjeVozilima();

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
				odabranoVozilo.setZauzeto(true); // stavljanje da je vozilo zauzeto
				if (odabranoVozilo.getCenaPoSatu() < this.nsgoKartica.getRaspolozivaSredstva()
						&& this.nsgoKartica.jeAktivna()) {
					System.out.println("Odabrali ste vozilo: " + odabranoVozilo);
					this.nsgoKartica.isVoziloAktivno(odabranoVozilo);
					// potom kreirati novu metodu u kojoj bi prosledili najam i onda dodavali u
					// listu istorija iznajmljivanja
					najam = new Najam(odabranoVozilo.getId(), this.datumIVremeTrenutno(), null, this, odabranoVozilo,
							false);
					upravljanjeVozilima.azurirajXML(odabranoVozilo);//azuriranje vozila.xml
					azurirajKarticu(this.nsgoKartica.getId(),odabranoVozilo.getId());
					break;
				} else {
					System.out.println("Greska proverite da li imate dovoljno sredstava ili vam kartica nije aktivna");
				}
			} else {
				System.out.println("Neispravan unos pokusajte opet!");
			}
		}
		najamUDatoteku(najam);
		// dodati istoriju kasnije
		odabirVozila.close();
		return najam;
	}

	public void azurirajKarticu(int idKartice, int idVozila) {
		try {			
			DocumentBuilderFactory dbFactory=DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder=dbFactory.newDocumentBuilder();
			File kariceFile =new File("Data/kartice.xml");
			
			Document doc =dBuilder.parse(kariceFile);
			NodeList karticeList=doc.getElementsByTagName("kartice");
			
			for(int i=0;i<karticeList.getLength();i++) {
				Node karticaNode=karticeList.item(i);
				
				if(karticaNode.getNodeType()==Node.ELEMENT_NODE) {
					Element karticaElement=(Element)karticaNode;
					int karticaId=Integer.parseInt(karticaElement.getElementsByTagName("id").item(0).getTextContent());
					if(karticaId==idKartice) {
						Element voziloAktivnoElement=(Element) karticaElement.getElementsByTagName("voziloAktivno").item(0);
						voziloAktivnoElement.setTextContent(String.valueOf(idVozila));
						break;
					}
				}
			}
			TransformerFactory transformerFactory=TransformerFactory.newInstance();
			Transformer transformer=transformerFactory.newTransformer();
			DOMSource source=new DOMSource(doc);
			StreamResult result=new StreamResult(new File("Data/kartice.xml"));
			transformer.transform(source,result);
		}catch(Exception e) {
			e.printStackTrace();
		}
	}

	public void najamUDatoteku(Najam najam) {
		try {
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc;

			File file = new File("Data/najam.xml");
			if (file.exists()) {
				doc = dBuilder.parse(file);
			} else {
				doc = dBuilder.newDocument();
				Element rootElement = doc.createElement("najmovi");
				doc.appendChild(rootElement);
			}

			Element najamElement = doc.createElement("najam");

			Element korisnickoImeElement = doc.createElement("korisnickoIme");
			String korisnickoIme = najam.getIznajmljivac().getUsername();
			korisnickoImeElement.appendChild(doc.createTextNode(korisnickoIme));
			najamElement.appendChild(korisnickoImeElement);

			Element tipVozilaElement = doc.createElement("tipVozila");
			String tipVozila = najam.getVozilo().tipVozila();
			tipVozilaElement.appendChild(doc.createTextNode(tipVozila));
			najamElement.appendChild(tipVozilaElement);

			Element idVozilaElement = doc.createElement("IdVozila");
			String idVozilaString = String.valueOf(najam.getVozilo().getId());
			idVozilaElement.appendChild(doc.createTextNode(idVozilaString));
			najamElement.appendChild(idVozilaElement);

			Element cenaPoSatuElement = doc.createElement("cenaPoSatu");
			String cenaPoSatuString = String.valueOf(najam.getVozilo().getCenaPoSatu());
			cenaPoSatuElement.appendChild(doc.createTextNode(cenaPoSatuString));
			najamElement.appendChild(cenaPoSatuElement);

			Element datumKreiranjaNajmaElement = doc.createElement("datumKreiranjNajma");
			String datumKreiranjaNajmaString = formatirajVreme(najam.getDatumPocetka());
			datumKreiranjaNajmaElement.appendChild(doc.createTextNode(datumKreiranjaNajmaString));
			najamElement.appendChild(datumKreiranjaNajmaElement);

			Element datumKrajaNajmaElement = doc.createElement("datumKrajaNajma");
			String datumKrajaNajmaString = "";
			datumKrajaNajmaElement.appendChild(doc.createTextNode(datumKrajaNajmaString));
			najamElement.appendChild(datumKrajaNajmaElement);

			doc.getDocumentElement().appendChild(najamElement);

			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			transformer.transform(new DOMSource(doc), new StreamResult(file));
		} catch (ParserConfigurationException | TransformerException | IOException | SAXException e) {
			e.printStackTrace();
		}
	}

	@Override
	public String toString() {
		return "Iznajmljivac { Korisnicko ime= '" + getUsername() + this.nsgoKartica.toString() + " }";
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
