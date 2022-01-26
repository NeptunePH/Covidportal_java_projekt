package main.java.sample;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class PocetniEkranController implements Initializable {
    @FXML
    public void prikaziEkranZaPretraguZupanija() throws IOException {
        Parent pretragaZupanijaFrame =
                FXMLLoader.load(getClass().getClassLoader().getResource("pretragaZupanija.fxml"));
        Scene pretragaZupanijaScene = new Scene(pretragaZupanijaFrame, 800, 500);
        Main.getMainStage().setScene(pretragaZupanijaScene);
    }

    @FXML
    public void prikaziEkranZaPretraguSimptoma() throws IOException {
        Parent pretragaSimptomaFrame =
                FXMLLoader.load(getClass().getClassLoader().getResource("pretragaSimptoma.fxml"));
        Scene pretragaSimptomaScene = new Scene(pretragaSimptomaFrame, 800, 500);
        Main.getMainStage().setScene(pretragaSimptomaScene);
    }

    @FXML
    public void prikaziEkranZaPretraguBolesti() throws IOException {
        Parent pretragaBolestiFrame =
                FXMLLoader.load(getClass().getClassLoader().getResource("pretragaBolesti.fxml"));
        Scene pretragaBolestiScene = new Scene(pretragaBolestiFrame, 800, 500);
        Main.getMainStage().setScene(pretragaBolestiScene);
    }

    @FXML
    public void prikaziEkranZaPretraguVirusa() throws IOException {
        Parent pretragaVirusaFrame =
                FXMLLoader.load(getClass().getClassLoader().getResource("pretragaVirusa.fxml"));
        Scene pretragaVirusaScene = new Scene(pretragaVirusaFrame, 800, 500);
        Main.getMainStage().setScene(pretragaVirusaScene);
    }

    @FXML
    public void prikaziEkranZaPretraguOsoba() throws IOException {
        Parent pretragaOsobaFrame =
                FXMLLoader.load(getClass().getClassLoader().getResource("pretragaOsoba.fxml"));
        Scene pretragaOsobaScene = new Scene(pretragaOsobaFrame, 800, 500);
        Main.getMainStage().setScene(pretragaOsobaScene);
    }

    @FXML
    public void prikaziEkranZaDodavanjeZupanija() throws IOException{
        Parent dodavenjeZupanijaFrame =
                FXMLLoader.load(getClass().getClassLoader().getResource("dodavanjeNoveZupanije.fxml"));
        Scene dodavanjeZupanijaScene = new Scene(dodavenjeZupanijaFrame, 800, 500);
        Main.getMainStage().setScene(dodavanjeZupanijaScene);
    }

    @FXML
    public void prikaziEkranZaDodavanjeSimptoma() throws IOException{
        Parent dodavenjeSimptomaFrame =
                FXMLLoader.load(getClass().getClassLoader().getResource("dodavanjeNovogSimptoma.fxml"));
        Scene dodavanjeSimptomaScene = new Scene(dodavenjeSimptomaFrame, 800, 500);
        Main.getMainStage().setScene(dodavanjeSimptomaScene);
    }

    @FXML
    public void prikaziEkranZaDodavanjeBolesti() throws IOException{
        Parent dodavenjeBolestiFrame =
                FXMLLoader.load(getClass().getClassLoader().getResource("dodavanjeNoveBolesti.fxml"));
        Scene dodavanjeBolestiScene = new Scene(dodavenjeBolestiFrame, 800, 500);
        Main.getMainStage().setScene(dodavanjeBolestiScene);
    }

    @FXML
    public void prikaziEkranZaDodavanjeVirusa() throws IOException{
        Parent dodavenjeVirusaFrame =
                FXMLLoader.load(getClass().getClassLoader().getResource("dodavanjeNovogVirusa.fxml"));
        Scene dodavanjeVirusaScene = new Scene(dodavenjeVirusaFrame, 800, 500);
        Main.getMainStage().setScene(dodavanjeVirusaScene);
    }

    @FXML
    public void prikaziEkranZaDodavanjeOsoba() throws IOException{
        Parent dodavenjeOsobaFrame =
                FXMLLoader.load(getClass().getClassLoader().getResource("dodavanjeNoveOsobe.fxml"));
        Scene dodavanjeOsobaScene = new Scene(dodavenjeOsobaFrame, 800, 500);
        Main.getMainStage().setScene(dodavanjeOsobaScene);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
    }
}