package platforma;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;

import Users.Korisnik;
import Users.Vlasnik;
import enumi.Stanje;
import vozila.Bicikl;
import vozila.Trotinet;
import vozila.Vozilo;

public class Platforma {
	private Korisnik korisnik;
	private List <Vozilo> dostupnaVozila;
	private String naziv;
	private String korisnici="../Data/users.txt";

	public static void main(String[] args) {
		Platforma platforma =new Platforma();
		
		Scanner inputScanner=new Scanner(System.in);
		
		System.out.println("Unesite korisnicko ime: ");
		String korisnickoIme=inputScanner.nextLine();
		
		System.out.println("Unesite lozinku: ");
		String lozinka=inputScanner.nextLine();
		
		platforma.prijavaKorinsika(korisnickoIme,lozinka);
		
		inputScanner.close();
	}
	
	
	public void prijavaKorinsika(String username,String password) {
		try(Scanner scanner=new Scanner(new File(korisnici))){
			while(scanner.hasNextLine()) {
				String linija=scanner.nextLine();
				String[] podaci= linija.split(";");
				
				if(podaci.length==3) {
					String korisnickoIme=podaci[0];
					String lozinka=podaci[1];
					String tipKorisnika=podaci[2];
				
					if(korisnickoIme.equals(username)&&lozinka.equals(password)) {
						meniZaKorisnika(tipKorisnika,korisnickoIme);
						return;
					}
				}
			}
			System.out.println("Neispravno korisnicko ime ili lozinka ");
		}
		catch(FileNotFoundException e) {
				System.out.println("Datoteka sa korisnicima nije pronadjena "+ e.getMessage());
		}
	}
		
	public void meniZaKorisnika(String tipKorisnika,String username) {
		System.out.println("Dobrodosli, "+ username);
		switch(tipKorisnika) {
		case "Iznajmljivac":
			System.out.println();
			//Iznajmi vozilo -Kreiranje najma
			//Vracanje vozila
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
	
	public void registracijaKorisnika(String username,String password) {
		Scanner inputScanner=new Scanner(System.in);
		
		System.out.println("Registracija novog korisnika");
		
		//System.out.println("Uneiste korisnicko ime");
		
		String tipKorisnika=null;
		boolean ispravanUnos=false;
		
		do {
			System.out.println("Izaberite tip korisnika:");
			System.out.println("1. Iznajmljivac");
			System.out.println("2. Serviser");
			
			int tipIzbor=inputScanner.nextInt();
			inputScanner.nextLine();
			
			if(tipIzbor==1) {
				tipKorisnika="Iznajmljivac";
				ispravanUnos=true;
				break;
			}
			else if(tipIzbor==2) {
				tipKorisnika="Serviser";
				ispravanUnos=true;
				break;
			}
			else {
				System.out.println("Nepoznat tip korisnika. Registracija nije uspešna. Pokusajte opet.");
				ispravanUnos=false;
			}
				
		}while(!ispravanUnos);
		
		try {
			File file=new File(korisnici);
			boolean noviFajl= file.createNewFile();
			
			try(PrintWriter writer=new PrintWriter(new FileOutputStream(file,true))){
				writer.println(username +";"+password+";"+tipKorisnika+";");
				System.out.println("Registrovan korisnik "+username);
				//Dodati prijavu korisnika nakon registracije
			}	
		}catch(Exception e) {
			System.out.println("Greska pri registraciji: "+ e.getMessage());
		}
		inputScanner.close();
	}

	public List<Vozilo> ucitajVozila() {
		ArrayList<Vozilo> dostupnaVozila = new ArrayList<>();
		
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
						dostupnaVozila.add(vozilo);
					}
				}
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
		return dostupnaVozila;
	}
}

