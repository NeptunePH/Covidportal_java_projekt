package main.java.sample;

import main.hr.java.covidportal.enums.VrijednostSimptoma;
import main.hr.java.covidportal.model.*;

import java.io.FileReader;
import java.io.IOException;

import java.sql.*;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * klasa koja sadrzi metode za rad sa bazom podataka
 */
public class BazaPodataka {
    private static final Logger logger = LoggerFactory.getLogger(DodavanjeNoveBolestiController.class);
    public static boolean aktivnaVezaSBazomPodataka = false;

    public static synchronized Connection vezaSaBazom() throws SQLException, IOException {
        Properties properties = new Properties();
        properties.load(new FileReader("src\\main\\resources\\baza.properties"));

        String urlBaze = properties.getProperty("bazaPodatakaURL");
        String username = properties.getProperty("username");
        String password = properties.getProperty("password");

        Connection veza = DriverManager.getConnection(urlBaze, username, password);

        logger.info("Uspjesno spajanje sa bazom.");
        return veza;
    }

    public static void zatvoriVezuSaBazom(Connection veza) throws SQLException{
        logger.info("Prekidanje veze sa bazom.");
        veza.close();
    }

    public static void dohvatiSveSimptome(Connection veza) throws SQLException, IOException {
        List<Simptom> dohvaceniSimptomi = new ArrayList<>();

        Statement statement = veza.createStatement();
        ResultSet result = statement.executeQuery("SELECT * FROM SIMPTOM");

        while(result.next()) {
            long id = result.getInt("ID");
            String naziv = result.getString("NAZIV");
            String vrijednost = result.getString("VRIJEDNOST");

            dohvaceniSimptomi.add(new Simptom(id, naziv, VrijednostSimptoma.valueOf(vrijednost.toUpperCase())));
        }

        PretragaSimptomaController.simptomi = dohvaceniSimptomi;
    }

    public static Simptom dohvatiJednogSimptoma(Connection veza, Long id) throws SQLException, IOException{
        Simptom dohvaceniSimptom = null;

        PreparedStatement statement = veza.prepareStatement("SELECT * FROM SIMPTOM WHERE ID = ?");
        statement.setLong(1, id);

        ResultSet rs = statement.executeQuery();
        while (rs.next()) {
            dohvaceniSimptom = new Simptom(Long.valueOf(rs.getInt("ID")), rs.getString("NAZIV"),
                    VrijednostSimptoma.valueOf(rs.getString("VRIJEDNOST").toUpperCase()));
        }

        return dohvaceniSimptom;
    }

    public static void dodajNoviSimptom(Connection veza, Simptom noviSimptom) throws SQLException, IOException{
        PreparedStatement statement = veza.prepareStatement("INSERT INTO SIMPTOM(NAZIV, VRIJEDNOST) VALUES (?, ?)",
                Statement.RETURN_GENERATED_KEYS);

        statement.setString(1, noviSimptom.getNaziv());
        statement.setString(2, noviSimptom.getVrijednost());
        statement.executeUpdate();

        ResultSet generiraniKey = statement.getGeneratedKeys();
        Long generiraniId = null;
        while(generiraniKey.next()){
            generiraniId = generiraniKey.getLong(1);
        }
        noviSimptom.setId(generiraniId);
    }

    public static void dohvatiSveBolesti(Connection veza) throws SQLException, IOException{
        List<Bolest> dohvaceneBolesti  = new ArrayList<>();

        Statement statement = veza.createStatement();
        ResultSet result = statement.executeQuery("SELECT * FROM BOLEST");

        while(result.next()){
            Long id = result.getLong("ID");
            String naziv = result.getString("NAZIV");
            boolean isVirus = result.getBoolean("VIRUS");

            if(!isVirus){
                dohvaceneBolesti.add(new Bolest(id, naziv, null));
            }
            else{
                dohvaceneBolesti.add(new Virus(id, naziv, null));
            }
        }

        for(int i = 0; i < dohvaceneBolesti.size(); i++) {
            PreparedStatement preparedStatement = veza.prepareStatement("SELECT * FROM BOLEST_SIMPTOM WHERE BOLEST_ID = ?");
            preparedStatement.setLong(1, dohvaceneBolesti.get(i).getId());
            ResultSet preparedStatementResult = preparedStatement.executeQuery();

            List<Simptom> simptomiBolesti = new ArrayList<>();

            while (preparedStatementResult.next()) {
                Long id = preparedStatementResult.getLong("SIMPTOM_ID");
                simptomiBolesti.add(dohvatiJednogSimptoma(veza, id));
            }

            dohvaceneBolesti.get(i).setSimptomi(simptomiBolesti);
        }

        for (int i = 0; i < dohvaceneBolesti.size(); i++) {
            if (dohvaceneBolesti.get(i) instanceof Virus) {
                PretragaVirusaController.virusi.add((Virus) dohvaceneBolesti.get(i));
            } else {
                PretragaBolestiController.bolesti.add(dohvaceneBolesti.get(i));
            }
        }
    }

    public static Bolest dohvatiJednuBolest(Connection veza, Long id) throws SQLException, IOException{
        Bolest dohvacenaBolest = null;

        PreparedStatement statement = veza.prepareStatement("SELECT * FROM BOLEST WHERE ID = ?");
        statement.setLong(1, id);

        ResultSet rs = statement.executeQuery();
        while (rs.next()) {
            dohvacenaBolest = new Bolest(Long.valueOf(rs.getInt("ID")), rs.getString("NAZIV"), null);
        }

        return dohvacenaBolest;
    }

    public static void dodajNovuBolest(Connection veza, Bolest novaBolest) throws SQLException, IOException{
        //ALTER TABLE BOLEST ALTER COLUMN ID RESTART WITH 4
        PreparedStatement statement = veza.prepareStatement("INSERT INTO BOLEST(NAZIV, VIRUS) VALUES (?, ?)",
                Statement.RETURN_GENERATED_KEYS);

        statement.setString(1, novaBolest.getNaziv());
        statement.setBoolean(2, novaBolest instanceof Virus);
        statement.executeUpdate();

        ResultSet generiraniKey = statement.getGeneratedKeys();
        Long generiraniId = null;
        while(generiraniKey.next()){
            generiraniId = generiraniKey.getLong(1);
        }
        novaBolest.setId(generiraniId);

        statement = veza.prepareStatement("INSERT INTO BOLEST_SIMPTOM(BOLEST_ID, SIMPTOM_ID) VALUES (?, ?)");
        statement.setLong(1, novaBolest.getId());
        for(int i = 0; i < novaBolest.getSimptomi().size(); i++){
            statement.setLong(2, novaBolest.getSimptomi().get(i).getId());
            statement.executeUpdate();
        }
    }

    public static void dohvatiSveZupanije(Connection veza) throws SQLException, IOException{
        List<Zupanija> dohvaceneZupanije = new ArrayList<>();

        Statement statement = veza.createStatement();
        ResultSet result = statement.executeQuery("SELECT * FROM ZUPANIJA");

        while(result.next()){
            long id = result.getInt("ID");
            String naziv = result.getString("NAZIV");
            int brojStanovnika = result.getInt("BROJ_STANOVNIKA");
            int brojZarazenih = result.getInt("BROJ_ZARAZENIH_STANOVNIKA");

            dohvaceneZupanije.add(new Zupanija(id, naziv, brojStanovnika, brojZarazenih));
        }

        PretragaZupanijaController.zupanije = dohvaceneZupanije;
    }

    public static Zupanija dohvatiJednuZupaniju(Connection veza, Long id) throws SQLException, IOException{
        Zupanija dohvacenaZupanija = null;

        PreparedStatement statement = veza.prepareStatement("SELECT * FROM ZUPANIJA WHERE ID = ?");
        statement.setLong(1, id);

        ResultSet rs = statement.executeQuery();
        while (rs.next()) {
            dohvacenaZupanija = new Zupanija(Long.valueOf(rs.getInt("ID")), rs.getString("NAZIV"),
                    rs.getInt("BROJ_STANOVNIKA"), rs.getInt("BROJ_ZARAZENIH_STANOVNIKA"));
        }

        return dohvacenaZupanija;
    }

    public static void dodajNovuZupaniju(Connection veza, Zupanija novaZupanija) throws SQLException, IOException{
        PreparedStatement statement = veza.prepareStatement("INSERT INTO ZUPANIJA(" +
                "NAZIV, BROJ_STANOVNIKA, BROJ_ZARAZENIH_STANOVNIKA) VALUES(?, ?, ?)", Statement.RETURN_GENERATED_KEYS);

        statement.setString(1, novaZupanija.getNaziv());
        statement.setInt(2, novaZupanija.getBrojStanovnika());
        statement.setInt(3, novaZupanija.getBrojZarazenih());
        statement.executeUpdate();

        ResultSet generiraniKey = statement.getGeneratedKeys();
        Long generiraniId = null;
        while(generiraniKey.next()){
            generiraniId = generiraniKey.getLong(1);
        }
        novaZupanija.setId(generiraniId);
    }

    public static void dohvatiSveOsobe(Connection veza) throws SQLException, IOException, InterruptedException {
        List<Osoba> dohvaceneOsobe = new ArrayList<>();

        Statement statement = veza.createStatement();
        ResultSet result = statement.executeQuery("SELECT * FROM OSOBA");

        while(result.next()){
            Long id = Long.valueOf(result.getInt("ID"));
            String ime = result.getString("IME");
            String prezime = result.getString("PREZIME");
            Date datumRodjenja = result.getDate("DATUM_RODJENJA");
            Long idZupanije = Long.valueOf(result.getInt("ZUPANIJA_ID"));
            Long idBolesti = result.getLong("BOLEST_ID");

            Zupanija zupanijaOsobe = dohvatiJednuZupaniju(veza, idZupanije);
            Bolest bolestOsobe = dohvatiJednuBolest(veza, idBolesti);

            dohvaceneOsobe.add(new Osoba.Builder()
                    .atId(id)
                    .withIme(ime)
                    .withPrezime(prezime)
                    .atDatumRodjenja(datumRodjenja.toLocalDate())
                    .atZupanija(zupanijaOsobe)
                    .withZarazenBolescu(bolestOsobe)
                    .build());
        }

        for(int i = 0; i < dohvaceneOsobe.size(); i++){
            PreparedStatement preparedStatement = veza.prepareStatement("SELECT * FROM KONTAKTIRANE_OSOBE WHERE OSOBA_ID = ?");
            preparedStatement.setLong(1, dohvaceneOsobe.get(i).getId());
            ResultSet preparedStatementResult = preparedStatement.executeQuery();
            List<Osoba> kontaktiraneOsobe = new ArrayList<>();

            while(preparedStatementResult.next()){
                Long idKontaktiraneOsobe = preparedStatementResult.getLong("KONTAKTIRANA_OSOBA_ID");
                kontaktiraneOsobe.add(dohvatiJednuOsobu(veza, idKontaktiraneOsobe));
            }

            dohvaceneOsobe.get(i).setKontaktiraneOsobe(kontaktiraneOsobe);
        }

        PretragaOsobaController.osobe = dohvaceneOsobe;
    }

    public static Osoba dohvatiJednuOsobu(Connection veza, Long id) throws SQLException, IOException, InterruptedException {
        Osoba dohvacenaOsoba = null;
        PreparedStatement statement = veza.prepareStatement("SELECT * FROM OSOBA WHERE ID = ?");

        statement.setLong(1, id);
        ResultSet rs = statement.executeQuery();


        while (rs.next()) {
            Long idOsobe = Long.valueOf(rs.getInt("ID"));
            String ime = rs.getString("IME");
            String prezime = rs.getString("PREZIME");
            Date datumRodjenja = rs.getDate("DATUM_RODJENJA");
            Long idZupanija = Long.valueOf(rs.getInt("ZUPANIJA_ID"));
            Bolest zarazenBolescu = dohvatiJednuBolest(veza, rs.getLong("BOLEST_ID"));

            dohvacenaOsoba = new Osoba.Builder()
                    .atId(idOsobe)
                    .withIme(ime)
                    .withPrezime(prezime)
                    .atDatumRodjenja(datumRodjenja.toLocalDate())
                    .atZupanija(dohvatiJednuZupaniju(veza, idZupanija))
                    .withZarazenBolescu(zarazenBolescu)
                    .build();
        }

        return dohvacenaOsoba;
    }

    public static void dodajNovuOsobu(Connection veza, Osoba novaOsoba) throws SQLException, IOException{
        //ALTER TABLE OSOBA ALTER COLUMN ID RESTART WITH 6
        PreparedStatement statement = veza.prepareStatement("INSERT INTO OSOBA(" +
                "IME, PREZIME, DATUM_RODJENJA, ZUPANIJA_ID, BOLEST_ID) VALUES(?, ?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS);

        statement.setString(1, novaOsoba.getIme());
        statement.setString(2, novaOsoba.getPrezime());
        statement.setDate(3, Date.valueOf(novaOsoba.getDatumRodjenja()));
        statement.setLong(4, novaOsoba.getZupanija().getId());
        statement.setLong(5, novaOsoba.getZarazenBolescu().getId());
        statement.executeUpdate();

        ResultSet generiraniKey = statement.getGeneratedKeys();
        Long generiraniId = null;
        while(generiraniKey.next()){
            generiraniId = generiraniKey.getLong(1);
        }
        novaOsoba.setId(generiraniId);

        statement = veza.prepareStatement("INSERT INTO KONTAKTIRANE_OSOBE(OSOBA_ID, KONTAKTIRANA_OSOBA_ID) VALUES(?, ?)");
        statement.setLong(1, novaOsoba.getId());
        for(int i = 0; i < novaOsoba.getKontaktiraneOsobe().size(); i++){
            statement.setLong(2, novaOsoba.getKontaktiraneOsobe().get(i).getId());
            statement.executeUpdate();
        }
    }
}