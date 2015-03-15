package com.taxiticket.client;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import com.taxiticket.shared.BookingInfo;

/**
 * The client-side stub for the RPC service.
 */
@RemoteServiceRelativePath("greet")
public interface TaxiticketService extends RemoteService {

	BookingInfo addBooking(BookingInfo bOOKINGINFO);
}
