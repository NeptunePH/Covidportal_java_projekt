package main.hr.java.covidportal.niti;

import main.hr.java.covidportal.enums.VrijednostSimptoma;
import main.hr.java.covidportal.model.Simptom;
import main.java.sample.BazaPodataka;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * dodaje simptom u bazu podataka
 */
public class DodajSimptomNit implements Runnable{
    private static final Logger logger = LoggerFactory.getLogger(DodajSimptomNit.class);
    private static String naziv;
    private static VrijednostSimptoma vrijednostSimptoma;
    private static Connection veza = null;

    public DodajSimptomNit(String naziv, VrijednostSimptoma vrijednostSimptoma) {
        this.naziv = naziv;
        this.vrijednostSimptoma = vrijednostSimptoma;
    }

    @Override
    public void run(){
        try{
            otvoriVezuSBazom();
            BazaPodataka.dodajNoviSimptom(veza, new Simptom(0L, naziv, vrijednostSimptoma));
            zatvoriVezuSBazom();
        } catch(SQLException | IOException | InterruptedException ex){
            ex.printStackTrace();
            logger.info("Greska u dodavanju simptoma.");
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
