package com.taxiticket.server;

import java.util.List;

import javax.persistence.EntityManager;

import com.google.appengine.api.datastore.KeyFactory;
import com.google.common.collect.Lists;
import com.taxiticket.server.entity.Booking;
import com.taxiticket.server.entity.EMF;
import com.taxiticket.server.entity.TaxiticketEntity;
import com.taxiticket.shared.BookingInfo;
import com.taxiticket.shared.Info;
import com.thoughtworks.xstream.XStream;

public class Manager<T extends Info, K extends TaxiticketEntity<?>>
{

    protected static EntityManager getEntityManager()
    {
        return EMF.get().createEntityManager();
    }

    public void deleteAll(Class<?> entityType)
    {
        EntityManager em = getEntityManager();
        @SuppressWarnings("unchecked")
        List<K> resultList = em.createQuery("select t from " + entityType.getName() + " t").getResultList();
        for (K entity : resultList)
        {
            em.getTransaction().begin();
            em.remove(entity);
            em.getTransaction().commit();
        }
        em.close();
    }

    @SuppressWarnings("unchecked")
    public List<T> getAllInfo(Class<?> entityType)
    {
        EntityManager em = getEntityManager();
        List<K> resultList = em.createQuery("select t from " + entityType.getName() + " t").getResultList();
        List<T> list = Lists.newArrayList();
        for (K entity : resultList)
        {
            em.detach(entity);
            list.add((T) entity.getInfo());
        }
        em.close();
        return list;
    }

    @SuppressWarnings("unchecked")
    public List<K> getAll(Class<?> entityType)
    {
        EntityManager em = getEntityManager();
        List<K> resultList = em.createQuery("select t from " + entityType.getName() + " t").getResultList();
        em.close();
        return resultList;
    }

    public void importDataset(String dataset, Class<?> type)
    {
        deleteAll(type);

        String[] datasets = dataset.split("<list>");
        for (String ds : datasets)
        {
            if (ds.contains(type.getSimpleName() + "Info"))
            {
                dataset = "<list>" + ds;
                break;
            }
        }

        @SuppressWarnings("unchecked")
        List<T> list = (List<T>) new XStream().fromXML(dataset);
        for (T info : list)
        {
            if (type.equals(Booking.class))
            {
            	Booking entity = Booking.getBooking((BookingInfo) info);
                save(entity, type, info);
            }
        }
    }

    public void save(TaxiticketEntity<?> entity, Class type, Info info)
    {
        EntityManager em = getEntityManager();
        entity.setKey(KeyFactory.createKey(type.getSimpleName(), info.getId()));
        em.getTransaction().begin();
        em.persist(entity);
        em.getTransaction().commit();
        em.close();

    }

    public String dump(Class<?> entityType)
    {
        return new XStream().toXML(getAllInfo(entityType));
    }

}
