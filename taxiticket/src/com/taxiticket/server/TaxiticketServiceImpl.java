package com.taxiticket.server;

import com.taxiticket.client.TaxiticketService;
import com.taxiticket.shared.BookingInfo;
import com.taxiticket.shared.FieldVerifier;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

/**
 * The server-side implementation of the RPC service.
 */
@SuppressWarnings("serial")
public class TaxiticketServiceImpl extends RemoteServiceServlet implements
		TaxiticketService {


	@Override
	public BookingInfo addBooking(BookingInfo bOOKINGINFO) {
		// TODO Auto-generated method stub
		return null;
	}
}
