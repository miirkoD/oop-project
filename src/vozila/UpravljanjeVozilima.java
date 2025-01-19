package vozila;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

import javax.management.modelmbean.XMLParseException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import Users.Korisnik;
import Users.Vlasnik;
import enumi.Stanje;
import platforma.Platforma;

public class UpravljanjeVozilima {
	//List<Vozilo> svaVozila = new ArrayList<>();

	public List<Vozilo> ucitajVozila() throws XMLParseException {
		ArrayList<Vozilo> svaVozila = new ArrayList<>();

		try {
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			String putanjaDoDatoteke = "Data/vozila.xml";
			File vozilaFile = new File(putanjaDoDatoteke);

			if (!vozilaFile.exists()) {
				throw new XMLParseException("Datoteka " + putanjaDoDatoteke + " ne postoji");
			}

			Document dokument = dBuilder.parse(vozilaFile);

			dokument.getDocumentElement().normalize();

			NodeList vozilaList = dokument.getElementsByTagName("vozilo");

			for (int i = 0; i < vozilaList.getLength(); i++) {
				Node vozilaNode = vozilaList.item(i);

				if (vozilaNode.getNodeType() == Node.ELEMENT_NODE) {
					Element voziloElement = (Element) vozilaNode;

					int id = Integer.parseInt(voziloElement.getElementsByTagName("id").item(0).getTextContent());
					double cenaPoSatu = Double
							.parseDouble(voziloElement.getElementsByTagName("cenaPoSatu").item(0).getTextContent());
					double maxTezina = Double
							.parseDouble(voziloElement.getElementsByTagName("maxTezina").item(0).getTextContent());
					Stanje stanje = Stanje
							.valueOf(voziloElement.getElementsByTagName("stanje").item(0).getTextContent());

					Element vlasnikElement = (Element) voziloElement.getElementsByTagName("vlasnik").item(0);
					String vlasnikUsername = vlasnikElement.getElementsByTagName("username").item(0).getTextContent();
					String vlasnikPassword = vlasnikElement.getElementsByTagName("password").item(0).getTextContent();
					Vlasnik vlasnik = new Vlasnik(vlasnikUsername, vlasnikPassword);
					String tip = voziloElement.getElementsByTagName("tip").item(0).getTextContent();
					Vozilo vozilo = null;

					if ("bicikl".equalsIgnoreCase(tip)) {
						int brojBrzina = Integer
								.parseInt(voziloElement.getElementsByTagName("brojBrzina").item(0).getTextContent());
						int visina = Integer
								.parseInt(voziloElement.getElementsByTagName("visina").item(0).getTextContent());
						vozilo = new Bicikl(id, vlasnik, cenaPoSatu, maxTezina, stanje, brojBrzina, visina);
					} else if ("trotinet".equalsIgnoreCase(tip)) {
						int maxBrzina = Integer
								.parseInt(voziloElement.getElementsByTagName("maxBrzina").item(0).getTextContent());
						double trajanjeBaterije = Double.parseDouble(
								voziloElement.getElementsByTagName("trajanjeBaterije").item(0).getTextContent());
						vozilo = new Trotinet(id, vlasnik, cenaPoSatu, maxTezina, stanje, maxBrzina, trajanjeBaterije);
					}

					if (vozilo != null) {
						svaVozila.add(vozilo);
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return svaVozila;
	}

	public void obrisiVozilo(int idVozila) {
		try {
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc;
			File vozilaFile = new File("Data/vozila.xml");
			doc = dBuilder.parse(vozilaFile);

			NodeList vozilaList = doc.getElementsByTagName("vozila");
			
			for (int i = 0; i < vozilaList.getLength(); i++) {
				Node voziloNode = vozilaList.item(i);

				if (voziloNode.getNodeType() == Node.ELEMENT_NODE) {
					Element voziloElement = (Element) voziloNode;
					
					int idVozilaFile= Integer.parseInt(voziloElement.getElementsByTagName("id").item(0).getTextContent());
					
					if(idVozilaFile==idVozila) {
						vozilaList.item(i).getParentNode().removeChild(voziloNode);
						break;
					}
				}
			}
			TransformerFactory transformerFactory=TransformerFactory.newInstance();
			Transformer transformer =transformerFactory.newTransformer();
			DOMSource source=new DOMSource(doc);
			StreamResult result=new StreamResult(vozilaFile);
			transformer.transform(source, result);
			
		}catch(Exception e){
			e.printStackTrace();
		}
		
	}
	
	public List<Vozilo> ostecenaVozila() throws XMLParseException {
		List<Vozilo>svaVozila=ucitajVozila();
		List<Vozilo>ostecenaVozila=new ArrayList<>();
		
		for(Vozilo vozilo:svaVozila) {
			if(vozilo.getStanje()==Stanje.Malo_Ostecenje||vozilo.getStanje()==Stanje.Veliko_Ostecenje) {
				ostecenaVozila.add(vozilo);
			}
		}
		return ostecenaVozila;
	}

	public List<Vozilo> slobodnaVozila(List<Vozilo> vozila) {
		ArrayList<Vozilo> slobodnaVozilaList = new ArrayList<>();
		for (Vozilo v : vozila) {
			if (v.isZauzeto() == false) {
				slobodnaVozilaList.add(v);
			}
		}
		return slobodnaVozilaList;
	}
	
	public List<Vozilo>vlasnikovaVozila(String username,String password) throws XMLParseException{
		List<Vozilo> vozilaVlasnika=new ArrayList<>();
		List<Vozilo> svaVozila=ucitajVozila();
		for(Vozilo v :svaVozila) {
			if(v.getVlasnik().getUsername().equals(username)&& v.getVlasnik().getPassword().equals(password)) {
				vozilaVlasnika.add(v);
			}
		}
		return vozilaVlasnika;
	}

	public void azurirajXML(Vozilo vozilo) {
		try {
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			File voziloFile = new File("Data/vozila.xml");

			Document doc = dBuilder.parse(voziloFile);
			NodeList vozilaList = doc.getElementsByTagName("vozilo");

			for (int i = 0; i < vozilaList.getLength(); i++) {
				Node voziloNode = vozilaList.item(i);

				if (voziloNode.getNodeType() == Node.ELEMENT_NODE) {
					Element voziloElement = (Element) voziloNode;
					int id = Integer.parseInt(voziloElement.getElementsByTagName("id").item(0).getTextContent());
					if (id == vozilo.getId()) {
						Element zauzetoElement = (Element) voziloElement.getElementsByTagName("zauzeto").item(0);
						zauzetoElement.setTextContent("true");
						break;
					}
				}
			}

			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			DOMSource source = new DOMSource(doc);
			StreamResult result = new StreamResult(new File("Data/vozila.xml"));
			transformer.transform(source, result);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void azurirajStanjeVozila(Vozilo vozilo) {
		try {
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			File voziloFile = new File("Data/vozila.xml");

			Document doc = dBuilder.parse(voziloFile);
			NodeList vozilaList = doc.getElementsByTagName("vozilo");

			for (int i = 0; i < vozilaList.getLength(); i++) {
				Node voziloNode = vozilaList.item(i);

				if (voziloNode.getNodeType() == Node.ELEMENT_NODE) {
					Element voziloElement = (Element) voziloNode;
					int id = Integer.parseInt(voziloElement.getElementsByTagName("id").item(0).getTextContent());
					if (id == vozilo.getId()) {
						String stanjeNovo="Bez_Ostecenja";
						Element stanjeElement = (Element) voziloElement.getElementsByTagName("stanje").item(0);
						stanjeElement.setTextContent(stanjeNovo);
						break;
					}
				}
			}

			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			DOMSource source = new DOMSource(doc);
			StreamResult result = new StreamResult(new File("Data/vozila.xml"));
			transformer.transform(source, result);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public Vozilo pronadjiVoziloPrekoID(int id) throws XMLParseException {
		List<Vozilo> svaVozila = ucitajVozila();

		for (Vozilo v : svaVozila) {
			if (v.getId() == id) {
				return v;
			}
		}
		return null;
	}

	public void pretragaVozila(Korisnik k) throws XMLParseException {
		Scanner pretragaInput = new Scanner(System.in);
		System.out.println("Izaberite nacin na koji zelite da pretrazite vozilo");
		System.out.println("1. preko tipa vozila /n" + "2. Preko zauzetosti /n" + "3. Po servisu");
		int odabir = pretragaInput.nextInt();
		switch (odabir) {
		case 1: 
			System.out.print("Trenutno imamo 2 tipa vozila bicikl i trotinet. Izaberite jedan od njih");
			String tip = pretragaInput.nextLine();
			pretragaPoTip(tip, k);
			break;
		case 2:
			System.out.println("Unesite 'zauzeto' ako zelite da vidite zauzeta vozila.");
			System.out.println("Unesite 'slobodno' ako zelite da vidite slobodna vozila.");
			String zauzeto=pretragaInput.nextLine();
			pretragaPoZauzetosti(zauzeto,k);
			break;
		case 3:
			System.out.print("Unesite 1 ako zelite da vidite vozila koja su servisirana.");
			System.out.print("Unesite 2 ako zelite da vidite vozila koja nisu servisirana");
			int servis=pretragaInput.nextInt();
			pretragaPoServisu(servis,k);
			break;			
		default:
			System.out.println("Pogresan unos probajte ponovo");
		}
		
		pretragaInput.close();
	}

	private void pretragaPoTip(String tip,Korisnik korisnik) throws XMLParseException {
		List<Vozilo> svaVozila=ucitajVozila();
		List<Vozilo> vozilaTipa = new ArrayList<Vozilo>();
		for (Vozilo v : svaVozila) {
			if (v.tipVozila().equals(tip)) {
				vozilaTipa.add(v);
			}
		}
		System.out.println(vozilaTipa);
		Platforma.meniZaKorisnika(korisnik);
	}

	private void pretragaPoZauzetosti(String zauzetost, Korisnik korisnik) throws XMLParseException {
		if (!Arrays.asList("zauzeto", "slobodno").contains(zauzetost.toLowerCase())) {
			System.out.println("Neispravan unos! Unesite 'zauzeto' ili 'slobodno' kako bismte pretrazili");
			return;
		}
		List<Vozilo> svaVozila=ucitajVozila();
		List<Vozilo> vozilaZauzetosti = new ArrayList<Vozilo>();
		boolean traziZauzeto = zauzetost.equalsIgnoreCase("zauzeto");

		for (Vozilo v : svaVozila) {
			if (v.isZauzeto() == traziZauzeto) {
				vozilaZauzetosti.add(v);
			}
		}
		System.out.println(vozilaZauzetosti);
	}

	private void pretragaPoServisu(int servis,Korisnik korisnik) throws XMLParseException {
		List<Vozilo> svaVozila=ucitajVozila();
		List<Vozilo> vozilaServis = new ArrayList<Vozilo>();
		boolean odabir = false;
		switch (servis) {
		case 1:
			odabir = true;
			break;
		case 2:
			odabir = false;
			break;
		default:
			System.out.println("Neispravan usno. Unesite 1 ili 2.");
			return;
		}
		try {
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			File file = new File("Data/najam.xml");

			Document doc = dBuilder.parse(file);
			NodeList najamList = doc.getElementsByTagName("najmovi");

			for (int i = 0; i < najamList.getLength(); i++) {
				Node najamNode = najamList.item(i);

				if (najamNode.getNodeType() == Node.ELEMENT_NODE) {
					Element nodeElement = (Element) najamNode;
					int voziloId = Integer.parseInt(nodeElement.getElementsByTagName("IdVozila").item(0).getTextContent());

					boolean servisBool = Boolean.parseBoolean(nodeElement.getElementsByTagName("servis").item(0).getTextContent());
					if (servisBool==odabir) {
						for (Vozilo vozilo : svaVozila) {
							if (vozilo.getId() == voziloId) {
								vozilaServis.add(vozilo);
								break;
							}
						}
					}
				}
			}
			System.out.println("Vozila koja su " + (odabir ? "servisiran":"neservisiran")+":");
			for(Vozilo v:vozilaServis) {
				System.out.println(v);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		Platforma.meniZaKorisnika(korisnik);
	}

}
