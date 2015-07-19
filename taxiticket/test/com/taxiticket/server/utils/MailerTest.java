package com.taxiticket.server.utils;

import java.util.Date;

import org.junit.Test;

import com.taxiticket.server.entity.Profile;
import com.taxiticket.shared.BookingInfo;

public class MailerTest
{

    @Test
    public void should_create_payment() throws Exception
    {
        BookingInfo bookingInfo = new BookingInfo();
        bookingInfo.setId(5644101080842240L);
        bookingInfo.setDate(new Date());
        bookingInfo.setPrice(100, new int[] { 100, 19, 119 });
        bookingInfo.setName("Beven Boo");
        bookingInfo.setPickup("pickup");
        bookingInfo.setDropoff("dropoff");

        Profile profile = new Profile();
        profile.setName("silmo");

        Mailer.send(bookingInfo, profile, "war/template/confirmation.html");

    }
}
