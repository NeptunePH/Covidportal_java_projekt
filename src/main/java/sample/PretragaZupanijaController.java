package main.java.sample;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.control.Alert;
import javafx.util.Duration;
import main.hr.java.covidportal.model.Zupanija;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

import main.hr.java.covidportal.niti.DohvacanjeSvihZupanijaNit;
import main.hr.java.covidportal.niti.NajviseZarazenihNit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;


public class PretragaZupanijaController implements Initializable {
    private static final Logger logger = LoggerFactory.getLogger(PretragaZupanijaController.class);

    /**
     * metoda za ucitavanje zupanija iz baze podataka
     * @throws SQLException iznimka baze podataka
     * @throws IOException iznimka properties datoteke
     */
    public static void ucitajZupanije() throws InterruptedException {
        if(!zupanije.isEmpty()) {
            zupanije.clear();
        }
        ExecutorService executorServiceUcitavanje = Executors.newCachedThreadPool();
        DohvacanjeSvihZupanijaNit dohvacanjeSvihZupanijaNit = new DohvacanjeSvihZupanijaNit();
        executorServiceUcitavanje.execute(dohvacanjeSvihZupanijaNit);
        executorServiceUcitavanje.shutdown();
        executorServiceUcitavanje.awaitTermination(500, TimeUnit.MILLISECONDS);
    }

    @FXML
    private TextField nazivZupanije;

    @FXML
    private TableView<Zupanija> tablicaZupanija;

    @FXML
    private TableColumn<Zupanija, Long> idZupanijeColumn;

    @FXML
    private TableColumn<Zupanija, String> nazivZupanijeColumn;

    @FXML
    private TableColumn<Zupanija, Integer> brojStanovnikaColumn;

    @FXML
    private TableColumn<Zupanija, Integer> brojZarazenihColumn;

    public static List<Zupanija> zupanije = new ArrayList<>();
    private NajviseZarazenihNit najviseZarazenih = new NajviseZarazenihNit();
    private ExecutorService executorService = Executors.newCachedThreadPool();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            ucitajZupanije();
        } catch (InterruptedException ex) {
            logger.error("Greska u bazi podataka.");
            ex.printStackTrace();
        }
        logger.info("Pokrenut ekran pretrage zupanija.");

        idZupanijeColumn
                .setCellValueFactory(new PropertyValueFactory<Zupanija, Long>("id"));

        nazivZupanijeColumn
                .setCellValueFactory(new PropertyValueFactory<Zupanija, String>("naziv"));

        brojStanovnikaColumn
                .setCellValueFactory(new PropertyValueFactory<Zupanija, Integer>("brojStanovnika"));

        brojZarazenihColumn
                .setCellValueFactory(new PropertyValueFactory<Zupanija, Integer>("brojZarazenih"));

        tablicaZupanija.setItems(FXCollections.observableList(zupanije));

        executorService.execute(najviseZarazenih);

        Timeline clock = new Timeline(new KeyFrame(Duration.ZERO, e -> {
            Main.mainStage.setTitle(PretragaZupanijaController.zupanije
                    .stream()
                    .max(Comparator.comparing(z -> z.getPostotakZarazenosti()))
                    .get()
                    .getNaziv());
        }), new KeyFrame(Duration.seconds(10)));

        clock.setCycleCount(Animation.INDEFINITE);
        clock.play();
    }

    /**
     * metoda za pretragu odredjene zupanije iz baze podataka
     */
    public void pretragaZupanije(){
        String trazenaZupanija = nazivZupanije.getText().toUpperCase();

        List<Zupanija> filtriraneZupanije = zupanije
                .stream()
                .filter(z -> z.getNaziv().toUpperCase().contains(trazenaZupanija))
                .collect(Collectors.toList());

        tablicaZupanija.setItems(FXCollections.observableList(filtriraneZupanije));

        if(tablicaZupanija.getItems().size() > 0){
            logger.info("Pronađena zupanija");
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Traženje zupanije");
            alert.setHeaderText("Pronađena zupanija");
            alert.setContentText("Pronađena je zupanija.");
            alert.showAndWait();
        }
        else{
            logger.info("Nije pronađena zupanija");
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Traženje zupanije");
            alert.setHeaderText("Nije pronađena zupanija");
            alert.setContentText("Nije pronađena takva zupanija.");
            alert.showAndWait();
        }
    }
}