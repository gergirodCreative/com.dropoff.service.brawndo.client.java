package com.dropoff.service.brawndo.client.java.api;

import java.util.Map;
import java.util.HashMap;

import com.dropoff.service.brawndo.client.java.api.beans.*;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

/**
 * Created by jasonkastner on 7/3/17.
 */
public class Order {
    private Client client;
    private Gson gson = null;
    public Tip tip;

    public Order(Client client) {
        this.client = client;
        tip = new Tip(client);
    }

    public JsonObject estimate(EstimateParameters parameters) throws IllegalArgumentException {
        Map<String,String> query = new HashMap<String,String>();

        if (parameters.getOrigin() == null) {
            throw new IllegalArgumentException("origin should not be null");
        } else {
            query.put("origin", parameters.getOrigin());
        }

        if (parameters.getDestination() == null) {
            throw new IllegalArgumentException("destination should not be null");
        } else {
            query.put("destination", parameters.getDestination());
        }

        if (parameters.getUtc_offset() == null) {
            throw new IllegalArgumentException("utc_offset should not be null");
        } else {
            query.put("utc_offset", parameters.getUtc_offset());
        }

        if (parameters.getReady_timestamp() > 0) {
            query.put("ready_timestamp", Long.toString(parameters.getReady_timestamp()));
        }

        if (parameters.getCompany_id() != null) {
            query.put("company_id", parameters.getCompany_id());
        }

        return client.doGet("/estimate", "estimate", query);
    }

    public JsonObject get(OrderGetParameters parameters) {
        Map<String,String> query = new HashMap<String,String>();

        if (parameters.getCompany_id() != null) {
            query.put("company_id", parameters.getCompany_id());
        }

        if (parameters.getLast_key() != null) {
            query.put("last_key", parameters.getLast_key());
        }

        String path = "/order";

        if (parameters.getOrder_id() != null) {
            path += "/" + parameters.getOrder_id();
        }

        return client.doGet(path, "order", query);
    }

    public JsonObject cancel(OrderCancelParameters parameters) throws IllegalArgumentException {
        if (parameters.getOrder_id() == null) {
            throw new IllegalArgumentException("order_id should not be null");
        }

        Map<String,String> query = new HashMap<String,String>();

        if (parameters.getCompany_id() != null) {
            query.put("company_id", parameters.getCompany_id());
        }

        return client.doPost("/order/" + parameters.getOrder_id() + "/cancel", "order", null, query);
    }

    public JsonObject create(OrderCreateParameters parameters) {
        Map<String,String> query = new HashMap<String,String>();

        if(parameters.getCompany_id() != null) {
            query.put("company_id", parameters.getCompany_id());
        }

        if(gson == null) {
            gson = new Gson();
        }
        String payload = gson.toJson(parameters);
        System.out.println("Payload: "+payload);

        return client.doPost("/order", "order", payload, query);
    }

    public JsonObject simulate(String market) throws IllegalArgumentException {
        if (market == null) {
            throw new IllegalArgumentException("market should not be null");
        }

        return client.doGet("/order/simulate" + market, "order", null);
    }
}
