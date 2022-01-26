package main.hr.java.covidportal.niti;

import main.hr.java.covidportal.model.Zupanija;
import main.java.sample.BazaPodataka;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * dodaje zupaniju u bazu podataka
 */
public class DodajZupanijuNit implements Runnable{
    private static final Logger logger = LoggerFactory.getLogger(DodajZupanijuNit.class);
    private static String naziv;
    private static Integer brojStanovnika;
    private static Integer brojZarazenihStanovnika;
    private static Connection veza = null;

    public DodajZupanijuNit(String naziv, Integer brojStanovnika, Integer brojZarazenihStanovnika) {
        this.naziv = naziv;
        this.brojStanovnika = brojStanovnika;
        this.brojZarazenihStanovnika = brojZarazenihStanovnika;
    }

    @Override
    public void run(){
        try{
            otvoriVezuSBazom();
            BazaPodataka.dodajNovuZupaniju(veza, new Zupanija(0L, naziv, brojStanovnika, brojZarazenihStanovnika));
            zatvoriVezuSBazom();
        } catch(SQLException | IOException | InterruptedException ex){
            ex.printStackTrace();
            logger.info("Greska u niti pri dodavanju zupanije.");
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