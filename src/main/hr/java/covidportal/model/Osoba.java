package main.hr.java.covidportal.model;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Klasa osoba. Sadrži sve informacije o pojedinoj osobi
 */

public class Osoba implements Serializable {
    private String ime, prezime;
    private LocalDate datumRodjenja;
    private Zupanija zupanija;
    private Bolest zarazenBolescu;
    private List<Osoba> kontaktiraneOsobe;
    private Long id;

    /**
     * Klasa koja implementira builder pattern
     */

    public static class Builder{
        private String ime, prezime;
        private LocalDate datumRodjenja;
        private Zupanija zupanija;
        private Bolest zarazenBolescu;
        private List<Osoba> kontaktiraneOsobe = new ArrayList<>();
        private Long id;

        /**
         * Konstruktor koji prima ime osobe
         * @param ime ime osobe
         * @return vraća ime
         */
        public Builder withIme(String ime){
            this.ime = ime;
            return this;
        }

        /**
         * Konstruktor koji prima prezime osobe
         * @param prezime prezime osobe
         * @return vraća prezime
         */
        public Builder withPrezime(String prezime){
            this.prezime = prezime;
            return this;
        }

        /**
         * Konstruktor koji prima datum rodjenja osobe
         * @param datumRodjenja datum rodjenja osobe
         * @return vraća datum rodjenja
         */
        public Builder atDatumRodjenja(LocalDate datumRodjenja){
            this.datumRodjenja = datumRodjenja;
            return this;
        }

        /**
         * Konstruktor koji prima županiju osobe
         * @param zupanija županija osobe
         * @return vraća županiju
         */
        public Builder atZupanija(Zupanija zupanija){
            this.zupanija = zupanija;
            return this;
        }

        /**
         * Konstruktor koji prima bolest kojom je osoba zaražena
         * @param zarazenBolescu bolest osobe
         * @return vraća bolest
         */
        public Builder withZarazenBolescu(Bolest zarazenBolescu){
            this.zarazenBolescu = zarazenBolescu;
            return this;
        }

        /**
         * Konstruktor koji prima popis osoba s kojim je trenutna osoba imala kontakt
         * @param kontaktiraneOsobe polje kontaktiranih osoba
         * @return vraća kontaktirane osobe
         */
        public Builder withKontaktiraneOsobe(List<Osoba> kontaktiraneOsobe){
            this.kontaktiraneOsobe = kontaktiraneOsobe;
            return this;
        }

        /**
         *
         * @param id id osobe
         * @return vraca objekt
         */
        public Builder atId(Long id){
            this.id = id;
            return this;
        }

        /**
         * Završna metoda build patterna, unosi sve parametre osobe i stvara novi objekt Osoba
         * @return vraća objekt Osoba
         */
        public Osoba build(){
            Osoba osoba = new Osoba();
            osoba.ime = ime;
            osoba.prezime = prezime;
            osoba.datumRodjenja = datumRodjenja;
            osoba.zupanija = zupanija;
            osoba.zarazenBolescu = zarazenBolescu;
            osoba.kontaktiraneOsobe = kontaktiraneOsobe;
            osoba.id = id;
            //pattern matching
            if(osoba.zarazenBolescu instanceof Virus virus){
                //ima li kontaktiranih osoba
                if(osoba.kontaktiraneOsobe != null) {
                    for (int i = 0; i < osoba.kontaktiraneOsobe.size(); i++) {
                        virus.prelazakZarazeNaOsobu(osoba.kontaktiraneOsobe.get(i));
                    }
                }
            }
            return osoba;
        }
    }

    public Osoba() {

    }

    public String getIme() {
        return ime;
    }

    public void setIme(String ime) {
        this.ime = ime;
    }

    public String getPrezime() {
        return prezime;
    }

    public void setPrezime(String prezime) {
        this.prezime = prezime;
    }

    public LocalDate getDatumRodjenja() {
        return datumRodjenja;
    }

    public void setDatumRodjenja(LocalDate datumRodjenja) {
        this.datumRodjenja = datumRodjenja;
    }

    public Zupanija getZupanija() {
        return zupanija;
    }

    public void setZupanija(Zupanija zupanija) {
        this.zupanija = zupanija;
    }

    public Bolest getZarazenBolescu() {
        return zarazenBolescu;
    }

    public void setZarazenBolescu(Bolest zarazenBolescu) {
        this.zarazenBolescu = zarazenBolescu;
    }

    public List<Osoba> getKontaktiraneOsobe() {
        return kontaktiraneOsobe;
    }

    public void setKontaktiraneOsobe(List<Osoba> kontaktiraneOsobe) {
        this.kontaktiraneOsobe = kontaktiraneOsobe;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    /**
     * metoda za izracunavanje starosti iz datuma
     * @return vraca starost osobe
     */
    public int getStarostOsobe(){
        return Period.between(this.datumRodjenja, LocalDate.now()).getYears();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Osoba)) return false;
        Osoba osoba = (Osoba) o;
        return datumRodjenja == osoba.datumRodjenja &&
                Objects.equals(ime, osoba.ime) &&
                Objects.equals(prezime, osoba.prezime) &&
                Objects.equals(zupanija, osoba.zupanija) &&
                Objects.equals(zarazenBolescu, osoba.zarazenBolescu) &&
                Objects.equals(kontaktiraneOsobe, osoba.kontaktiraneOsobe);
    }

    @Override
    public int hashCode() {
        return Objects.hash(ime, prezime, datumRodjenja, zupanija, zarazenBolescu, kontaktiraneOsobe);
    }

    @Override
    public String toString(){
        return this.getId() + " "
                + this.getIme() + " "
                + this.getPrezime() + " "
                + this.getStarostOsobe();
    }
}
