package main.hr.java.covidportal.main;

import main.hr.java.covidportal.enums.VrijednostSimptoma;
import main.hr.java.covidportal.genericsi.KlinikaZaInfektivneBolesti;
import main.hr.java.covidportal.model.*;

import main.hr.java.covidportal.sort.CovidSorter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.time.Duration;
import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Služi za unos županija, simptoma, bolesti, te osoba.
 */

public class Glavna {
    private static final Logger logger = LoggerFactory.getLogger(Glavna.class);

//    /**
//     * sluzi za citanje podataka o zupanijama iz datoteke
//     * @return vraca listu zupanija
//     */
//    private static List<Zupanija> citanjeZupanija(){
//        List<Zupanija> zupanijeLista = new ArrayList<>();
//        String linija;
//
//        try(BufferedReader in = new BufferedReader(new FileReader("dat/zupanije.txt"))){
//            while((linija = in.readLine()) != null){
//                Long zupanijaId = Long.parseLong(linija);
//                String imeZupanije = in.readLine();
//                Integer brojStanovnika = Integer.parseInt(in.readLine());
//                Integer brojZarazenih = Integer.parseInt(in.readLine());
//                zupanijeLista.add(new Zupanija(zupanijaId, imeZupanije, brojStanovnika, brojZarazenih));
//            }
//        } catch(IOException ex){
//            logger.error("IO Exception.");
//            ex.printStackTrace();
//        }
//
//        return zupanijeLista;
//    }
//
//    /**
//     * sluzi za citanje podataka o simptomima iz datoteke
//     * @return vraca listu simptoma
//     */
//    private static List<Simptom> citanjeSimptoma(){
//        List<Simptom> simptomiLista = new ArrayList<>();
//        String linija;
//
//        try(BufferedReader in = new BufferedReader(new FileReader("dat/simptomi.txt"))){
//            while((linija = in.readLine()) != null){
//                Long id = Long.parseLong(linija);
//                String nazivSimptoma = in.readLine();
//                VrijednostSimptoma vrijednost = VrijednostSimptoma.valueOf(in.readLine());
//                simptomiLista.add(new Simptom(id, nazivSimptoma, vrijednost));
//            }
//        } catch(IOException ex){
//            logger.error("IO Exception.");
//            ex.printStackTrace();
//        }
//
//        return simptomiLista;
//    }
//
//    /**
//     * sluzi za citanje podataka o bolestima iz datoteke
//     * @param simptomi lista simptoma koje bolesti mogu imati
//     * @return vraca listu bolesti
//     */
//    private static List<Bolest> citanjeBolesti(List<Simptom> simptomi){
//        List<Bolest> bolesti = new ArrayList<>();
//        String linija;
//
//        try(BufferedReader in = new BufferedReader(new FileReader("dat/bolesti.txt"))){
//            while((linija = in.readLine()) != null){
//                Long idBolesti = Long.parseLong(linija);
//                String nazivBolesti = in.readLine();
//                String[] simptomId = in.readLine().split(",");
//                List<Simptom> bolestSimptomi = new ArrayList<>();
//
//                for(int i = 0; i < simptomId.length; i++){
//                    Long tmpId = Long.parseLong(simptomId[i]);
//                    bolestSimptomi.add(simptomi
//                            .stream()
//                            .filter(s -> s.getId().equals(tmpId))
//                            .findFirst()
//                            .get());
//                }
//
//
//                bolesti.add(new Bolest(idBolesti, nazivBolesti, bolestSimptomi));
//            }
//        } catch(IOException ex){
//            logger.error("IO Exception.");
//            ex.printStackTrace();
//        }
//
//        return bolesti;
//    }
//
//    /**
//     * sluzi za citanje podataka o virusima iz datoteke
//     * @param simptomi lista simptoma koje virusi mogu imati
//     * @param sizeBolest velicina liste bolesti
//     * @return vraca listu virusa
//     */
//    private static List<Virus> citanjeVirusa(List<Simptom> simptomi, int sizeBolest, KlinikaZaInfektivneBolesti<Virus, Osoba> klinika){
//        List<Virus> virusi = new ArrayList<>();
//        String linija;
//        try(BufferedReader in = new BufferedReader(new FileReader("dat/virusi.txt"))){
//            while((linija = in.readLine()) != null){
//                Long idVirusa = sizeBolest + Long.parseLong(linija);
//                String nazivVirusa = in.readLine();
//                String[] virusId = in.readLine().split(",");
//                List<Simptom> virusSimptomi = new ArrayList<>();
//
//                for(int i = 0; i < virusId.length; i++){
//                    Long tmpId = Long.parseLong(virusId[i]);
//                    virusSimptomi.add(simptomi
//                            .stream()
//                            .filter(v -> v.getId().equals(tmpId))
//                            .findFirst()
//                            .get());
//                }
//
//                virusi.add(new Virus(idVirusa, nazivVirusa, virusSimptomi));
//                klinika.addVirus(virusi.get(virusi.size() - 1));
//            }
//        } catch(IOException ex){
//            logger.error("IO Exception.");
//            ex.printStackTrace();
//        }
//
//        return virusi;
//    }
//
//    /**
//     * sluzi za citanje podataka o osobama iz datoteke
//     * @param listaZupanija lista zupanija iz koje osoba moze biti
//     * @param listaBolesti lista bolesti i virusa koje osoba moze imati
//     * @return vraca listu osoba
//     */
//    private static List<Osoba> citanjeOsoba(List<Zupanija> listaZupanija, List<Bolest> listaBolesti){
//        List<Osoba> osobe = new ArrayList<>();
//        String linija;
//        boolean prvaOsoba = true;
//
//        try(BufferedReader in = new BufferedReader(new FileReader("dat/osobe.txt"))){
//            while((linija = in.readLine()) != null){
//                Long idOsobe = Long.parseLong(linija);
//                String ime = in.readLine();
//                String prezime = in.readLine();
//                Integer starost = Integer.parseInt(in.readLine());
//                Long idZupanija = Long.parseLong(in.readLine());
//
//                Zupanija osobaZupanija = listaZupanija
//                        .stream()
//                        .filter(z -> z.getId().equals(idZupanija))
//                        .findFirst()
//                        .get();
//
//                Long idBolest = Long.parseLong(in.readLine());
//
//                Bolest bolestOsobe = listaBolesti
//                        .stream()
//                        .filter(b -> b.getId().equals(idBolest))
//                        .findFirst()
//                        .get();
//
//                if(prvaOsoba){
//                    prvaOsoba = false;
//                    Osoba tmpOsoba = new Osoba.Builder()
//                            .atId(idOsobe)
//                            .withIme(ime)
//                            .withPrezime(prezime)
//                            .atStarost(starost)
//                            .atZupanija(osobaZupanija)
//                            .withKontaktiraneOsobe(null)
//                            .withZarazenBolescu(bolestOsobe)
//                            .build();
//                    osobe.add(tmpOsoba);
//                }
//                else{
//                    List<Osoba> kontaktiraneOsobe = new ArrayList<>();
//                    String[] tmp = in.readLine().split(",");
//                    for(int i = 0; i < tmp.length; i++){
//                        kontaktiraneOsobe.add(osobe.get(Integer.parseInt(tmp[i]) - 1));
//                    }
//                    Osoba tmpOsoba = new Osoba.Builder()
//                            .atId(idOsobe)
//                            .withIme(ime)
//                            .withPrezime(prezime)
//                            .atStarost(starost)
//                            .atZupanija(osobaZupanija)
//                            .withKontaktiraneOsobe(kontaktiraneOsobe)
//                            .withZarazenBolescu(bolestOsobe)
//                            .build();
//                    osobe.add(tmpOsoba);
//                }
//            }
//        } catch(IOException ex){
//            logger.error("IO Exception.");
//            ex.printStackTrace();
//        }
//
//        return osobe;
//    }
//
//    /**
//     * Služi za ispis svih unesenih osoba, ako je polje kontaktiranih osoba prazno, nema kontaktiranih osoba. U protivnome se i one ispisuju
//     * @param osobe lista osoba
//     */
//    private static void ispisOsobe(List<Osoba> osobe){
//        for(int i = 0; i < osobe.size(); i++){
//            System.out.println("Ime i prezime: " + osobe.get(i).getIme() + " " + osobe.get(i).getPrezime());
//            System.out.println("Starost: " + " " + osobe.get(i).getDatumRodjenja());
//            System.out.println("Županija prebivališta: " + osobe.get(i).getZupanija().getNaziv());
//            System.out.println("Zaražen bolešću: " + osobe.get(i).getZarazenBolescu().getNaziv());
//            System.out.println("Kontaktirane osobe: ");
//            if(osobe.get(i).getKontaktiraneOsobe() == null){
//                System.out.println("Nema kontaktiranih osoba.");
//            }
//            else{
//                for(int j = 0; j < osobe.get(i).getKontaktiraneOsobe().size(); j++){
//                    System.out.println(osobe.get(i).getKontaktiraneOsobe().get(j).getIme() + " " + osobe.get(i).getKontaktiraneOsobe().get(j).getPrezime());
//                }
//            }
//        }
//    }
//
//    /**
//     * Služi za pokretanje programa koji traži unos potrebnih podataka
//     *
//     * @param args argumenti komandle linije (ne koriste se)
//     */
//    public static void main(String[] args){
//        logger.info("Pokrenut program");
//        Scanner scanner = new Scanner(System.in);
//        KlinikaZaInfektivneBolesti<Virus, Osoba> klinika = new KlinikaZaInfektivneBolesti<>();
//
//        //unos zupanija
//        System.out.println("Ucitavanje podataka o županijama...");
//        List<Zupanija> zupanije = citanjeZupanija();
//
//        //unos simptoma
//        System.out.println("Ucitavanje podataka o simptomima...");
//        List<Simptom> simptomi = citanjeSimptoma();
//
//        //unos bolesti
//        System.out.println("Ucitavanje podataka o bolestima...");
//        List<Bolest> bolesti = citanjeBolesti(simptomi);
//
//        //unos virusa
//        System.out.println("Ucitavanje podataka o virusima...");
//        List<Virus> virusi = citanjeVirusa(simptomi, bolesti.size(), klinika);
//        bolesti.addAll(virusi);
//
//        //unos osoba
//        System.out.println("Ucitavanje podataka o osobama...");
//        List<Osoba> osobe = citanjeOsoba(zupanije, bolesti);
//
//
//        for(int i = 0; i < osobe.size(); i++){
//            if(osobe.get(i).getZarazenBolescu() instanceof Virus){
//                klinika.addOsoba(osobe.get(i));
//            }
//        }
//
//        //instanciranje mape
//        Map<Bolest, List<Osoba>> popisOsobaBolesti = new HashMap<>();
//        for(int i = 0; i < osobe.size(); i++){
//            if(popisOsobaBolesti.containsKey(osobe.get(i).getZarazenBolescu())){
//                popisOsobaBolesti.get(osobe.get(i).getZarazenBolescu()).add(osobe.get(i));
//            }
//            else{
//                popisOsobaBolesti.put(osobe.get(i).getZarazenBolescu(), new ArrayList<>());
//                popisOsobaBolesti.get(osobe.get(i).getZarazenBolescu()).add(osobe.get(i));
//            }
//        }
//
//        System.out.println("Popis osoba: ");
//        ispisOsobe(osobe);
//        logger.info("Kraj programa.");
//
//        //ispis mape
//        for(Bolest b : popisOsobaBolesti.keySet()){
//            List<Osoba> tmp = popisOsobaBolesti.get(b);
//            if(b instanceof Virus){
//                System.out.print("Od virusa " + b.getNaziv() + " boluju: ");
//            }
//            else{
//                System.out.print("Od bolesti " + b.getNaziv() + " boluju: ");
//            }
//            for(int i = 0; i < tmp.size(); i++) {
//                System.out.print(tmp.get(i).getIme() + " " + tmp.get(i).getPrezime() + ", ");
//            }
//            System.out.println();
//        }
//
//        Collections.sort(zupanije, new CovidSorter());
//        System.out.println("Županija sa najviše zaraženih je " + zupanije.get(0).getNaziv() + " " + zupanije.get(0).getPostotakZarazenosti() + "%");
//        System.out.println();
//
//        //sortiranje virusa lambde
//        List<Virus> sortiraniVirusiLambde = klinika.getVirusiAplikacije();
//
//        Instant start = Instant.now();
//        sortiraniVirusiLambde = sortiraniVirusiLambde
//                .stream()
//                .sorted(Comparator.comparing(Virus::getNaziv).reversed())
//                .collect(Collectors.toList());
//        Instant end = Instant.now();
//        long vrijemeLambde = Duration.between(start, end).toMillis();
//
//
//        //sortiranje virusa bez lambdi
//        List<Virus> sortiraniVirusBezLambde = klinika.getVirusiAplikacije();
//
//        start = Instant.now();
//        Collections.sort(sortiraniVirusBezLambde, new Comparator<>(){
//            public int compare(Virus v1, Virus v2){
//                return v2.getNaziv().compareTo(v1.getNaziv());
//            }
//        });
//        end = Instant.now();
//        long vrijemeBezLambde = Duration.between(start, end).toMillis();
//
//        System.out.println("Virusi sortirani po nazivu suprotno od abecede");
//        sortiraniVirusiLambde
//                .stream()
//                .forEach(System.out::println);
//        System.out.println("Vrijeme sortiranja lambde: " + vrijemeLambde + " milisekunde, vrijeme sortiranja bez lambde: " + vrijemeBezLambde + " milisekunde.");
//
//        //filtriranje liste osobe
//        System.out.print("Unesite string za pretragu po prezimenu: ");
//        String filterPrezime = scanner.nextLine();
//        Optional<List<Osoba>> optionalOsobe = Optional.of(osobe
//                .stream()
//                .filter(o -> o.getPrezime().contains(filterPrezime))
//                .collect(Collectors.toList()));
//
//        if(optionalOsobe.get().isEmpty()){
//            System.out.println("Nema osoba sa takvim prezimenom.");
//        }
//        else{
//            optionalOsobe
//                    .get()
//                    .stream()
//                    .forEach(System.out::println);
//        }
//
//        //broj simptoma
//        bolesti
//                .stream()
//                .map(b -> b.getNaziv() + " "  + b.getSimptomi().size() + " simptoma")
//                .forEach(System.out::println);
//
//        //serijalizacija zupanija
//        List<Zupanija> serijaliziraneZupanije = zupanije
//                .stream()
//                .filter(z -> z.getPostotakZarazenosti() > 2.0)
//                .collect(Collectors.toList());
//
//        try(ObjectOutputStream serijalizacija = new ObjectOutputStream(new FileOutputStream("dat/serijaliziraneZupanije.dat"))){
//            serijalizacija.writeObject(serijaliziraneZupanije);
//        } catch(IOException ex){
//            logger.error("IO Exception.");
//            ex.printStackTrace();
//        }
//
//    }
}