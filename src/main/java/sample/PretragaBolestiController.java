package main.java.sample;

import javafx.scene.control.Alert;
import main.hr.java.covidportal.model.*;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

import main.hr.java.covidportal.niti.DohvacanjeSvihBolestiNit;
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

/**
 * controller klasa za ekran pretrage bolesti baze podataka
 */
public class PretragaBolestiController implements Initializable {
    private static final Logger logger = LoggerFactory.getLogger(PretragaBolestiController.class);

    /**
     * metoda za ucitavanje bolesti i virusa iz baze podataka
     * @throws SQLException iznimka baze podataka
     * @throws IOException iznimka properties datoteke
     */
    public static void ucitajBolestiIViruse() throws InterruptedException {
        if(!PretragaBolestiController.bolesti.isEmpty() || !PretragaVirusaController.virusi.isEmpty()) {
            PretragaBolestiController.bolesti.clear();
            PretragaVirusaController.virusi.clear();
        }

        ExecutorService executorService = Executors.newCachedThreadPool();
        DohvacanjeSvihBolestiNit dohvacanjeSvihBolestiNit = new DohvacanjeSvihBolestiNit();
        executorService.execute(dohvacanjeSvihBolestiNit);
        executorService.shutdown();
        executorService.awaitTermination(100, TimeUnit.MILLISECONDS);
    }

    @FXML
    private TextField nazivBolesti;

    @FXML
    private TableView<Bolest> tablicaBolesti;

    @FXML
    private TableColumn<Bolest, Long> idBolestiColumn;

    @FXML
    private TableColumn<Bolest, String> nazivBolestiColumn;

    @FXML
    private TableColumn<Bolest, String> simptomiColumn;

    protected static List<Bolest> bolesti = new ArrayList<>();


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            ucitajBolestiIViruse();
        } catch(InterruptedException ex){
            logger.error("Greska u bazi podataka.");
            ex.printStackTrace();
        }
        logger.info("Pokrenut ekran pretrage bolesti");

        idBolestiColumn
                .setCellValueFactory(new PropertyValueFactory<Bolest, Long>("id"));

        nazivBolestiColumn
                .setCellValueFactory(new PropertyValueFactory<Bolest, String>("naziv"));

        simptomiColumn
                .setCellValueFactory(d->new SimpleObjectProperty(d.getValue().getSimptomi().toString()
                        .substring(1, d.getValue().getSimptomi().toString().length() - 1)));

        tablicaBolesti.setItems(FXCollections.observableList(bolesti));
    }

    /**
     * metoda za pretragu odredjene bolesti
     */
    public void pretragaBolesti(){
        String trazenaBolest = nazivBolesti.getText().toUpperCase();

        List<Bolest> filtriraneBolesti = bolesti
                .stream()
                .filter(z -> z.getNaziv().toUpperCase().contains(trazenaBolest))
                .collect(Collectors.toList());

        tablicaBolesti.setItems(FXCollections.observableList(filtriraneBolesti));

        if(tablicaBolesti.getItems().size() > 0){
            logger.info("Pronađena bolest");
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Traženje bolesti");
            alert.setHeaderText("Pronađena bolest");
            alert.setContentText("Pronađena je bolest.");
            alert.showAndWait();
        }
        else{
            logger.info("Pronađena bolest");
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Traženje bolesti");
            alert.setHeaderText("Nije pronađena bolest");
            alert.setContentText("Nije pronađena takva bolest.");
            alert.showAndWait();
        }
    }
}