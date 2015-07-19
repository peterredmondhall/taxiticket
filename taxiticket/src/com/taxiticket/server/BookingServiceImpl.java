package com.taxiticket.server;

import java.util.logging.Logger;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.taxiticket.client.service.TaxiticketService;
import com.taxiticket.server.entity.Profile;
import com.taxiticket.shared.BookingInfo;

/**
 * The server-side implementation of the RPC service.
 */
@SuppressWarnings("serial")
public class BookingServiceImpl extends RemoteServiceServlet implements
        TaxiticketService
{
    private static final Logger logger = Logger.getLogger(BookingServiceImpl.class.getName());

    private final BookingManager bookingManager = new BookingManager();
    private final ProfileManager profileManager = new ProfileManager();
    private final CustomerManager customerManager = new CustomerManager();
    private final ConfigManager configManager = new ConfigManager();
    private final SofortPaymentManager sofortPaymentManager = new SofortPaymentManager();

    @Override
    public BookingInfo createBooking(BookingInfo bookingInfo) throws IllegalArgumentException
    {
        BookingInfo customer = customerManager.getCustomer(bookingInfo);
        bookingInfo = bookingManager.createBooking(bookingInfo);
        Profile profile = profileManager.getProfile();
        if (customer.isCustomer())
        {
            logger.info(bookingInfo.getEmail());
            bookingInfo.setPaymentResponseCode("");
            logger.info("transaction success: " + true);
            bookingInfo = bookingManager.createBooking(bookingInfo);
        }
        else
        {
            sofortPaymentManager.initSofortPayment(bookingInfo, configManager.getConfig(), profile);
            bookingManager.updateBooking(bookingInfo, BookingManager.Update.TransId);

        }

        return bookingInfo;
    }

    @Override
    public BookingInfo check(BookingInfo bookingInfo) throws IllegalArgumentException
    {

        logger.info(bookingInfo.getEmail());
        Profile profile = profileManager.getProfile();
//        bookingInfo = paymentManager.getToken(bookingInfo, profile);
        bookingInfo = customerManager.getCustomer(bookingInfo);
        return bookingInfo;
    }
}
