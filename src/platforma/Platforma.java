package platforma;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.time.LocalDate;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;

import Users.Iznajmljivac;
import Users.Korisnik;
import Users.Serviser;
import Users.Vlasnik;
import kartica.Kartica;
import vozila.Vozilo;

public class Platforma {
	private Korisnik korisnikUlogovan;
	// private List <Vozilo> dostupnaVozila;
	// private String naziv;
	// private String korisnici="../Data/users.xml";

	public static void main(String[] args) {
		Platforma platforma = new Platforma();

		Scanner inputScanner = new Scanner(System.in);

		System.out.println("Unesite korisnicko ime: ");
		String korisnickoIme = inputScanner.nextLine();

		System.out.println("Unesite lozinku: ");
		String lozinka = inputScanner.nextLine();

		platforma.prijavaKorinsika(korisnickoIme, lozinka);

		inputScanner.close();
	}

	public List<Korisnik> ucitajKorisnike() {
		List<Korisnik> korisnici = new ArrayList<>();

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

				Korisnik korisnik;

				if (korisnikElement.getElementsByTagName("nsgoKartica").getLength() > 0) {

					Element nsgoKarticaElement = korisnikElement.getElementsByTagName("nsgoKartica").item(0);

					int id = Integer.parseInt(nsgoKarticaElement.getElementsByTagName("id").item(0).getTextContent());
					SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
					LocalDate datumOd = nsgoKarticaElement.getElementsByTagName("datumOd").item(0).getTextContent();
					LocalDate datumDo = nsgoKarticaElement.getElementsByTagName("datumDo").item(0).getTextContent();
					double raspolozivaSredstva = Double.parseDouble(
							nsgoKarticaElement.getElementsByTagName("raspolozivaSredstva").item(0).getTextContent());
					Kartica nsgoKartica = new Kartica(id, datumOd, datumDo, raspolozivaSredstva);
					// mozda je bolje da kreiram metodu set karica a da uklonim karticu iz
					// konstruktora
					Iznajmljivac iznajmljivac = new Iznajmljivac(username, password, nsgoKartica);
					korisnik = iznajmljivac;
				} else if (korisnikElement.getElementsByTagName("vozila").getLength() > 0) {
					Vlasnik vlasnik = new Vlasnik(username, password);

//						Element vozilaElement=korisnikElement.getElementsByTagName("vozila").item(0);
//						NodeList voziloList=vozilaElement.getElementsByTagName("vozilo");
//						List<Vozilo> vozila = new ArrayList<>();
//						for(int j=0;i<voziloList.getLength();i++) {
//							Element voziloElement =(Element) voziloList.item(i);
//							int id =Integer.parseInt(voziloElement.getElementsByTagName("id").item(0).getTextContent());
//							String tip = voziloElement.getElementsByTagName("tip").item(0).getTextContent();
//	                        double cenaPoSatu = Double.parseDouble(voziloElement.getElementsByTagName("cenaPoSatu").item(0).getTextContent());
//						}
					// ovo srediti posle
				} else {
					Serviser serviser = new Serviser(username, password);
					korisnik = serviser;
				}
				korisnici.add(korisnik);
			}
		}

	}

	}

	public void prijavaKorinsika(String username, String password) {
		try (Scanner scanner = new Scanner(new File(korisnici))) {
			while (scanner.hasNextLine()) {
				String linija = scanner.nextLine();
				String[] podaci = linija.split(";");

				if (podaci.length == 3) {
					String korisnickoIme = podaci[0];
					String lozinka = podaci[1];
					String tipKorisnika = podaci[2];

					if (korisnickoIme.equals(username) && lozinka.equals(password)) {
						meniZaKorisnika(tipKorisnika, korisnickoIme);
						return;
					}
				}
			}
			System.out.println("Neispravno korisnicko ime ili lozinka ");
		} catch (FileNotFoundException e) {
			System.out.println("Datoteka sa korisnicima nije pronadjena " + e.getMessage());
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
			File file = new File(korisnici);
			boolean noviFajl = file.createNewFile();

			try (PrintWriter writer = new PrintWriter(new FileOutputStream(file, true))) {
				writer.println(username + ";" + password + ";" + tipKorisnika + ";");
				System.out.println("Registrovan korisnik " + username);
				// Dodati prijavu korisnika nakon registracije
			}
		} catch (Exception e) {
			System.out.println("Greska pri registraciji: " + e.getMessage());
		}
		inputScanner.close();
	}

}
