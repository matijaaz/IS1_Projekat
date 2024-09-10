package entities;

import entities.Korisnik;
import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.7.12.v20230209-rNA", date="2024-05-25T18:36:57")
@StaticMetamodel(Mesto.class)
public class Mesto_ { 

    public static volatile SingularAttribute<Mesto, Integer> sifM;
    public static volatile SingularAttribute<Mesto, String> naziv;
    public static volatile ListAttribute<Mesto, Korisnik> korisnikList;

}