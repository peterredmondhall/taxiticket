package com.taxiticket.server;

import java.util.logging.Logger;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.paymill.models.Transaction;
import com.taxiticket.client.service.TaxiticketService;
import com.taxiticket.server.entity.Profile;
import com.taxiticket.server.utils.Mailer;
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
    private final PaymentManager paymentManager = new PaymentManager();
    private final ProfileManager profileManager = new ProfileManager();

    @Override
    public BookingInfo createBooking(BookingInfo bookingInfo) throws IllegalArgumentException
    {
    	logger.info(bookingInfo.getEmail());
    	Profile profile = profileManager.getProfile();
    	Transaction transaction = paymentManager.pay(bookingInfo,profile);
    	if (transaction != null)
    	{
			bookingInfo.setPaymentSuccessful(transaction.isSuccessful());
			bookingInfo.setPaymentResponseCode(transaction.getResponseCodeDetail());
			logger.info("transaction success: "+transaction.isSuccessful());
			logger.info("transaction response detail: "+transaction.getResponseCodeDetail());
	    	if (transaction.isSuccessful())
	    	{
	    		bookingInfo = bookingManager.createBooking(bookingInfo);
	    		bookingInfo.setPaymentSuccessful(true);
	    		Mailer.send(bookingInfo,profile,Mailer.CONFIRMATION);
	    	}
    	}
    	return bookingInfo;
    }


}
