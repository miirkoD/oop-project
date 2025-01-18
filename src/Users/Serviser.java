package Users;

import java.io.File;
import java.util.ArrayList;
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

import enumi.Stanje;
import vozila.Odrzavanje;
import vozila.UpravljanjeVozilima;
import vozila.Vozilo;

public class Serviser extends Korisnik implements Odrzavanje{
	public Serviser(String username,String password) {
		super(username,password);
	}
	
	//pregledanje vozila
	
	public void pregledanjeVozila() throws XMLParseException {
		Scanner izborInput=new Scanner(System.in);
		UpravljanjeVozilima upravljanjeVozilima=new UpravljanjeVozilima();
		List<Vozilo>vozilaZaPregled=neServisiranaVozila();
		
		int brojVozila=0;
		Vozilo izabranoVozilo=null;
		
		System.out.println("Izaberite vozilo koje zelite da proverite");
		for(int i=0; i<vozilaZaPregled.size();i++){
			brojVozila++;
			System.out.println(brojVozila +". "+vozilaZaPregled.get(i));
			int izborBroj=izborInput.nextInt()-1;
			
			if(izborBroj>=0&& izborBroj<vozilaZaPregled.size()) {
				izabranoVozilo=vozilaZaPregled.get(izborBroj);
				System.out.println("Izabrano vozilo " +izabranoVozilo);
			}
			else {
				System.out.println("Neipravan unos");
			}
		}
		
		System.out.println("Izaberite koje stanje biste da dodelite vozilu");
		int brojStanja=0;
		for(int i=0;i<Stanje.values().length;i++) {
			brojStanja++;
			System.out.println(brojStanja+". "+Stanje.values()[i]);
		}
		
		System.out.print("Odaberite stanje (broj): ");
		int odabraniBroj = izborInput.nextInt();
		
		if(odabraniBroj >=1 && odabraniBroj<=Stanje.values().length) {
			Stanje odabranoStanje=Stanje.values()[odabraniBroj-1];
			promeniStanje(odabranoStanje,izabranoVozilo.getId());
		}
		else {
			System.out.println("Neipravan unos");
		}
		
		izborInput.close();
	}
	public List<Vozilo>neServisiranaVozila() throws XMLParseException{
		List<Vozilo> vozilaBezServisa = new ArrayList<Vozilo>();
		UpravljanjeVozilima upravljanjeVozilima=new UpravljanjeVozilima();
		List<Vozilo>svaVozila=upravljanjeVozilima.ucitajVozila();

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
					String krajnjiDatum=nodeElement.getElementsByTagName("datumKrajaNajma").item(0).getTextContent();
					if(!krajnjiDatum.isEmpty()&& !krajnjiDatum.isBlank()) {
						if (servisBool==false) {
							for (Vozilo vozilo : svaVozila) {
								if (vozilo.getId() == voziloId) {
										vozilaBezServisa.add(vozilo);
										break;
								}
							}
						}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return vozilaBezServisa;
	}
	
	public void promeniStanje(Stanje stanje,int idVozila) {
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
					
					if(id==idVozila) {
						Element stanjeVozilaElement=(Element) voziloElement.getElementsByTagName("stanje").item(0);
						stanjeVozilaElement.setTextContent(stanje.toString());
						break;
					}
				}
			}
			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			DOMSource source = new DOMSource(dokument);
			StreamResult result = new StreamResult(new File(putanjaDoDatoteke));
			transformer.transform(source, result);
			
			System.out.println("Stanje je promenjeno");
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void popravi() throws XMLParseException {
		UpravljanjeVozilima upravljanjeVozilima=new UpravljanjeVozilima();
		
		List<Vozilo>ostecenaVozila=upravljanjeVozilima.ostecenaVozila();
		
		Vozilo izabranoVozilo = null;
		int brojVozila=0;
		
		Scanner izborInput=new Scanner(System.in);
		
		System.out.println("Ovo su ostecena vozila");
		for(int i=0; i<ostecenaVozila.size();i++){
			brojVozila++;
			System.out.println(brojVozila +". "+ostecenaVozila.get(i));
		}
		
		System.out.print("Izaberite vozilo koje zelite da popravite ");
		int izborBroj=izborInput.nextInt()-1;
		
		if(izborBroj>=0 && izborBroj<ostecenaVozila.size()) {
			izabranoVozilo=ostecenaVozila.get(izborBroj);
			System.out.println("Izabrano vozilo " +izabranoVozilo);
		}
		else {
			System.out.println("Neipravan unos");
		}
		upravljanjeVozilima.azurirajStanjeVozila(izabranoVozilo);
		izborInput.close();

	}

	@Override
	public void proveriStanje() throws XMLParseException {
		Scanner izborInput=new Scanner(System.in);
		UpravljanjeVozilima upravljanjeVozilima=new UpravljanjeVozilima();
		List<Vozilo>svaVozila=upravljanjeVozilima.ucitajVozila();
		
		int brojVozila=0;
		Vozilo izabranoVozilo=null;
		
		System.out.println("Izaberite vozilo koje zelite da proverite");
		for(int i=0; i<svaVozila.size();i++){
			brojVozila++;
			System.out.println(brojVozila +". "+svaVozila.get(i));
			int izborBroj=izborInput.nextInt()-1;
			
			if(izborBroj>=0&& izborBroj<svaVozila.size()) {
				izabranoVozilo=svaVozila.get(izborBroj);
				System.out.println("Izabrano vozilo " +izabranoVozilo);
			}
			else {
				System.out.println("Neipravan unos");
			}
			System.out.print("Stanje izabranog vozila "+izabranoVozilo.getStanje());
		}
		izborInput.close();
	}


}
