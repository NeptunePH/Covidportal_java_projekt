package main.hr.java.covidportal.sort;

import main.hr.java.covidportal.model.Zupanija;
import java.util.Comparator;

/**
 * Klasa za sortiranje zupanija po postotku zarazenog stanovnistva
 */
public class CovidSorter implements Comparator<Zupanija> {
    @Override
    public int compare(Zupanija z1, Zupanija z2){
        if(z1.getPostotakZarazenosti() < z2.getPostotakZarazenosti()){
            return 1;
        }
        else if(z1.getPostotakZarazenosti() > z2.getPostotakZarazenosti()){
            return -1;
        }
        else{
            return 0;
        }
    }
}