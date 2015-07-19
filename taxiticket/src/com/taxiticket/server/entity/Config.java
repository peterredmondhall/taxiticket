package com.taxiticket.server.entity;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import com.google.appengine.api.datastore.Key;

@Entity
public class Config implements Serializable
{
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Key key;
    private String url = "http://127.0.0.1:8888";
    private Boolean active = false;
    private int customerId = 113494;
    private int projectId = 233785;
    private String apiKey = "c61bdad24a9ea73ec6a77a7b9684a694";

    public int getCustomerId()
    {
        return customerId;
    }

    public int getProjectId()
    {
        return projectId;
    }

    public String getApiKey()
    {
        return apiKey;
    }

    public String getUrl()
    {
        return url;
    }

    public void setUrl(String url)
    {
        this.url = url;
    }

    public Boolean getActive()
    {
        return active;
    }

    public void setActive(Boolean active)
    {
        this.active = active;
    }

}
