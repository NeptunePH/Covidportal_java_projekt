package main.hr.java.covidportal.iznimke;

/**
 * Klasa koja predstavlja iznimku u slučaju da je unesena lista simptoma jednaka sa prijasnjom. Nasljeđuje RuntimeException
 */
public class BolestIstihSimptoma extends RuntimeException{
    /**
     * Konstruktur klase. Prima poruku iznimke tipa string koju prosljeđuje nadklasi
     * @param message poruka iznimke
     */
    public BolestIstihSimptoma(String message) {
        super(message);
    }

    /**
     * Konstruktor klase. Prima objekt tipa throwable, koji prosljeđuje nadklasi
     * @param cause tip iznimke
     */
    public BolestIstihSimptoma(Throwable cause) {
        super(cause);
    }

    /**
     * Konstruktor klase. Prima poruku iznimke i razlog iznimke, koji se prosljeđuju nadklasi
     * @param message poruka iznimke
     * @param cause tip iznimke
     */
    public BolestIstihSimptoma(String message, Throwable cause) {
        super(message, cause);
    }
}