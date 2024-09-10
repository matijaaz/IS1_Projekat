/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package entities;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Matija
 */
@Entity
@Table(name = "pretplata")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Pretplata.findAll", query = "SELECT p FROM Pretplata p"),
    @NamedQuery(name = "Pretplata.findBySifP", query = "SELECT p FROM Pretplata p WHERE p.sifP = :sifP"),
    @NamedQuery(name = "Pretplata.findByDatumvremepocetka", query = "SELECT p FROM Pretplata p WHERE p.datumvremepocetka = :datumvremepocetka"),
    @NamedQuery(name = "Pretplata.findByCena", query = "SELECT p FROM Pretplata p WHERE p.cena = :cena")})
public class Pretplata implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "SifP")
    private Integer sifP;
    @Basic(optional = false)
    @NotNull
    @Column(name = "Datum_vreme_pocetka")
    @Temporal(TemporalType.TIMESTAMP)
    private Date datumvremepocetka;
    @Basic(optional = false)
    @NotNull
    @Column(name = "Cena")
    private int cena;
    @JoinColumn(name = "Korisnik", referencedColumnName = "SifK")
    @ManyToOne(optional = false)
    private Korisnik korisnik;
    @JoinColumn(name = "Paket", referencedColumnName = "SifP")
    @ManyToOne(optional = false)
    private Paket paket;
    @Transient
    String datumVreme;

    public Pretplata() {
    }

    public Pretplata(Integer sifP) {
        this.sifP = sifP;
    }

    public Pretplata(Integer sifP, Date datumvremepocetka, int cena) {
        this.sifP = sifP;
        this.datumvremepocetka = datumvremepocetka;
        this.cena = cena;
    }

    public String getDatumVreme() {
        return datumVreme;
    }

    public void setDatumVreme(String datumVreme) {
        this.datumVreme = datumVreme;
    }
    
    
    public Integer getSifP() {
        return sifP;
    }

    public void setSifP(Integer sifP) {
        this.sifP = sifP;
    }

    public Date getDatumvremepocetka() {
        return datumvremepocetka;
    }

    public void setDatumvremepocetka(Date datumvremepocetka) {
        this.datumvremepocetka = datumvremepocetka;
    }

    public int getCena() {
        return cena;
    }

    public void setCena(int cena) {
        this.cena = cena;
    }

    public Korisnik getKorisnik() {
        return korisnik;
    }

    public void setKorisnik(Korisnik korisnik) {
        this.korisnik = korisnik;
    }

    public Paket getPaket() {
        return paket;
    }

    public void setPaket(Paket paket) {
        this.paket = paket;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (sifP != null ? sifP.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Pretplata)) {
            return false;
        }
        Pretplata other = (Pretplata) object;
        if ((this.sifP == null && other.sifP != null) || (this.sifP != null && !this.sifP.equals(other.sifP))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entities.Pretplata[ sifP=" + sifP + " ]";
    }
    
}
