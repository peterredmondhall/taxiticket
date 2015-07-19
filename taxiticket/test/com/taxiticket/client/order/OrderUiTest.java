package com.taxiticket.client.order;

import java.util.Date;

import com.google.gwt.junit.client.GWTTestCase;
import com.google.gwt.user.datepicker.client.DateBox;

public class OrderUiTest extends GWTTestCase
{

    /**
     * Specifies a module to use when running this test case. The returned
     * module must include the source for this class.
     * 
     * @see com.google.gwt.junit.client.GWTTestCase#getModuleName()
     */
    @Override
    public String getModuleName()
    {
        return "com.taxiticket";
    }

    public void testUpperCasingLabel()
    {
        OrderUi orderUi = new OrderUi();
        DateBox dateBox = orderUi.dateBox;
        Date date = dateBox.getValue();
        assertEquals(new Date(), date);
    }
}