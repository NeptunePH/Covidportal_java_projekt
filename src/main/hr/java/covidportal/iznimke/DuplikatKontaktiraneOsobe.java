package main.hr.java.covidportal.iznimke;

/**
 * Klasa koja predstavlja iznimku u slučaju da je unesena osoba već na popisu kontaktiranih osoba. Nasljeđuje RuntimeException
 */
public class DuplikatKontaktiraneOsobe extends Exception{
    /**
     * Konstruktur klase. Prima poruku iznimke tipa string koju prosljeđuje nadklasi
     * @param message poruka iznimke
     */
    public DuplikatKontaktiraneOsobe(String message) {
        super(message);
    }

    /**
     * Konstruktor klase. Prima objekt tipa throwable, koji prosljeđuje nadklasi
     * @param cause tip iznimke
     */
    public DuplikatKontaktiraneOsobe(Throwable cause) {
        super(cause);
    }

    /**
     * Konstruktor klase. Prima poruku iznimke i razlog iznimke, koji se prosljeđuju nadklasi
     * @param message poruka iznimke
     * @param cause tip iznimke
     */
    public DuplikatKontaktiraneOsobe(String message, Throwable cause) {
        super(message, cause);
    }
}