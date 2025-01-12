package vozila;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

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
	
	
	public int getId() {
		return this.id;
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

	//pretraga se moze vrsiti preko tipa, zauzetorsti, ili po servisu
	
	
}
