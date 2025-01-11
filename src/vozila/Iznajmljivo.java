package vozila;

import java.util.List;
import java.util.Scanner;

import najmovi.Najam;

public interface Iznajmljivo {
	Najam unajmi(List<Vozilo>slobodnaVozila);
	void vrati(Vozilo v);

		//podesiti da kako ce se vozila ispisivati npr:id ,ime vozila, max brzina/broj brzina, max tezina
}
