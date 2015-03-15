package com.taxiticket.client.order;

import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;

public interface Step
{

    String getCaption();

    Composite getContent();

    Boolean onNext();

    Boolean onBack();

    void clear();

    void show(boolean visible, Button prev, Button next);

}
