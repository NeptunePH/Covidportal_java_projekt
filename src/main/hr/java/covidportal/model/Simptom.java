package main.hr.java.covidportal.model;

import main.hr.java.covidportal.enums.*;
import main.hr.java.covidportal.enums.VrijednostSimptoma;

import java.io.Serializable;
import java.util.Objects;

/**
 * Klasa koja reprezentira simptom bolesti, sadrži vrijednost (učestalost) simptoma. Nasljeđuje klasu ImenovaniEntitet.
 */

public class Simptom extends ImenovaniEntitet implements Serializable {
    private VrijednostSimptoma vrijednost;

    /**
     * Konstruktor klase, prima vrijednost simptoma i naziv. Naziv se prosljeđuje konstruktoru klase ImenovaniEntitet
     * @param id id entiteta
     * @param naziv naziv simptoma
     * @param vrijednost učestalost simptoma
     */
    public Simptom(Long id, String naziv, VrijednostSimptoma vrijednost) {
        super(naziv, id);
        this.vrijednost = vrijednost;
    }

    public String getVrijednost() {
        return vrijednost.getVrijednost();
    }

    @Override
    public String toString(){
        return this.getId() + " " + this.getNaziv() + " " + this.getVrijednost();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Simptom)) return false;
        if (!super.equals(o)) return false;
        Simptom simptom = (Simptom) o;
        return vrijednost == simptom.vrijednost;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), vrijednost);
    }
}
