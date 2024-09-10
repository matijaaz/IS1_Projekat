/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package entities;

import java.io.Serializable;
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
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Matija
 */
@Entity
@Table(name = "korisnik")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Korisnik.findAll", query = "SELECT k FROM Korisnik k"),
    @NamedQuery(name = "Korisnik.findBySifK", query = "SELECT k FROM Korisnik k WHERE k.sifK = :sifK"),
    @NamedQuery(name = "Korisnik.findByIme", query = "SELECT k FROM Korisnik k WHERE k.ime = :ime"),
    @NamedQuery(name = "Korisnik.findByMejl", query = "SELECT k FROM Korisnik k WHERE k.mejl = :mejl"),
    @NamedQuery(name = "Korisnik.findByGodiste", query = "SELECT k FROM Korisnik k WHERE k.godiste = :godiste")})
public class Korisnik implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "SifK")
    private Integer sifK;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "Ime")
    private String ime;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "Mejl")
    private String mejl;
    @Basic(optional = false)
    @NotNull
    @Column(name = "Godiste")
    private int godiste;
    @JoinColumn(name = "SifM", referencedColumnName = "SifM")
    @ManyToOne(optional = false)
    private Mesto sifM;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 1)
    @Column(name = "Pol")
    private String pol;

    public Korisnik() {
    }

    public Korisnik(Integer sifK) {
        this.sifK = sifK;
    }

    public Korisnik(Integer sifK, String ime, String prezime, String mejl, int godiste) {
        this.sifK = sifK;
        this.ime = ime;
        
        this.mejl = mejl;
        this.godiste = godiste;
    }

    public String getPol() {
        return pol;
    }

    public void setPol(String pol) {
        this.pol = pol;
    }
    
    public Integer getSifK() {
        return sifK;
    }

    public void setSifK(Integer sifK) {
        this.sifK = sifK;
    }

    public String getIme() {
        return ime;
    }

    public void setIme(String ime) {
        this.ime = ime;
    }

    public String getMejl() {
        return mejl;
    }

    public void setMejl(String mejl) {
        this.mejl = mejl;
    }

    public int getGodiste() {
        return godiste;
    }

    public void setGodiste(int godiste) {
        this.godiste = godiste;
    }

    public Mesto getSifM() {
        return sifM;
    }

    public void setSifM(Mesto sifM) {
        this.sifM = sifM;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (sifK != null ? sifK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Korisnik)) {
            return false;
        }
        Korisnik other = (Korisnik) object;
        if ((this.sifK == null && other.sifK != null) || (this.sifK != null && !this.sifK.equals(other.sifK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entities.Korisnik[ sifK=" + sifK + " ]";
    }
    
}
