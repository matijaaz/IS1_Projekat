/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package endpoints;



import entities.Korisnik;
import entities.Mesto;
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

@Path("podsistem1")
public class Podsistem1 {
    
    @Resource(lookup="jms/__defaultConnectionFactory")
    private ConnectionFactory connectionFactory;
    
    @Resource(lookup="RedZahteva1")
    private Queue myQueue;
    
    @Resource(lookup ="RedOdgovora1")
    private Queue MyQueueResponse;
    
    @POST
    @Path("grad")
    public Response kreirajGrad(String naziv) {
        
        JMSContext context = connectionFactory.createContext();
        JMSProducer producer = context.createProducer();
        JMSConsumer consumer = context.createConsumer(MyQueueResponse);
        
        String poruka = "Nije stigla poruka";
        
        try {
            TextMessage msg = context.createTextMessage(naziv);
            msg.setIntProperty("Zahtev", 1);
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
   @Path("korisnik")
   @Consumes(MediaType.APPLICATION_JSON)
   public Response kreirajKorisnika(Korisnik k) {
       
        JMSContext context = connectionFactory.createContext();
        JMSProducer producer = context.createProducer();
        JMSConsumer consumer = context.createConsumer(MyQueueResponse);
        
        String poruka = "Nije stigla poruka";
        
        try {
            ObjectMessage msg = context.createObjectMessage(k);
            msg.setIntProperty("Zahtev", 2);
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
   @Path("korisnik/{idKor}/{email}")
   public Response promeniEmail(@PathParam("idKor") Integer idKor, @PathParam("email") String email) {
       
        JMSContext context = connectionFactory.createContext();
        JMSProducer producer = context.createProducer();
        JMSConsumer consumer = context.createConsumer(MyQueueResponse);
        
        String poruka = "Nije stigla poruka";
        
        try {
            TextMessage msg = context.createTextMessage();
            msg.setIntProperty("Zahtev", 3);
            msg.setIntProperty("idKor", idKor);
            msg.setStringProperty("email", email);
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
   @Path("korisnik/{sifM}")
   public Response promeniMestoKorisnika(int idKor,@PathParam("sifM") int sifM) {
       
        JMSContext context = connectionFactory.createContext();
        JMSProducer producer = context.createProducer();
        JMSConsumer consumer = context.createConsumer(MyQueueResponse);
        
        String poruka = "Nije stigla poruka";
        
        try {
            TextMessage msg = context.createTextMessage();
            msg.setIntProperty("Zahtev", 4);
            msg.setIntProperty("idKor", idKor);
            msg.setIntProperty("sifM", sifM);
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
   @Path("mesto")
   @Produces(MediaType.APPLICATION_JSON)
   public Response dohvatiMesta() {
        JMSContext context = connectionFactory.createContext();
        JMSProducer producer = context.createProducer();
        JMSConsumer consumer = context.createConsumer(MyQueueResponse);
        
        List<Mesto> poruka = null;
        
        try {
            TextMessage msg = context.createTextMessage();
            msg.setIntProperty("Zahtev", 17);
            producer.send(myQueue, msg);
            
            Message response = consumer.receive();
            if (response instanceof ObjectMessage) {
                ObjectMessage msg1 = (ObjectMessage)response;
                poruka =  (ArrayList<Mesto>)msg1.getObject();
            }
        }
        catch (JMSException ex) {
            context.close();
            consumer.close();
            return Response.status(Response.Status.BAD_REQUEST).entity("Nije poslata poruka.").build();
        }
        
        context.close();
        consumer.close();
        return Response.status(Response.Status.OK).entity(new GenericEntity<List<Mesto>>(poruka){}).build();
   }
   
   @GET
   @Path("korisnik")
   @Produces(MediaType.APPLICATION_JSON)
   public Response dohvatiKorisnike() {
       
       JMSContext context = connectionFactory.createContext();
        JMSProducer producer = context.createProducer();
        JMSConsumer consumer = context.createConsumer(MyQueueResponse);
        
        List<Korisnik> poruka = null;
        
        try {
            TextMessage msg = context.createTextMessage();
            msg.setIntProperty("Zahtev", 18);
            producer.send(myQueue, msg);
            
            Message response = consumer.receive();
            if (response instanceof ObjectMessage) {
                ObjectMessage msg1 = (ObjectMessage)response;
                poruka =  (ArrayList<Korisnik>)msg1.getObject();
            }
        }
        catch (JMSException ex) {
            context.close();
            consumer.close();
            return Response.status(Response.Status.BAD_REQUEST).entity("Nije poslata poruka.").build();
        }
        
        context.close();
        consumer.close();
        
        return Response.status(Response.Status.OK).entity(new GenericEntity<List<Korisnik>>(poruka){}).build();
       
   }
}
