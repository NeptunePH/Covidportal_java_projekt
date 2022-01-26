package main.hr.java.covidportal.niti;

import main.hr.java.covidportal.model.Osoba;
import main.java.sample.BazaPodataka;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * dohvaca jednu osobu iz baze podataka
 */
public class DohvatiOsobuNit implements Runnable{
    private static Connection veza = null;
    private static Long id = null;
    Osoba dohvacenaOsoba = null;

    public DohvatiOsobuNit(Long id){
        this.id = id;
    }

    @Override
    public void run(){
        try{
            otvoriVezuSBazom();
            dohvacenaOsoba = BazaPodataka.dohvatiJednuOsobu(veza, id);
            zatvoriVezuSBazom();
        } catch(SQLException | IOException | InterruptedException ex){
            ex.printStackTrace();
        }
    }

    public Long getId() {
        return id;
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
