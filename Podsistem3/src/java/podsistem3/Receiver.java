/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package podsistem3;

import entities.Korisnik;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.jms.JMSConsumer;
import javax.jms.JMSContext;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.persistence.EntityManager;


public class Receiver extends Thread {

    private EntityManager em;
    
    
    @Override
    public void run() {
       JMSContext context = Main.connectionFactory.createContext();
       JMSConsumer consumer = context.createConsumer(Main.myTopic, "Type = 'korisnik' or Type = 'email'");
        
       while(true) {
           
            Object obj = null;
            String email = null;
         try {
              Message txt = consumer.receive();
              String type = txt.getStringProperty("Type");
              
              switch(type) {
              
                  case "korisnik" : {
                      System.out.println("PRIMIO KORISNIKA.");
                      Korisnik korisnik = new Korisnik();
                      korisnik.setSifK( txt.getIntProperty("id"));
                      korisnik.setIme(txt.getStringProperty("ime"));
                      korisnik.setPol(txt.getStringProperty("pol"));
                      korisnik.setMejl(txt.getStringProperty("mejl"));
                      korisnik.setGodiste(txt.getIntProperty("godiste"));
                      obj = korisnik;
                      break;
                  }
                  
                  case "email" : {
                      System.out.println("PRIMIO NOVI EMAIL KORISNIKA.");
                      int x = txt.getIntProperty("id");
                      email = txt.getStringProperty("email");
                      obj = em.find(Korisnik.class, x);
                      break;
                  }
              
              }
              
              
            em.getTransaction().begin();
            switch(type) {
                case "korisnik" : { em.persist((Korisnik)obj); break; }
                case "email" : { if (obj != null)((Korisnik)obj).setMejl(email);break;}
              
            }
              
              
            em.getTransaction().commit();
            
           
               
               
           } catch (JMSException ex) {
               Logger.getLogger(Receiver.class.getName()).log(Level.SEVERE, null, ex);
           }finally {
                if (em.getTransaction().isActive()) {
                    em.getTransaction().rollback();
                }
            }
        
       }
    }

    public Receiver(EntityManager em) {
        this.em = em;
    }
    
    
    
    
    
}
