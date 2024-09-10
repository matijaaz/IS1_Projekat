/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package entities;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author Matija
 */
@Entity
@Table(name = "video")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Video.findAll", query = "SELECT v FROM Video v"),
    @NamedQuery(name = "Video.findBySifV", query = "SELECT v FROM Video v WHERE v.sifV = :sifV"),
    @NamedQuery(name = "Video.findByNaziv", query = "SELECT v FROM Video v WHERE v.naziv = :naziv"),
    @NamedQuery(name = "Video.findByTrajanje", query = "SELECT v FROM Video v WHERE v.trajanje = :trajanje"),
    @NamedQuery(name = "Video.findByDatumvremepostavljanja", query = "SELECT v FROM Video v WHERE v.datumvremepostavljanja = :datumvremepostavljanja")})
public class Video implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "SifV")
    private Integer sifV;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "Naziv")
    private String naziv;
    @Basic(optional = false)
    @NotNull
    @Column(name = "Trajanje")
    private int trajanje;
    @Basic(optional = false)
    @NotNull
    @Column(name = "Datum_vreme_postavljanja")
    @Temporal(TemporalType.TIMESTAMP)
    private Date datumvremepostavljanja;
    @ManyToMany(mappedBy = "videoList")
    private List<Kategorija> kategorijaList;
    @JoinColumn(name = "Vlasnik", referencedColumnName = "SifK")
    @ManyToOne(optional = false)
    private Korisnik vlasnik;
    @Transient
    private String DatumVreme;
    
    public Video() {
    }

    public Video(Integer sifV) {
        this.sifV = sifV;
    }

    public Video(Integer sifV, String naziv, int trajanje, Date datumvremepostavljanja) {
        this.sifV = sifV;
        this.naziv = naziv;
        this.trajanje = trajanje;
        this.datumvremepostavljanja = datumvremepostavljanja;
    }

    public String getDatumVreme() {
        return DatumVreme;
    }

    public void setDatumVreme(String DatumVreme) {
        this.DatumVreme = DatumVreme;
    }
    
    
    public Integer getSifV() {
        return sifV;
    }

    public void setSifV(Integer sifV) {
        this.sifV = sifV;
    }

    public String getNaziv() {
        return naziv;
    }

    public void setNaziv(String naziv) {
        this.naziv = naziv;
    }

    public int getTrajanje() {
        return trajanje;
    }

    public void setTrajanje(int trajanje) {
        this.trajanje = trajanje;
    }

    public Date getDatumvremepostavljanja() {
        return datumvremepostavljanja;
    }

    public void setDatumvremepostavljanja(Date datumvremepostavljanja) {
        this.datumvremepostavljanja = datumvremepostavljanja;
    }

    @XmlTransient
    public List<Kategorija> getKategorijaList() {
        return kategorijaList;
    }

    public void setKategorijaList(List<Kategorija> kategorijaList) {
        this.kategorijaList = kategorijaList;
    }

    public Korisnik getVlasnik() {
        return vlasnik;
    }

    public void setVlasnik(Korisnik vlasnik) {
        this.vlasnik = vlasnik;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (sifV != null ? sifV.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Video)) {
            return false;
        }
        Video other = (Video) object;
        if ((this.sifV == null && other.sifV != null) || (this.sifV != null && !this.sifV.equals(other.sifV))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entities.Video[ sifV=" + sifV + " ]";
    }
    
}
