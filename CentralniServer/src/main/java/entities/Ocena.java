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
@Table(name = "ocena")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Ocena.findAll", query = "SELECT o FROM Ocena o"),
    @NamedQuery(name = "Ocena.findBySifO", query = "SELECT o FROM Ocena o WHERE o.sifO = :sifO"),
    @NamedQuery(name = "Ocena.findByOcena", query = "SELECT o FROM Ocena o WHERE o.ocena = :ocena"),
    @NamedQuery(name = "Ocena.findByDatumvreme", query = "SELECT o FROM Ocena o WHERE o.datumvreme = :datumvreme")})
public class Ocena implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "SifO")
    private Integer sifO;
    @Basic(optional = false)
    @NotNull
    @Column(name = "ocena")
    private int ocena;
    @Basic(optional = false)
    @NotNull
    @Column(name = "Datum_vreme")
    @Temporal(TemporalType.TIMESTAMP)
    private Date datumvreme;
    @JoinColumn(name = "Korisnik", referencedColumnName = "SifK")
    @ManyToOne(optional = false)
    private Korisnik korisnik;
    @JoinColumn(name = "Video", referencedColumnName = "SifV")
    @ManyToOne(optional = false)
    private Video video;
    @Transient
    private String datumVreme;
    
    public Ocena() {
    }

    public Ocena(Integer sifO) {
        this.sifO = sifO;
    }

    public Ocena(Integer sifO, int ocena, Date datumvreme) {
        this.sifO = sifO;
        this.ocena = ocena;
        this.datumvreme = datumvreme;
    }

    public String getDatumVreme() {
        return datumVreme;
    }

    public void setDatumVreme(String datumVreme) {
        this.datumVreme = datumVreme;
    }
    
    
    public Integer getSifO() {
        return sifO;
    }

    public void setSifO(Integer sifO) {
        this.sifO = sifO;
    }

    public int getOcena() {
        return ocena;
    }

    public void setOcena(int ocena) {
        this.ocena = ocena;
    }

    public Date getDatumvreme() {
        return datumvreme;
    }

    public void setDatumvreme(Date datumvreme) {
        this.datumvreme = datumvreme;
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
        hash += (sifO != null ? sifO.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Ocena)) {
            return false;
        }
        Ocena other = (Ocena) object;
        if ((this.sifO == null && other.sifO != null) || (this.sifO != null && !this.sifO.equals(other.sifO))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entities.Ocena[ sifO=" + sifO + " ]";
    }
    
}
