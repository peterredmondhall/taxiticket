package com.taxiticket.server.utils;

import java.io.File;
import java.io.FileInputStream;
import java.util.Date;

import org.junit.Test;

import com.google.common.io.Files;
import com.taxiticket.server.utils.PdfUtil;
import com.taxiticket.shared.BookingInfo;

public class PdfUtilTest
{

	

    @Test
    public void should_create_payment() throws Exception
    {
    	BookingInfo bookingInfo = new BookingInfo();
    	bookingInfo.setId(5644101080842240L);
    	bookingInfo.setDate(new Date());
    	bookingInfo.setPrice(100, new double[]{100,19,119});
    	bookingInfo.setName("Beven Boo");
    	bookingInfo.setPickup("pickup");
    	bookingInfo.setDropoff("dropoff");
    	File f = new File("war/template/Fahrtenscheck_with_fields.pdf");
    	String path =f.getAbsolutePath();
    	boolean ex = f.exists();
        byte[] bytes = new PdfUtil().generate(bookingInfo, new FileInputStream("war/template/Fahrtenscheck.pdf"), new FileInputStream("war/template/Rechnung.pdf"));
        
        Files.write(bytes, new File("test.pdf"));
        System.out.println(bytes.length);
    }
}
