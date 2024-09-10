/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package endpoints;

import entities.Kategorija;
import entities.Korisnik;
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
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.GenericEntity;
/**
 *
 * @author Matija
 */

@Path("podsistem2")
public class Podsistem2 {
    
    @Resource(lookup="jms/__defaultConnectionFactory")
    private ConnectionFactory connectionFactory;
    
    @Resource(lookup="RedZahteva1")
    private Queue myQueue;
    
    @Resource(lookup ="RedOdgovora1")
    private Queue MyQueueResponse;
    
    @POST
    @Path("kategorija")
    public Response kreirajKategoriju(String naziv) {
        
        JMSContext context = connectionFactory.createContext();
        JMSProducer producer = context.createProducer();
        JMSConsumer consumer = context.createConsumer(MyQueueResponse);
        
        String poruka = "Nije stigla poruka";
        
        try {
            TextMessage msg = context.createTextMessage(naziv);
            msg.setIntProperty("Zahtev", 5);
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
   @Path("video/{vlasnik}")
   @Consumes(MediaType.APPLICATION_JSON)
   public Response kreirajVideo(Video video, @PathParam("vlasnik") Integer vlasnik) {
        JMSContext context = connectionFactory.createContext();
        JMSProducer producer = context.createProducer();
        JMSConsumer consumer = context.createConsumer(MyQueueResponse);
        
        String poruka = "Nije stigla poruka";
        
        try {
            ObjectMessage msg = context.createObjectMessage(video);
            msg.setIntProperty("Zahtev", 6);
            msg.setIntProperty("Vlasnik",vlasnik);
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
   @Path("video/{video}/{naziv}")
   public Response promeniNaziv(@PathParam("video") Integer video,@PathParam("naziv") String naziv) {
       
        JMSContext context = connectionFactory.createContext();
        JMSProducer producer = context.createProducer();
        JMSConsumer consumer = context.createConsumer(MyQueueResponse);
        
        String poruka = "Nije stigla poruka";
        
        try {
            TextMessage msg = context.createTextMessage();
            msg.setIntProperty("Zahtev", 7);
            msg.setIntProperty("video", video);
            msg.setStringProperty("naziv", naziv);
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
   @Path("video/{video}")
   public Response promeniEmail(@PathParam("video") Integer video,String naziv) {
       
        JMSContext context = connectionFactory.createContext();
        JMSProducer producer = context.createProducer();
        JMSConsumer consumer = context.createConsumer(MyQueueResponse);
        
        String poruka = "Nije stigla poruka";
        
        try {
            TextMessage msg = context.createTextMessage();
            msg.setIntProperty("Zahtev", 8);
            msg.setIntProperty("video", video);
            msg.setStringProperty("naziv", naziv);
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
   @Path("video/{video}/{idKor}")
   public Response obrisiVideoVlasnika(@PathParam("video") Integer video,@PathParam("idKor") Integer idKor) {
       
        JMSContext context = connectionFactory.createContext();
        JMSProducer producer = context.createProducer();
        JMSConsumer consumer = context.createConsumer(MyQueueResponse);
        
        String poruka = "Nije stigla poruka";
        
        try {
            TextMessage msg = context.createTextMessage();
            msg.setIntProperty("Zahtev", 16);
            msg.setIntProperty("video", video);
            msg.setIntProperty("vlasnik", idKor);
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
   @Path("kategorija")
   @Produces(MediaType.APPLICATION_JSON)
   public Response dohvatiKategorije() {
       
        JMSContext context = connectionFactory.createContext();
        JMSProducer producer = context.createProducer();
        JMSConsumer consumer = context.createConsumer(MyQueueResponse);
        
        List<Kategorija> poruka = null;
        
        try {
            TextMessage msg = context.createTextMessage();
            msg.setIntProperty("Zahtev", 19);
            producer.send(myQueue, msg);
            
            Message response = consumer.receive();
            if (response instanceof ObjectMessage) {
                ObjectMessage msg1 = (ObjectMessage)response;
                poruka =  (ArrayList<Kategorija>)msg1.getObject();
            }
        }
        catch (JMSException ex) {
            context.close();
            consumer.close();
            return Response.status(Response.Status.BAD_REQUEST).entity("Nije poslata poruka.").build();
        }
        
        context.close();
        consumer.close();
        
        return Response.status(Response.Status.OK).entity(new GenericEntity<List<Kategorija>>(poruka){}).build();
       
   }
   
   @GET
   @Path("video")
   @Produces(MediaType.APPLICATION_JSON)
   public Response dohvatiVideoSnimke() {
       
        JMSContext context = connectionFactory.createContext();
        JMSProducer producer = context.createProducer();
        JMSConsumer consumer = context.createConsumer(MyQueueResponse);
        
        List<Video> poruka = null;
        
        try {
            TextMessage msg = context.createTextMessage();
            msg.setIntProperty("Zahtev", 20);
            producer.send(myQueue, msg);
            
            Message response = consumer.receive();
            if (response instanceof ObjectMessage) {
                ObjectMessage msg1 = (ObjectMessage)response;
                poruka =  (ArrayList<Video>)msg1.getObject();
            }
        }
        catch (JMSException ex) {
            context.close();
            consumer.close();
            return Response.status(Response.Status.BAD_REQUEST).entity("Nije poslata poruka.").build();
        }
        
        context.close();
        consumer.close();
        
        return Response.status(Response.Status.OK).entity(new GenericEntity<List<Video>>(poruka){}).build();
       
   }
   
   @GET
   @Path("kategorija/{video}")
   @Produces(MediaType.APPLICATION_JSON)
   public Response dohvatiKategorijeZaVideo(@PathParam("video") Integer video) {
       
        JMSContext context = connectionFactory.createContext();
        JMSProducer producer = context.createProducer();
        JMSConsumer consumer = context.createConsumer(MyQueueResponse);
        
        List<Kategorija> poruka = null;
        
        try {
            TextMessage msg = context.createTextMessage();
            msg.setIntProperty("Zahtev", 21);
            msg.setIntProperty("video", video);
            producer.send(myQueue, msg);
            
            Message response = consumer.receive();
            if (response instanceof ObjectMessage) {
                ObjectMessage msg1 = (ObjectMessage)response;
                poruka =  (ArrayList<Kategorija>)msg1.getObject();
            }
        }
        catch (JMSException ex) {
            context.close();
            consumer.close();
            return Response.status(Response.Status.BAD_REQUEST).entity("Nije poslata poruka.").build();
        }
        
        context.close();
        consumer.close();
        
        return Response.status(Response.Status.OK).entity(new GenericEntity<List<Kategorija>>(poruka){}).build();
       
   }
   
}
