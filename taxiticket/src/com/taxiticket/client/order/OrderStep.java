package com.taxiticket.client.order;

import java.util.logging.Logger;

import com.google.gwt.user.client.ui.Composite;
import com.taxiticket.shared.BookingInfo;

public class OrderStep implements Step
{
    private static final Logger logger = Logger.getLogger(OrderStep.class.getName());

    private final OrderUi ui;

    public OrderStep()
    {
            ui = new OrderUi();
    }

    @Override
    public String getCaption()
    {
        return "Order";
    }

    @Override
    public Composite getContent()
    {
        return ui;
    }

    private static final String EMAIL_PATTERN =
            "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

    private static final String IBAN_PATTERN =
    "[a-zA-Z]{2}[0-9]{2}[a-zA-Z0-9]{4}[0-9]{7}([a-zA-Z0-9]?){0,16}";
    
    public static boolean isEmailValid(String email)
    {
        boolean result = false;
        if (email != null)
        {
            result = email.matches(EMAIL_PATTERN);
        }
        return result;
    }

    public static boolean isIBANValid(String iban)
    {
        boolean result = false;
        if (iban != null)
        {
            result = iban.matches(IBAN_PATTERN);
        }
        return result;
    }

	public void setStatus(BookingInfo bookingInfo) {
		ui.setStatus(bookingInfo);		
	}

}
