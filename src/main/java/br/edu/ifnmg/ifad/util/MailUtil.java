package br.edu.ifnmg.ifad.util;

import java.io.UnsupportedEncodingException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

/**
 *
 * @author danilo
 */
public class MailUtil {
    
    public static void sendMail(String[] emailsDest, String nomeDest, String emailRemet, String senhaEmail, String nomeRemet, String assunto, String corpo) {
        try {
            Properties props = System.getProperties();

            props.put("mail.smtp.host", "smtp.gmail.com"); 
//            props.put("mail.smtp.port", "25"); 
            props.put("mail.debug", "true"); 
            props.put("mail.smtp.auth", "true"); 
            props.put("mail.smtp.starttls.enable","true"); 
            props.put("mail.smtp.EnableSSL.enable","true");

            props.setProperty("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");   
            props.setProperty("mail.smtp.socketFactory.fallback", "false");   
//            props.setProperty("mail.smtp.port", "587");   
            props.setProperty("mail.smtp.port", "465");   
            props.setProperty("mail.smtp.socketFactory.port", "465"); 
//            props.setProperty("mail.smtp.socketFactory.port", "587"); 
            
            final String remetente = emailRemet;
            final String senha = senhaEmail;
            
            Authenticator auth = new Authenticator() {

                @Override
                public PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(remetente, senha);
                }
            };
            
            Session session = Session.getDefaultInstance(props, auth);
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(emailRemet, nomeRemet));
            Address[] addresses = new Address[emailsDest.length];
            for(int i = 0; i < emailsDest.length; i++){
                addresses[i] = new InternetAddress(emailsDest[i].toLowerCase().trim());
            }
            message.addRecipients(Message.RecipientType.TO, addresses);
            message.setSubject(assunto);
//            message.setContent(corpo, "text/plain");
            message.setContent(corpo, "text/html");
            
//            // connect to the transport
//            Transport transport = session.getTransport("smtps");
//            transport.connect("smtp.gmail.com", 465, "dsalinux@gmail.com", "isolinux571243"); // host, user, password
//            // send the msg and close the connection
//            transport.sendMessage(message, message.getAllRecipients());
//            transport.close();

            
            Transport.send(message);
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(MailUtil.class.getName()).log(Level.SEVERE, null, ex);
        } catch (MessagingException ex) {
            Logger.getLogger(MailUtil.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public static void main(String[] args) {
    
        String senha = "nayara571243";
        
        
        
        
        
        String[] destinatarios = new String[]{"nairaancelmodosreis@gmail.com","roneferrera25@gmail.com"};
        MailUtil.sendMail(destinatarios, "Alunos", "dsalinux@gmail.com", senha, "Danilo", "Teste legal", "<h1>Teste que legal - Enviado via java</h1>");
    }
    
}