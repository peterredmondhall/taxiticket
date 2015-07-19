package com.taxiticket.client.order;

import static com.google.common.base.Strings.isNullOrEmpty;
import static com.taxiticket.client.Taxiticket.BOOKINGINFO;

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
import com.google.gwt.event.logical.shared.ShowRangeEvent;
import com.google.gwt.event.logical.shared.ShowRangeHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.RadioButton;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.SuggestBox;
import com.google.gwt.user.client.ui.SuggestOracle;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
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
    private static NumberFormat euroFormat = NumberFormat.getFormat(".00");
    private static final String EURO = "\u20ac";

    interface OrderUiUiBinder extends UiBinder<Widget, OrderUi>
    {
    }

    private GoogleMap map;

    // boolean test = false;
    @UiField
    Panel mainPanel, autoOrderPanel;

    @UiField
    DateBox dateBox;

    @UiField
    Label dateMsg, dateErrorMsg, emailErrorMsg, email2ErrorMsg, errorMsgPickup, errorMsgDropoff, errorMsgName;

    @UiField
    Label labelEmailMsg, labelEmail2Msg, labelBookingStatus, labelPrice, labelBookingError;

    @UiField
    TextBox email, email2, textboxName;

    @UiField
    SuggestBox startSuggestionBox, destSuggestionBox;

    @UiField
    HTMLPanel mapContainer;

    @UiField
    Button payWithCard;

    @UiField
    Anchor orderFormlink;

    @UiField
    ListBox anzahlTaxiListBox, pickupListBox;

    private final List<LatLng> places = new ArrayList<>();
    private LatLng destination;

    public OrderUi()
    {
        createUi();
        createMap();

        fillListBoxes();
        addSelfOrderCheckbox();
        dateBox.setFormat(new DefaultFormat(DateTimeFormat.getFormat("dd.MM.yyyy")));
        restrictDates();
        orderFormlink.setVisible(false);
        // payWithCard.setText("Prüfen");
        payWithCard.setText("Bestellen");
        labelBookingError.setVisible(false);
        pickupListBox.setVisible(false);
        payWithCard.addClickHandler(new ClickHandler()
        {

            @Override
            public void onClick(ClickEvent event)
            {
                clearMessages();
                if (validate())
                {
                    Date date = dateBox.getValue();
                    BOOKINGINFO.setDate(date);
                    BOOKINGINFO.setPickup(pickupListBox.getSelectedItemText());
                    BOOKINGINFO.setDateText(DateTimeFormat.getFormat("dd.MM.yyyy").format(date));
                    BOOKINGINFO.setEmail(email.getValue());
                    Taxiticket.BOOKINGINFO.setPickup(startSuggestionBox.getText());
                    Taxiticket.BOOKINGINFO.setDropoff(destSuggestionBox.getText());
                    Taxiticket.BOOKINGINFO.setName(textboxName.getText());
                    Taxiticket.BOOKINGINFO.setNumTaxis(anzahlTaxiListBox.getSelectedIndex() + 1);
                    Taxiticket.BOOKINGINFO.setPickupTime(pickupListBox.getItemText(pickupListBox.getSelectedIndex()));

                    Taxiticket.book();
                    labelBookingStatus.setVisible(true);
                    labelBookingStatus.setText("Ticket wird erstellt...");
                }

            }

        });

        if (false)
        {
            email.setText("hall@hall-services.de");
            email2.setText("hall@hall-services.de");
            textboxName.setText("Peter Hall");

        }

    }

    private void fillListBoxes()
    {
        for (int i = 1; i < 10; i++)
        {
            anzahlTaxiListBox.addItem("" + i);
        }

        for (int hour = 0; hour < 23; hour++)
        {
            pickupListBox.addItem(hour + ":00 Uhr");
            pickupListBox.addItem(hour + ":15 Uhr");
            pickupListBox.addItem(hour + ":30 Uhr");
            pickupListBox.addItem(hour + ":45 Uhr");
        }
    }

    private void addSelfOrderCheckbox()
    {
        RadioButton rb0 = new RadioButton("myRadioGroup", "Ich rufe selbst ein Taxi an.");
        RadioButton rb1 = new RadioButton("myRadioGroup", "Bitte bestelle mir ein Taxi um ...");

        rb0.setValue(true);

        // Add them to the root panel.
        VerticalPanel panel = new VerticalPanel();
        panel.add(rb0);
        panel.add(rb1);

        ClickHandler ch = new ClickHandler()
        {

            @Override
            public void onClick(ClickEvent event)
            {
                BOOKINGINFO.setAutoOrder(!BOOKINGINFO.isAutoOrder());
                pickupListBox.setVisible(BOOKINGINFO.isAutoOrder());
            }
        };
        rb0.addClickHandler(ch);
        rb1.addClickHandler(ch);

        // Add it to the root panel.
        autoOrderPanel.add(panel);
    }

    private void restrictDates()
    {
        dateBox.addValueChangeHandler(new ValueChangeHandler<Date>()
        {
            @Override
            public void onValueChange(final ValueChangeEvent<Date> dateValueChangeEvent)
            {
                if (dateValueChangeEvent.getValue().before(earliest()))
                {
                    dateBox.setValue(earliest(), false);
                }
            }
        });
        dateBox.getDatePicker().addShowRangeHandler(new ShowRangeHandler<Date>()
        {
            @Override
            public void onShowRange(final ShowRangeEvent<Date> dateShowRangeEvent)
            {
                final Date today = zeroTime(new Date());
                Date d = zeroTime(dateShowRangeEvent.getStart());
                while (d.before(today))
                {
                    dateBox.getDatePicker().setTransientEnabledOnDates(false, d);
                    nextDay(d);
                }
            }
        });
    }

    private Date earliest()
    {
        Date earliest = new Date();
        com.google.gwt.user.datepicker.client.CalendarUtil.addDaysToDate(earliest, -1);
        return zeroTime(earliest);
    }

    private static void nextDay(final Date date)
    {
        com.google.gwt.user.datepicker.client.CalendarUtil.addDaysToDate(date, 1);
    }

    /** this is important to get rid of the time portion, including ms */
    private Date zeroTime(final Date date)
    {
        Date zeroTimeDate = DateTimeFormat.getFormat("yyyyMMdd").parse(DateTimeFormat.getFormat("yyyyMMdd").format(date));
        // zeroTimeDate.setHours(12);
        return zeroTimeDate;
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

//    public String getIBAN()
//    {
//        return textboxIBAN.getValue();
//    }

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
                                    // map.setCenter(result.getGeometry().getLocation());
                                    MarkerOptions markerOptions = MarkerOptions.create();
                                    markerOptions.setDraggable(false);
                                    Marker marker = Marker.create(markerOptions);
                                    marker.setPosition(result.getGeometry().getLocation());
                                    // marker.setMap(map);
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
                                if (dropoff != null && pickup != null)
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
                            for (int i = 0; i < route.getLegs().length(); i++)
                            {
                                distance += route.getLegs().get(i).getDistance().getValue();
                            }
                            int[] price = PriceCalc.getPrice(distance);
                            Taxiticket.BOOKINGINFO.setPrice(distance, price);
                            labelPrice.setText(getEndPrice(price));
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

    private String getEndPrice(int[] price)
    {
        return EURO + euroFormat.format(price[2] / 100);
    }

    public boolean isEmailValid()
    {
        return email.getText() != null && email.getText().matches(EMAIL_PATTERN);
    }

    private boolean validate()
    {

        Lists.newArrayList();
        if (isNullOrEmpty(startSuggestionBox.getText()))
        {
            setErrorMsg("Darf nicht leer sein", ErrorMsg.PICKUP);
            return false;
        }
        if (isNullOrEmpty(destSuggestionBox.getText()))
        {
            setErrorMsg("Darf nicht leer sein", ErrorMsg.DROPOFF);
            return false;
        }

        if (isNullOrEmpty(email.getText()))
        {
            setErrorMsg("Darf nicht leer sein", ErrorMsg.EMAIL);
            return false;
        }
        if (!email.getText().matches(EMAIL_PATTERN))
        {
            setErrorMsg(VALID_EMAIL, ErrorMsg.EMAIL);
            return false;
        }

        if (isNullOrEmpty(email2.getText()))
        {
            setErrorMsg("Darf nicht leer sein", ErrorMsg.EMAIL2);
            return false;
        }
        if (!email.getText().matches(EMAIL_PATTERN))
        {
            setErrorMsg(VALID_EMAIL, ErrorMsg.EMAIL2);
            return false;
        }

        if (!email.getText().equals(email2.getText()))
        {
            setErrorMsg("Muss gleich sein", ErrorMsg.EMAIL2);
            return false;
        }

        if (isNullOrEmpty(textboxName.getText()))
        {
            setErrorMsg("Darf nicht leer sein", ErrorMsg.NAME);
            return false;
        }
//        if (isNullOrEmpty(textboxIBAN.getText()))
//        {
//            setErrorMsg("Darf nicht leer sein", ErrorMsg.IBAN);
//            return false;
//        }
        if (isNullOrEmpty(startSuggestionBox.getText()))
        {
            setErrorMsg("Darf nicht leer sein", ErrorMsg.PICKUP);
            return false;
        }

        return true;
    }

    private void clearMessages()
    {
        for (ErrorMsg msg : ErrorMsg.values())
        {
            setErrorMsg("", msg);
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
//            case IBAN:
//                ibanErrorMsg.setText(msg);
//                break;
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

    public enum Status
    {
        CHECK,
        PAYMENT_FAILED
    }

    public void updateStatus(Status status)
    {
        labelBookingError.setVisible(false);
        labelBookingError.setVisible(true);
        switch (status)
        {
            case CHECK:
                if (BOOKINGINFO.getToken() == null)
                {
                    labelBookingError.setText("IBAN/Name nicht akzeptiert");
                    labelBookingError.setVisible(true);
                    labelBookingStatus.setVisible(false);
                }
                else
                {
                    if (BOOKINGINFO.isCustomer())
                    {
                        labelBookingStatus.setVisible(true);
                        labelBookingStatus.setText("Bitte bezahlen.");
                        payWithCard.setText("Bezahlen Sie " + getEndPrice(BOOKINGINFO.getPrice()));
                    }
                    else
                    {
                        payWithCard.setVisible(false);
                        labelBookingError.setText("Nicht autorisiert.");
                        labelBookingError.setVisible(true);
                        labelBookingStatus.setVisible(false);
                    }
                }
                break;
            case PAYMENT_FAILED:
                labelBookingError.setText("Fehler beim Erstellung des Tickets.");
                labelBookingError.setVisible(true);
                labelBookingStatus.setVisible(false);

                break;
            default:
                break;

        }
    }

}
