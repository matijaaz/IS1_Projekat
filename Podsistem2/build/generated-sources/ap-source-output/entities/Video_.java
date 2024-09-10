package entities;

import entities.Kategorija;
import entities.Korisnik;
import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.7.12.v20230209-rNA", date="2024-05-29T17:56:55")
@StaticMetamodel(Video.class)
public class Video_ { 

    public static volatile ListAttribute<Video, Kategorija> kategorijaList;
    public static volatile SingularAttribute<Video, Integer> sifV;
    public static volatile SingularAttribute<Video, Integer> trajanje;
    public static volatile SingularAttribute<Video, String> naziv;
    public static volatile SingularAttribute<Video, Date> datumvremepostavljanja;
    public static volatile SingularAttribute<Video, Korisnik> vlasnik;

}