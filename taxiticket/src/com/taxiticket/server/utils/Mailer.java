package com.taxiticket.server.utils;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Map;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

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

import com.google.common.base.Charsets;
import com.google.common.io.Files;
import com.google.common.io.Resources;
import com.google.gwt.thirdparty.guava.common.collect.Maps;
import com.taxiticket.server.entity.Profile;
import com.taxiticket.shared.BookingInfo;

public class Mailer
{
    private static final Logger log = Logger.getLogger(Mailer.class.getName());
    public static Map<String, File> templateMap = Maps.newHashMap();

    private static final String DISPATCHER = "peterredmondhall@gmail.com";

    public static final String CONFIRMATION = "template/confirmation.html";
    
    private static String LINK = "###_LINK_###";


    public static void send(BookingInfo bookingInfo, Profile profile, String confirmationUrl)
    {
        log.info("Mailer.send:" + bookingInfo.getEmail());
		try {
			String html = Files.toString(new File(confirmationUrl), Charsets.UTF_8);
			html = html.replace(LINK, profile.getName()+"/ticket?order="+bookingInfo.getId());
	        send(DISPATCHER, html);
	        send(bookingInfo.getEmail(), html);
		} catch (IOException e) {
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
                msg.setSubject("Arugam Taxi");
                // msg.setText(msgBody);

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

    public static void sendError(Exception exception)
    {
        //send(DISPATCHER, exception.getMessage());
    }


}
