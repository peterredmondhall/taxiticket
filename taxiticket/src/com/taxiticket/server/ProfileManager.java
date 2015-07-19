package com.taxiticket.server;

import java.util.logging.Logger;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;

import com.taxiticket.server.entity.Profile;

/**
 * The server-side implementation of the RPC service.
 */
@SuppressWarnings("serial")
public class ProfileManager extends Manager
{
    private static final Logger logger = Logger.getLogger(ProfileManager.class.getName());

    public Profile getProfile() throws IllegalArgumentException
    {
        Profile profile = null;
        EntityManager em = getEntityManager();
        String query = "select t from Profile t where active=true";
        try
        {
            profile = (Profile) em.createQuery(query).getSingleResult();

        }
        catch (NoResultException e)
        {
            em.getTransaction().begin();
            profile = new Profile();
            em.persist(profile);
            em.getTransaction().commit();
            try
            {
                /*
                 * URL url = Resources.getResource("paymill.txt");
                 * String text = Resources.toString(url, Charsets.UTF_8);
                 * Iterator<String> iter = Splitter.on("/").split(text).iterator();
                 * String paymitllSecret = iter.next();
                 * String paymillPublishable = iter.next();
                 * 
                 * profile = (Profile)em.createQuery("select t from Profile t").getResultList().get(0);
                 * profile.setPaymillPublishable(paymillPublishable);
                 * profile.setPaymitllSecret(paymitllSecret);
                 * profile.setActive(true);
                 * em.getTransaction().begin();
                 * em.persist(profile);
                 * em.getTransaction().commit();
                 */

            }
            catch (Exception ex)
            {
                System.out.println("couldnt get paymill file");
            }

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        finally
        {
            em.close();
        }
        return profile;
    }

}
