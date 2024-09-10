/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package entities;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author Matija
 */
@Entity
@Table(name = "paket")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Paket.findAll", query = "SELECT p FROM Paket p"),
    @NamedQuery(name = "Paket.findBySifP", query = "SELECT p FROM Paket p WHERE p.sifP = :sifP"),
    @NamedQuery(name = "Paket.findByCena", query = "SELECT p FROM Paket p WHERE p.cena = :cena")})
public class Paket implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "SifP")
    private Integer sifP;
    @Basic(optional = false)
    @NotNull
    @Column(name = "Cena")
    private int cena;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "paket")
    private List<Pretplata> pretplataList;

    public Paket() {
    }

    public Paket(Integer sifP) {
        this.sifP = sifP;
    }

    public Paket(Integer sifP, int cena) {
        this.sifP = sifP;
        this.cena = cena;
    }

    public Integer getSifP() {
        return sifP;
    }

    public void setSifP(Integer sifP) {
        this.sifP = sifP;
    }

    public int getCena() {
        return cena;
    }

    public void setCena(int cena) {
        this.cena = cena;
    }

    @XmlTransient
    public List<Pretplata> getPretplataList() {
        return pretplataList;
    }

    public void setPretplataList(List<Pretplata> pretplataList) {
        this.pretplataList = pretplataList;
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
        if (!(object instanceof Paket)) {
            return false;
        }
        Paket other = (Paket) object;
        if ((this.sifP == null && other.sifP != null) || (this.sifP != null && !this.sifP.equals(other.sifP))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entities.Paket[ sifP=" + sifP + " ]";
    }
    
}
