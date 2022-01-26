package main.java.sample;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TextField;
import main.hr.java.covidportal.enums.VrijednostSimptoma;
import main.hr.java.covidportal.niti.DodajSimptomNit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * controller klasa za ekran dodavanja novog simptoma
 */
public class DodavanjeNovogSimptomaController implements Initializable {
    private static final Logger logger = LoggerFactory.getLogger(DodavanjeNovogSimptomaController.class);

    @FXML
    private TextField nazivSimptoma;

    @FXML
    private ListView<String> vrijednostSimptoma;

    @Override
    public void initialize(URL url, ResourceBundle resourcebundle){
        vrijednostSimptoma.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        for(VrijednostSimptoma s : VrijednostSimptoma.values()){
            vrijednostSimptoma.getItems().add(s.getVrijednost());
        }
    }

    /**
     * metoda za dodavanje simptoma u bazu podataka
     */
    public void dodajSimptom(){
        String nazivNovogSimptoma = nazivSimptoma.getText();

        try{
            if(!nazivNovogSimptoma.equals("") && !vrijednostSimptoma.getSelectionModel().isEmpty()) {
                ExecutorService executorService = Executors.newCachedThreadPool();
                DodajSimptomNit dodavanjeSimptomaNit = new DodajSimptomNit(nazivNovogSimptoma,
                        VrijednostSimptoma.valueOf(vrijednostSimptoma.getSelectionModel().getSelectedItem()));
                executorService.execute(dodavanjeSimptomaNit);
                executorService.shutdown();
                executorService.awaitTermination(100, TimeUnit.MILLISECONDS);

                logger.info("Dodan novi simptom.");
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Dodavanje simptoma");
                alert.setHeaderText("Uspjesno dodavanje");
                alert.setContentText("Uspjesno je dodan simptom.");
                alert.showAndWait();
            }
            else{
                logger.error("Nedostaje vrijednost u simptomu.");
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Dodavanje simptoma");
                alert.setHeaderText("Kriva vrijednost");
                alert.setContentText("Nedostaje vrijednost simptoma.");
                alert.showAndWait();
            }
        } catch(InterruptedException ex){
            logger.error("Greska pri otvaranju.");
        }
    }
}