package main.hr.java.covidportal.model;


import java.io.Serializable;
import java.util.List;

/**
 * Klasa koja reprezentira virus. Nasljeđuje klasu bolest, a implementira sučelje zarazno.
 */

public class Virus extends Bolest implements Zarazno, Serializable {
    /**
     * Konstruktor klase. Prima naziv virusa i polje simptoma, te ih prosljeđuje klasi Bolest
     * @param id id entiteta
     * @param naziv naziv virusa
     * @param simptomi simptomi virusa
     */
    public Virus(Long id, String naziv, List<Simptom> simptomi) {
        super(id, naziv, simptomi);
    }

    /**
     * Implementacija metode sučelja zarazno. Postavlja bolest kojom je osoba zaražena na trenutni virus
     * @param osoba osoba kojoj se postavlja vrijednost
     */
    public void prelazakZarazeNaOsobu(Osoba osoba){
        osoba.setZarazenBolescu(this);
    }

    @Override
    public String toString() {
        return this.getId() + " " + this.getNaziv();
    }
}