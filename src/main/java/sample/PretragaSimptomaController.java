package main.java.sample;

import javafx.scene.control.Alert;
import main.hr.java.covidportal.model.Simptom;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import main.hr.java.covidportal.niti.DohvacanjeSvihSimptomaNit;
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
import java.util.stream.Collectors;

public class PretragaSimptomaController implements Initializable {
    private static final Logger logger = LoggerFactory.getLogger(PretragaSimptomaController.class);

    /**
     * metoda za ucitavanje simptoma iz baze podataka
     * @throws SQLException iznimka baze podataka
     * @throws IOException iznimka properties datoteke
     */
    public static void ucitajSimptome() throws InterruptedException {
        if(!simptomi.isEmpty()) {
            simptomi.clear();
        }
        ExecutorService executorService = Executors.newCachedThreadPool();
        DohvacanjeSvihSimptomaNit dohvacanjeSvihSimptomaNit = new DohvacanjeSvihSimptomaNit();
        executorService.execute(dohvacanjeSvihSimptomaNit);
        executorService.shutdown();
        executorService.awaitTermination(100, TimeUnit.MILLISECONDS);
    }

    @FXML
    private TextField nazivSimptoma;

    @FXML
    private TableView<Simptom> tablicaSimptoma;

    @FXML
    private TableColumn<Simptom, Long> idSimptomaColumn;

    @FXML
    private TableColumn<Simptom, String> nazivSimptomaColumn;

    @FXML
    private TableColumn<Simptom, String> vrijednostSimptomaColumn;

    protected static List<Simptom> simptomi = new ArrayList<>();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            ucitajSimptome();
        } catch(InterruptedException ex){
            logger.error("Greska u bazi podataka.");
            ex.printStackTrace();
        }
        logger.info("Pokrenut ekran pretrage simptoma.");

        idSimptomaColumn
                .setCellValueFactory(new PropertyValueFactory<Simptom, Long>("id"));

        nazivSimptomaColumn
                .setCellValueFactory(new PropertyValueFactory<Simptom, String>("naziv"));

        vrijednostSimptomaColumn
                .setCellValueFactory(new PropertyValueFactory<Simptom, String>("vrijednost"));

        tablicaSimptoma.setItems(FXCollections.observableList(simptomi));
    }

    /**
     * metoda za pretragu odredjenog simptoma iz baze podataka
     */
    public void pretragaSimptoma(){
       String trazeniSimptom = nazivSimptoma.getText().toUpperCase();

        List<Simptom> filtriraneZupanije = simptomi
                .stream()
                .filter(z -> z.getNaziv().toUpperCase().contains(trazeniSimptom))
                .collect(Collectors.toList());

        tablicaSimptoma.setItems(FXCollections.observableList(filtriraneZupanije));

        if(tablicaSimptoma.getItems().size() > 0){
            logger.info("Pronađen simptom");
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Traženje simptoma");
            alert.setHeaderText("Pronađen simptom");
            alert.setContentText("Pronađen je simptom.");
            alert.showAndWait();
        }
        else{
            logger.info("Nije pronađen simptom");
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Traženje simptoma");
            alert.setHeaderText("Nije pronađen simptom");
            alert.setContentText("Nije pronađen takav simptom.");
            alert.showAndWait();
        }
    }
}
