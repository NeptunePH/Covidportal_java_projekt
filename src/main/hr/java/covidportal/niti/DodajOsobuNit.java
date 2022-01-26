package main.hr.java.covidportal.niti;

import main.hr.java.covidportal.model.Bolest;
import main.hr.java.covidportal.model.Osoba;
import main.hr.java.covidportal.model.Zupanija;
import main.java.sample.BazaPodataka;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

/**
 * dodaje osobu u bazu podataka
 */
public class DodajOsobuNit implements Runnable{
    private static final Logger logger = LoggerFactory.getLogger(DodajOsobuNit.class);
    private static String ime, prezime;
    private static LocalDate datumRodjenja;
    private static Zupanija zupanija;
    private static Bolest zarazenBolescu;
    private static List<Osoba> kontaktiraneOsobe;
    private static Connection veza = null;

    public DodajOsobuNit(String ime, String prezime, LocalDate datumRodjenja, Zupanija zupanija, Bolest zarazenBolescu,
                         List<Osoba> kontaktiraneOsobe) {
        this.ime = ime;
        this.prezime = prezime;
        this.datumRodjenja = datumRodjenja;
        this.zupanija = zupanija;
        this.zarazenBolescu = zarazenBolescu;
        this.kontaktiraneOsobe = kontaktiraneOsobe;
    }

    @Override
    public void run(){
        try{
            otvoriVezuSBazom();
            BazaPodataka.dodajNovuOsobu(veza, new Osoba.Builder()
                    .withIme(ime)
                    .withPrezime(prezime)
                    .atDatumRodjenja(datumRodjenja)
                    .atZupanija(zupanija)
                    .withZarazenBolescu(zarazenBolescu)
                    .withKontaktiraneOsobe(kontaktiraneOsobe)
                    .build());
            zatvoriVezuSBazom();
        } catch(SQLException | IOException | InterruptedException ex){
            ex.printStackTrace();
            logger.info("Greska u niti pri dodavanju osobe.");
        }
    }

    public synchronized void otvoriVezuSBazom() throws SQLException, IOException, InterruptedException{
        if(BazaPodataka.aktivnaVezaSBazomPodataka){
            wait();
        }
        else {
            veza = BazaPodataka.vezaSaBazom();
            BazaPodataka.aktivnaVezaSBazomPodataka = true;
        }
    }

    public synchronized void zatvoriVezuSBazom() throws SQLException {
        BazaPodataka.zatvoriVezuSaBazom(veza);
        BazaPodataka.aktivnaVezaSBazomPodataka = false;
        notifyAll();
    }
}