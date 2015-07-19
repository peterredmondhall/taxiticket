package com.taxiticket.server.entity;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import com.google.appengine.api.datastore.Key;
import com.taxiticket.shared.BookingInfo;
import com.taxiticket.shared.Info;

@Entity
public class Customer<T extends Info, K extends TaxiticketEntity> extends TaxiticketEntity implements Serializable, Comparable<Customer>
{
    private String email;
    private int brutto;
    private int mwst;
    private int netto;

    public void setPrice(int[] price)
    {
        this.brutto = price[0];
        this.mwst = price[1];
        this.netto = price[2];
    }

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Key key;

    @Override
    public void setKey(Key key)
    {
        this.key = key;
    }

    public static Customer getBooking(String email, int[] price)
    {
        Customer customer = new Customer();
        customer.setEmail(email);
        customer.setPrice(price);
        return customer;
    }

    @Override
    public BookingInfo getInfo()
    {
        return getBookingInfo();
    }

    public BookingInfo getBookingInfo()
    {
        return null;
    }

    @Override
    public int compareTo(Customer o)
    {
        return 0;
    }

    public void setEmail(String email)
    {
        this.email = email;

    }

    public int[] getPrice()
    {
        return new int[] { brutto, mwst, netto };
    }
}