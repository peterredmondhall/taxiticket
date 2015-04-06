package com.taxiticket.shared;

import java.util.Date;

public class BookingInfo extends Info
{
    private static final long serialVersionUID = 1L;

    private Date date;

    private String name;
    private String email;
    private String iban;
    private String pickup;
    private String dropoff;
 
	private double[] price;
	private Long id;

	private boolean paymentSuccessful;
	private String paymentResponseCode;
	
	

    public boolean isPaymentSuccessful() {
		return paymentSuccessful;
	}

	public void setPaymentSuccessful(boolean paymentSuccessful) {
		this.paymentSuccessful = paymentSuccessful;
	}

	public String getPaymentResponseCode() {
		return paymentResponseCode;
	}

	public void setPaymentResponseCode(String paymentResponseCode) {
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

    public String getIban() {
		return iban;
	}

	public void setIban(String iban) {
		this.iban = iban;
	}


    public String getOrderNo()
    {
        int len = id.toString().length();
        return id.toString().substring(len - 5, len - 1);
    }

	public void setPrice(double distance, double[] price) {
		this.price = price;
		this.distance = distance;		
	}

	   public String getPickup() {
			return pickup;
		}

		public void setPickup(String pickup) {
			this.pickup = pickup;
		}

		public String getDropoff() {
			return dropoff;
		}

		public void setDropoff(String dropoff) {
			this.dropoff = dropoff;
		}

		private double distance;
	    public double getDistance() {
			return distance;
		}

		public double[] getPrice() {
			return price;
		}


}
