package main.hr.java.covidportal.enums;

/**
 * Sadrzi vrijednosti simptoma (RIJETKO, SREDNJE, CESTO)
 */
public enum VrijednostSimptoma {
    RIJETKO("RIJETKO"),
    SREDNJE("SREDNJE"),
    CESTO("CESTO"),
    PRODUKTIVNI("PRODUKTIVNI"),
    INTENZIVNO("INTENZIVNO"),
    JAKA("JAKA"),
    VISOKA("VISOKA");

    private final String vrijednost;

    /**
     * Konstruktor klase, prima string vrijednost
     * @param vrijednost vrijednost simptoma
     */
    VrijednostSimptoma(String vrijednost) {
        this.vrijednost = vrijednost;
    }

    public String getVrijednost() {
        return vrijednost;
    }
}
