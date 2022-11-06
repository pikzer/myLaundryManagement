package th.ac.ku.mylaundry.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Properties;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

public class SendEmail {

    public static void sendEmail(String header, String body, String email) throws IOException, MessagingException {

        Properties properties = System.getProperties();
        properties.put("mail.smtp.auth", true);
        properties.put("mail.smtp.host", "smtp.gmail.com");
        properties.put("mail.smtp.port", 587);
        properties.put("mail.smtp.starttls.enable", true);
        properties.put("mail.transport.protocol", "smtp");

//        properties.put("mail.smtp.auth", true);
//        properties.put("mail.smtp.starttls.enable", "true");
//        properties.put("mail.smtp.host", "smtp.mailtrap.io");
//        properties.put("mail.smtp.port", "25");
//        properties.put("mail.smtp.ssl.trust", "smtp.mailtrap.io");

        Session sessions = Session.getInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                // TODO
                return new PasswordAuthentication("mylaundryshopmail@gmail.com", "penxayotfodonekt");
            }
        });

        try {
            Message message = new MimeMessage(sessions);
            message.setSubject(header);

            Address addressTo = new InternetAddress(email);

            message.setRecipient(Message.RecipientType.TO, addressTo);

            MimeMultipart multipart = new MimeMultipart();

//            MimeBodyPart attachment = new MimeBodyPart();

//        attachment.attachFile(file) ;
            MimeBodyPart messageBodyPart = new MimeBodyPart();

            messageBodyPart.setContent("<h1>" + header+ "</h1>", "text/html");
            multipart.addBodyPart(messageBodyPart);
            MimeBodyPart messageBodyPart1 = new MimeBodyPart();
            messageBodyPart1.setText(body);

//            multipart.addBodyPart(attachment);
            multipart.addBodyPart(messageBodyPart1);
            message.setContent(multipart);
            // send email
            Transport.send(message);
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }


//        // Create a mail session so you can create and send an email message.
//        Session sessions = Session.getInstance(properties, new Authenticator() {
//            @Override
//            protected PasswordAuthentication getPasswordAuthentication() {
//                return new PasswordAuthentication("checking.boq@gmail.com", "Code1234");
//            }
//        });
//        Message message = new MimeMessage(sessions);
//        message.setSubject("นำส่งใบประเมินราคา(BOQ)ของโปรเจค " + selectedBoq.getBO_ProjName() + " ว่าจ้างโดย " + serCusDataList.searchCusByID(selectedBoq.getBO_GroupID()).getCS_Name());
//
//        Address addressTo = new InternetAddress(serCusDataList.searchCusByID(selectedBoq.getBO_GroupID()).getCS_Email());
//
//        message.setRecipient(Message.RecipientType.TO, addressTo);
//
//        MimeMultipart multipart = new MimeMultipart();
//
//        MimeBodyPart attachment = new MimeBodyPart();

//        File file = new File("Excel/"+selectedBoq.getBO_ProjName()+".xlsx") ;
//        accButton.setDisable(true);
//        declineButton.setDisable(true);
//        if(file.exists()){
//            attachment.attachFile(file) ;
//            MimeBodyPart messageBodyPart = new MimeBodyPart();
//            messageBodyPart.setContent("<h1>Thank you for using the service.</h1>", "text/html");
//            multipart.addBodyPart(messageBodyPart);
//            MimeBodyPart messageBodyPart1 = new MimeBodyPart();
//            messageBodyPart1.setText("ลิงค์ข้อสอบถาม: "+"https://forms.gle/eP48xni8tZMyMzf57");
//
//            multipart.addBodyPart(attachment);
//            multipart.addBodyPart(messageBodyPart1);
//            message.setContent(multipart);
//
//            // send email
//            Transport.send(message);
//        }
//    }
}
