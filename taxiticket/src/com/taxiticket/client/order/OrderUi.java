package com.taxiticket.client.order;

import static com.taxiticket.client.Taxiticket.BOOKINGINFO;

import java.util.Date;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Display;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.datepicker.client.DateBox;
import com.google.gwt.user.datepicker.client.DateBox.DefaultFormat;
import com.taxiticket.client.Screen;
import com.taxiticket.client.Taxiticket;

public class OrderUi extends Composite
{
    public enum ErrorMsg
    {
        DATE,
        FIRST_NAME,
        LAST_NAME,
        FLIGHTNO,
        EMAIL,
        EMAIL2,
        ARRIVAL
    };

    private static OrderUiUiBinder uiBinder = GWT.create(OrderUiUiBinder.class);

    interface OrderUiUiBinder extends UiBinder<Widget, OrderUi>
    {
    }

    // boolean test = false;
    @UiField
    HTMLPanel mainPanel;

    @UiField
    DateBox dateBox;

    @UiField
    ListBox pax, surfboards;

    @UiField
    Label dateMsg, dateErrorMsg, flightErrorMsg, firstNameErrorMsg, lastNameErrorMsg, emailErrorMsg, email2ErrorMsg, arrivalErrorMsg, labelWanttoShare;

    @UiField
    Label labelSharing, labelBooking, labelFlightLandingTime, labelFlightNo, firstNameMsg, lastNameMsg, labelEmailMsg, labelEmail2Msg;

    @UiField
    Label labelRequirementsField;

    @UiField
    TextBox flightLandingTime, flightNo, firstName, lastName, email, email2;

    @UiField
    CheckBox checkboxWanttoShare;

    @UiField
    TextArea requirementsBox;

    public OrderUi()
    {
        createUi();

        mainPanel.getElement().getStyle().setDisplay(Display.NONE);
        for (int i = 1; i < 20; i++)
        {
            pax.addItem("" + i);
            surfboards.addItem("" + i);
        }
        dateBox.setFormat(new DefaultFormat(DateTimeFormat.getFormat("dd.MM.yyyy")));
        checkboxWanttoShare.setValue(true);

//        if (test)
//        {
//            dateBox.setValue(new Date());
//            flightLandingTime.setValue("12:00");
//            flightNo.setText("MH111");
//            firstName.setText("Peter");
//            lastName.setText("Hall");
//            email.setText("info@taxigang.com");
//            email2.setText("info@taxigang.com");
//
//        }
    }

    protected void createUi()
    {
        initWidget(uiBinder.createAndBindUi(this));
    }

    public Date getDate()
    {
        Date date = dateBox.getValue();
        if (date != null)
        {
            date.setHours(12);
        }
        return date;
    }

    public String getFirstName()
    {
        return firstName.getValue();
    }

    public String getLastName()
    {
        return lastName.getValue();
    }

    public String getEmail()
    {
        return email.getValue();
    }

    public String getEmail2()
    {
        return email2.getValue();
    }

    public String getArrivalTime()
    {
        return flightLandingTime.getText();
    }

    public String getSurfboards()
    {
        return surfboards.getItemText(surfboards.getSelectedIndex());
    }

    public String getPax()
    {
        return pax.getItemText(pax.getSelectedIndex());
    }

    public String getFlightNo()
    {
        return flightNo.getValue();
    }

    public boolean getWantToShare()
    {
        return checkboxWanttoShare.getValue();
    }

    public String getRequirements()
    {
        String requirements = requirementsBox.getText();
        if (requirements == null)
        {
            requirements = "none";
        }
        return requirements;
    }

    public void setErrorMsg(String msg, ErrorMsg errorMsg)
    {
        switch (errorMsg)
        {
            case DATE:
                dateErrorMsg.setText(msg);
                break;
            case FIRST_NAME:
                firstNameErrorMsg.setText(msg);
                break;
            case LAST_NAME:
                lastNameErrorMsg.setText(msg);
                break;
            case EMAIL:
                emailErrorMsg.setText(msg);
                break;
            case EMAIL2:
                email2ErrorMsg.setText(msg);
                break;
            case FLIGHTNO:
                flightErrorMsg.setText(msg);
                break;
            case ARRIVAL:
                arrivalErrorMsg.setText(msg);
                break;
        }
    }

    public void show(boolean visible, Button prev, Button next)
    {
        mainPanel.setVisible(visible);
        mainPanel.getElement().getStyle().setDisplay(visible ? Display.BLOCK : Display.NONE);

        next.setVisible(true);
        prev.setEnabled(true);
        prev.setVisible(true);


        dateBox.setValue(BOOKINGINFO.getDate());


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

}
