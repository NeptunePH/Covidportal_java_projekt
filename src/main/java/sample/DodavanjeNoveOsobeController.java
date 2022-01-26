package main.java.sample;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import main.hr.java.covidportal.model.*;
import main.hr.java.covidportal.niti.DodajOsobuNit;
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
 * controller klasa za ekran dodavanja nove osobe
 */
public class DodavanjeNoveOsobeController implements Initializable {
    private static final Logger logger = LoggerFactory.getLogger(DodavanjeNoveOsobeController.class);
    @FXML
    private TextField imeOsobe;

    @FXML
    private TextField prezimeOsobe;

    @FXML
    private DatePicker datumRodjenja;

    @FXML
    private ListView<String> listaZupanijeOsobe;

    @FXML
    private ListView<String> listaBolestiOsobe;

    @FXML
    private ListView<String> listaKontaktiranihOsoba;

    @Override
    public void initialize(URL url, ResourceBundle resourcebundle) {
        try {
            PretragaZupanijaController.ucitajZupanije();
            PretragaOsobaController.ucitajOsobe();
            PretragaBolestiController.ucitajBolestiIViruse();
            List<Bolest> tmpBolesti = new ArrayList<>();
            tmpBolesti.addAll(PretragaBolestiController.bolesti);
            tmpBolesti.addAll(PretragaVirusaController.virusi);

            listaZupanijeOsobe.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
            for (int i = 0; i < PretragaZupanijaController.zupanije.size(); i++) {
                listaZupanijeOsobe.getItems().add(PretragaZupanijaController.zupanije.get(i).getNaziv());
            }
            listaBolestiOsobe.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
            for (int i = 0; i < tmpBolesti.size(); i++) {
                listaBolestiOsobe.getItems().add(tmpBolesti.get(i).getNaziv());
            }
            listaKontaktiranihOsoba.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
            for (int i = 0; i < PretragaOsobaController.osobe.size(); i++) {
                listaKontaktiranihOsoba.getItems().add(PretragaOsobaController.osobe.get(i).getIme() + " " + PretragaOsobaController.osobe.get(i).getPrezime());
            }
        } catch(SQLException | IOException | InterruptedException ex){
            logger.error("Greska u bazi podataka.");
            ex.printStackTrace();
        }
    }

    /**
     * metoda za dodavanje osobe u bazu podataka
     */
    public void dodajOsobu(){
        List<String> listaOdabranihKontaktiranihOsoba;
        String imeNoveOsobe = imeOsobe.getText();
        String prezimeNoveOsobe = prezimeOsobe.getText();
        Zupanija zupanijaOsobe = null;
        Bolest bolestOsobe = null;

        try{
            if(!imeNoveOsobe.equals("") && !prezimeNoveOsobe.equals("") && datumRodjenja.getValue() != null
                    && !listaZupanijeOsobe.getSelectionModel().isEmpty()
                    && !listaBolestiOsobe.getSelectionModel().isEmpty() && !listaKontaktiranihOsoba.getSelectionModel().isEmpty()) {


                for(int i = 0; i < PretragaZupanijaController.zupanije.size(); i++){
                    if(PretragaZupanijaController.zupanije.get(i).getNaziv().equals(listaZupanijeOsobe.getSelectionModel().getSelectedItem())){
                        zupanijaOsobe = PretragaZupanijaController.zupanije.get(i);
                    }
                }

                boolean tmpBoolBolest = false;
                for(int i = 0; i < PretragaBolestiController.bolesti.size(); i++){
                    if(PretragaBolestiController.bolesti.get(i).getNaziv().equals(listaBolestiOsobe.getSelectionModel().getSelectedItem())){
                        bolestOsobe = PretragaBolestiController.bolesti.get(i);
                        tmpBoolBolest = true;
                    }
                }

                if(!tmpBoolBolest){
                    for(int i = 0; i < PretragaVirusaController.virusi.size(); i++){
                        if(PretragaVirusaController.virusi.get(i).getNaziv().equals(listaBolestiOsobe.getSelectionModel().getSelectedItem())){
                            bolestOsobe = PretragaVirusaController.virusi.get(i);
                        }
                    }
                }

                listaOdabranihKontaktiranihOsoba = FXCollections.observableArrayList(listaKontaktiranihOsoba.getSelectionModel().getSelectedItems());
                List<Osoba> odabraneKontaktiraneOsobe = new ArrayList<>();

                for(int i = 0; i < listaOdabranihKontaktiranihOsoba.size(); i++){
                    for(int j = 0; j < PretragaOsobaController.osobe.size(); j++){
                        if(listaOdabranihKontaktiranihOsoba.get(i).equals(PretragaOsobaController.osobe.get(j).getIme() + " "
                                + PretragaOsobaController.osobe.get(j).getPrezime())){
                            odabraneKontaktiraneOsobe.add(PretragaOsobaController.osobe.get(j));
                        }
                    }
                }

                ExecutorService executorService = Executors.newCachedThreadPool();
                DodajOsobuNit dodavanjeOsobeNit = new DodajOsobuNit(imeNoveOsobe, prezimeNoveOsobe,
                        datumRodjenja.getValue(), zupanijaOsobe, bolestOsobe, odabraneKontaktiraneOsobe);
                executorService.execute(dodavanjeOsobeNit);
                executorService.shutdown();
                executorService.awaitTermination(100, TimeUnit.MILLISECONDS);

                PretragaOsobaController.ucitajOsobe();

                logger.info("Dodana nova osoba.");
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Dodavanje osobe");
                alert.setHeaderText("Uspjesno dodavanje");
                alert.setContentText("Uspjesno je dodana osoba.");
                alert.showAndWait();
            }
            else{
                logger.error("Nedostaje vrijednost u osobi.");
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Dodavanje osobe");
                alert.setHeaderText("Kriva vrijednost");
                alert.setContentText("Nedostaje vrijednost osobe.");
                alert.showAndWait();
            }
        } catch(SQLException | IOException | InterruptedException ex){
            logger.error("Greska pri otvaranju.");
            ex.printStackTrace();
        }
    }
}
