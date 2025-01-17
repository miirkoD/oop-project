package platforma;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import javax.management.modelmbean.XMLParseException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;

import Users.Iznajmljivac;
import Users.Korisnik;
import Users.Serviser;
import Users.Vlasnik;
import kartica.Kartica;
import vozila.UpravljanjeVozilima;
import vozila.Vozilo;

public class Platforma {
	private static Korisnik korisnikUlogovan;
	// private List <Vozilo> dostupnaVozila;
	// private String naziv;
	// private String korisnici="../Data/users.xml";

	public static void main(String[] args)
			throws SAXException, IOException, ParserConfigurationException, XMLParseException {
		// Scanner izbor1Scanner=new Scanner(System.in);
		System.out.println("Dobrodosli u aplikaciju za iznajmljivanje trotineta i bicikala!");

		Scanner inputScanner = new Scanner(System.in);

		int izbor = 0;

		while (izbor != 1 && izbor != 2) {
			System.out.println("\n 1. Prijava");
			System.out.println(" 2.  Registracija");
			try {
				izbor = inputScanner.nextInt();
			} catch (Exception e) {
				System.out.println("Unesite broj 1 ili 2.");
				inputScanner.nextLine();
				continue;
			}
			inputScanner.nextLine();
			switch (izbor) {
			case 1:
				prijavaKorinsika(inputScanner);
				break;
			case 2:
				registracijaKorisnika(inputScanner); // registraciju treba popraviti
				break;
			default:
				System.out.println("Pogresan unos, probajte opet");
			}
		}
		// System.out.println("Ovo je ulogovani korisnik"+ korisnikUlogovan);//provera
		// da li su svi korisnici tu
		inputScanner.close();
	}

	public static List<Korisnik> ucitajKorisnike()
			throws SAXException, IOException, ParserConfigurationException, XMLParseException {
		List<Korisnik> korisnici = new ArrayList<>();
		try {
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(new File("Data/users.xml"));
			doc.getDocumentElement().normalize();

			NodeList korisnikNodeList = doc.getElementsByTagName("korisnik");

			for (int i = 0; i < korisnikNodeList.getLength(); i++) {
				Node korisnikNode = korisnikNodeList.item(i);

				if (korisnikNode.getNodeType() == Node.ELEMENT_NODE) {
					Element korisnikElement = (Element) korisnikNode;

					String username = korisnikElement.getElementsByTagName("korisnickoIme").item(0).getTextContent();
					String password = korisnikElement.getElementsByTagName("lozinka").item(0).getTextContent();
					String tipKorisnika = korisnikElement.getElementsByTagName("tipKorisnika").item(0).getTextContent();
					// System.out.println(tipKorisnika);

					// Korisnik korisnik;

					if (tipKorisnika.equals("iznajmljivac")) {
						Iznajmljivac iznajmljivac = new Iznajmljivac(username, password);
						Kartica k = ucitaneKartice(username, iznajmljivac);
						iznajmljivac = new Iznajmljivac(username, password, k);
						korisnici.add(iznajmljivac);
					} else if (tipKorisnika.equals("vlasnik")) {
						Vlasnik vlasnik = new Vlasnik(username, password);
						korisnici.add(vlasnik);
					} else if (tipKorisnika.equals("serviser")) {
						Serviser serviser = new Serviser(username, password);
						korisnici.add(serviser);
					} else {
						System.out.println("Nepoznat korisnik " + tipKorisnika);
					}

				}
			}
		} catch (SAXException | IOException | ParserConfigurationException e) {
			e.printStackTrace();
			throw e;
		} catch (XMLParseException e) {
			System.err.println("Greška pri čitanju XML datoteke: " + e.getMessage());
		}
		//System.out.println("ovo su svi ucitani korisnici " + korisnici);// obrisati posle
		return korisnici;
	}

	public static Kartica ucitaneKartice(String username, Iznajmljivac iznajmljivac) throws XMLParseException {
		// List<Kartica> karticeIznajmljivaca = new ArrayList<>();
		try {
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(new File("Data/kartice.xml"));

			NodeList karticaList = doc.getElementsByTagName("kartica");

			for (int i = 0; i < karticaList.getLength(); i++) {
				Node karticaNode = karticaList.item(i);
				if (karticaNode.getNodeType() == Node.ELEMENT_NODE) {
					Element karticaElement = (Element) karticaNode;

					String imeIznajmljivaca = karticaElement.getElementsByTagName("Iznajmljivac").item(0)
							.getTextContent();

					if (imeIznajmljivaca.equals(username)) {
						int id = Integer.parseInt(karticaElement.getElementsByTagName("id").item(0).getTextContent());
						LocalDateTime datumOd = LocalDateTime.parse(
								karticaElement.getElementsByTagName("datumKreiranja").item(0).getTextContent(),
								DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm"));
						LocalDateTime datumDo = LocalDateTime.parse(
								karticaElement.getElementsByTagName("datumIsteka").item(0).getTextContent(),
								DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm"));
						double raspolozivaSredstva = Double.parseDouble(
								karticaElement.getElementsByTagName("raspolozivaSredstva").item(0).getTextContent());
						String voziloAktivnoIDString = karticaElement.getElementsByTagName("voziloAktivno").item(0)
								.getTextContent();
						int voziloAktivnoID = 0;
						if (voziloAktivnoIDString.isEmpty() || voziloAktivnoIDString.isBlank()) {
							voziloAktivnoID = 0;
						} else {
							try {
								voziloAktivnoID = Integer.parseInt(voziloAktivnoIDString);
							} catch (NumberFormatException e) {
								System.err.println("Nije moguce pretvoriti " + voziloAktivnoIDString + " u broj");
							}
						}

						UpravljanjeVozilima upravljanjeVozilima = new UpravljanjeVozilima();
						Vozilo voziloAktivno = upravljanjeVozilima.pronadjiVoziloPrekoID(voziloAktivnoID);
						return new Kartica(id, iznajmljivac, datumOd, datumDo, raspolozivaSredstva, voziloAktivno);
					}
				}
			}

		} catch (SAXException | IOException | ParserConfigurationException e) {
			e.printStackTrace();
		} catch (XMLParseException e) {
			System.err.println("Greska pri citanju XML datoteke: " + e.getMessage());
		}
		return null;
	}

	public static void prijavaKorinsika(Scanner inputScanner)
			throws SAXException, IOException, ParserConfigurationException, XMLParseException {
		System.out.println("Unesite korisnicko ime: ");
		String korisnickoIme = inputScanner.nextLine();

		System.out.println("Unesite lozinku: ");
		String lozinka = inputScanner.nextLine();

		List<Korisnik> korisnici = ucitajKorisnike();
		for (Korisnik korisnik : korisnici) {
			if (korisnik.getUsername().equals(korisnickoIme) && korisnik.getPassword().equals(lozinka)) {
				System.out.println("Uspesno ulogovani");
				System.out.println("Dobrodosli, " + korisnickoIme);
				korisnikUlogovan = korisnik;
				meniZaKorisnika(korisnik);
			}else {
				System.out.println("Neuspesna prijava");
			}
			break;
		}
	}

	public static void meniZaKorisnika(Korisnik korisnik) throws XMLParseException {
		Scanner inputScanner = new Scanner(System.in);
		UpravljanjeVozilima upravljanjeVozilima = new UpravljanjeVozilima();
		if (korisnik instanceof Iznajmljivac) {
			System.out.println("1. Iznjami vozilo, 2. Vrati vozilo, 3. Odjavi se");
			int brojOdabira = inputScanner.nextInt();
			if (brojOdabira == 1) {
				// iznajmi vozilo
				try {
					List<Vozilo> svaVozila = upravljanjeVozilima.ucitajVozila();
					List<Vozilo> slobodnaVozila = upravljanjeVozilima.slobodnaVozila(svaVozila);
					System.out.println("Ovo su slobodna Vozila \n" + slobodnaVozila);
					((Iznajmljivac) korisnik).unajmi(slobodnaVozila);
				} catch (XMLParseException e) {
					e.printStackTrace();
				}
			} else if (brojOdabira == 2) {//vrati vozilo
				((Iznajmljivac) korisnik).vrati();
			} else if (brojOdabira == 3) {
				odjavaKorisnika();
			}
		} else if (korisnik instanceof Serviser) {
//			System.out.println(); //ovde da budu metoda vezane za servisera
		} else if (korisnik instanceof Vlasnik) {
//			System.out.println(); //ovde da budu metoda vezane za vlasnika
		}
		inputScanner.close();
	}

	public static void odjavaKorisnika() {
		System.out.println("Korisnik je uspesno odjavljen");
		korisnikUlogovan = null;
		System.exit(0);
	}

	public static void registracijaKorisnika(Scanner inputScanner) {
		System.out.println("Registracija novog korisnika\n");

		System.out.println("Unesite korisnicko ime: ");
		String korisnickoIme = inputScanner.nextLine();

		System.out.println("Unesite lozinku: ");
		String lozinka = inputScanner.nextLine();

		String tipKorisnika = "";
		boolean ispravanUnos = false;

		do {
			System.out.println("Izaberite tip korisnika:");
			System.out.println("1. Iznajmljivac");
			System.out.println("2. Serviser");

			int tipIzbor = inputScanner.nextInt();
			inputScanner.nextLine();

			if (tipIzbor == 1) {
				tipKorisnika = "Iznajmljivac";
				ispravanUnos = true;
				Iznajmljivac iznajmljivacPomocna=new Iznajmljivac(korisnickoIme,lozinka);
				Kartica kartica=new Kartica(iznajmljivacPomocna);
				Iznajmljivac iznajmljivacRegistrovan=new Iznajmljivac(korisnickoIme,lozinka,kartica);
					korisnikUlogovan=iznajmljivacRegistrovan;
				break;
			} else if (tipIzbor == 2) {
				tipKorisnika = "Serviser";
				ispravanUnos = true;
				break;
			} else {
				System.out.println("Nepoznat tip korisnika. Registracija nije uspešna. Pokusajte opet.");
				ispravanUnos = false;
			}

		} while (!ispravanUnos);

		try {
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder=dbFactory.newDocumentBuilder();
			Document doc;
			
			 File file = new File("Data/users.xml");
			 if(file.exists()) {
				 doc=dBuilder.parse(file);
			 }else {
				 doc=dBuilder.newDocument();
				 Element rootElement=doc.createElement("korisnici");
				 doc.appendChild(rootElement);
			 }
			 Element korisnikElement=doc.createElement("korisnik");
			 
			 Element korisnickoImeElement=doc.createElement("korisnickoIme");
			 korisnickoImeElement.appendChild(doc.createTextNode(korisnickoIme));
			 korisnikElement.appendChild(korisnickoImeElement);
			 
			 Element lozinkaElement=doc.createElement("lozinka");
			 lozinkaElement.appendChild(doc.createTextNode(lozinka));
			 korisnikElement.appendChild(lozinkaElement);
			 
			 Element tipKorisnikaElement=doc.createElement("tipKorisnika");
			 tipKorisnikaElement.appendChild(doc.createTextNode(tipKorisnika));
			 korisnikElement.appendChild(tipKorisnikaElement);
			 
			 TransformerFactory transformerFactory=TransformerFactory.newInstance();
			 Transformer transformer=transformerFactory.newTransformer();
			 transformer.transform(new DOMSource(doc), new StreamResult(file));
			 
			 
		} catch (Exception e) {
			System.out.println("Greska pri registraciji: " + e.getMessage());
		}
		inputScanner.close();
	}

}
