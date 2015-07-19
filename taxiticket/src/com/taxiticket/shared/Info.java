package com.taxiticket.shared;

import java.io.Serializable;

public class Info implements Serializable
{
    public static String SOFORTSUCCESS = "sofucc";

    private Long id;

    public Long getId()
    {
        return id;
    }

    public void setId(Long id)
    {
        this.id = id;
    }

}
