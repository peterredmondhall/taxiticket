package com.taxiticket.server;

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;

import com.taxiticket.server.entity.Config;
import com.taxiticket.server.entity.Customer;
import com.taxiticket.server.entity.TaxiticketEntity;
import com.taxiticket.shared.Info;

public class ConfigManager extends Manager<Info, TaxiticketEntity<?>>
{
    private static final Logger logger = Logger.getLogger(BookingManager.class.getName());

    public Config getConfig()
    {
        Config config = null;
        EntityManager em = getEntityManager();
        try
        {
            config = (Config) em.createQuery("select t from Config t").getSingleResult();
        }
        catch (NoResultException ex)
        {
            try
            {
                config = new Config();
                em.getTransaction().begin();
                em.persist(config);
                em.getTransaction().commit();
                em.detach(config);
            }
            catch (Throwable e)
            {
                logger.log(Level.SEVERE, "getConfig", e);
            }
        }
        finally
        {
            em.close();
        }

        return config;
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
