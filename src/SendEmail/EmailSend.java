/**
 * Created by Gebruiker on 26/02/2017.
 */
package SendEmail;

import java.util.Date;
import java.util.Properties;
import javax.mail.*;
//import javax.mail.Session;
import javax.mail.internet.*;

public class EmailSend {
    public static void main(String args[])  {
        try {
            String host = "smtp.gmail.com";

            String user = "dbtlive@gmail.com";
            String pass = "dbtlive24";//your own password
            String to = "zilbermane36@gmail.com";
            String from = "dbtlive@gmail.com";
            String subject = "text";
            String messageText = "hello world";
           
            boolean sessionDebug = false;

            Properties props = System.getProperties();
            props.put("mail.smtp.starttis.enable", "true");
            props.put("mail.smtp.host", host);
            props.put("mail.smtp.port", "587");
            props.put("mail.smtp.auth", "true");
            props.put("mail.smtp.starttls.required", "true");

            java.security.Security.addProvider(new com.sun.net.ssl.internal.ssl.Provider());
            Session mailSesion = Session.getDefaultInstance(props, null);
            mailSesion.setDebug(sessionDebug);
            Message msg = new MimeMessage(mailSesion);
            msg.setFrom(new InternetAddress(from));
            InternetAddress[] address = {
                    new InternetAddress(to)
            };
            msg.setRecipients(Message.RecipientType.TO, address);
            msg.setSubject(subject);
            msg.setSentDate(new Date());
            msg.setText(messageText);

            Multipart multipart = new MimeMultipart();
            MimeBodyPart image = new MimeBodyPart();
            image.attachFile("C:\\Users\\Gebruiker\\Desktop\\free.jpg");
            //image.setText(messageText);
            multipart.addBodyPart(image);

            msg.setContent(multipart);



            Transport transport = mailSesion.getTransport("smtp");
            transport.connect(host, user, pass);
            transport.sendMessage(msg, msg.getAllRecipients());
            transport.close();
            System.out.println("message send ");
        }catch (Exception e){
            System.out.println(e);
        }
    }

}
