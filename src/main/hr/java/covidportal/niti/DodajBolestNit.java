package main.hr.java.covidportal.niti;

import main.hr.java.covidportal.model.*;
import main.java.sample.BazaPodataka;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

/**
 * dodaje bolest u bazu podataka
 */
public class DodajBolestNit implements Runnable{
    private static final Logger logger = LoggerFactory.getLogger(DodajBolestNit.class);
    private static String naziv;
    private static List<Simptom> simptomi;
    private static Connection veza = null;

    public DodajBolestNit(String naziv, List<Simptom> simptomi) {
        this.naziv = naziv;
        this.naziv = naziv;
        this.simptomi = simptomi;
    }

    @Override
    public void run(){
        try{
            otvoriVezuSBazom();
            BazaPodataka.dodajNovuBolest(veza, new Bolest(0L, naziv, simptomi));
            zatvoriVezuSBazom();
        } catch(SQLException | IOException | InterruptedException ex){
            ex.printStackTrace();
            logger.info("Greska u niti pri dodavanju bolesti.");
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
