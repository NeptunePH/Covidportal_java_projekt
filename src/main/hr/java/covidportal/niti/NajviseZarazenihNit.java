package main.hr.java.covidportal.niti;

import main.hr.java.covidportal.model.Zupanija;
import main.java.sample.PretragaZupanijaController;
import java.util.Comparator;

/**
 * ispisuje ime zupanije sa najvise zarazenih stanovnika u zadanom intervalu
 */
public class NajviseZarazenihNit implements Runnable{
    private static Zupanija najviseZarazenihZupanija;

    @Override
    public void run(){
        while(true) {
            najviseZarazenihZupanija = PretragaZupanijaController.zupanije
                    .stream()
                    .max(Comparator.comparing(z -> z.getPostotakZarazenosti()))
                    .get();
            System.out.println("Zupanija sa najvise zarazenih stanovnika je: " + najviseZarazenihZupanija.getNaziv());
            try {
                Thread.sleep(10000);
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        }
    }
}