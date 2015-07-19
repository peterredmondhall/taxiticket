package com.taxiticket.server.utils;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Map;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.util.ByteArrayDataSource;

import com.google.common.base.Charsets;
import com.google.common.io.Files;
import com.google.gwt.thirdparty.guava.common.collect.Maps;
import com.taxiticket.server.entity.Profile;
import com.taxiticket.shared.BookingInfo;

public class Mailer
{
    private static TaxiOrderUtil taxiOrderUtil = new TaxiOrderUtil();

    private static final Logger log = Logger.getLogger(Mailer.class.getName());
    public static Map<String, File> templateMap = Maps.newHashMap();

    private static final String DISPATCHER = "dispatch@taxisurfr.com";
    // private static final String TAXI_FAX = "004932224063613@simple-fax.de";

    public static final String CONFIRMATION = "template/confirmation.html";

    private static String LINK = "###_LINK_###";

    public static void sendTaxiOrder(BookingInfo bookingInfo, Profile profile)
    {
        String taxifax = profile.getTaxiFax();
        log.info("Mailer.send: taxi provider" + taxifax);
        byte[] generateTaxiOrder = taxiOrderUtil.generateTaxiOrder(bookingInfo);
        sendTaxiBestellung(taxifax, "bestellung" + bookingInfo.getOrderNo(), generateTaxiOrder);
    }

    public static void send(BookingInfo bookingInfo, Profile profile, String confirmationUrl)
    {
        log.info("Mailer.send:" + bookingInfo.getEmail());
        try
        {
            String html = Files.toString(new File(confirmationUrl), Charsets.UTF_8);
            html = html.replace(LINK, bookingInfo.getOrderLink());
            send(DISPATCHER, html);
            send(bookingInfo.getEmail(), html);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    private static void send(String toEmail, String htmlBody)
    {
        if (toEmail != null)
        {
            log.info("send:" + toEmail);
            // ...
            Properties props = new Properties();
            Session session = Session.getDefaultInstance(props, null);

            try
            {
                Message msg = new MimeMessage(session);
                msg.setFrom(new InternetAddress(DISPATCHER, "taxiticket"));
                msg.addRecipient(Message.RecipientType.TO,
                        new InternetAddress(toEmail, toEmail));
                msg.setSubject("Ihre Taxi Bestellung");

                Multipart mp = new MimeMultipart();

                // html
                {
                    MimeBodyPart htmlPart = new MimeBodyPart();
                    htmlPart.setContent(htmlBody, "text/html");
                    mp.addBodyPart(htmlPart);
                }

                // pdf
                msg.setContent(mp);

                Transport.send(msg);
                log.info("sent message to :" + toEmail);

            }
            catch (Exception e)
            {
                log.log(Level.SEVERE, "send exception :" + e.getMessage());

            }
        }
    }

    public static boolean sendTaxiBestellung(String toEmail, String subject, byte[] attachment)
    {
        Properties props = new Properties();
        Session session = Session.getDefaultInstance(props, null);
        try
        {
            Message msg = new MimeMessage(session);
            if (attachment != null)
            {

                msg.setFrom(new InternetAddress(DISPATCHER, "silvermobilityservices.com"));
                msg.addRecipient(Message.RecipientType.TO,
                        new InternetAddress(toEmail, "Silver Mobility"));

                msg.setSubject(subject);
                msg.setText("..");
                DataSource dataSource = new ByteArrayDataSource(attachment, "application/pdf");
                Multipart mp = new MimeMultipart();
                MimeBodyPart mbp = new MimeBodyPart();
                mbp.setFileName("taxibestellung.pdf");
                mbp.setDataHandler(new DataHandler(dataSource));
                mp.addBodyPart(mbp);
                msg.setContent(mp);

                Transport.send(msg);
                log.info("Message sent to " + toEmail);
                return true;
            }
        }
        catch (AddressException e)
        {
            try
            {
                Message msg = new MimeMessage(session);
                msg.setFrom(new InternetAddress(DISPATCHER, "silvermobilityservices.com Admin"));
                msg.addRecipient(Message.RecipientType.TO,
                        new InternetAddress("peterredmondhall@gmail.com", "Silver Mobility"));
                msg.setSubject("Error: Silver Mobility");
                msg.setText(e.getMessage());
                Transport.send(msg);
                log.info("sent message to Admin");
            }
            catch (Exception ex)
            {
                //
            }
            log.log(Level.SEVERE, "address exception :" + e.getMessage());
            throw new RuntimeException(e);
        }
        catch (MessagingException e)
        {
            throw new RuntimeException(e);
        }
        catch (UnsupportedEncodingException e)
        {
            throw new RuntimeException(e);
        }
        catch (Exception e)
        {

        }
        return false;
    }

    public static void sendError(Exception exception)
    {
        // send(DISPATCHER, exception.getMessage());
    }

}
