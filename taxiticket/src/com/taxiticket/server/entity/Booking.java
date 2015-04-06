package com.taxiticket.server.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import org.apache.http.conn.routing.RouteInfo;

import com.google.appengine.api.datastore.Key;
import com.taxiticket.shared.BookingInfo;
import com.taxiticket.shared.Info;

@Entity
public class Booking<T extends Info, K extends TaxiticketEntity> extends TaxiticketEntity implements Serializable, Comparable<Booking>
{
    private String name;
    private String email;
    private String iban;
    private String pickup;
    public void setPickup(String pickup) {
		this.pickup = pickup;
	}

	public void setDropoff(String dropoff) {
		this.dropoff = dropoff;
	}

	private String dropoff;
    private double mwst;
    private double brutto;
    private double netto;
    private double distance;

    private Date instanziated;

    public Booking()
    {
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

    private Date date;

    public Date getDate()
    {
        return date;
    }

    public void setDate(Date date)
    {
        this.date = date;
    }


 
    public Date getInstanziated()
    {
        return instanziated;
    }

    public void setInstanziated(Date instanziated)
    {
        this.instanziated = instanziated;
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


    public static Booking getBooking(BookingInfo bookingInfo)
    {
        Booking booking = new Booking();
        booking.setDate(bookingInfo.getDate());
        booking.setEmail(bookingInfo.getEmail());
        booking.setName(bookingInfo.getName());
        booking.setPickup(bookingInfo.getPickup());
        booking.setDropoff(bookingInfo.getDropoff());
        booking.setIBAN(bookingInfo.getIban());
        booking.brutto = bookingInfo.getPrice()[0];
        booking.mwst = bookingInfo.getPrice()[1];
        booking.netto = bookingInfo.getPrice()[2];

        return booking;
    }

    @Override
    public BookingInfo getInfo()
    {
        return getBookingInfo();
    }

    public BookingInfo getBookingInfo()
    {
        BookingInfo bookingInfo = new BookingInfo();
        bookingInfo.setId(key.getId());
        bookingInfo.setDate(getDate());
        bookingInfo.setEmail(getEmail());
        bookingInfo.setName(getName());
        bookingInfo.setPickup(pickup);
        bookingInfo.setDropoff(dropoff);
        double[] price = {brutto,mwst,netto};
        bookingInfo.setPrice(distance, price);
        return bookingInfo;
    }


    public void setIBAN(String iban)
    {
        this.iban = iban;
    }



    @Override
    public int compareTo(Booking other)
    {
        // compareTo should return < 0 if this is supposed to be
        // less than other, > 0 if this is supposed to be greater than
        // other and 0 if they are supposed to be equal
        return this.instanziated.after(instanziated) ? -1 : 1;
    }

}