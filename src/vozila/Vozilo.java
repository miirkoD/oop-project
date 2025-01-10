package vozila;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import Users.Vlasnik;
import enumi.Stanje;

abstract public class Vozilo {
	private int id;
	private  Vlasnik vlasnik;
	private double cenaPoSatu;
	private double maxTezina;
	private Stanje stanje;
	private boolean zauzeto;
	
	public Vozilo(int id, Vlasnik vlasnik, double cenaPoSatu, double maxTezina,Stanje stanje) {
		this.id=id;
		this.vlasnik=vlasnik;
		this.cenaPoSatu=cenaPoSatu;
		this.maxTezina=maxTezina;
		this.stanje=stanje;
		this.zauzeto=false;
	}
	
	public void postaviNaZauzeto() {
		this.zauzeto=true;
	}
	
	public void postaviNaSlobodno() {
		this.zauzeto=false;
	}
	
	public boolean isZauzeto() {
		return this.zauzeto;
	}
	
	public double getCenaPoSatu() {
		return cenaPoSatu;
	}

	public double getMaxTezina() {
		return maxTezina;
	}

	public List<Vozilo> ucitajVozila() {
		ArrayList<Vozilo> svaVozila = new ArrayList<>();
		
		try {
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder =dbFactory.newDocumentBuilder();
			
			File vozilaFile=new File("../Data/vozila.xml");
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
		//int brojSlobodnihVozila=0;
		ArrayList<Vozilo>slobodnaVozilaList=new ArrayList<>();
		for(Vozilo v : vozila) {
			if(v.isZauzeto()==false) {
				slobodnaVozilaList.add(v);
			}
		}
//		if(brojSlobodnihVozila==0) {
//			System.out.println("Trenutno nemamo slobodnih vozila");
//		}
		return slobodnaVozilaList;
	}
	
	
}
