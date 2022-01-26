package main.hr.java.covidportal.genericsi;

import main.hr.java.covidportal.model.*;
import main.hr.java.covidportal.model.Osoba;
import main.hr.java.covidportal.model.Virus;

import java.util.ArrayList;
import java.util.List;

/**
 * sadrzi liste virusa i osoba zarazene tim virusima
 * @param <T> parametar nasljeduje Virus
 * @param <S> parametar nasljeduje Osoba
 */
public class KlinikaZaInfektivneBolesti <T extends Virus, S extends Osoba>{
    private List<T> virusiAplikacije;
    private List<S> listaOsoba;

    /**
     * konstruktor klase, instancira liste za Osobe i Viruse
     */
    public KlinikaZaInfektivneBolesti() {
        this.virusiAplikacije = new ArrayList<>();
        this.listaOsoba = new ArrayList<>();
    }

    public List<T> getVirusiAplikacije() {
        return virusiAplikacije;
    }

    public List<S> getListaOsoba() {
        return listaOsoba;
    }

    /**
     * metoda za dodavanje virusa u listu
     * @param virus virus koji dodajemo u listu
     */
    public void addVirus(T virus){
        this.virusiAplikacije.add(virus);
    }

    /**
     * metoda za dodavanje osoba u listu
     * @param osoba osoba koju dodajemo u listu
     */
    public void addOsoba(S osoba){
        this.listaOsoba.add(osoba);
    }
}