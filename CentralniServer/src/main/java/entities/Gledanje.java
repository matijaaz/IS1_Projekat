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
@Table(name = "gledanje")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Gledanje.findAll", query = "SELECT g FROM Gledanje g"),
    @NamedQuery(name = "Gledanje.findBySifG", query = "SELECT g FROM Gledanje g WHERE g.sifG = :sifG"),
    @NamedQuery(name = "Gledanje.findByDatumvremepocetka", query = "SELECT g FROM Gledanje g WHERE g.datumvremepocetka = :datumvremepocetka"),
    @NamedQuery(name = "Gledanje.findBySekund", query = "SELECT g FROM Gledanje g WHERE g.sekund = :sekund"),
    @NamedQuery(name = "Gledanje.findByOdgledanosekundi", query = "SELECT g FROM Gledanje g WHERE g.odgledanosekundi = :odgledanosekundi")})
public class Gledanje implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "SifG")
    private Integer sifG;
    @Basic(optional = false)
    @NotNull
    @Column(name = "Datum_vreme_pocetka")
    @Temporal(TemporalType.TIMESTAMP)
    private Date datumvremepocetka;
    @Basic(optional = false)
    @NotNull
    @Column(name = "Sekund")
    private int sekund;
    @Basic(optional = false)
    @NotNull
    @Column(name = "Odgledano_sekundi")
    private int odgledanosekundi;
    @JoinColumn(name = "Korisnik", referencedColumnName = "SifK")
    @ManyToOne(optional = false)
    private Korisnik korisnik;
    @JoinColumn(name = "Video", referencedColumnName = "SifV")
    @ManyToOne(optional = false)
    private Video video;
    @Transient
    private String datumVreme;

    public Gledanje() {
    }

    public Gledanje(Integer sifG) {
        this.sifG = sifG;
    }

    public Gledanje(Integer sifG, Date datumvremepocetka, int sekund, int odgledanosekundi) {
        this.sifG = sifG;
        this.datumvremepocetka = datumvremepocetka;
        this.sekund = sekund;
        this.odgledanosekundi = odgledanosekundi;
    }

    public String getDatumVreme() {
        return datumVreme;
    }

    public void setDatumVreme(String datumVreme) {
        this.datumVreme = datumVreme;
    }
    
    
    public Integer getSifG() {
        return sifG;
    }

    public void setSifG(Integer sifG) {
        this.sifG = sifG;
    }

    public Date getDatumvremepocetka() {
        return datumvremepocetka;
    }

    public void setDatumvremepocetka(Date datumvremepocetka) {
        this.datumvremepocetka = datumvremepocetka;
    }

    public int getSekund() {
        return sekund;
    }

    public void setSekund(int sekund) {
        this.sekund = sekund;
    }

    public int getOdgledanosekundi() {
        return odgledanosekundi;
    }

    public void setOdgledanosekundi(int odgledanosekundi) {
        this.odgledanosekundi = odgledanosekundi;
    }

    public Korisnik getKorisnik() {
        return korisnik;
    }

    public void setKorisnik(Korisnik korisnik) {
        this.korisnik = korisnik;
    }

    public Video getVideo() {
        return video;
    }

    public void setVideo(Video video) {
        this.video = video;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (sifG != null ? sifG.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Gledanje)) {
            return false;
        }
        Gledanje other = (Gledanje) object;
        if ((this.sifG == null && other.sifG != null) || (this.sifG != null && !this.sifG.equals(other.sifG))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entities.Gledanje[ sifG=" + sifG + " ]";
    }
    
}
