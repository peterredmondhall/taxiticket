package com.taxiticket.client.order;

import com.google.gwt.user.client.ui.Composite;

public interface Step
{

    String getCaption();

    Composite getContent();

}
