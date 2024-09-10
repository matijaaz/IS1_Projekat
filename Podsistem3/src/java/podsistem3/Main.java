/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package podsistem3;

import entities.Gledanje;
import entities.Korisnik;
import entities.Ocena;
import entities.Paket;
import entities.Pretplata;
import entities.Video;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
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

    @Resource(lookup="Topic23")
    static Topic myTopic;
    
    @Resource(lookup="jms/__defaultConnectionFactory")
    static ConnectionFactory connectionFactory;
    
    @Resource(lookup="red23")
    static Queue red;
    
    @Resource(lookup="RedZahteva1")
    private static Queue myQueue;
    
    @Resource(lookup ="RedOdgovora1")
    private static Queue MyQueueResponse;
    
    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("Podsistem3PU");
        EntityManager em = emf.createEntityManager();
        
        new Receiver(em).start();
        new Info23(em).start();
        
        JMSContext context = connectionFactory.createContext();
        JMSProducer producer = context.createProducer();
        JMSConsumer consumer = context.createConsumer(myQueue,  "Zahtev = 9 OR Zahtev = 10 OR Zahtev = 11 OR Zahtev = 12 OR Zahtev = 13 OR Zahtev = 14 OR Zahtev = 15 OR Zahtev = 22 OR Zahtev = 23 OR Zahtev = 24 OR Zahtev = 25");
        
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

    private static void doRequest(Message message, int request, EntityManager em, JMSContext context, JMSProducer producer) throws JMSException {
        
        Object object = null; 
        Integer cena = null;
        Integer ocenaNova = null;
        
        switch(request) {
            case 9 : {
                if (message instanceof TextMessage) {
                   int cena1 = message.getIntProperty("cena");
                   Paket p = new Paket();
                   p.setCena(cena1);
                   object = p;
                }
                break;
            }
            
            case 10 : {
                
                if (message instanceof TextMessage) {
                   cena = message.getIntProperty("cena");
                   Paket p = em.find(Paket.class, message.getIntProperty("paket"));
                   if (p == null) {
                       TextMessage msg = context.createTextMessage("Ne postoji zadati paket.");
                       producer.send(MyQueueResponse,msg);
                       return;
                   }
                   object = p;
                }
                break;
            
            }
            
            case 11 : {
                
                 if (message instanceof TextMessage) {
                    int paket = message.getIntProperty("paket");
                    int korisnik = message.getIntProperty("idKor");
                    Korisnik k = em.find(Korisnik.class, korisnik);
                    if (k == null) {
                       TextMessage msg = context.createTextMessage("Ne postoji zadati korisnik.");
                       producer.send(MyQueueResponse,msg);
                       return;
                    }
                    Paket p = em.find(Paket.class, paket);
                    if (p == null) {
                       TextMessage msg = context.createTextMessage("Ne postoji zadati paket.");
                       producer.send(MyQueueResponse,msg);
                       return;
                    }
                    //List<Pretplata> pretplate = k.getPretplataList();
                    List<Pretplata> pretplate = em.createQuery("select p from Pretplata p where p.korisnik.sifK = :sifK", Pretplata.class).setParameter("sifK", k.getSifK()).getResultList();
                    for (Pretplata pr : pretplate) {
                       if(!(imaViseOdMesecDana(pr.getDatumvremepocetka()))) {
                           TextMessage msg = context.createTextMessage("Nije proslo mesec dana od poslednje pretplate navedenog korisnika.");
                           producer.send(MyQueueResponse,msg);
                           return;
                       }
                    }

                    Date date = new Date();
                    //Date date = getDatum();
                    
                    Pretplata pretplata = new Pretplata();
                    pretplata.setPaket(p);
                    pretplata.setKorisnik(k);
                    pretplata.setCena(p.getCena());
                    pretplata.setDatumvremepocetka(date);
                    object = pretplata;
                }
                break;
                
            }
            
            case 12 : {
                
                 if (message instanceof TextMessage) {
                    int vid = message.getIntProperty("video");
                    int kor = message.getIntProperty("idKor");
                    int sekundi = message.getIntProperty("sekundi");
                    int odgledano = message.getIntProperty("odgledano");
                    Video video = em.find(Video.class, vid);
                    if (video == null) {
                       TextMessage msg = context.createTextMessage("Ne postoji zadati video.");
                       producer.send(MyQueueResponse,msg);
                       return;
                    }
                    Korisnik korisnik = em.find(Korisnik.class, kor);
                    if(korisnik == null) {
                       TextMessage msg = context.createTextMessage("Ne postoji zadati korisnik.");
                       producer.send(MyQueueResponse,msg);
                       return; 
                    }
                    if (sekundi > video.getTrajanje()) {
                        sekundi = video.getTrajanje();
                    }
                    if (odgledano + sekundi > video.getTrajanje()) {
                        odgledano = video.getTrajanje() - sekundi;
                    }
                    Gledanje gledanje = new Gledanje();
                    gledanje.setDatumvremepocetka(new Date());
                    gledanje.setSekund(sekundi);
                    gledanje.setOdgledanosekundi(odgledano);
                    gledanje.setKorisnik(korisnik);
                    gledanje.setVideo(video);
                    object = gledanje;
                }
                break;
                
                
            }
            
            case 13 : {
            
                if (message instanceof TextMessage) {
                   
                   int ocena = message.getIntProperty("ocena");
                   int kor = message.getIntProperty("idKor");
                   int vid = message.getIntProperty("video");
                    
                   Video video = em.find(Video.class, vid);
                    if (video == null) {
                       TextMessage msg = context.createTextMessage("Ne postoji zadati video.");
                       producer.send(MyQueueResponse,msg);
                       return;
                    }
                    Korisnik korisnik = em.find(Korisnik.class, kor);
                    if(korisnik == null) {
                       TextMessage msg = context.createTextMessage("Ne postoji zadati korisnik.");
                       producer.send(MyQueueResponse,msg);
                       return; 
                    }
                    
                    List<Ocena> lista = em.createQuery("select o from Ocena o where o.video.sifV = :sifV and o.korisnik.sifK = :sifK",Ocena.class).setParameter("sifV", video.getSifV()).setParameter("sifK", korisnik.getSifK()).getResultList();
                    if (!lista.isEmpty()) {
                        TextMessage msg = context.createTextMessage("Korisnik je vec dao ocenu navedenom videu.");
                        producer.send(MyQueueResponse,msg);
                        return; 
                    }
                    if (ocena < 1 || ocena > 5) {
                       TextMessage msg = context.createTextMessage("Ocena mora biti od 1 do 5.");
                       producer.send(MyQueueResponse,msg);
                       return; 
                    }
                    
                    Ocena o = new Ocena();
                    o.setVideo(video);
                    o.setKorisnik(korisnik);
                    o.setDatumvreme(new Date());
                    o.setOcena(ocena);
                    
                    object = o;
                    
                  
                }
                break;
                
            }
            
            case 14 : {
                
                if (message instanceof TextMessage) {
                   ocenaNova = message.getIntProperty("ocena");
                   int kor = message.getIntProperty("korisnik");
                   int vid = message.getIntProperty("video");
                   
                   Video video = em.find(Video.class, vid);
                    if (video == null) {
                       TextMessage msg = context.createTextMessage("Ne postoji zadati video.");
                       producer.send(MyQueueResponse,msg);
                       return;
                    }
                    Korisnik korisnik = em.find(Korisnik.class, kor);
                    if(korisnik == null) {
                       TextMessage msg = context.createTextMessage("Ne postoji zadati korisnik.");
                       producer.send(MyQueueResponse,msg);
                       return; 
                    }
                    
                    List<Ocena> lista = em.createQuery("select o from Ocena o where o.video.sifV = :sifV and o.korisnik.sifK = :sifK",Ocena.class).setParameter("sifV", video.getSifV()).setParameter("sifK", korisnik.getSifK()).getResultList();
                    if (lista.isEmpty()) {
                        TextMessage msg = context.createTextMessage("Ne postoji ocena korisnika za navedeni video.");
                        producer.send(MyQueueResponse,msg);
                        return; 
                    }
                    if (ocenaNova < 1 || ocenaNova > 5) {
                       TextMessage msg = context.createTextMessage("Ocena mora biti od 1 do 5.");
                       producer.send(MyQueueResponse,msg);
                       return; 
                    }
                    
                    object = lista.get(0);
                   
                }
                break;
            
            
            }
            
            case 15 : {
                
                if (message instanceof TextMessage) {
                   int kor = message.getIntProperty("korisnik");
                   int vid = message.getIntProperty("video");
                   
                   Video video = em.find(Video.class, vid);
                    if (video == null) {
                       TextMessage msg = context.createTextMessage("Ne postoji zadati video.");
                       producer.send(MyQueueResponse,msg);
                       return;
                    }
                    Korisnik korisnik = em.find(Korisnik.class, kor);
                    if(korisnik == null) {
                       TextMessage msg = context.createTextMessage("Ne postoji zadati korisnik.");
                       producer.send(MyQueueResponse,msg);
                       return; 
                    }
                    
                    List<Ocena> lista = em.createQuery("select o from Ocena o where o.video.sifV = :sifV and o.korisnik.sifK = :sifK",Ocena.class).setParameter("sifV", video.getSifV()).setParameter("sifK", korisnik.getSifK()).getResultList();
                    if (lista.isEmpty()) {
                        TextMessage msg = context.createTextMessage("Ne postoji ocena korisnika za navedeni video.");
                        producer.send(MyQueueResponse,msg);
                        return; 
                    }
                    
                    
                    object = lista.get(0);
                   
                }
                break;
                
            
            
            }
            
            case 22 : {
                
                List<Paket> paket =  em.createNamedQuery("Paket.findAll", Paket.class).getResultList();
                if (paket == null) {
                    
                    ArrayList<Paket> prazno = new ArrayList<>();
                    ObjectMessage msg = context.createObjectMessage(prazno);
                    producer.send(MyQueueResponse,msg);
                    return;
                    
                }
                ArrayList<Paket> paketConvert = new ArrayList<>(paket);
                for(Paket o : paketConvert ) {
                    o.setPretplataList(null);
                }
                ObjectMessage msg = context.createObjectMessage(paketConvert);
                producer.send(MyQueueResponse,msg);
                return;
            }
            
            case 23 : {
                int korisnik = message.getIntProperty("korisnik");
                Korisnik k = em.find(Korisnik.class, korisnik);
                if (k == null) {
                    ArrayList<Pretplata> prazno = new ArrayList<>();
                    ObjectMessage msg = context.createObjectMessage(prazno);
                    producer.send(MyQueueResponse,msg);
                    return;
                }
                //if (k.getPretplataList().isEmpty()) {
                   // ArrayList<Pretplata> prazno = new ArrayList<>();
                    //ObjectMessage msg = context.createObjectMessage(prazno);
                    //producer.send(MyQueueResponse,msg);
                    //return;
                //}
                //List<Pretplata> pretplate =  k.getPretplataList();
                List<Pretplata> pretplate = em.createQuery("select p from Pretplata p where p.korisnik.sifK = :sifK", Pretplata.class).setParameter("sifK", k.getSifK()).getResultList();
                if (pretplate == null) {
                    ArrayList<Pretplata> prazno = new ArrayList<>();
                    ObjectMessage msg = context.createObjectMessage(prazno);
                    producer.send(MyQueueResponse,msg);
                    return;
                }
                if(pretplate.isEmpty()) {
                    ArrayList<Pretplata> prazno = new ArrayList<>();
                    ObjectMessage msg = context.createObjectMessage(prazno);
                    producer.send(MyQueueResponse,msg);
                    return;
                }
                ArrayList<Pretplata> pretplataConvert = new ArrayList<>(pretplate);
                for (Pretplata p : pretplataConvert) {
                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    //Pretvaranje Date objekta u String
                    Date datum = p.getDatumvremepocetka();
                    String datumString = dateFormat.format(datum);
                    p.setDatumVreme(datumString);
                    p.getKorisnik().setGledanjeList(null);
                    p.getKorisnik().setPretplataList(null);
                    p.getKorisnik().setVideoList(null);
                    p.getKorisnik().setOcenaList(null);
                    p.getPaket().setPretplataList(null);
                    
                }
                ObjectMessage msg = context.createObjectMessage(pretplataConvert);
                producer.send(MyQueueResponse,msg);
                return;
            }
            
            case 24 : {
                 
                
                int video = message.getIntProperty("video");
                Video v = em.find(Video.class, video);
                if (v == null) {
                    ArrayList<Gledanje> prazno = new ArrayList<>();
                    ObjectMessage msg = context.createObjectMessage(prazno);
                    producer.send(MyQueueResponse,msg);
                    return;
                }
                //if (v.getGledanjeList().isEmpty()) {
                   // ArrayList<Gledanje> prazno = new ArrayList<>();
                    //ObjectMessage msg = context.createObjectMessage(prazno);
                   // producer.send(MyQueueResponse,msg);
                    //return;
                //}
                //List<Gledanje> gledanje =  v.getGledanjeList();
                List<Gledanje> gledanje = em.createQuery("select g from Gledanje g where g.video.sifV = :sifV", Gledanje.class).setParameter("sifV", v.getSifV()).getResultList();
                if (gledanje == null) {
                    ArrayList<Gledanje> prazno = new ArrayList<>();
                    ObjectMessage msg = context.createObjectMessage(prazno);
                    producer.send(MyQueueResponse,msg);
                    return;
                }
                if(gledanje.isEmpty()) {
                    ArrayList<Gledanje> prazno = new ArrayList<>();
                    ObjectMessage msg = context.createObjectMessage(prazno);
                    producer.send(MyQueueResponse,msg);
                    return;
                }
                ArrayList<Gledanje> gledanjeConvert = new ArrayList<>(gledanje);
                for (Gledanje p : gledanjeConvert) {
                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    //Pretvaranje Date objekta u String
                    Date datum = p.getDatumvremepocetka();
                    String datumString = dateFormat.format(datum);
                    p.setDatumVreme(datumString);
                    p.getKorisnik().setGledanjeList(null);
                    p.getKorisnik().setPretplataList(null);
                    p.getKorisnik().setVideoList(null);
                    p.getKorisnik().setOcenaList(null);
                    p.getVideo().setGledanjeList(null);
                    p.getVideo().setOcenaList(null);
                    
                }
                ObjectMessage msg = context.createObjectMessage(gledanjeConvert);
                producer.send(MyQueueResponse,msg);
                return;
                
            }
            
            case 25 : {
            
                int video = message.getIntProperty("video");
                Video v = em.find(Video.class, video);
                if (v == null) {
                    ArrayList<Ocena> prazno = new ArrayList<>();
                    ObjectMessage msg = context.createObjectMessage(prazno);
                    producer.send(MyQueueResponse,msg);
                    return;
                }
                //if (v.getOcenaList().isEmpty()) {
                   // ArrayList<Ocena> prazno = new ArrayList<>();
                   // ObjectMessage msg = context.createObjectMessage(prazno);
                   // producer.send(MyQueueResponse,msg);
                    //return;
                //}
                //List<Ocena> ocene =  v.getOcenaList();
                List<Ocena> ocene = em.createQuery("select o from Ocena o where o.video.sifV = :sifV", Ocena.class).setParameter("sifV", v.getSifV()).getResultList();
                if (ocene == null) {
                    ArrayList<Ocena> prazno = new ArrayList<>();
                    ObjectMessage msg = context.createObjectMessage(prazno);
                    producer.send(MyQueueResponse,msg);
                    return;
                }
                if(ocene.isEmpty()) {
                    ArrayList<Ocena> prazno = new ArrayList<>();
                    ObjectMessage msg = context.createObjectMessage(prazno);
                    producer.send(MyQueueResponse,msg);
                    return;
                }
               
                ArrayList<Ocena> oceneConvert = new ArrayList<>(ocene);
                for (Ocena p : oceneConvert) {
                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    //Pretvaranje Date objekta u String
                    Date datum = p.getDatumvreme();
                    String datumString = dateFormat.format(datum);
                    p.setDatumVreme(datumString);
                    p.getKorisnik().setGledanjeList(null);
                    p.getKorisnik().setPretplataList(null);
                    p.getKorisnik().setVideoList(null);
                    p.getKorisnik().setOcenaList(null);
                    p.getVideo().setGledanjeList(null);
                    p.getVideo().setOcenaList(null);
                    
                }
                ObjectMessage msg = context.createObjectMessage(oceneConvert);
                producer.send(MyQueueResponse,msg);
                return;
                
            }
        }
            
        
        try {
            em.getTransaction().begin();
            switch (request) {
                case 9:
                    em.persist((Paket)object);
                    break;
                case 10 :
                    if(object != null) ((Paket)object).setCena(cena);break;
                case 11 :
                    em.persist((Pretplata)object); break;
                case 12 : 
                    em.persist((Gledanje)object);break;
                case 13 :
                    em.persist((Ocena)object);break;
                case 14 :
                    if(object != null) ((Ocena)object).setOcena(ocenaNova);break;
                case 15 : 
                    em.remove((Ocena)object);break;
                default:
                    break;
            }
            em.getTransaction().commit();
            }finally {
                if (em.getTransaction().isActive()) {
                    em.getTransaction().rollback();
                }
            }
        
        
        if (request == 9) {
            TextMessage msg = context.createTextMessage("Uspesno kreiran paket.");
            producer.send(MyQueueResponse,msg);
        }
        if (request == 10) {
            TextMessage msg = context.createTextMessage("Uspesno promenjena mesecna cena paketa.");
            producer.send(MyQueueResponse,msg);
        }
        if(request == 11) {
            TextMessage msg = context.createTextMessage("Uspesno kreirana pretplata.");
            producer.send(MyQueueResponse,msg);
        }
        if(request == 12) {
            TextMessage msg = context.createTextMessage("Uspesno kreirano gledanje.");
            producer.send(MyQueueResponse,msg);
        }
        if(request == 13) {
            TextMessage msg = context.createTextMessage("Uspesno postavljanje ocene.");
            producer.send(MyQueueResponse,msg);
        }
        if(request == 14) {
            TextMessage msg = context.createTextMessage("Uspesno izmenjena ocena.");
            producer.send(MyQueueResponse,msg);
        }
        if(request == 15) {
            TextMessage msg = context.createTextMessage("Uspesno obrisana ocena.");
            producer.send(MyQueueResponse,msg);
        }

    }

    private static boolean imaViseOdMesecDana(Date pocetniDatum) {
       // Konvertovanje java.util.Date u LocalDateTime
        Date krajnjiDatum = new Date();
        //Calendar kalendar = Calendar.getInstance();
        //kalendar.setTime(krajnjiDatum1);
        
        // Dodavanje jednog dana na trenutni datum
        //kalendar.add(Calendar.DAY_OF_MONTH, 1);
        //Date krajnjiDatum = kalendar.getTime();
        // Dobijanje novog datuma
        //Date sutrasnjiDatum = kalendar.getTime();
        LocalDateTime pocetni = pocetniDatum.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
        LocalDateTime krajnji = krajnjiDatum.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
        
        // Proveravamo razliku u mesecima
        long monthsBetween = ChronoUnit.MONTHS.between(pocetni, krajnji);
        
        // Ako je razlika u mesecima veća od 1, vraćamo true
        if (monthsBetween > 1) {
            return true;
        }
        
        // Ako je razlika tačno 1 mesec, proveravamo tačan datum i vreme
        LocalDateTime mesecDanaPoslePocetnog = pocetni.plusMonths(1);
        return krajnji.isAfter(mesecDanaPoslePocetnog);
    }

    private static Date getDatum() {
        Calendar calendar = Calendar.getInstance();
        //Postavi proizvoljan datum i vreme
        calendar.set(Calendar.YEAR, 2023);
        calendar.set(Calendar.MONTH, Calendar.JUNE); // Meseci su 0-bazirani (0 = januar)
        calendar.set(Calendar.DAY_OF_MONTH, 15);
        calendar.set(Calendar.HOUR_OF_DAY, 20);
        calendar.set(Calendar.MINUTE, 00);
        calendar.set(Calendar.SECOND, 00);

        
        return calendar.getTime();
    }
    
}
