package main.hr.java.covidportal.model;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

/**
 * Klasa koja reprezentira bolest, sadrži polje simptoma. Nasljeđuje klasu ImenovaniEntitet.
 */

public class Bolest extends ImenovaniEntitet implements Serializable {
    private List<Simptom> simptomi;

    /**
     * Konstruktor klase, prima polje simptoma i naziv. Naziv se prosljeđuje konstruktoru klase ImenovaniEntitet
     * @param id id entiteta
     * @param naziv naziv bolesti
     * @param simptomi popis simptoma
     */
    public Bolest(Long id, String naziv, List<Simptom> simptomi) {
        super(naziv, id);
        this.simptomi = simptomi;
    }

    public List<Simptom> getSimptomi() {
        return simptomi;
    }

    public void setSimptomi(List<Simptom> simptomi) {
        this.simptomi = simptomi;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Bolest)) return false;
        if (!super.equals(o)) return false;
        Bolest bolest = (Bolest) o;
        return Objects.equals(simptomi, bolest.simptomi);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), simptomi);
    }

    @Override
    public String toString(){
        return this.getId() + " " + this.getNaziv();
    }
}