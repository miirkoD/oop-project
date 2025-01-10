package platforma;

import java.util.List;
import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;

import Users.Korisnik;
import vozila.Vozilo;

public class Platforma {
	private Korisnik korisnik;
	//private List <Vozilo> dostupnaVozila;
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
	
}

