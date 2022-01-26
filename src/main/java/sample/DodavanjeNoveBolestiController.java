package main.java.sample;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TextField;
import main.hr.java.covidportal.model.*;
import main.hr.java.covidportal.niti.DodajBolestNit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * controller klasa za ekran dodavanja nove bolesti
 */
public class DodavanjeNoveBolestiController implements Initializable {
    private static final Logger logger = LoggerFactory.getLogger(DodavanjeNoveBolestiController.class);

    @FXML
    private TextField nazivBolesti;

    @FXML
    private ListView<String> listaSimptoma;

    @Override
    public void initialize(URL url, ResourceBundle resourcebundle){
        try {
            PretragaSimptomaController.ucitajSimptome();
            listaSimptoma.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
            for (int i = 0; i < PretragaSimptomaController.simptomi.size(); i++) {
                listaSimptoma.getItems().add(PretragaSimptomaController.simptomi.get(i).getNaziv());
            }
        } catch(InterruptedException ex){
            logger.error("Greska u bazi podataka.");
            ex.printStackTrace();
        }

    }

    /**
     * metoda za dodavanje bolesti u bazu podataka
     */
    public void dodajBolest(){
        List<String> stringOdabraniSimptomi;
        String nazivNoveBolesti = nazivBolesti.getText();

        try{
            if(!nazivNoveBolesti.equals("") && !listaSimptoma.getSelectionModel().isEmpty()) {

                stringOdabraniSimptomi = FXCollections.observableArrayList(listaSimptoma.getSelectionModel().getSelectedItems());
                List<Simptom> odabraniSimptomi = new ArrayList<>();

                for(int i = 0; i < stringOdabraniSimptomi.size(); i++){
                    for(int j = 0; j < PretragaSimptomaController.simptomi.size(); j++){
                        if(stringOdabraniSimptomi.get(i).equals(PretragaSimptomaController.simptomi.get(j).getNaziv())){
                            odabraniSimptomi.add(PretragaSimptomaController.simptomi.get(j));
                        }
                    }
                }

                ExecutorService executorService = Executors.newCachedThreadPool();
                DodajBolestNit dodavanjeBolestiNit = new DodajBolestNit(nazivNoveBolesti, odabraniSimptomi);
                executorService.execute(dodavanjeBolestiNit);
                executorService.shutdown();
                executorService.awaitTermination(100, TimeUnit.MILLISECONDS);

                PretragaBolestiController.ucitajBolestiIViruse();

                logger.info("Dodana nova bolest.");
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Dodavanje bolesti");
                alert.setHeaderText("Uspjesno dodavanje");
                alert.setContentText("Uspjesno je dodana bolest.");
                alert.showAndWait();
            }
            else{
                logger.error("Nedostaje vrijednost u bolesti.");
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Dodavanje bolesti");
                alert.setHeaderText("Kriva vrijednost");
                alert.setContentText("Nedostaje vrijednost bolesti.");
                alert.showAndWait();
            }
        } catch(InterruptedException ex){
            logger.error("Greska u bazi podataka.");
            ex.printStackTrace();
        }
    }
}