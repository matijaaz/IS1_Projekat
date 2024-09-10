/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package podsistem2;

import entities.Kategorija;
import entities.Korisnik;
import entities.Video;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.Resource;
import javax.jms.ConnectionFactory;
import javax.jms.JMSConsumer;
import javax.jms.JMSContext;
import javax.jms.JMSException;
import javax.jms.JMSProducer;
import javax.jms.Message;
import javax.jms.ObjectMessage;
import javax.jms.Queue;
import javax.jms.TextMessage;
import javax.jms.Topic;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 *
 * @author Matija
 */
public class Main {

    @Resource(lookup="jms/__defaultConnectionFactory")
    static ConnectionFactory connectionFactory;
    
    @Resource(lookup="RedZahteva1")
    private static Queue myQueue;
    
    @Resource(lookup ="RedOdgovora1")
    private static Queue MyQueueResponse;
    
    @Resource(lookup="Topic23")
    static Topic myTopic;
    
    @Resource(lookup="red23")
    private static Queue red;
    
    
    
    
    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("Podsistem2PU");
        EntityManager em = emf.createEntityManager();
        
        new Receiver(em).start();
        JMSContext context = connectionFactory.createContext();
        JMSProducer producer = context.createProducer();
        JMSConsumer consumer = context.createConsumer(myQueue,  "Zahtev = 5 OR Zahtev = 6 OR Zahtev = 7 OR Zahtev = 8 OR Zahtev = 16 OR Zahtev = 19 OR Zahtev = 20 OR Zahtev = 21");
        
        while(true) {
            try {
                Message message = consumer.receive();
                int request = message.getIntProperty("Zahtev"); 
                System.out.println("PRIMLJEN ZAHTEV SA REDNIM BROJEM : " + request);
                doRequest(message,request,em,context,producer);
                System.out.println("POSLAT ODGOVOR SERVERU NA ZAHTEV : " + request);
            } 
            catch (JMSException ex) {
                Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    
    private static void doRequest(Message message, int request,EntityManager em, JMSContext context, JMSProducer producer) throws JMSException {
         Object object = null; 
         String naziv1 = null;
         Video video1 = null;
         switch(request) {
            case 5 : {
                if (message instanceof TextMessage) {
                   String naziv = ((TextMessage)message).getText();
                   List<Kategorija> kategorija = em.createNamedQuery("Kategorija.findByNaziv", Kategorija.class).setParameter("naziv", naziv).getResultList();
                   if(!kategorija.isEmpty()) {
                       TextMessage msg = context.createTextMessage("Vec postoji kategorija sa tim imenom.");
                       producer.send(MyQueueResponse,msg);
                       return;
                   }
                   Kategorija k = new Kategorija();
                   k.setNaziv(naziv);
                   object = k;
                }
                break;
            }
            
            case 6 : {
                if (message instanceof ObjectMessage) {
                   ObjectMessage obj = (ObjectMessage)message;
                   Video video = (Video)obj.getObject();
                   Integer vlasnik = obj.getIntProperty("Vlasnik");
                   Korisnik k = em.find(Korisnik.class, vlasnik);
                   
                   if(k == null) {
                       TextMessage msg = context.createTextMessage("Ne postoji zadati korisnik.");
                       producer.send(MyQueueResponse,msg);
                       return;
                   }
                   
                    video.setVlasnik(k);
                    //Calendar calendar = Calendar.getInstance();

                    // Postavi proizvoljan datum i vreme
                    //calendar.set(Calendar.YEAR, 2023);
                    //calendar.set(Calendar.MONTH, Calendar.MARCH); // Meseci su 0-bazirani (0 = januar)
                    //calendar.set(Calendar.DAY_OF_MONTH, 23);
                    //calendar.set(Calendar.HOUR_OF_DAY, 23);
                    //calendar.set(Calendar.MINUTE, 10);
                    //calendar.set(Calendar.SECOND, 00);

                    Date date = new Date();
                    //Date date = calendar.getTime();
                    
                   video.setDatumvremepostavljanja(date);
                   object = video;
                }
                break;
            }
            
            case 7 : {
            
                 if (message instanceof TextMessage) {
                   int video = message.getIntProperty("video");
                   naziv1 = message.getStringProperty("naziv");
                   Video vid = em.find(Video.class, video);
                   if (vid == null) {
                       TextMessage txt = context.createTextMessage("Ne postoji taj video.");
                       producer.send(MyQueueResponse,txt);
                       return;
                   }
                   promeniNaziv(naziv1,vid,producer,context);
                   object = vid;
                  
                }
                break;
            }
            
            case 8 : {
                if (message instanceof TextMessage) {
                   int video = message.getIntProperty("video");
                   String nazivKategorije = message.getStringProperty("naziv");
                   video1 = em.find(Video.class, video);
                   if (video1 == null) {
                       TextMessage txt = context.createTextMessage("Ne postoji taj video.");
                       producer.send(MyQueueResponse,txt);
                       return;
                   }
                   List<Kategorija> kategorija = em.createNamedQuery("Kategorija.findByNaziv", Kategorija.class).setParameter("naziv", nazivKategorije).getResultList();
                   if(kategorija.isEmpty()) {
                       TextMessage txt = context.createTextMessage("Ne postoji navedena kategorija.");
                       producer.send(MyQueueResponse,txt);
                       return;
                   }
                   
                 List<Kategorija> kategorije = em.createQuery("select k from Kategorija k join k.videoList v where v.sifV = :sifV", Kategorija.class).setParameter("sifV", video1.getSifV()).getResultList();
                 
                 for (Kategorija ka : kategorije) {
                     if(Objects.equals(ka.getSifK(), kategorija.get(0).getSifK())) {
                          TextMessage txt = context.createTextMessage("Kategorija je vec dodeljena videu.");
                          producer.send(MyQueueResponse,txt);
                          return;
                     }
                 }
                 
                 object = kategorija.get(0);
                   
                   
                }
                break;
                
            }
            
            case 16 : {
            
                if (message instanceof TextMessage) {
                   int video = message.getIntProperty("video");
                   int vlasnik = message.getIntProperty("vlasnik");
                   Video vid = em.find(Video.class, video);
                   if (vid == null) {
                       TextMessage txt = context.createTextMessage("Ne postoji taj video.");
                       producer.send(MyQueueResponse,txt);
                       return;
                   }
                   
                   Korisnik vl = em.find(Korisnik.class, vlasnik);
                   if (vl == null) {
                       TextMessage txt = context.createTextMessage("Ne postoji taj korisnik.");
                       producer.send(MyQueueResponse,txt);
                       return;
                   }
                   
                   if(!vid.getVlasnik().equals(vl)) {
                       TextMessage txt = context.createTextMessage("Navedeni korisnik nije vlasnik videa.");
                       producer.send(MyQueueResponse,txt);
                       return;
                   }
                   posaljiSignalBrisanja(vid,context,producer);
                   object = vid;
                  
                }
                break;
            
            }
            
            case 19 : {
                
                List<Kategorija> kategorije =  em.createNamedQuery("Kategorija.findAll", Kategorija.class).getResultList();
                if (kategorije == null) {
                    ArrayList<Kategorija> prazno = new ArrayList<>();
                    ObjectMessage msg = context.createObjectMessage(prazno);
                    producer.send(MyQueueResponse,msg);
                    return;
                }
                ArrayList<Kategorija> kategorijaConvert = new ArrayList<>(kategorije);
                for(Kategorija o : kategorijaConvert ) {
                    o.setVideoList(null);
                }
                ObjectMessage msg = context.createObjectMessage(kategorijaConvert);
                producer.send(MyQueueResponse,msg);
                return;
                
            
            }
            
            case 20 : {
                List<Video> vid =  em.createNamedQuery("Video.findAll", Video.class).getResultList();
                if (vid == null) {
                    ArrayList<Video> prazno = new ArrayList<>();
                    ObjectMessage msg = context.createObjectMessage(prazno);
                    producer.send(MyQueueResponse,msg);
                    return;
                }
                ArrayList<Video> videoConvert = new ArrayList<>(vid);
                for(Video o : videoConvert ) {
                    o.setKategorijaList(null);
                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    //Pretvaranje Date objekta u String
                    Date datum = o.getDatumvremepostavljanja();
                    String datumString = dateFormat.format(datum);
                    o.setDatumVreme(datumString);
                }
                
                ObjectMessage msg = context.createObjectMessage(videoConvert);
                producer.send(MyQueueResponse,msg);
                return;
            }
            
            case 21 : {
                int videoId = message.getIntProperty("video");
                Video v = em.find(Video.class,videoId);
                if(v == null) {
                    ArrayList<Kategorija> prazno = new ArrayList<>();
                    ObjectMessage obj = context.createObjectMessage(prazno);
                    producer.send(MyQueueResponse,obj);
                    return;
                }
                //List<Kategorija> kategorije = v.getKategorijaList();
                List<Kategorija> kategorije = em.createQuery("select k from Kategorija k join k.videoList v where v.sifV = :sifV", Kategorija.class).setParameter("sifV", v.getSifV()).getResultList();
                 if (kategorije == null) {
                    ArrayList<Kategorija> prazno = new ArrayList<>();
                    ObjectMessage msg = context.createObjectMessage(prazno);
                    producer.send(MyQueueResponse,msg);
                    return;
                }
                if(kategorije.isEmpty()) {
                    ArrayList<Kategorija> prazno = new ArrayList<>();
                    ObjectMessage msg = context.createObjectMessage(prazno);
                    producer.send(MyQueueResponse,msg);
                    return;
                }
                ArrayList<Kategorija> kategorijaConvert = new ArrayList<>(kategorije);
                for(Kategorija o : kategorijaConvert ) {
                    o.setVideoList(null);
                }
                ObjectMessage msg = context.createObjectMessage(kategorijaConvert);
                producer.send(MyQueueResponse,msg);
                return;
            }
        }
         
         
         try {
            em.getTransaction().begin();
            switch (request) {
                case 5:
                    em.persist((Kategorija)object);
                    break;
                case 6 :
                    em.persist((Video)object);
                    break;
                case 7 :
                    if (object != null)((Video)object).setNaziv(naziv1);
                    break;
                case 8 :
                   Kategorija k = (Kategorija)object;
                   if(k != null) {
                    List<Video> lista = em.createQuery("select v from Video v join v.kategorijaList k where k.sifK = :sifK", Video.class).setParameter("sifK", k.getSifK()).getResultList();
                    System.out.println(lista.size());
                    lista.add(video1);
                    k.setVideoList(lista);
                    
                   }
                   break;
                case 16 : {
                    em.remove((Video)object);
                    break;
                }
                default:
                    break;
            }
            em.getTransaction().commit();
            }finally {
                if (em.getTransaction().isActive()) {
                    em.getTransaction().rollback();
                }
            }
        
        if (request == 5) {
            TextMessage msg = context.createTextMessage("Uspesno kreirana kategorija.");
            producer.send(MyQueueResponse,msg);
        }
        if (request == 6) {
            TextMessage msg = context.createTextMessage("Uspesno postavljen video.");
            posaljiVideo((Video)object, context, producer);
            producer.send(MyQueueResponse,msg);
        }
        if (request == 7) {
            TextMessage msg = context.createTextMessage("Uspesno izmenjen naziv videa.");
            producer.send(MyQueueResponse,msg);
        }
        if (request == 8) {
            TextMessage msg = context.createTextMessage("Uspesno dodata kategorija navedenom videu.");
            producer.send(MyQueueResponse,msg);
        }
         if (request == 16) {
            TextMessage msg = context.createTextMessage("Uspesno izbrisan video snimak.");
            producer.send(MyQueueResponse,msg);
        }
    }

    private static void posaljiVideo(Video video, JMSContext context, JMSProducer producer) {
        try {
            TextMessage txt = context.createTextMessage();
            txt.setStringProperty("Type", "video");
            txt.setIntProperty("id", video.getSifV());
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date datum = video.getDatumvremepostavljanja();
            String datumString = dateFormat.format(datum);
            txt.setStringProperty("datum_vreme", datumString);
            txt.setStringProperty("naziv", video.getNaziv());
            txt.setIntProperty("trajanje",video.getTrajanje());
            txt.setIntProperty("korisnik", video.getVlasnik().getSifK());
            producer.send(red,txt);
            System.out.println("POSLAT VIDEO PODSISTEMU 3.");
        } catch (JMSException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
       
    }

    private static void promeniNaziv(String naziv,Video video, JMSProducer producer, JMSContext context) {
         try {
            TextMessage txt = context.createTextMessage();
            txt.setStringProperty("Type", "videoPromenaNaziva");
            txt.setStringProperty("naziv", naziv);
            txt.setIntProperty("idVid", video.getSifV());
            producer.send(red,txt);
            System.out.println("POSLAT NOV NAZIV VIDEA PODSISTEMU 3.");
        } catch (JMSException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private static void posaljiSignalBrisanja(Video vid, JMSContext context, JMSProducer producer) {
        try {
            TextMessage txt = context.createTextMessage();
            txt.setStringProperty("Type", "obrisiVideo");
            txt.setIntProperty("idVid", vid.getSifV());
            producer.send(red,txt);
            System.out.println("POSLAT ID VIDEA ZA BRISENJE PODSISTEMU 3.");
        } catch (JMSException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
