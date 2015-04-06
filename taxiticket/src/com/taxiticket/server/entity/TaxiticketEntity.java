package com.taxiticket.server.entity;

import java.io.Serializable;

import com.google.appengine.api.datastore.Key;
import com.taxiticket.shared.Info;

abstract public class TaxiticketEntity<T extends Info> implements Serializable
{
    private static final long serialVersionUID = 1L;

    abstract public void setKey(Key key);

    abstract public T getInfo();
}
