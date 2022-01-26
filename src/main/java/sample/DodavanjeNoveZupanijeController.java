package main.java.sample;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import main.hr.java.covidportal.niti.DodajZupanijuNit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * controller klasa za ekran dodavanja nove zupanije
 */
public class DodavanjeNoveZupanijeController implements Initializable {
    private static final Logger logger = LoggerFactory.getLogger(DodavanjeNoveZupanijeController.class);
    @FXML
    private TextField nazivZupanije;

    @FXML
    private TextField brojStanovnika;

    @FXML
    private TextField brojZarazenih;

    @Override
    public void initialize(URL url, ResourceBundle resourcebundle){
    }

    /**
     * metoda za dodavanje zupanije u bazu podataka
     */
    public void dodajZupaniju(){
        String nazivNoveZupanije = nazivZupanije.getText();
        String brojStanovnikaZupanije = brojStanovnika.getText();
        String brojZarazenihZupanije = brojZarazenih.getText();

        try{
            if(!nazivNoveZupanije.equals("") && !brojStanovnikaZupanije.equals("") && !brojZarazenihZupanije.equals("")
                    && brojStanovnikaZupanije.matches("[0-9]+") && brojZarazenihZupanije.matches("[0-9]+")) {

                ExecutorService executorService = Executors.newCachedThreadPool();
                DodajZupanijuNit dodavanjeZupanijeNit = new DodajZupanijuNit(nazivNoveZupanije,
                        Integer.parseInt(brojStanovnikaZupanije), Integer.parseInt(brojZarazenihZupanije));
                executorService.execute(dodavanjeZupanijeNit);
                executorService.shutdown();
                executorService.awaitTermination(100, TimeUnit.MILLISECONDS);

                logger.info("Dodana nova zupanija.");
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Dodavanje županije");
                alert.setHeaderText("Uspjesno dodavanje");
                alert.setContentText("Uspjesno je dodana zupanija.");
                alert.showAndWait();
            }
            else{
                logger.error("Nedostaje vrijednost u zupaniji.");
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Dodavanje zupanije");
                alert.setHeaderText("Kriva vrijednost");
                alert.setContentText("Nedostaje vrijednost zupanije.");
                alert.showAndWait();
            }
        } catch(InterruptedException ex){
            logger.error("Greska kod niti.");
            ex.printStackTrace();
        }
    }
}