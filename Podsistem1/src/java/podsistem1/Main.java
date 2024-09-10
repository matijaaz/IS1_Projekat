/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package podsistem1;
 
import entities.Korisnik;
import entities.Mesto;
import java.util.ArrayList;
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
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;


/**
 *
 * @author Matija
 */
public class Main {

    @Resource(lookup="jms/__defaultConnectionFactory")
    private static ConnectionFactory connectionFactory;
    
    @Resource(lookup="RedZahteva1")
    private static Queue myQueue;
    
    @Resource(lookup ="RedOdgovora1")
    private static Queue MyQueueResponse;
    
    @Resource(lookup="Topic23")
    private static Topic myTopic;
    
    //@Resource(lookup ="helper")
    //private static Queue helper;
    
    private static void posaljiKorisnika(Korisnik k,JMSProducer producer,JMSContext context) {
        
        try {
                TextMessage txt = context.createTextMessage();
                txt.setStringProperty("Type", "korisnik");
                txt.setIntProperty("id", k.getSifK());
                txt.setStringProperty("ime", k.getIme());
                txt.setStringProperty("pol", k.getPol());
                txt.setStringProperty("mejl", k.getMejl());
                txt.setIntProperty("godiste", k.getGodiste());
                producer.send(myTopic, txt);
                System.out.println("POSLAO KORISNIKA.");
                
            } catch (JMSException ex) {
                Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
            }
     }
    
    private static void promeniEmail(Korisnik k,String email,JMSProducer producer,JMSContext context) {
            
        try {
                TextMessage txt = context.createTextMessage();
                txt.setStringProperty("Type", "email");
                txt.setStringProperty("email", email);
                txt.setIntProperty("id", k.getSifK());
                producer.send(myTopic, txt);
                System.out.println("POSLAO NOVI EMAIL.");
            } catch (JMSException ex) {
                Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
            }
        
    }
    public static void main(String[] args) {
        
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("Podsistem1PU");
        EntityManager em = emf.createEntityManager();
        
        JMSContext context = connectionFactory.createContext();
        JMSProducer producer = context.createProducer();
        JMSConsumer consumer = context.createConsumer(myQueue,  "Zahtev = 1 OR Zahtev = 2 OR Zahtev = 3 OR Zahtev = 4 OR Zahtev = 17 OR Zahtev = 18");

        
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
        String email = null;
        Mesto me = null;
        switch(request) {
            case 1 : {
                if (message instanceof TextMessage) {
                   String naziv = ((TextMessage)message).getText();
                   List<Mesto> mesto = em.createNamedQuery("Mesto.findByNaziv", Mesto.class).setParameter("naziv", naziv).getResultList();
                   if(!mesto.isEmpty()) {
                       TextMessage msg = context.createTextMessage("Vec postoji grad sa tim imenom.");
                       producer.send(MyQueueResponse,msg);
                       return;
                   }
                   Mesto m = new Mesto();
                   m.setNaziv(naziv);
                   object = m;
                }
                break;
            }
            
            case 2 : {
                if (message instanceof ObjectMessage) {
                   ObjectMessage obj = (ObjectMessage)message;
                   Korisnik k = (Korisnik)obj.getObject();
                   String naziv = k.getSifM().getNaziv();
                   if (!k.getPol().equals("M") && !k.getPol().equals("Z")) {
                       TextMessage msg = context.createTextMessage("Za pol je dozvoljeno uneti M ili Z.");
                       producer.send(MyQueueResponse,msg);
                       return;
                   }
                   List<Mesto> mesto = em.createNamedQuery("Mesto.findByNaziv", Mesto.class).setParameter("naziv", naziv).getResultList();
                   if(mesto.isEmpty()) {
                       TextMessage msg = context.createTextMessage("Ne postoji trazeni grad.");
                       producer.send(MyQueueResponse,msg);
                       return;
                   }
                   
                   k.getSifM().setSifM(mesto.get(0).getSifM());
                   object = k;
                }
                break;
            }
            
            case 3 : {
                if (message instanceof TextMessage) {
                   int kor = message.getIntProperty("idKor");
                   email = message.getStringProperty("email");
                   Korisnik korisnik = em.find(Korisnik.class, kor);
                   if (korisnik == null) {
                       TextMessage txt = context.createTextMessage("Ne postoji taj korisnik.");
                       producer.send(MyQueueResponse,txt);
                       return;
                   }
                   promeniEmail(korisnik,email,producer,context);
                   object = korisnik;
                  
                }
                break;
            }
            
            case 4 : {
                
                 if (message instanceof TextMessage) {
                   int kor = message.getIntProperty("idKor");
                   int sifM = message.getIntProperty("sifM");
                   Korisnik korisnik = em.find(Korisnik.class, kor);
                   if (korisnik == null) {
                       TextMessage txt = context.createTextMessage("Ne postoji taj korisnik.");
                       producer.send(MyQueueResponse,txt);
                       return;
                   }
                   me = em.find(Mesto.class, sifM);
                   if (me == null) {
                       TextMessage txt = context.createTextMessage("Ne postoji navedeno mesto.");
                       producer.send(MyQueueResponse,txt);
                       return;
                   }
                   
                   object = korisnik;
                   break;
                }
                 
            }
            
            case 17 : {
            
                List<Mesto> mesta =  em.createNamedQuery("Mesto.findAll", Mesto.class).getResultList();
                if (mesta == null) {
                    ArrayList<Mesto> prazno = new ArrayList<>();
                    ObjectMessage msg = context.createObjectMessage(prazno);
                    producer.send(MyQueueResponse,msg);
                    return;
                }
                ArrayList<Mesto> mestaConvert = new ArrayList<>(mesta);
                for(Mesto o : mestaConvert ) {
                    o.setKorisnikList(null);
                }
                ObjectMessage msg = context.createObjectMessage(mestaConvert);
                producer.send(MyQueueResponse,msg);
                return;
               
            }
            
            case 18 : {
                List<Korisnik> korisnici =  em.createNamedQuery("Korisnik.findAll", Korisnik.class).getResultList();
                if (korisnici == null) {
                
                    ArrayList<Korisnik> prazno = new ArrayList<>();
                    ObjectMessage msg = context.createObjectMessage(prazno);
                    producer.send(MyQueueResponse,msg);
                    return;
                }
                ArrayList<Korisnik> korisniciConvert = new ArrayList<>(korisnici);
                for(Korisnik k : korisniciConvert) {
                    k.getSifM().setKorisnikList(null);
                }
                ObjectMessage msg = context.createObjectMessage(korisniciConvert);
                producer.send(MyQueueResponse,msg);
                return;
            }
        }
        
        try {
            em.getTransaction().begin();
            switch (request) {
                case 1:
                    em.persist((Mesto)object);
                    break;
                case 2:
                    em.persist((Korisnik)object);
                    break;
                case 3:
                    Korisnik k = (Korisnik)object;
                    if (k != null ) k.setMejl(email);
                    break;
                case 4:
                    Korisnik kor = (Korisnik)object;
                    if(kor != null) kor.setSifM(me);
                default:
                    break;
            }
            em.getTransaction().commit();
            }finally {
                if (em.getTransaction().isActive()) {
                    em.getTransaction().rollback();
                }
            }
        
        if (request == 1) {
            TextMessage msg = context.createTextMessage("Uspesno kreiran grad.");
            producer.send(MyQueueResponse,msg);
        }
        
        if (request == 2) {
            TextMessage msg = context.createTextMessage("Uspesno kreiran korisnik.");
            posaljiKorisnika((Korisnik)object, producer,context);
            producer.send(MyQueueResponse,msg);
        }
        if (request == 3) {
            TextMessage msg = context.createTextMessage("Uspesno izmenjen email za datog korisnika.");
            producer.send(MyQueueResponse,msg);
        }
        if (request == 4) {
            TextMessage msg = context.createTextMessage("Uspesno promenjeno mesto za datog korisnika.");
            producer.send(MyQueueResponse,msg);
        }
        
    }
    
}
