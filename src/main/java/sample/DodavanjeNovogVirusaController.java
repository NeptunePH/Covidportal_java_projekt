package main.java.sample;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TextField;
import main.hr.java.covidportal.model.Simptom;
import main.hr.java.covidportal.niti.DodajVirusNit;
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
 * controller klasa za ekran dodavanja novog virusa
 */
public class DodavanjeNovogVirusaController implements Initializable {
    private static final Logger logger = LoggerFactory.getLogger(DodavanjeNovogVirusaController.class);

    @FXML
    private TextField nazivVirusa;

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
     * metoda za dodavanje virusa u bazu podataka
     */
    public void dodajVirus(){
        List<String> stringOdabraniSimptomi;
        String nazivNovogVirusa = nazivVirusa.getText();

        try{
            if(!nazivNovogVirusa.equals("") && !listaSimptoma.getSelectionModel().isEmpty()) {

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
                DodajVirusNit dodavanjeVirusaNit = new DodajVirusNit(nazivNovogVirusa, odabraniSimptomi);
                executorService.execute(dodavanjeVirusaNit);
                executorService.shutdown();
                executorService.awaitTermination(100, TimeUnit.MILLISECONDS);

                PretragaBolestiController.ucitajBolestiIViruse();

                logger.info("Dodan novi virus.");
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Dodavanje virusa");
                alert.setHeaderText("Uspjesno dodavanje");
                alert.setContentText("Uspjesno je dodan virus.");
                alert.showAndWait();
            }
            else{
                logger.error("Nedostaje vrijednost u virusu.");
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Dodavanje virusa");
                alert.setHeaderText("Kriva vrijednost");
                alert.setContentText("Nedostaje vrijednost virusa.");
                alert.showAndWait();
            }
        } catch(InterruptedException ex){
            logger.error("Greska pri otvaranju.");
            ex.printStackTrace();
        }
    }
}