package com.taxiticket.shared;

import java.util.Date;

public class BookingInfo extends Info
{
    private static final long serialVersionUID = 1L;

    public enum OrderStatus
    {
        ABSENT,
        WAITING,
        CONFIRMED
    }

    private Date date;

    private String dateText;

    private String name;
    private String email;
    private String pickup;
    private String pickupTime;
    private String dropoff;
    private String orderLink;
    private String token;
    private String paymentUrl;
    private String transID;
    private boolean autoOrder;
    private int numTaxis = 1;

    private boolean customer;

    private int[] price;
    private Long id;
    private OrderStatus orderStatus;

    private String paymentResponseCode;

    public String getPaymentResponseCode()
    {
        return paymentResponseCode;
    }

    public void setPaymentResponseCode(String paymentResponseCode)
    {
        this.paymentResponseCode = paymentResponseCode;
    }

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

    public void setPrice(double distance, int[] price)
    {
        this.price = price;
        this.distance = distance;
    }

    public String getPickup()
    {
        return pickup;
    }

    public void setPickup(String pickup)
    {
        this.pickup = pickup;
    }

    public String getDropoff()
    {
        return dropoff;
    }

    public void setDropoff(String dropoff)
    {
        this.dropoff = dropoff;
    }

    private double distance;

    public double getDistance()
    {
        return distance;
    }

    public int[] getPrice()
    {
        return price;
    }

    public String getOrderLink()
    {
        return orderLink;
    }

    public String getPaymentUrl()
    {
        return paymentUrl;
    }

    public void setPaymentUrl(String paymentUrl)
    {
        this.paymentUrl = paymentUrl;
    }

    public String getTransID()
    {
        return transID;
    }

    public void setTransID(String transID)
    {
        this.transID = transID;
    }

    public void setOrderLink(String orderLink)
    {
        this.orderLink = orderLink;
    }

    public boolean isCustomer()
    {
        return customer;
    }

    public void setCustomer(boolean customer)
    {
        this.customer = customer;
    }

    public void setToken(String token)
    {
        this.token = token;
    }

    public String getToken()
    {
        return token;
    }

    public String getDateText()
    {
        return dateText;
    }

    public void setDateText(String dateText)
    {
        this.dateText = dateText;
    }

    public void setPaymentTransId(String paymentUrl)
    {
        // TODO Auto-generated method stub

    }

    public int getNumTaxis()
    {
        return numTaxis;
    }

    public void setNumTaxis(int numTaxis)
    {
        this.numTaxis = numTaxis;
    }

    public boolean isAutoOrder()
    {
        return autoOrder;
    }

    public void setAutoOrder(boolean autoOrder)
    {
        this.autoOrder = autoOrder;
    }

    public String getPickupTime()
    {
        return pickupTime;
    }

    public void setPickupTime(String pickupTime)
    {
        this.pickupTime = pickupTime;
    }

    public OrderStatus getOrderStatus()
    {
        return orderStatus;
    }

    public void setOrderStatus(OrderStatus orderStatus)
    {
        this.orderStatus = orderStatus;
    }
}
