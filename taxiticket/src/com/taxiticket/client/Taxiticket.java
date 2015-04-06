package com.taxiticket.client;

import java.util.logging.Logger;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.http.client.RequestException;
import com.google.gwt.http.client.Response;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.RootPanel;
import com.taxiticket.client.order.OrderStep;
import com.taxiticket.client.order.Step;
import com.taxiticket.client.resources.ClientMessages;
import com.taxiticket.client.service.TaxiticketService;
import com.taxiticket.client.service.TaxiticketServiceAsync;
import com.taxiticket.shared.BookingInfo;
import com.taxiticket.shared.StatInfo.Update;



/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class Taxiticket implements EntryPoint
{
    public static final Logger logger = Logger.getLogger(Taxiticket.class.getName());
    public static final TaxiticketServiceAsync SERVICE = GWT.create(TaxiticketService.class);
    public static com.taxiticket.client.resources.ClientMessages MESSAGES = GWT.create(ClientMessages.class);

    private static OrderStep orderStep;

    private static Long SESSION_IDENT = Double.doubleToLongBits(Math.random());
    private Screen screen;
    public static BookingInfo BOOKINGINFO = new BookingInfo();
    /**
     * This is the entry point method.
     */
    @Override
    public void onModuleLoad()
    {
        Window.setTitle("taxiticket");

        Screen.SCREEN_WIDTH = Window.getClientWidth();
        Screen.SCREEN_HEIGHT = Window.getClientHeight();

        screen = new Screen();

        orderStep = new OrderStep();
        String shareId = Window.Location.getParameter("share");
        String review = Window.Location.getParameter("review");
        String nick = Window.Location.getParameter("nick");
        String defaultuser = Window.Location.getParameter("defaultagent");
        String routeId = Window.Location.getParameter("route");
        
        completeSetup(orderStep);
        

    }

 
    private void collectStats(String src)
    {
        String protocol = Window.Location.getProtocol();
        String url = protocol + "//" + Window.Location.getHost() + "/stat?src=" + src + "&session=" + SESSION_IDENT;
        RequestBuilder builder = new RequestBuilder(RequestBuilder.GET, url);

        try
        {
            Request response = builder.sendRequest(null, new RequestCallback()
            {
                @Override
                public void onError(Request request, Throwable exception)
                {
                }

                @Override
                public void onResponseReceived(Request request, Response response)
                {
                }
            });
        }
        catch (RequestException e)
        {
        }
    }
        
        private static void showPdf(BookingInfo bookingInfo)
        {
            String protocol = Window.Location.getProtocol();
            String url = protocol + "//" + Window.Location.getHost() + "/ticket?order=" + bookingInfo.getId();
            RequestBuilder builder = new RequestBuilder(RequestBuilder.GET, url);

            try
            {
                Request response = builder.sendRequest(null, new RequestCallback()
                {
                    @Override
                    public void onError(Request request, Throwable exception)
                    {
                    }

                    @Override
                    public void onResponseReceived(Request request, Response response)
                    {
                    }
                });
            }
            catch (RequestException e)
            {
            }

//        
//
//        X-AppEngine-Region
//
//        Name of region from which the request originated. This value only makes sense in the context of the country in X-AppEngine-Country. For example, if the country is "US" and the region is "ca", that "ca" means "California", not Canada.
//        X-AppEngine-City
//
//        Name of the city from which the request originated. For example, a request from the city of Mountain View might have the header value mountain view.
//        X-AppEngine-CityLatLong        
    }



    private void completeSetup(Step step)
    {


        screen.init(step);
        RootPanel.get().add(screen);

    }


	public static void sendStat(String string, Update type) {
		// TODO Auto-generated method stub
		
	}


	public static void book() 
	{
	        try
	        {
	            SERVICE.createBooking(BOOKINGINFO, new AsyncCallback<BookingInfo>()
	            {

	                @Override
	                public void onSuccess(BookingInfo bookingInfo)
	                {
	                	if (bookingInfo.isPaymentSuccessful())
	                	{
	                		//showPdf(bookingInfo);
	                		Window.Location.assign("ticket?order="+bookingInfo.getId());
	                	} else
	                	{
	                		orderStep.setStatus(bookingInfo);
	                	}
	                }

	                @Override
	                public void onFailure(Throwable caught)
	                {
	                }
	            });
	        }
	        catch (Exception ex)
	        {

	        }
	    
		
	}

}
