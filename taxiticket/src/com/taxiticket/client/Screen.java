package com.taxiticket.client;

import java.util.Map;

import com.google.common.collect.Maps;
import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Widget;
import com.taxiticket.client.order.Step;

public class Screen extends Composite
{

    private static ScreenBinder uiBinder = GWT.create(ScreenBinder.class);

    interface ScreenBinder extends UiBinder<Widget, Screen>
    {
    }

    //public static BookingInfo BOOKINGINFO = new BookingInfo();
    public static int SCREEN_WIDTH;
    public static int SCREEN_HEIGHT;

    @UiField
    HTMLPanel mainPanel;
    @UiField
    HTMLPanel steps;
    @UiField
    FlowPanel header;
    @UiField
    HTML progressBar;

    @UiField
    Button prev;
    @UiField
    Button next;

    public Screen()
    {
        initWidget();
        steps.clear();
        header.clear();
        mainPanel.setVisible(false);
        next.ensureDebugId("button_next");
    }

    protected void initWidget()
    {
        initWidget(uiBinder.createAndBindUi(this));
    }

    public void add(Step step)
    {
//        HTML headerHTML = new HTML((stepList.size() + 1) + ". " + step.getCaption());
//        header.add(headerHTML);
//
//        step.getContent().setVisible(false);

    }
    
    

    @Override
    public void setHeight(String height)
    {
        mainPanel.setHeight(height);
    }

    @Override
    public void setWidth(String width)
    {
        mainPanel.setWidth(width);
    }


    public void init(Step step)
    {
      HTML headerHTML = new HTML("Caption XXX");

        mainPanel.setVisible(true);

    }

}
