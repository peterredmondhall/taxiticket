package com.taxiticket.client;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.taxiticket.shared.BookingInfo;

/**
 * The async counterpart of <code>GreetingService</code>.
 */
public interface TaxiticketServiceAsync {

	void addBooking(BookingInfo bOOKINGINFO,
			AsyncCallback<BookingInfo> asyncCallback);
}
