package vozila;

import javax.management.modelmbean.XMLParseException;

public interface Odrzavanje {
	void proveriStanje() throws XMLParseException;
	void popravi() throws XMLParseException;
}
