package com.taxiticket.client.service;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import com.taxiticket.shared.BookingInfo;

/**
 * The client-side stub for the RPC service.
 */
@RemoteServiceRelativePath("service")
public interface TaxiticketService extends RemoteService
{
    BookingInfo createBooking(BookingInfo model) throws IllegalArgumentException;

    BookingInfo check(BookingInfo model) throws IllegalArgumentException;

}
