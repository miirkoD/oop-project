package Users;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import javax.management.modelmbean.XMLParseException;
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
import platforma.Platforma;
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

	private String formatirajVreme(LocalDateTime vreme) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");
		String formiranoVreme = vreme.format(formatter);
		return formiranoVreme;
	}

	@Override
	public Najam unajmi(List<Vozilo> slobodnaVozila) throws XMLParseException {
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
				System.out.println(brojVozila + ". "+v.tipVozila() + " Cena po satu " + v.getCenaPoSatu() + " Maksimalna tezina "
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
					LocalDateTime trenutnoVreme=LocalDateTime.now();
					najam = new Najam(trenutnoVreme, null, this, odabranoVozilo,false);
					upravljanjeVozilima.azurirajXML(odabranoVozilo);// azuriranje vrednosti zauzeto u vozila.xml
					azurirajKarticuAktivnoVozilo(this.nsgoKartica.getId(), odabranoVozilo.getId());
					break;
				} else {
					System.out.println("Greska proverite da li imate dovoljno sredstava ili vam kartica nije aktivna");
				}
			} else {
				System.out.println("Neispravan unos pokusajte opet!");
			}
		}
		najamUDatoteku(najam);
		this.nsgoKartica.dodajUIstoriju(najam);
		odabirVozila.close();
		Platforma.meniZaKorisnika(this);
		return najam;
	}

	private void azurirajKarticuAktivnoVozilo(int idKartice, int idVozila) {
		try {
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			File kariceFile = new File("Data/kartice.xml");

			Document doc = dBuilder.parse(kariceFile);
			NodeList karticeList = doc.getElementsByTagName("kartice");

			for (int i = 0; i < karticeList.getLength(); i++) {
				Node karticaNode = karticeList.item(i);

				if (karticaNode.getNodeType() == Node.ELEMENT_NODE) {
					Element karticaElement = (Element) karticaNode;
					int karticaId = Integer
							.parseInt(karticaElement.getElementsByTagName("id").item(0).getTextContent());
					if (karticaId == idKartice) {
						Element voziloAktivnoElement = (Element) karticaElement.getElementsByTagName("voziloAktivno")
								.item(0);
						voziloAktivnoElement.setTextContent(String.valueOf(idVozila));
						break;
					}
				}
			}
			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			DOMSource source = new DOMSource(doc);
			StreamResult result = new StreamResult(new File("Data/kartice.xml"));
			transformer.transform(source, result);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	private void azurirajKarticuSredstva(int idKartice, double novaRaspolozivaSredstva) {
		try {
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			File kariceFile = new File("Data/kartice.xml");

			Document doc = dBuilder.parse(kariceFile);
			NodeList karticeList = doc.getElementsByTagName("kartice");

			for (int i = 0; i < karticeList.getLength(); i++) {
				Node karticaNode = karticeList.item(i);

				if (karticaNode.getNodeType() == Node.ELEMENT_NODE) {
					Element karticaElement = (Element) karticaNode;
					int karticaId = Integer
							.parseInt(karticaElement.getElementsByTagName("id").item(0).getTextContent());
					if (karticaId == idKartice) {
						if (karticaId == idKartice) {
							Element sredstvaElement = (Element) karticaElement.getElementsByTagName("raspolozivaSredstva").item(0);
							sredstvaElement.setTextContent(String.valueOf(novaRaspolozivaSredstva));
							break;
						}
					}
				}
			}
			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			DOMSource source = new DOMSource(doc);
			StreamResult result = new StreamResult(new File("Data/kartice.xml"));
			transformer.transform(source, result);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void najamUDatoteku(Najam najam) {
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
			
			Element idNajmaElement=doc.createElement("idNajma");
			String idNajam=Integer.toString(najam.getId());
			idNajmaElement.appendChild(doc.createTextNode(idNajam));
			najamElement.appendChild(idNajmaElement);

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

			Element servisElement = doc.createElement("servis");
			String servisString = "false";
			servisElement.appendChild(doc.createTextNode(servisString));
			najamElement.appendChild(servisElement);

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
	
	public void proveriSredstva() {
		System.out.println("Imate "+this.nsgoKartica.getRaspolozivaSredstva() +" sredstava");
	}

	public void dodajSredstva() throws XMLParseException {
		System.out.println("Unesite koliko sredstava zelite da dodate");

		Scanner sredstvaInput = new Scanner(System.in);
		double novaSredstva = 0.0;
		int idKartice = this.nsgoKartica.getId();
		boolean karticaPronadjena = false;

		double sredstva = sredstvaInput.nextInt();
		novaSredstva = this.nsgoKartica.setSredstava(sredstva);

		if (novaSredstva > 0) {
			try {
				DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
				DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
				File kariceFile = new File("Data/kartice.xml");

				Document doc = dBuilder.parse(kariceFile);
				NodeList karticeList = doc.getElementsByTagName("kartice");

				for (int i = 0; i < karticeList.getLength(); i++) {
					Node karticaNode = karticeList.item(i);

					if (karticaNode.getNodeType() == Node.ELEMENT_NODE) {
						Element karticaElement = (Element) karticaNode;
						int karticaId = Integer
								.parseInt(karticaElement.getElementsByTagName("id").item(0).getTextContent());
						if (karticaId == idKartice) {
							karticaPronadjena = true;
							Element sredstvaElement = (Element) karticaElement
									.getElementsByTagName("raspolozivaSredstva").item(0);
							sredstvaElement.setTextContent(String.valueOf(novaSredstva));
							break;
						}
					}
				}
				if (!karticaPronadjena) {
					System.out.println("Kartica sa ID-om " + idKartice + " nije pronadjena");
				}
				TransformerFactory transformerFactory = TransformerFactory.newInstance();
				Transformer transformer = transformerFactory.newTransformer();
				DOMSource source = new DOMSource(doc);
				StreamResult result = new StreamResult(new File("Data/kartice.xml"));
				transformer.transform(source, result);
				System.out.print("Sredstva su uneta na karticu "+ this.nsgoKartica.getRaspolozivaSredstva());
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		sredstvaInput.close();
		Platforma.meniZaKorisnika(this);
	}

	private List<Vozilo> iznajmljenaVozila() throws XMLParseException {
		UpravljanjeVozilima upravljanjeVozilima = new UpravljanjeVozilima();
		List<Vozilo> svaVozila = upravljanjeVozilima.ucitajVozila();

		List<Vozilo> iznajmljenaVozila = new ArrayList<Vozilo>();

		int idVozilaNajam = 0;

		try {
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc;
			File fileNajam = new File("Data/najam.xml");

			doc = dBuilder.parse(fileNajam);

			NodeList najamList = doc.getElementsByTagName("najmovi");

			for (int i = 0; i < najamList.getLength(); i++) {
				Node najamNode = najamList.item(i);

				if (najamNode.getNodeType() == Node.ELEMENT_NODE) {
					Element najamElement = (Element) najamNode;
					idVozilaNajam = Integer.parseInt(najamElement.getElementsByTagName("IdVozila").item(0).getTextContent());

				}
				for (Vozilo v : svaVozila) {
					if (v.getId() == idVozilaNajam) {
						iznajmljenaVozila.add(v);
					}
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return iznajmljenaVozila;
	}

	private double izracunajCenuNajma(double cenaPoSatu) {
		LocalDateTime datumPocetka=null;
		LocalDateTime vremeKraja=LocalDateTime.now();
		try {
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc;
			File fileNajam = new File("Data/najam.xml");
			doc = dBuilder.parse(fileNajam);

			NodeList najamList = doc.getElementsByTagName("najmovi");
			
			for (int i = 0; i < najamList.getLength(); i++) {
				Node najamNode = najamList.item(i);

				if (najamNode.getNodeType() == Node.ELEMENT_NODE) {
					Element najamElement = (Element) najamNode;
					String datumString=najamElement.getElementsByTagName("datumKreiranjNajma").item(0).getTextContent();
					DateTimeFormatter formatter=DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");
					datumPocetka=LocalDateTime.parse(datumString,formatter);
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
			return -1;
		}
		if(datumPocetka!=null) {
			long trajanjeNajmaUSatima=ChronoUnit.HOURS.between(datumPocetka, vremeKraja);
			return cenaPoSatu*trajanjeNajmaUSatima;
		}else {
			return -1;
		}
		
	}

	private void postaviKranjeVreme() {
		String korisnickoIme=this.getUsername();
		LocalDateTime vremeKraja=LocalDateTime.now();
		String formatiranoVreme=formatirajVreme(vremeKraja);
		//formiratiVreme
		try {
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			File najamFile = new File("Data/najam.xml");

			Document doc = dBuilder.parse(najamFile);
			NodeList najamList = doc.getElementsByTagName("najmovi");

			for (int i = 0; i < najamList.getLength(); i++) {
				Node najamNode = najamList.item(i);

				if (najamNode.getNodeType() == Node.ELEMENT_NODE) {
					Element najamElement = (Element) najamNode;
					
					String korisnickoImeNajam = najamElement.getElementsByTagName("korisnickoIme").item(0).getTextContent();
					
					if (korisnickoImeNajam.equals(korisnickoIme)) {
							Element datumKrajaElement = (Element) najamElement.getElementsByTagName("datumKrajaNajma").item(0);
							datumKrajaElement.setTextContent(String.valueOf(formatiranoVreme));
							break;
					}
				}
			}
			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			DOMSource source = new DOMSource(doc);
			StreamResult result = new StreamResult(new File("Data/najam.xml"));
			transformer.transform(source, result);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void vrati() throws XMLParseException {
		//UpravljanjeVozilima upravljanjeVozilima = new UpravljanjeVozilima();
		List<Vozilo> iznajmljenaVozilaList = iznajmljenaVozila();
		
		Vozilo izabranoVozilo = null;

		Scanner izborInput = new Scanner(System.in);

		int brojIznajmljenihVozila = 0;

		System.out.println("Izaberite vozilo koje zelite da vratite");

		for (int i = 0; i < iznajmljenaVozilaList.size(); i++) {
			brojIznajmljenihVozila++;
			System.out.println(brojIznajmljenihVozila + ". " + iznajmljenaVozilaList.get(i));
		}
		int izborInt = izborInput.nextInt() - 1;

		if (izborInt >= 0 && izborInt < iznajmljenaVozilaList.size()) {
			izabranoVozilo = iznajmljenaVozilaList.get(izborInt);
			System.out.println(izabranoVozilo);
		} else {
			System.out.println("Neispravan unos");
		}
		izabranoVozilo.setZauzeto(false);
		double cenaPoSatu=izabranoVozilo.getCenaPoSatu();
		
		double cenaNajma=izracunajCenuNajma(cenaPoSatu);
		
		double novaRaspolozivaSredstva= this.nsgoKartica.getRaspolozivaSredstva()- cenaNajma;
		
		if(novaRaspolozivaSredstva<=0) {
			System.out.print("Nemate sredstava za sledeca iznajmljivanja sve dok ne dopunite sredstva");
		}else {
			System.out.println("Uspesno vraceno vozilo");
			
		}
		azurirajKarticuSredstva(this.nsgoKartica.getId(),novaRaspolozivaSredstva);
		postaviKranjeVreme();
		izborInput.close();
		Platforma.meniZaKorisnika(this);
	}

}
