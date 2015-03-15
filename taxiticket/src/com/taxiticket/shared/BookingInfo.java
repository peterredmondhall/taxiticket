package com.taxiticket.shared;

import java.util.Date;

public class BookingInfo extends Info
{
    private static final long serialVersionUID = 1L;

    private Date date;

    private String name;
    private String email;
    private Long id;


    @Override
    public Long getId()
    {
        return id;
    }

    @Override
    public void setId(Long id)
    {
        this.id = id;
    }

    private String requirements = "";

    public String getRequirements()
    {
        return requirements;
    }

    public void setRequirements(String requirements)
    {
        this.requirements = requirements;
    }

    public Date getDate()
    {
        return date;
    }

    public void setDate(Date date)
    {
        this.date = date;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getEmail()
    {
        return email;
    }

    public void setEmail(String email)
    {
        this.email = email;
    }


    public String getOrderNo()
    {
        int len = id.toString().length();
        return id.toString().substring(len - 5, len - 1);
    }


}
