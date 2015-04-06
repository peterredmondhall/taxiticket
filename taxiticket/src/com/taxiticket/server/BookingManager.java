package com.taxiticket.server;

import java.util.logging.Logger;

import javax.persistence.EntityManager;

import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import com.taxiticket.server.entity.Booking;
import com.taxiticket.shared.BookingInfo;

/**
 * The server-side implementation of the RPC service.
 */
public class BookingManager extends Manager
{
    private static final Logger logger = Logger.getLogger(BookingManager.class.getName());
    static final DateTimeFormatter fmt = DateTimeFormat.forPattern("dd.MM.yyyy");

    public BookingInfo createBooking(BookingInfo bookingInfo) throws IllegalArgumentException
    {
        BookingInfo result = null;
        logger.info(bookingInfo.toString());
        EntityManager em = getEntityManager();
        try
        {
            Booking booking = Booking.getBooking(bookingInfo);
            em.getTransaction().begin();
            em.persist(booking);
            em.getTransaction().commit();
            em.detach(booking);
            result = booking.getBookingInfo();

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        finally
        {
            em.close();
        }
        return result;
    }

	public BookingInfo getBookingInfo(long id) {
		Booking booking = getEntityManager().find(Booking.class, id);
		return booking.getBookingInfo();
	}

}
