/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package podsistem3;

import entities.Gledanje;
import entities.Korisnik;
import entities.Ocena;
import entities.Video;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.jms.JMSConsumer;
import javax.jms.JMSContext;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.persistence.EntityManager;
import static podsistem3.Main.red;

/**
 *
 * @author Matija
 */
public class Info23 extends Thread{
    
    private EntityManager em;

    public Info23(EntityManager em) {
        this.em = em;
    }

    @Override
    public void run() {
        JMSContext context = Main.connectionFactory.createContext();
        JMSConsumer consumer = context.createConsumer(red);
        
        while (true) {
        
            Object obj = null;
            String naziv = null;
            List<Ocena> ocene = null;
            List<Gledanje> gledanja = null;
         try {
              Message txt = consumer.receive();
              String type = txt.getStringProperty("Type");
              
              switch(type) {
              
                  case "video" : {
                      System.out.println("PRIMIO VIDEO.");
                      Video video = new Video();
                      video.setSifV( txt.getIntProperty("id"));
                      video.setNaziv(txt.getStringProperty("naziv"));
                      video.setTrajanje(txt.getIntProperty("trajanje"));
                      int idKor = txt.getIntProperty("korisnik");
                      Korisnik korisnik = em.find(Korisnik.class, idKor);
                      video.setVlasnik(korisnik);
                      String datum_vreme = txt.getStringProperty("datum_vreme");
                      SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                      video.setDatumvremepostavljanja(dateFormat.parse(datum_vreme));
                      obj = video;
                      break;
                  }
                  
                  case "videoPromenaNaziva" : {
                      
                      System.out.println("PRIMIO NOV NAZIV VIDEA.");
                      
                      naziv = txt.getStringProperty("naziv");
                      int video = txt.getIntProperty("idVid");
                      
                      obj = em.find(Video.class,video);
                  
                      break;
                  }
                  
                  case "obrisiVideo" : {
                     
                    System.out.println("PRIMLJEN VIDEO ZA BRISANJE");
                    int id = txt.getIntProperty("idVid");
                    Video video = em.find(Video.class, id);
                    
                    ocene = video.getOcenaList();
                    gledanja = video.getGledanjeList();
                    obj = video;
                    break;
                  }
                  
              
              }
              
              
            em.getTransaction().begin();
            switch(type) {
                case "video" : { em.persist((Video)obj); break; }
                case "videoPromenaNaziva" : { if(obj != null) ((Video)obj).setNaziv(naziv);break;}
                case "obrisiVideo" : {
                    if (ocene != null && !ocene.isEmpty()) {
                        for (Ocena o : ocene) {
                            em.remove(o);
                        }
                    }
                    if (gledanja != null && !gledanja.isEmpty()) {
                        for (Gledanje g : gledanja) {
                            em.remove(g);
                        }
                    }
                    if (obj!=null) em.remove((Video)obj);
                    break;
                }
            }
              
              
            em.getTransaction().commit();
            
           
               
               
           } catch (JMSException ex) {
               Logger.getLogger(Receiver.class.getName()).log(Level.SEVERE, null, ex);
           } catch (ParseException ex) {
                Logger.getLogger(Info23.class.getName()).log(Level.SEVERE, null, ex);
            }finally {
                if (em.getTransaction().isActive()) {
                    em.getTransaction().rollback();
                }
            }
        
        }
    }
    
    
    
}
