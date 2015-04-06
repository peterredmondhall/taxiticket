package com.taxiticket.client.service;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.taxiticket.shared.BookingInfo;

/**
 * The async counterpart of <code>GreetingService</code>.
 */
public interface TaxiticketServiceAsync
{
    void createBooking(BookingInfo bookingInfo, AsyncCallback<BookingInfo> callback)
            throws IllegalArgumentException;

}
