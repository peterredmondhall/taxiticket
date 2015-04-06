package com.taxiticket.client.order;


import static com.google.common.base.Strings.isNullOrEmpty;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.google.common.collect.Lists;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.JsArray;
import com.google.gwt.dom.client.Style.Display;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.SuggestBox;
import com.google.gwt.user.client.ui.SuggestOracle;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.datepicker.client.DateBox;
import com.google.gwt.user.datepicker.client.DateBox.DefaultFormat;
import com.google.maps.gwt.client.DirectionsRenderer;
import com.google.maps.gwt.client.DirectionsRendererOptions;
import com.google.maps.gwt.client.DirectionsRequest;
import com.google.maps.gwt.client.DirectionsResult;
import com.google.maps.gwt.client.DirectionsRoute;
import com.google.maps.gwt.client.DirectionsService;
import com.google.maps.gwt.client.DirectionsStatus;
import com.google.maps.gwt.client.Geocoder;
import com.google.maps.gwt.client.GeocoderRequest;
import com.google.maps.gwt.client.GeocoderResult;
import com.google.maps.gwt.client.GeocoderStatus;
import com.google.maps.gwt.client.GoogleMap;
import com.google.maps.gwt.client.LatLng;
import com.google.maps.gwt.client.MapOptions;
import com.google.maps.gwt.client.MapTypeId;
import com.google.maps.gwt.client.Marker;
import com.google.maps.gwt.client.MarkerOptions;
import com.google.maps.gwt.client.TravelMode;
import com.taxiticket.client.Taxiticket;
import com.taxiticket.client.utils.PriceCalc;
import com.taxiticket.client.utils.Utils;
import com.taxiticket.shared.BookingInfo;

public class OrderUi extends Composite
{
    public enum ErrorMsg
    {
    	PICKUP,
    	DROPOFF,
        DATE,
        NAME,
        IBAN,
        EMAIL,
        EMAIL2,
        EMAILMATCHES
    };
    
    private static final String VALID_EMAIL = "Muss ein valide Email sein";

    private static OrderUiUiBinder uiBinder = GWT.create(OrderUiUiBinder.class);
    private static NumberFormat usdFormat = NumberFormat.getFormat(".00");
    private static final String EURO = "\u20ac";
    interface OrderUiUiBinder extends UiBinder<Widget, OrderUi>
    {
    }

    private GoogleMap map;

    // boolean test = false;
    @UiField
    Panel mainPanel;

    @UiField
    DateBox dateBox;

     @UiField
    Label dateMsg, dateErrorMsg, ibanErrorMsg, emailErrorMsg, email2ErrorMsg,errorMsgPickup,errorMsgDropoff,errorMsgName;

    @UiField
    Label lastNameMsg, labelEmailMsg, labelEmail2Msg,labelBookingStatus,labelPrice;

    @UiField
    TextBox textboxIBAN, email, email2,textboxName;

    @UiField
    SuggestBox startSuggestionBox, destSuggestionBox;
    
    @UiField
    HTMLPanel mapContainer;

     @UiField
     Button payWithCard;
     
     private final List<LatLng> places = new ArrayList<>();
     private LatLng destination;

    public OrderUi()
    {
        createUi();
        createMap();

        dateBox.setFormat(new DefaultFormat(DateTimeFormat.getFormat("dd.MM.yyyy")));
        dateBox.setValue(new Date());

        payWithCard.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) 
			{
				clearMessages();
				if (validate())
				{
					Taxiticket.BOOKINGINFO.setDate(dateBox.getValue());
					Taxiticket.BOOKINGINFO.setEmail(email.getValue());
					Taxiticket.BOOKINGINFO.setPickup(startSuggestionBox.getText());
					Taxiticket.BOOKINGINFO.setDropoff(destSuggestionBox.getText());
					Taxiticket.BOOKINGINFO.setName(textboxName.getText());
					Taxiticket.BOOKINGINFO.setIban(textboxIBAN.getText());
					Taxiticket.book();
					labelBookingStatus.setText("Ticket wird erstellt...");
				}
				
			}

			
		});

        if (true)
        {
            email.setText("info@silmo.co");
            email2.setText("infox@silmo.cox");

        }

    }

    protected void createUi()
    {
        initWidget(uiBinder.createAndBindUi(this));
        mainPanel.getElement().getStyle().setDisplay(Display.BLOCK);
    	startSuggestionBox.addSelectionHandler(getSelectionHandler(startSuggestionBox));
    	destSuggestionBox.addSelectionHandler(getSelectionHandler(destSuggestionBox));

   }

    private void createMap()
    {
        SimplePanel mapWidget = new SimplePanel();
        mapWidget.setSize("260px", "260px");
        MapOptions options = MapOptions.create();
        options.setCenter(LatLng.create(48.10, 10.87));
        options.setZoom(10);
        options.setMapTypeId(MapTypeId.ROADMAP);
        options.setDraggable(true);
        options.setMapTypeControl(true);
        options.setScaleControl(true);
        options.setScrollwheel(true);
        map = GoogleMap.create(mapWidget.getElement(), options);
        map = GoogleMap.create(mapWidget.getElement());
        mapContainer.add(mapWidget);
    }


    public String getIBAN()
    {
        return textboxIBAN.getValue();
    }


    
    private GeocoderResult pickup;
    private GeocoderResult dropoff;
    
    private SelectionHandler<SuggestOracle.Suggestion> getSelectionHandler(final SuggestBox suggestBox)
    {
        SelectionHandler<SuggestOracle.Suggestion> handler = new SelectionHandler<SuggestOracle.Suggestion>()
        {

            @Override
            public void onSelection(SelectionEvent<SuggestOracle.Suggestion> event)
            {
            	clearMessages();
            	labelPrice.setText("");
                {
                    if (!isNullOrEmpty(suggestBox.getText()))
                    {
                        final String address = suggestBox.getText();
                        GeocoderRequest request = GeocoderRequest.create();
                        request.setAddress(address);
                        Geocoder geoCoder = Geocoder.create();
                        geoCoder.geocode(request, new Geocoder.Callback()
                        {
                            @Override
                            public void handle(JsArray<GeocoderResult> a, GeocoderStatus b)
                            {
                                if (b == GeocoderStatus.OK)
                                {
                                    GeocoderResult result = a.shift();
                                    //map.setCenter(result.getGeometry().getLocation());
                                    MarkerOptions markerOptions = MarkerOptions.create();
                                    markerOptions.setDraggable(false);
                                    Marker marker = Marker.create(markerOptions);
                                    marker.setPosition(result.getGeometry().getLocation());
                                    //marker.setMap(map);
                                    if (suggestBox.equals(destSuggestionBox))// destination == null)
                                    {
                                    	dropoff = result;
                                        destination = result.getGeometry().getLocation();
//                                        destinationName = address;
                                    }
                                    else
                                    {
                                    	pickup = result;
                                        places.add(result.getGeometry().getLocation());
//                                        placeNames.add(address);
//                                        btnMenu.setVisible(true);
                                    }
                                }
                                else
                                {
                                    // Window.alert("Something went wrong in google to get this location, check address");
                                }
                                if (dropoff !=null && pickup !=null)
                                {
                                	calculateRoute();
                                }
                                	
                            }
                        });
                    }
                    else
                    {
                        Window.alert("Please Enter Place!");
                    }
                }
            }
        };

        return handler;
    }

    final DirectionsRenderer directionsDisplay = DirectionsRenderer.create();
    DirectionsRendererOptions options = DirectionsRendererOptions.create();

    public void calculateRoute()
    {
        
            List<LatLng> optimizedPlaces = new ArrayList<>();
            Utils.getOpimimzedPlaces(optimizedPlaces, places, destination);
            int select = 1;
            for (final LatLng pickupPlace : optimizedPlaces)
            {
                options.setMap(map);
                directionsDisplay.setOptions(options);
                DirectionsRequest request = DirectionsRequest.create();
                request.setOrigin(pickupPlace);
                if (select < optimizedPlaces.size())
                {
                    request.setDestination(optimizedPlaces.get(select));
                    select = select + 2;
                }
                else
                {
                    request.setDestination(destination);
                }
                request.setTravelMode(TravelMode.DRIVING);
                DirectionsService.create().route(request, new com.google.maps.gwt.client.DirectionsService.Callback()
                {
                    @Override
                    public void handle(DirectionsResult a, DirectionsStatus b)
                    {
                        if (b == DirectionsStatus.OK)
                        {
                        	if (a.getRoutes().length() > 0)
                        	{
                        	DirectionsRoute route = a.getRoutes().get(0);
                        	double distance = 0;
                        	for (int i=0; i <route.getLegs().length();i++)
                        	{                        		
                        		distance += route.getLegs().get(i).getDistance().getValue();
                        	}
                        	double[]price = PriceCalc.getPrice(distance);
                        	Taxiticket.BOOKINGINFO.setPrice(distance,price);
                        	labelPrice.setText(EURO+usdFormat.format(price[2]/100));
                            directionsDisplay.setDirections(a);
                            map.setZoom(16);
                            map.setCenter(destination);
                        	}
                        	else
                        	{
                                Window.alert("Failed to fetch directions from server!");
                        	}
                        }
                        else
                        {
                            Window.alert("Failed to fetch directions from server!");
                        }
                    }
                });
            }
    }

    public boolean isEmailValid()
    {
        return email.getText() != null && email.getText().matches(EMAIL_PATTERN);
    }
    private boolean validate() {
		
    	Lists.newArrayList();
    	if (isNullOrEmpty(startSuggestionBox.getText()))
    	{
			setErrorMsg("Darf nicht leer sein",ErrorMsg.PICKUP);
			return false;
    	}
       	if (isNullOrEmpty(destSuggestionBox.getText()))
    	{
			setErrorMsg("Darf nicht leer sein",ErrorMsg.DROPOFF);
			return false;
    	}
       	
       	if (isNullOrEmpty(email.getText()))
    	{
			setErrorMsg("Darf nicht leer sein",ErrorMsg.EMAIL);
			return false;
    	}
       	if ( !email.getText().matches(EMAIL_PATTERN))
    	{
			setErrorMsg(VALID_EMAIL,ErrorMsg.EMAIL);
			return false;
    	}
       	
       	if (isNullOrEmpty(email2.getText()))
    	{
			setErrorMsg("Darf nicht leer sein",ErrorMsg.EMAIL2);
			return false;
    	}
       	if ( !email.getText().matches(EMAIL_PATTERN))
    	{
			setErrorMsg(VALID_EMAIL,ErrorMsg.EMAIL2);
			return false;
    	}
       	
       	if ( !email.getText().equals(email2.getText()))
    	{
			setErrorMsg("Muss gleich sein",ErrorMsg.EMAIL2);
			return false;
    	}
       	
       	if (isNullOrEmpty(textboxName.getText()))
    	{
			setErrorMsg("Darf nicht leer sein",ErrorMsg.NAME);
			return false;
    	}
       	if (isNullOrEmpty(textboxIBAN.getText()))
    	{
			setErrorMsg("Darf nicht leer sein",ErrorMsg.IBAN);
			return false;
    	}
    	if (isNullOrEmpty(startSuggestionBox.getText()))
    	{
			setErrorMsg("Darf nicht leer sein",ErrorMsg.PICKUP);
			return false;
    	}
		return true;
	}

	private void clearMessages() {
		for (ErrorMsg msg : ErrorMsg.values())
		{
			setErrorMsg("",msg);
		}
		
	}

	private static final String EMAIL_PATTERN =
            "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

    public static boolean isEmailValid(String email)
    {
        boolean result = false;
        if (email != null)
        {
            result = email.matches(EMAIL_PATTERN);
        }
        return result;
    }


	private void setErrorMsg(String msg, ErrorMsg errorMsg)
    {
        switch (errorMsg)
        {
        case DATE:
            dateErrorMsg.setText(msg);
            break;
        case NAME:
        	errorMsgName.setText(msg);
            break;
            case IBAN:
                ibanErrorMsg.setText(msg);
                break;
            case EMAIL:
                emailErrorMsg.setText(msg);
                break;
            case EMAIL2:
                email2ErrorMsg.setText(msg);
                break;
            case EMAILMATCHES:
                email2ErrorMsg.setText(msg);
                break;
            case PICKUP:
            	errorMsgPickup.setText(msg);
                break;
            case DROPOFF:
            	errorMsgDropoff.setText(msg);
                break;
                
        }
    }

    @Override
    public void setHeight(String height)
    {
        super.setHeight(height);
    }

    @Override
    public void setWidth(String width)
    {
        super.setWidth(width);
    }

	public void setStatus(BookingInfo bookingInfo) {
		labelBookingStatus.setText(bookingInfo.getPaymentResponseCode());		
	}

}
