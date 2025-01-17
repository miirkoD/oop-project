package vozila;

import java.util.List;

import javax.management.modelmbean.XMLParseException;

import najmovi.Najam;

public interface Iznajmljivo {
	Najam unajmi(List<Vozilo>slobodnaVozila);
	void vrati() throws XMLParseException;

		//podesiti da kako ce se vozila ispisivati npr:id ,ime vozila, max brzina/broj brzina, max tezina
}
