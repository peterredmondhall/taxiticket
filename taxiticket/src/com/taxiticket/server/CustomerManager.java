package com.taxiticket.server;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;

import com.taxiticket.server.entity.Customer;
import com.taxiticket.server.entity.TaxiticketEntity;
import com.taxiticket.shared.BookingInfo;
import com.taxiticket.shared.Info;

public class CustomerManager extends Manager<Info, TaxiticketEntity<?>>
{
    static final String TEST_EMAIL = "test@test.com";

    public BookingInfo getCustomer(BookingInfo bookingInfo)
    {

        bookingInfo.setCustomer(false);
        EntityManager em = getEntityManager();
        String query = String.format("select t from Customer t where email='%s'", TEST_EMAIL);
        try
        {
            em.createQuery(query).getSingleResult();
        }
        catch (NoResultException ex)
        {
            addCustomer(TEST_EMAIL, new int[] { 81, 19, 100 });
        }

        query = String.format("select t from Customer t where email='%s'", bookingInfo.getEmail());
        try
        {
            Customer customer = (Customer) em.createQuery(query).getSingleResult();
            bookingInfo.setCustomer(true);
            bookingInfo.setPrice(0, customer.getPrice());
        }
        catch (Throwable e)
        {

        }

        return bookingInfo;
    }

    public void addCustomer(String email, int[] price)
    {
        EntityManager em = getEntityManager();
        try
        {
            Customer customer = Customer.getBooking(email, price);
            em.getTransaction().begin();
            em.persist(customer);
            em.getTransaction().commit();
        }
        catch (Throwable e)
        {

        }
        finally
        {
            em.close();
        }
    }
}
