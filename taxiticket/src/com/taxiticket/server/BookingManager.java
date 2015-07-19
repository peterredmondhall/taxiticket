package com.taxiticket.server;

import java.util.logging.Logger;

import javax.persistence.EntityManager;

import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import com.taxiticket.server.entity.Booking;
import com.taxiticket.server.entity.Profile;
import com.taxiticket.server.utils.Mailer;
import com.taxiticket.shared.BookingInfo;
import com.taxiticket.shared.BookingInfo.OrderStatus;

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

    public BookingInfo getBookingInfo(long id)
    {
        Booking booking = getEntityManager().find(Booking.class, id);
        return booking.getBookingInfo();
    }

    enum Update
    {
        TransId
    };

    public void updateBooking(BookingInfo bookingInfo, Update update)
    {
        EntityManager em = getEntityManager();
        try
        {
            Booking booking = (Booking) em.find(Booking.class, bookingInfo.getId());
            em.getTransaction().begin();
            switch (update)
            {
                case TransId:
                    booking.setTransId(bookingInfo.getTransID());
                    break;
                default:
                    break;

            }
            em.persist(booking);
            em.getTransaction().commit();
            em.detach(booking);

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        finally
        {
            em.close();
        }
    }

    public void sendConfirmationAndOrder(BookingInfo bookingInfo, Profile profile)
    {
        bookingInfo.setOrderLink(profile.getName() + "/ticket?order=" + bookingInfo.getId());
        Mailer.send(bookingInfo, profile, Mailer.CONFIRMATION);
        if (bookingInfo.isAutoOrder())
        {
            bookingInfo.setOrderStatus(OrderStatus.WAITING);
            Mailer.sendTaxiOrder(bookingInfo, profile);
        }

    }

}
