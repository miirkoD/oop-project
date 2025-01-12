package vozila;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import javax.management.modelmbean.XMLParseException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import Users.Vlasnik;
import enumi.Stanje;

public class UpravljanjeVozilima {
	
	
	public List<Vozilo> ucitajVozila() throws XMLParseException {
		ArrayList<Vozilo> svaVozila = new ArrayList<>();
		
		try {
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder =dbFactory.newDocumentBuilder();
			String putanjaDoDatoteke="../Data/vozila.xml";
			File vozilaFile=new File(putanjaDoDatoteke);
			
			if(!vozilaFile.exists()) {
				throw new XMLParseException ("Datoteka "+putanjaDoDatoteke+" ne postoji");
			}
			Document dokument=dBuilder.parse(vozilaFile);
			
			dokument.getDocumentElement().normalize();
			
			NodeList vozilaList=dokument.getElementsByTagName("vozilo");
			
			for(int i=0;i<vozilaList.getLength();i++) {
				Node vozilaNode=vozilaList.item(i);
				
				if(vozilaNode.getNodeType()==Node.ELEMENT_NODE) {
					Element voziloElement =(Element) vozilaNode;
					
					int id=Integer.parseInt(voziloElement.getElementsByTagName("id").item(0).getTextContent());
					double cenaPoSatu=Double.parseDouble(voziloElement.getElementsByTagName("cenaPoSatu").item(0).getTextContent());
					double maxTezina=Double.parseDouble(voziloElement.getElementsByTagName("maxTezina").item(0).getTextContent());
					Stanje stanje=Stanje.valueOf(voziloElement.getElementsByTagName("stanje").item(0).getTextContent());
					boolean zauzeto= Boolean.parseBoolean(voziloElement.getElementsByTagName("zauzeto").item(0).getTextContent());
					
					if(zauzeto) {
						continue;
					}
					
					Element vlasnikElement =(Element) voziloElement.getElementsByTagName("vlasnik").item(0);
					String vlasnikUsername=vlasnikElement.getElementsByTagName("username").item(0).getTextContent();
					String vlasnikPassword = vlasnikElement.getElementsByTagName("password").item(0).getTextContent();
					Vlasnik vlasnik= new Vlasnik(vlasnikUsername,vlasnikPassword);
					//posle staviti da poziva funkciju koja proverava username iz users.xml
					
					String tip=voziloElement.getElementsByTagName("tip").item(0).getTextContent();
					Vozilo vozilo=null;
					
					if("bicikl".equalsIgnoreCase(tip)) {
						int brojBrzina=Integer.parseInt(voziloElement.getElementsByTagName("brojBrzina").item(0).getTextContent());
						int visina=Integer.parseInt(voziloElement.getElementsByTagName("visina").item(0).getTextContent());
						vozilo=new Bicikl(id,vlasnik,cenaPoSatu,maxTezina,stanje,brojBrzina,visina);
					}
					else if("trotinet".equalsIgnoreCase(tip)) {
						int maxBrzina=Integer.parseInt(voziloElement.getElementsByTagName("maxBrzina").item(0).getTextContent());
						double trajanjeBaterije=Double.parseDouble(voziloElement.getElementsByTagName("trajanjeBaterije").item(0).getTextContent());
						vozilo=new Trotinet(id,vlasnik,cenaPoSatu,maxTezina,stanje,maxBrzina,trajanjeBaterije);
					}
					
					if(vozilo!=null) {
						svaVozila.add(vozilo);
					}
				}
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
		return svaVozila;
	}
	
	public List<Vozilo> slobodnaVozila(List<Vozilo> vozila) {
		ArrayList<Vozilo>slobodnaVozilaList=new ArrayList<>();
		for(Vozilo v : vozila) {
			if(v.isZauzeto()==false) {
				slobodnaVozilaList.add(v);
			}
		}
		return slobodnaVozilaList;
	}
	
	
	public Vozilo pronadjiVoziloPrekoID(int id) throws XMLParseException {
		List<Vozilo> svaVozila= ucitajVozila();
		
		for(Vozilo v:svaVozila) {
			if(v.getId()==id) {
				return v;
			}
		}
		return null;
	}
	
	public void pretragaVozila() {
		Scanner pretragaInput=new Scanner(System.in);
		System.out.println("Izaberite nacin na koji zelite da pretrazite vozilo");
		System.out.println("1. preko tipa vozila /n" +"2. Preko zauzetosti /n" + "3. Po servisu");
		
		pretragaInput.close();
	}
}
