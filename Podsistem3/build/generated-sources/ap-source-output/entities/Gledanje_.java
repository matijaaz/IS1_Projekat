package entities;

import entities.Korisnik;
import entities.Video;
import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.7.12.v20230209-rNA", date="2024-05-31T15:19:46")
@StaticMetamodel(Gledanje.class)
public class Gledanje_ { 

    public static volatile SingularAttribute<Gledanje, Date> datumvremepocetka;
    public static volatile SingularAttribute<Gledanje, Integer> sifG;
    public static volatile SingularAttribute<Gledanje, Video> video;
    public static volatile SingularAttribute<Gledanje, Integer> sekund;
    public static volatile SingularAttribute<Gledanje, Integer> odgledanosekundi;
    public static volatile SingularAttribute<Gledanje, Korisnik> korisnik;

}