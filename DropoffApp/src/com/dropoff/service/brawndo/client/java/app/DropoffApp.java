package com.dropoff.service.brawndo.client.java.app;

import com.dropoff.service.brawndo.client.java.api.beans.*;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import com.dropoff.service.brawndo.client.java.api.ApiV1;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by jasonkastner on 7/3/17.
 */
public class DropoffApp {
    public static void main(String[] args) {
        System.out.println("HelloWorld!");
        ApiV1 brawndo = new ApiV1();
        String url = "http://localhost:9094/v1";
        //String url = "https://sandbow-brawndo.dropoff.com/v1";
        String host = "localhost:9094";
        //String host = "sandbox-brawndo.dropoff.com";
        String private_key = "74ac377c478a9fbd05203b3125db3f6402ead2d2ce1b9fa936c04fce43d8c168";
        String public_key = "11981f9d4c223a598fd2a550568064a259c08c367ce6d46cde2a47026b5e4bcb";

        brawndo.initialize(url, host, private_key, public_key);
        System.out.println("------------------------------");
        System.out.println("Getting API Info");
        JsonObject info = brawndo.info();
        System.out.println("++++++++++++++++++++++++++++++");
        System.out.println("++++++++++++++++++++++++++++++");
        System.out.println("++++++++++++++++++++++++++++++");
        if (info != null) {
            System.out.println("Info: " + info.toString());
        } else {
            System.out.println("Info: NULL");
        }

        OrderGetParameters orderGetParams = new OrderGetParameters();

        System.out.println("------------------------------");
        System.out.println("Getting Order Page");
        JsonObject page = brawndo.order.get(orderGetParams);
        System.out.println("++++++++++++++++++++++++++++++");
        System.out.println("++++++++++++++++++++++++++++++");
        System.out.println("++++++++++++++++++++++++++++++");
        System.out.println("OrderPage: " + page.toString());

        String page1LastKey = page.get("last_key").getAsString();

        if (page.get("last_key") != null) {
            orderGetParams.setLast_key(page.get("last_key").getAsString());
        }

        System.out.println("------------------------------");
        System.out.println("Getting Order Page 2");
        page = brawndo.order.get(orderGetParams);
        System.out.println("++++++++++++++++++++++++++++++");
        System.out.println("++++++++++++++++++++++++++++++");
        System.out.println("++++++++++++++++++++++++++++++");
        System.out.println("Order Page 2: " + page.toString());

        String page2LastKey = page.get("last_key").getAsString();

        System.out.println("page1LastKey: " + page1LastKey);
        System.out.println("page2LastKey: " + page2LastKey);
        System.out.println("last keys are equal? " + (page1LastKey == page2LastKey));

        JsonElement order = page.get("data").getAsJsonArray().get(0);
        String order_id = order.getAsJsonObject().get("details").getAsJsonObject().get("order_id").getAsString();

        System.out.println("------------------------------");
        System.out.println("Getting order_id: " + order_id);
        orderGetParams = new OrderGetParameters();
        orderGetParams.setOrder_id(order_id);
        JsonObject anOrder = brawndo.order.get(orderGetParams);
        System.out.println("++++++++++++++++++++++++++++++");
        System.out.println("++++++++++++++++++++++++++++++");
        System.out.println("++++++++++++++++++++++++++++++");
        System.out.println("An order: " + anOrder.toString());

        System.out.println("------------------------------");
        System.out.println("Getting Order Estimate");
        EstimateParameters estimateParams = new EstimateParameters();
        estimateParams.setOrigin("3544 RR 620 South, Bee Cave, TX 78738");
        estimateParams.setDestination("800 Brazos Street, Austin, TX 78701");
        SimpleDateFormat sdf = new SimpleDateFormat("zzz");
        estimateParams.setUtc_offset(sdf.format(new Date()));
        JsonObject estimate = null;
        try {
            estimate = brawndo.order.estimate(estimateParams);
            System.out.println("++++++++++++++++++++++++++++++");
            System.out.println("++++++++++++++++++++++++++++++");
            System.out.println("++++++++++++++++++++++++++++++");
            System.out.println("Estimate: " + estimate.toString());
        } catch (IllegalArgumentException iae) {
            iae.printStackTrace();
        }

        System.out.println("------------------------------");
        System.out.println("Getting Order Estimate 2");
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.add(Calendar.DATE, 1);
        cal.add(Calendar.HOUR, 10);
        estimateParams.setUtc_offset(sdf.format(cal.getTime()));

        Calendar origin = Calendar.getInstance();
        origin.setTime(new Date(0));
        Long diff = new Long((cal.getTimeInMillis() - origin.getTimeInMillis())/1000);
        estimateParams.setReady_timestamp(diff);

        try {
            estimate = brawndo.order.estimate(estimateParams);
            System.out.println("++++++++++++++++++++++++++++++");
            System.out.println("++++++++++++++++++++++++++++++");
            System.out.println("++++++++++++++++++++++++++++++");
            System.out.println("Estimate 2: " + estimate.toString());
        } catch (IllegalArgumentException iae) {
            iae.printStackTrace();
        }

        System.out.println("------------------------------");
        System.out.println("Creating Order");
        OrderCreateParameters orderCreateParams = new OrderCreateParameters();
        //orderCreateParams.setCompany_id("3e8e7d4a596ae41448d7e9c55a3a79bc");
        OrderCreateAddress originParams = new OrderCreateAddress();
        originParams.setCompany_name("Jason's House");
        originParams.setFirst_name("Jason");
        originParams.setLast_name("Kastner");
        originParams.setAddress_line_1("3544 RR 620 South");
        //originParams.setAddress_line_2("");
        originParams.setCity("Bee Cave");
        originParams.setState("TX");
        originParams.setZip("78738");
        originParams.setPhone("5555555555");
        originParams.setEmail("jkastner+java@dropoff.com");
        originParams.setLat(30.3215456);
        originParams.setLng(-97.9591534);
        originParams.setRemarks("Origin Remarks");
        orderCreateParams.setOrigin(originParams);

        OrderCreateAddress destinationParams = new OrderCreateAddress();
        destinationParams.setCompany_name("Dropoff");
        destinationParams.setFirst_name("Jason");
        destinationParams.setLast_name("Kastner");
        destinationParams.setAddress_line_1("800 Brazos Street");
        destinationParams.setAddress_line_2("Suite 250");
        destinationParams.setCity("Austin");
        destinationParams.setState("TX");
        destinationParams.setZip("78701");
        destinationParams.setPhone("555-555-5555");
        destinationParams.setEmail("jkastner+java+dropoff@dropoff.com");
        destinationParams.setLat(30.2700705);
        destinationParams.setLng(-97.7432062);
        destinationParams.setRemarks("Destination Remarks");
        orderCreateParams.setDestination(destinationParams);

        OrderCreateDetails details = new OrderCreateDetails();
        details.setReady_date(diff);
        details.setType("two_hr");
        details.setQuantity(10);
        details.setWeight(20);
        details.setDistance(estimate.get("data").getAsJsonObject().get("Distance").getAsString());
        details.setEta(estimate.get("data").getAsJsonObject().get("ETA").getAsString());
        details.setPrice(estimate.get("data").getAsJsonObject().get("two_hr").getAsJsonObject().get("Price").getAsString());
        //details.setReference_code("");
        //details.setReference_name("");
        orderCreateParams.setDetails(details);

        JsonObject createResponse = brawndo.order.create(orderCreateParams);
        System.out.println("++++++++++++++++++++++++++++++");
        System.out.println("++++++++++++++++++++++++++++++");
        System.out.println("++++++++++++++++++++++++++++++");
        System.out.println("Create Order: " + createResponse.toString());
        String created_order_id = createResponse.get("data").getAsJsonObject().get("order_id").getAsString();

        System.out.println("------------------------------");
        System.out.println("Adding Tip");
        TipParameters tipParams = new TipParameters();
        tipParams.setOrder_id(created_order_id);
        tipParams.setAmount(4.44);

        JsonObject tipResponse = brawndo.order.tip.create(tipParams);
        System.out.println("++++++++++++++++++++++++++++++");
        System.out.println("++++++++++++++++++++++++++++++");
        System.out.println("++++++++++++++++++++++++++++++");
        System.out.println("Created Tip: " + tipResponse.toString());

        System.out.println("------------------------------");
        System.out.println("Getting Tip");
        tipResponse = brawndo.order.tip.get(tipParams);
        System.out.println("++++++++++++++++++++++++++++++");
        System.out.println("++++++++++++++++++++++++++++++");
        System.out.println("++++++++++++++++++++++++++++++");
        System.out.println("Got Tip: " + tipResponse.toString());

        System.out.println("------------------------------");
        System.out.println("Deleting Tip");
        tipResponse = brawndo.order.tip.delete(tipParams);
        System.out.println("++++++++++++++++++++++++++++++");
        System.out.println("++++++++++++++++++++++++++++++");
        System.out.println("++++++++++++++++++++++++++++++");
        System.out.println("Deleted Tip: " + tipResponse.toString());

        System.out.println("------------------------------");
        System.out.println("Cancelling Order");
        OrderCancelParameters cancelParams = new OrderCancelParameters();
        cancelParams.setOrder_id(created_order_id);
        JsonObject cancelResponse = brawndo.order.cancel(cancelParams);
        System.out.println("++++++++++++++++++++++++++++++");
        System.out.println("++++++++++++++++++++++++++++++");
        System.out.println("++++++++++++++++++++++++++++++");
        System.out.println("Cancelled Order: " + cancelResponse.toString());
    }
}