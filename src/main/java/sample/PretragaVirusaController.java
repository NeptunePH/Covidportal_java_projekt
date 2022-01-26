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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;


public class PretragaVirusaController implements Initializable {
    private static final Logger logger = LoggerFactory.getLogger(PretragaBolestiController.class);

    @FXML
    private TextField nazivVirusa;

    @FXML
    private TableView<Virus> tablicaVirusa;

    @FXML
    private TableColumn<Virus, Long> idVirusaColumn;

    @FXML
    private TableColumn<Virus, String> nazivVirusaColumn;

    @FXML
    private TableColumn<Virus, String> simptomiColumn;

    protected static List<Virus> virusi = new ArrayList<>();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            PretragaBolestiController.ucitajBolestiIViruse();
        } catch(InterruptedException ex){
            logger.error("Greska u bazi podataka.");
            ex.printStackTrace();
        }
        logger.info("Pokrenut ekran pretrage virusa");

        idVirusaColumn
                .setCellValueFactory(new PropertyValueFactory<Virus, Long>("id"));

        nazivVirusaColumn
                .setCellValueFactory(new PropertyValueFactory<Virus, String>("naziv"));

        simptomiColumn
                .setCellValueFactory(d->new SimpleObjectProperty(d.getValue().getSimptomi().toString()
                        .substring(1, d.getValue().getSimptomi().toString().length() - 1)));

        tablicaVirusa.setItems(FXCollections.observableList(virusi));
    }

    /**
     * metoda za pretragu odredjenog virusa iz baze podataka
     */
    public void pretragaVirusa(){
        String trazeniVirus = nazivVirusa.getText().toUpperCase();

        List<Virus> filtriraniVirusi = virusi
                .stream()
                .filter(z -> z.getNaziv().toUpperCase().contains(trazeniVirus))
                .collect(Collectors.toList());

        tablicaVirusa.setItems(FXCollections.observableList(filtriraniVirusi));

        if(tablicaVirusa.getItems().size() > 0){
            logger.info("Pronađen virus");
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Traženje virusa");
            alert.setHeaderText("Pronađen virus");
            alert.setContentText("Pronađen je virus.");
            alert.showAndWait();
        }
        else{
            logger.info("Nije pronađen virus");
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Traženje virusa");
            alert.setHeaderText("Nije pronađen virus");
            alert.setContentText("Nije pronađen takav virus.");
            alert.showAndWait();
        }
    }
}