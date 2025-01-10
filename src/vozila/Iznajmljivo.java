package vozila;

import java.util.List;
import java.util.Scanner;

public interface Iznajmljivo {
	void unajmi(List<Vozilo>slobodnaVozila);
	void vrati();

		//podesiti da kako ce se vozila ispisivati npr:id ,ime vozila, max brzina/broj brzina, max tezina
}
