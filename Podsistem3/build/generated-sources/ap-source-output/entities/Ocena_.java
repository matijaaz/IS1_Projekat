package entities;

import entities.Korisnik;
import entities.Video;
import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.7.12.v20230209-rNA", date="2024-05-31T15:40:36")
@StaticMetamodel(Ocena.class)
public class Ocena_ { 

    public static volatile SingularAttribute<Ocena, Date> datumvreme;
    public static volatile SingularAttribute<Ocena, Integer> sifO;
    public static volatile SingularAttribute<Ocena, Video> video;
    public static volatile SingularAttribute<Ocena, Integer> ocena;
    public static volatile SingularAttribute<Ocena, Korisnik> korisnik;

}