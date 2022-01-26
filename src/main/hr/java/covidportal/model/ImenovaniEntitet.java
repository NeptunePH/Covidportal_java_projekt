package main.hr.java.covidportal.model;

import java.io.Serializable;
import java.util.Objects;

/**
 * Apstraktna klasa, služi za unos naziva više klasa
 */

public abstract class ImenovaniEntitet implements Serializable {
    private String naziv;
    private Long id;

    /**
     * Konstruktor klase
     * @param naziv naziv objekta
     */

    public ImenovaniEntitet(String naziv, Long id) {
        this.naziv = naziv;
        this.id = id;
    }

    public String getNaziv() {
        return naziv;
    }

    public Long getId() {
        return id;
    }

    public void setNaziv(String naziv) {
        this.naziv = naziv;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ImenovaniEntitet)) return false;
        ImenovaniEntitet that = (ImenovaniEntitet) o;
        return Objects.equals(naziv, that.naziv);
    }

    @Override
    public int hashCode() {
        return Objects.hash(naziv);
    }
}