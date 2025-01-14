package vozila;

import Users.Vlasnik;
import enumi.Stanje;

public class Trotinet extends Vozilo {
	private int maxBrzina;
	private double trajanjeBaterije;

	public Trotinet(int id, Vlasnik vlasnik, double cenaPoSatu, double maxTezina, Stanje stanje, int maxBrzina,
			double trajanjeBaterije) {
		super(id, vlasnik, cenaPoSatu, maxTezina, stanje);
		this.maxBrzina = maxBrzina;
		this.trajanjeBaterije = trajanjeBaterije;
	}

	public int getMaxBrzina() {
		return maxBrzina;
	}

	public double getTrajanjeBaterije() {
		return trajanjeBaterije;
	}

	@Override
	public String toString() {
		return "Trotinet {" + "id: " + this.getId() + "\nCena po satu " + this.getCenaPoSatu() + "\nMaksimalna kilaza "
				+ this.getMaxTezina() + "\nMaksimalna brzina " + this.getMaxBrzina() + "\nTrajanje Baterije"
				+ this.getTrajanjeBaterije()+" }";

	}

}
