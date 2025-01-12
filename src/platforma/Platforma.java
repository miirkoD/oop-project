package platforma;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import javax.management.modelmbean.XMLParseException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.time.LocalDate;
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
	private Korisnik korisnikUlogovan;
	// private List <Vozilo> dostupnaVozila;
	// private String naziv;
	// private String korisnici="../Data/users.xml";

	public static void main(String[] args) throws SAXException, IOException, ParserConfigurationException, XMLParseException {
		Platforma platforma = new Platforma();

		Scanner inputScanner = new Scanner(System.in);

		System.out.println("Unesite korisnicko ime: ");
		String korisnickoIme = inputScanner.nextLine();

		System.out.println("Unesite lozinku: ");
		String lozinka = inputScanner.nextLine();

		prijavaKorinsika(korisnickoIme, lozinka);

		inputScanner.close();
	}

	public static List<Korisnik> ucitajKorisnike()
			throws SAXException, IOException, ParserConfigurationException, XMLParseException {
		List<Korisnik> korisnici = new ArrayList<>();
		try {
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(new File("../Data/korisnici.xml"));

			doc.getDocumentElement().normalize();

			NodeList korisnikNodeList = doc.getElementsByTagName("korisnik");

			for (int i = 0; i < korisnikNodeList.getLength(); i++) {
				Node korisnikNode = korisnikNodeList.item(i);

				if (korisnikNode.getNodeType() == Node.ELEMENT_NODE) {
					Element korisnikElement = (Element) korisnikNode;

					String username = korisnikElement.getElementsByTagName("username").item(0).getTextContent();
					String password = korisnikElement.getElementsByTagName("password").item(0).getTextContent();
					String tipKorisnika = korisnikElement.getElementsByTagName("tipKorisnika").item(0).getTextContent();

					Korisnik korisnik;

					if (tipKorisnika == "iznajmljivac") {
						Iznajmljivac iznajmljivac = new Iznajmljivac(username, password);
						ucitaneKartice(username, iznajmljivac);
//						korisnik=iznajmljivac;
						korisnici.add(iznajmljivac);
					} else if (tipKorisnika == "vlasnik") {
						Vlasnik vlasnik = new Vlasnik(username, password);
						korisnici.add(vlasnik);
					} else {
						Serviser serviser = new Serviser(username, password);
						korisnici.add(serviser);
					}

					// korisnici.add(korisnik);
				}
			}
		} catch (SAXException | IOException | ParserConfigurationException e) {
			e.printStackTrace();
			throw e;
		} catch (XMLParseException e) {
			System.err.println("Greška pri čitanju XML datoteke: " + e.getMessage());
		}
		System.out.println(korisnici);
		return korisnici;
	}

	public static Kartica ucitaneKartice(String username, Iznajmljivac iznajmljivac) throws XMLParseException {
		List<Kartica> karticeIznajmljivaca = new ArrayList<>();
		try {
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(new File("../Data/kartice.xml"));

			NodeList karticaList = doc.getElementsByTagName("kartica");

			for (int i = 0; i < karticaList.getLength(); i++) {
				Node karticaNode = karticaList.item(i);
				if (karticaNode.getNodeType() == Node.ELEMENT_NODE) {
					Element karticaElement = (Element) karticaNode;

					String imeIznajmljivaca = karticaElement.getElementsByTagName("Iznajmljivac").item(0)
							.getTextContent();

					if (imeIznajmljivaca.equals(username)) {
						int id = Integer.parseInt(karticaElement.getElementsByTagName("id").item(0).getTextContent());
						LocalDate datumOd = LocalDate.parse(
								karticaElement.getElementsByTagName("datumKreiranja").item(0).getTextContent(),
								DateTimeFormatter.ofPattern("dd.MM.yyyy"));
						LocalDate datumDo = LocalDate.parse(
								karticaElement.getElementsByTagName("datumIsteka").item(0).getTextContent(),
								DateTimeFormatter.ofPattern("dd.MM.yyyy"));
						double raspolozivaSredstva = Double.parseDouble(
								karticaElement.getElementsByTagName("raspolozivaSredstva").item(0).getTextContent());
						int voziloAktivnoID = Integer.parseInt(
								karticaElement.getElementsByTagName("voziloAktivno").item(0).getTextContent());
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

	public static void prijavaKorinsika(String username, String password) throws SAXException, IOException, ParserConfigurationException, XMLParseException {
		List <Korisnik> korisnici=ucitajKorisnike();
		for(Korisnik korisnik: korisnici) {
			if(korisnik.getUsername().equals(username)&&korisnik.getPassword().equals(password)) {
				System.out.print("Uspesno ulogovani");
			}
		}
	}

	public void meniZaKorisnika(String tipKorisnika, String username) {
		System.out.println("Dobrodosli, " + username);
		switch (tipKorisnika) {
		case "Iznajmljivac":
			System.out.println();
			// Iznajmi vozilo -Kreiranje najma
			// Vracanje vozila
			break;
		case "Vlasnik":
			System.out.println();
			break;
		case "Serviser":
			System.out.println();
			break;
		}
	}

	public void odjavaKorisnika() {
		System.out.println("Korisnik je uspesno odjavljen");
		System.exit(0);
	}

	public void registracijaKorisnika(String username, String password) {
		Scanner inputScanner = new Scanner(System.in);

		System.out.println("Registracija novog korisnika");

		// System.out.println("Uneiste korisnicko ime");

		String tipKorisnika = null;
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
			//File file = new File(korisnici);
			//boolean noviFajl = file.createNewFile();

//			try (PrintWriter writer = new PrintWriter(new FileOutputStream(file, true))) {
//				writer.println(username + ";" + password + ";" + tipKorisnika + ";");
//				System.out.println("Registrovan korisnik " + username);
//				// Dodati prijavu korisnika nakon registracije
//			}
		} catch (Exception e) {
			System.out.println("Greska pri registraciji: " + e.getMessage());
		}
		inputScanner.close();
	}

}
