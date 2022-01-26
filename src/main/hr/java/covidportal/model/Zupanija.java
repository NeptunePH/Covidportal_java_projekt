package main.hr.java.covidportal.model;

import java.io.Serializable;
import java.util.Objects;

/**
 * Klasa koja reprezentira županiju, sadrži broj stanovnika. Nasljeđuje klasu ImenovaniEntitet.
 */
public class Zupanija extends ImenovaniEntitet implements Serializable {
    private Integer brojZarazenih, brojStanovnika;
    private double postotakZarazenosti;

    /**
     * Konstruktor klase. Prima naziv i broj stanovnika, te naziv prosljeđuje klasi ImenovaniEntitet
     * @param id id entiteta
     * @param naziv naziv županije
     * @param brojStanovnika broj stanovnika županija
     * @param brojZarazenih broj zaraženih stanovnika
     */
    public Zupanija(Long id, String naziv, Integer brojStanovnika, Integer brojZarazenih) {
        super(naziv, id);
        this.brojStanovnika = brojStanovnika;
        this.brojZarazenih = brojZarazenih;
        this.postotakZarazenosti = (brojZarazenih / (double) brojStanovnika) * 100;
    }

    public Integer getBrojZarazenih() {
        return brojZarazenih;
    }

    public Integer getBrojStanovnika() {
        return brojStanovnika;
    }

    public double getPostotakZarazenosti() {
        return postotakZarazenosti;
    }

    public void setBrojStanovnika(Integer brojStanovnika) {
        this.brojStanovnika = brojStanovnika;
    }

    public void setBrojZarazenih(Integer brojZarazenih){
        this.brojZarazenih = brojZarazenih;
    }

    public void setPostotakZarazenosti(double postotakZarazenosti) {
        this.postotakZarazenosti = postotakZarazenosti;
    }

    @Override
    public String toString(){
        return this.getId() + " " + this.getNaziv() + " " + this.getPostotakZarazenosti() + "%";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Zupanija)) return false;
        if (!super.equals(o)) return false;
        Zupanija zupanija = (Zupanija) o;
        return brojStanovnika == zupanija.brojStanovnika &&
                Objects.equals(brojZarazenih, zupanija.brojZarazenih);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), brojStanovnika, brojZarazenih);
    }
}
