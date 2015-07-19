package com.taxiticket.server.entity;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import com.google.appengine.api.datastore.Key;

@Entity
public class Profile implements Serializable
{
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Key key;
    private boolean active = false;
    private String name;
    private String paymitllSecret;
    private String paymillPublishable;
    private String taxiFax;

    public void setPaymitllSecret(String paymitllSecret)
    {
        this.paymitllSecret = paymitllSecret;
    }

    public void setPaymillPublishable(String paymillPublishable)
    {
        this.paymillPublishable = paymillPublishable;
    }

    public boolean isActive()
    {
        return active;
    }

    public void setActive(boolean active)
    {
        this.active = active;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getPaymitllSecret()
    {
        return paymitllSecret;
    }

    public String getPaymillPublishable()
    {
        return paymillPublishable;
    }

    public String getTaxiFax()
    {
        return taxiFax;
    }

    public String getSofortApiKey()
    {
        // TODO Auto-generated method stub
        return "TODO";
    }

}
