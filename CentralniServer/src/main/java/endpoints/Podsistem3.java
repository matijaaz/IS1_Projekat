/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package endpoints;

import entities.Gledanje;
import entities.Kategorija;
import entities.Ocena;
import entities.Paket;
import entities.Pretplata;
import entities.Video;
import java.util.ArrayList;
import java.util.List;
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
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 *
 * @author Matija
 */

@Path("podsistem3")
public class Podsistem3 {
    
    @Resource(lookup="jms/__defaultConnectionFactory")
    private ConnectionFactory connectionFactory;
    
    @Resource(lookup="RedZahteva1")
    private Queue myQueue;
    
    @Resource(lookup ="RedOdgovora1")
    private Queue MyQueueResponse;
    
    @POST
    @Path("paket")
    public Response kreirajPaket(Integer cena) {
        
        JMSContext context = connectionFactory.createContext();
        JMSProducer producer = context.createProducer();
        JMSConsumer consumer = context.createConsumer(MyQueueResponse);
        
        String poruka = "Nije stigla poruka";
        
        try {
            TextMessage msg = context.createTextMessage();
            msg.setIntProperty("cena", cena);
            msg.setIntProperty("Zahtev", 9);
            producer.send(myQueue, msg);
            
            Message response = consumer.receive();
            if (response instanceof TextMessage) {
               poruka = ((TextMessage)response).getText();
            }
        }
        catch (JMSException ex) {
            context.close();
            consumer.close();
            return Response.status(Response.Status.BAD_REQUEST).entity("Nije poslata poruka.").build();
        }
        
        context.close();
        consumer.close();
        return Response.status(Response.Status.OK).entity(poruka).build();
        
    
    }
    
    
   @PUT
   @Path("paket/{paket}")
   public Response promeniCenuPaketa(@PathParam("paket")Integer paket,Integer novaCena) {
       
        JMSContext context = connectionFactory.createContext();
        JMSProducer producer = context.createProducer();
        JMSConsumer consumer = context.createConsumer(MyQueueResponse);
        
        String poruka = "Nije stigla poruka";
        
        try {
            TextMessage msg = context.createTextMessage();
            msg.setIntProperty("Zahtev", 10);
            msg.setIntProperty("paket", paket);
            msg.setIntProperty("cena", novaCena);
            producer.send(myQueue, msg);
            
            Message response = consumer.receive();
            if (response instanceof TextMessage) {
               poruka = ((TextMessage)response).getText();
            }
        }
        catch (JMSException ex) {
            context.close();
            consumer.close();
            return Response.status(Response.Status.BAD_REQUEST).entity("Nije poslata poruka.").build();
        }
        
        context.close();
        consumer.close();
        return Response.status(Response.Status.OK).entity(poruka).build();
        
   }
    
   
    @POST
    @Path("pretplata/{idKor}")
    public Response kreirajPretplatu(@PathParam("idKor") Integer idKor,Integer paket) {
        
        JMSContext context = connectionFactory.createContext();
        JMSProducer producer = context.createProducer();
        JMSConsumer consumer = context.createConsumer(MyQueueResponse);
        
        String poruka = "Nije stigla poruka";
        
        try {
            TextMessage msg = context.createTextMessage();
            msg.setIntProperty("paket", paket);
            msg.setIntProperty("idKor", idKor);
            msg.setIntProperty("Zahtev", 11);
            producer.send(myQueue, msg);
            
            Message response = consumer.receive();
            if (response instanceof TextMessage) {
               poruka = ((TextMessage)response).getText();
            }
        }
        catch (JMSException ex) {
            context.close();
            consumer.close();
            return Response.status(Response.Status.BAD_REQUEST).entity("Nije poslata poruka.").build();
        }
        
        context.close();
        consumer.close();
        return Response.status(Response.Status.OK).entity(poruka).build();
        
    
    }
    
    @POST
    @Path("gledanje/{idKor}/{sekundi}/{odgledano}")
    public Response kreirajGledanje(@PathParam("idKor") Integer idKor,@PathParam("sekundi") Integer sekundi, @PathParam("odgledano") Integer odgledano,Integer video) {
        
        JMSContext context = connectionFactory.createContext();
        JMSProducer producer = context.createProducer();
        JMSConsumer consumer = context.createConsumer(MyQueueResponse);
        
        String poruka = "Nije stigla poruka";
        
        try {
            TextMessage msg = context.createTextMessage();
            msg.setIntProperty("video", video);
            msg.setIntProperty("idKor", idKor);
            msg.setIntProperty("sekundi", sekundi);
            msg.setIntProperty("odgledano", odgledano);
            msg.setIntProperty("Zahtev", 12);
            producer.send(myQueue, msg);
            
            Message response = consumer.receive();
            if (response instanceof TextMessage) {
               poruka = ((TextMessage)response).getText();
            }
        }
        catch (JMSException ex) {
            context.close();
            consumer.close();
            return Response.status(Response.Status.BAD_REQUEST).entity("Nije poslata poruka.").build();
        }
        
        context.close();
        consumer.close();
        return Response.status(Response.Status.OK).entity(poruka).build();
        
    
    }
    
    @POST
    @Path("ocena/{idKor}/{ocena}")
    public Response oceniVideo(@PathParam("idKor") Integer idKor,@PathParam("ocena") Integer ocena,Integer video) {
        
        JMSContext context = connectionFactory.createContext();
        JMSProducer producer = context.createProducer();
        JMSConsumer consumer = context.createConsumer(MyQueueResponse);
        
        String poruka = "Nije stigla poruka";
        
        try {
            TextMessage msg = context.createTextMessage();
            msg.setIntProperty("ocena", ocena);
            msg.setIntProperty("idKor", idKor);
            msg.setIntProperty("video", video);
            msg.setIntProperty("Zahtev", 13);
            producer.send(myQueue, msg);
            
            Message response = consumer.receive();
            if (response instanceof TextMessage) {
               poruka = ((TextMessage)response).getText();
            }
        }
        catch (JMSException ex) {
            context.close();
            consumer.close();
            return Response.status(Response.Status.BAD_REQUEST).entity("Nije poslata poruka.").build();
        }
        
        context.close();
        consumer.close();
        return Response.status(Response.Status.OK).entity(poruka).build();
        
    
    }
    
   @PUT
   @Path("ocena/{ocena}/{video}")
   public Response promeniOcenu(@PathParam("ocena") Integer ocena,@PathParam("video") Integer video,Integer idKor) {
       
        JMSContext context = connectionFactory.createContext();
        JMSProducer producer = context.createProducer();
        JMSConsumer consumer = context.createConsumer(MyQueueResponse);
        
        String poruka = "Nije stigla poruka";
        
        try {
            TextMessage msg = context.createTextMessage();
            msg.setIntProperty("Zahtev", 14);
            msg.setIntProperty("ocena", ocena);
            msg.setIntProperty("video", video);
            msg.setIntProperty("korisnik", idKor);
            producer.send(myQueue, msg);
            
            Message response = consumer.receive();
            if (response instanceof TextMessage) {
               poruka = ((TextMessage)response).getText();
            }
        }
        catch (JMSException ex) {
            context.close();
            consumer.close();
            return Response.status(Response.Status.BAD_REQUEST).entity("Nije poslata poruka.").build();
        }
        
        context.close();
        consumer.close();
        return Response.status(Response.Status.OK).entity(poruka).build();
        
   }
   
   @DELETE
   @Path("ocena/{video}/{idKor}")
   public Response promeniOcenu(@PathParam("video") Integer video,@PathParam("idKor") Integer idKor) {
       
        JMSContext context = connectionFactory.createContext();
        JMSProducer producer = context.createProducer();
        JMSConsumer consumer = context.createConsumer(MyQueueResponse);
        
        String poruka = "Nije stigla poruka";
        
        try {
            TextMessage msg = context.createTextMessage();
            msg.setIntProperty("Zahtev", 15);
            msg.setIntProperty("video", video);
            msg.setIntProperty("korisnik", idKor);
            producer.send(myQueue, msg);
            
            Message response = consumer.receive();
            if (response instanceof TextMessage) {
               poruka = ((TextMessage)response).getText();
            }
        }
        catch (JMSException ex) {
            context.close();
            consumer.close();
            return Response.status(Response.Status.BAD_REQUEST).entity("Nije poslata poruka.").build();
        }
        
        context.close();
        consumer.close();
        return Response.status(Response.Status.OK).entity(poruka).build();
        
   }
   
   
   @GET
   @Path("paket")
   @Produces(MediaType.APPLICATION_JSON)
   public Response dohvatiPakete() {
       
        JMSContext context = connectionFactory.createContext();
        JMSProducer producer = context.createProducer();
        JMSConsumer consumer = context.createConsumer(MyQueueResponse);
        
        List<Paket> poruka = null;
        
        try {
            TextMessage msg = context.createTextMessage();
            msg.setIntProperty("Zahtev", 22);
            producer.send(myQueue, msg);
            
            Message response = consumer.receive();
            if (response instanceof ObjectMessage) {
                ObjectMessage msg1 = (ObjectMessage)response;
                poruka =  (ArrayList<Paket>)msg1.getObject();
            }
        }
        catch (JMSException ex) {
            context.close();
            consumer.close();
            return Response.status(Response.Status.BAD_REQUEST).entity("Nije poslata poruka.").build();
        }
        
        context.close();
        consumer.close();
        
        return Response.status(Response.Status.OK).entity(new GenericEntity<List<Paket>>(poruka){}).build();
       
   }
   
   @GET
   @Path("pretplata/{korisnik}")
   @Produces(MediaType.APPLICATION_JSON)
   public Response dohvatiPretplateKorisnika(@PathParam("korisnik") Integer korisnik) {
       
        JMSContext context = connectionFactory.createContext();
        JMSProducer producer = context.createProducer();
        JMSConsumer consumer = context.createConsumer(MyQueueResponse);
        
        List<Pretplata> poruka = null;
        
        try {
            TextMessage msg = context.createTextMessage();
            msg.setIntProperty("korisnik", korisnik);
            msg.setIntProperty("Zahtev", 23);
            producer.send(myQueue, msg);
            
            Message response = consumer.receive();
            if (response instanceof ObjectMessage) {
                ObjectMessage msg1 = (ObjectMessage)response;
                poruka =  (ArrayList<Pretplata>)msg1.getObject();
            }
        }
        catch (JMSException ex) {
            context.close();
            consumer.close();
            return Response.status(Response.Status.BAD_REQUEST).entity("Nije poslata poruka.").build();
        }
        
        context.close();
        consumer.close();
        
        return Response.status(Response.Status.OK).entity(new GenericEntity<List<Pretplata>>(poruka){}).build();
       
   }
   
   @GET
   @Path("gledanje/{video}")
   @Produces(MediaType.APPLICATION_JSON)
   public Response dohvatiGledanjaKorisnika(@PathParam("video") Integer video) {
       
        JMSContext context = connectionFactory.createContext();
        JMSProducer producer = context.createProducer();
        JMSConsumer consumer = context.createConsumer(MyQueueResponse);
        
        List<Gledanje> poruka = null;
        
        try {
            TextMessage msg = context.createTextMessage();
            msg.setIntProperty("video", video);
            msg.setIntProperty("Zahtev", 24);
            producer.send(myQueue, msg);
            
            Message response = consumer.receive();
            if (response instanceof ObjectMessage) {
                ObjectMessage msg1 = (ObjectMessage)response;
                poruka =  (ArrayList<Gledanje>)msg1.getObject();
            }
        }
        catch (JMSException ex) {
            context.close();
            consumer.close();
            return Response.status(Response.Status.BAD_REQUEST).entity("Nije poslata poruka.").build();
        }
        
        context.close();
        consumer.close();
        
        return Response.status(Response.Status.OK).entity(new GenericEntity<List<Gledanje>>(poruka){}).build();
       
   }
   
   @GET
   @Path("ocena/{video}")
   @Produces(MediaType.APPLICATION_JSON)
   public Response dohvatiOceneVidea(@PathParam("video") Integer video) {
       
        JMSContext context = connectionFactory.createContext();
        JMSProducer producer = context.createProducer();
        JMSConsumer consumer = context.createConsumer(MyQueueResponse);
        
        List<Ocena> poruka = null;
        
        try {
            TextMessage msg = context.createTextMessage();
            msg.setIntProperty("video", video);
            msg.setIntProperty("Zahtev", 25);
            producer.send(myQueue, msg);
            
            Message response = consumer.receive();
            if (response instanceof ObjectMessage) {
                ObjectMessage msg1 = (ObjectMessage)response;
                poruka =  (ArrayList<Ocena>)msg1.getObject();
            }
        }
        catch (JMSException ex) {
            context.close();
            consumer.close();
            return Response.status(Response.Status.BAD_REQUEST).entity("Nije poslata poruka.").build();
        }
        
        context.close();
        consumer.close();
        
        return Response.status(Response.Status.OK).entity(new GenericEntity<List<Ocena>>(poruka){}).build();
       
   }
}
