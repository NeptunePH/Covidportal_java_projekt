package main.java.sample;

import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

import main.hr.java.covidportal.model.*;
import main.hr.java.covidportal.niti.DohvacanjeSvihOsobaNit;
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
 * controller klasa za ekran pretrage osoba
 */
public class PretragaOsobaController implements Initializable {
    private static final Logger logger = LoggerFactory.getLogger(PretragaZupanijaController.class);

    /**
     * metoda za ucitavanje osoba iz baze podataka
     * @throws SQLException iznimka baze podataka
     * @throws IOException iznimka properties datoteke
     */
    public static void ucitajOsobe() throws SQLException, IOException, InterruptedException {
        if(!osobe.isEmpty()){
            osobe.clear();
        }
        ExecutorService executorService = Executors.newCachedThreadPool();
        DohvacanjeSvihOsobaNit dohvacenjeSvihOsobaNit = new DohvacanjeSvihOsobaNit();
        executorService.execute(dohvacenjeSvihOsobaNit);
        executorService.shutdown();
        executorService.awaitTermination(500, TimeUnit.MILLISECONDS);
    }

    @FXML
    private TextField imeOsobe;

    @FXML
    private TextField prezimeOsobe;

    @FXML
    private TableView<Osoba> tablicaOsoba;

    @FXML
    private TableColumn<Osoba, Long> idOsobeColumn;

    @FXML
    private TableColumn<Osoba, String> imeOsobeColumn;

    @FXML
    private TableColumn<Osoba, String> prezimeOsobeColumn;

    @FXML
    private TableColumn<Osoba, Integer> starostOsobeColumn;

    @FXML
    private TableColumn<Osoba, String> zupanijaOsobeColumn;

    @FXML
    private TableColumn<Osoba, String> bolestOsobeColumn;

    @FXML
    private TableColumn<Osoba, String> kontaktiraneOsobeColumn;

    protected static List<Osoba> osobe = new ArrayList<>();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            ucitajOsobe();
        } catch(SQLException | IOException | InterruptedException ex){
            logger.error("Greska u bazi podataka.");
            ex.printStackTrace();
        }
        logger.info("Pokrenut ekran pretrage osoba.");

        idOsobeColumn
                .setCellValueFactory(new PropertyValueFactory<Osoba, Long>("id"));

        imeOsobeColumn
                .setCellValueFactory(new PropertyValueFactory<Osoba, String>("ime"));

        prezimeOsobeColumn
                .setCellValueFactory(new PropertyValueFactory<Osoba, String>("prezime"));

        starostOsobeColumn
                .setCellValueFactory(new PropertyValueFactory<Osoba, Integer>("starostOsobe"));

        zupanijaOsobeColumn
                .setCellValueFactory(new PropertyValueFactory<Osoba, String>("zupanija"));

        bolestOsobeColumn
                .setCellValueFactory(new PropertyValueFactory<Osoba, String>("zarazenBolescu"));

        kontaktiraneOsobeColumn
                .setCellValueFactory(d -> new SimpleObjectProperty(d.getValue().getKontaktiraneOsobe().toString()
                .substring(1, d.getValue().getKontaktiraneOsobe().toString().length() - 1)));

        tablicaOsoba.setItems(FXCollections.observableList(osobe));
    }

    /**
     * metoda za pretragu odredjene osobe iz baze podataka
     */
    public void pretragaOsoba(){
        String trazenoImeOsobe = imeOsobe.getText().toUpperCase();
        String trazenoPrezimeOsobe = prezimeOsobe.getText().toUpperCase();

        List<Osoba> filtriraneOsobe = osobe
                .stream()
                .filter(z -> z.getIme().toUpperCase().contains(trazenoImeOsobe)
                        && z.getPrezime().toUpperCase().contains(trazenoPrezimeOsobe))
                .collect(Collectors.toList());

        tablicaOsoba.setItems(FXCollections.observableList(filtriraneOsobe));

        if(tablicaOsoba.getItems().size() > 0){
            logger.info("Pronađena osoba");
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Traženje osobe");
            alert.setHeaderText("Pronađena osoba");
            alert.setContentText("Pronađena je osoba.");
            alert.showAndWait();
        }
        else{
            logger.info("Nije pronađena osoba");
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Traženje osobe");
            alert.setHeaderText("Nije pronađena osoba");
            alert.setContentText("Nije pronađena takva osoba.");
            alert.showAndWait();
        }
    }
}