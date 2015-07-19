package com.taxiticket.server;

import static org.junit.Assert.assertEquals;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.google.appengine.tools.development.testing.LocalDatastoreServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalServiceTestHelper;
import com.taxiticket.shared.BookingInfo;

public class CustomerManagerTest
{

    private final LocalServiceTestHelper helper =
            new LocalServiceTestHelper(new LocalDatastoreServiceTestConfig());

    CustomerManager cm = new CustomerManager();

    @Before
    public void setUp()
    {
        helper.setUp();
        cm.addCustomer("email", new int[] { 1, 2, 3 });
    }

    @After
    public void tearDown()
    {
        helper.tearDown();
    }

    @Test
    public void should_return_customer_hall()
    {
        BookingInfo bookingInfo = new BookingInfo();
        bookingInfo.setEmail(CustomerManager.TEST_EMAIL);
        bookingInfo = cm.getCustomer(bookingInfo);
        assertEquals(bookingInfo.isCustomer(), true);
        assertEquals(81, bookingInfo.getPrice()[0]);
        assertEquals(19, bookingInfo.getPrice()[1]);
        assertEquals(100, bookingInfo.getPrice()[2]);
    }

    @Test
    public void should_return_customer()
    {
        BookingInfo bookingInfo = new BookingInfo();
        bookingInfo = cm.getCustomer(bookingInfo);
        assertEquals(bookingInfo.isCustomer(), false);
    }

    @Test
    public void should_not_return_customer()
    {

        BookingInfo bookingInfo = new BookingInfo();
        bookingInfo.setEmail("email");
        bookingInfo = cm.getCustomer(bookingInfo);
        assertEquals(bookingInfo.isCustomer(), true);
    }
}
