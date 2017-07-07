package com.dropoff.service.brawndo.client.java.app

import com.dropoff.service.brawndo.client.java.api.beans._
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import com.dropoff.service.brawndo.client.java.api.ApiV1
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date


/**
  * Created by jasonkastner on 7/3/17.
  */
object DropoffApp {
  def main(args: Array[String]): Unit = {
    System.out.println("HelloWorld!")
    val brawndo = new ApiV1
    //String url = "http://localhost:9094/v1";
    val url = "https://sandbow-brawndo.dropoff.com/v1"
    //String host = "localhost:9094";
    val host = "sandbox-brawndo.dropoff.com"
    val private_key = ""
    val public_key = ""
    brawndo.initialize(url, host, private_key, public_key)
    System.out.println("------------------------------")
    System.out.println("Getting API Info")
    val info = brawndo.info
    System.out.println("++++++++++++++++++++++++++++++")
    System.out.println("++++++++++++++++++++++++++++++")
    System.out.println("++++++++++++++++++++++++++++++")
    if (info != null) System.out.println("Info: " + info.toString)
    else System.out.println("Info: NULL")
    var orderGetParams = new OrderGetParameters
    System.out.println("------------------------------")
    System.out.println("Getting Order Page")
    var page = brawndo.order.get(orderGetParams)
    System.out.println("++++++++++++++++++++++++++++++")
    System.out.println("++++++++++++++++++++++++++++++")
    System.out.println("++++++++++++++++++++++++++++++")
    System.out.println("OrderPage: " + page.toString)
    val page1LastKey = page.get("last_key").getAsString
    if (page.get("last_key") != null) orderGetParams.setLast_key(page.get("last_key").getAsString)
    System.out.println("------------------------------")
    System.out.println("Getting Order Page 2")
    page = brawndo.order.get(orderGetParams)
    System.out.println("++++++++++++++++++++++++++++++")
    System.out.println("++++++++++++++++++++++++++++++")
    System.out.println("++++++++++++++++++++++++++++++")
    System.out.println("Order Page 2: " + page.toString)
    val page2LastKey = page.get("last_key").getAsString
    System.out.println("page1LastKey: " + page1LastKey)
    System.out.println("page2LastKey: " + page2LastKey)
    System.out.println("last keys are equal? " + (page1LastKey eq page2LastKey))
    val order = page.get("data").getAsJsonArray.get(0)
    val order_id = order.getAsJsonObject.get("details").getAsJsonObject.get("order_id").getAsString
    System.out.println("------------------------------")
    System.out.println("Getting order_id: " + order_id)
    orderGetParams = new OrderGetParameters
    orderGetParams.setOrder_id(order_id)
    val anOrder = brawndo.order.get(orderGetParams)
    System.out.println("++++++++++++++++++++++++++++++")
    System.out.println("++++++++++++++++++++++++++++++")
    System.out.println("++++++++++++++++++++++++++++++")
    System.out.println("An order: " + anOrder.toString)
    System.out.println("------------------------------")
    System.out.println("Getting Order Estimate")
    val estimateParams = new EstimateParameters
    estimateParams.setOrigin("117 San Jacinto Blvd, Austin, TX 78701")
    estimateParams.setDestination("901 S MoPac Expy, Austin, TX 78746")
    val sdf = new SimpleDateFormat("zzz")
    estimateParams.setUtc_offset(sdf.format(new Date))
    var estimate = null
    try {
      estimate = brawndo.order.estimate(estimateParams)
      System.out.println("++++++++++++++++++++++++++++++")
      System.out.println("++++++++++++++++++++++++++++++")
      System.out.println("++++++++++++++++++++++++++++++")
      System.out.println("Estimate: " + estimate.toString)
    } catch {
      case iae: IllegalArgumentException =>
        iae.printStackTrace()
    }
    System.out.println("------------------------------")
    System.out.println("Getting Order Estimate 2")
    val tomorrowTenAM = Calendar.getInstance
    tomorrowTenAM.setTime(new Date)
    tomorrowTenAM.set(Calendar.HOUR_OF_DAY, 0)
    tomorrowTenAM.set(Calendar.MINUTE, 0)
    tomorrowTenAM.set(Calendar.SECOND, 0)
    tomorrowTenAM.add(Calendar.DATE, 1)
    tomorrowTenAM.add(Calendar.HOUR, 10)
    estimateParams.setUtc_offset(sdf.format(tomorrowTenAM.getTime))
    val origin = Calendar.getInstance
    origin.setTime(new Date(0))
    val diff = (tomorrowTenAM.getTimeInMillis - origin.getTimeInMillis) / 1000
    estimateParams.setReady_timestamp(diff)
    try {
      estimate = brawndo.order.estimate(estimateParams)
      System.out.println("++++++++++++++++++++++++++++++")
      System.out.println("++++++++++++++++++++++++++++++")
      System.out.println("++++++++++++++++++++++++++++++")
      System.out.println("Estimate 2: " + estimate.toString)
    } catch {
      case iae: IllegalArgumentException =>
        iae.printStackTrace()
    }
    System.out.println("------------------------------")
    System.out.println("Creating Order")
    val orderCreateParams = new OrderCreateParameters
    //orderCreateParams.setCompany_id("3e8e7d4a596ae41448d7e9c55a3a79bc");
    val originParams = new OrderCreateAddress
    originParams.setCompany_name("Gus's Fried Chicken")
    originParams.setFirst_name("Napoleon")
    originParams.setLast_name("Bonner")
    originParams.setAddress_line_1("117 San Jacinto Blvd")
    //originParams.setAddress_line_2("");
    originParams.setCity("Austin")
    originParams.setState("TX")
    originParams.setZip("78701")
    originParams.setPhone("5125555555")
    originParams.setEmail("cluckcluck@gusfriedchicken.com")
    originParams.setLat(30.263706)
    originParams.setLng(-97.741703)
    originParams.setRemarks("Origin Remarks")
    orderCreateParams.setOrigin(originParams)
    val destinationParams = new OrderCreateAddress
    destinationParams.setCompany_name("Dropoff")
    destinationParams.setFirst_name("Jason")
    destinationParams.setLast_name("Kastner")
    destinationParams.setAddress_line_1("901 S MoPac Expy")
    destinationParams.setAddress_line_2("#150")
    destinationParams.setCity("Austin")
    destinationParams.setState("TX")
    destinationParams.setZip("78746")
    destinationParams.setPhone("512-555-5555")
    destinationParams.setEmail("jkastner+java+dropoff@dropoff.com")
    destinationParams.setLat(30.264573)
    destinationParams.setLng(-97.782073)
    destinationParams.setRemarks("Please use the front entrance. The back on is guarded by cats!")
    orderCreateParams.setDestination(destinationParams)
    val details = new OrderCreateDetails
    details.setReady_date(diff)
    details.setType("two_hr")
    details.setQuantity(10)
    details.setWeight(20)
    details.setDistance(estimate.get("data").getAsJsonObject.get("Distance").getAsString)
    details.setEta(estimate.get("data").getAsJsonObject.get("ETA").getAsString)
    details.setPrice(estimate.get("data").getAsJsonObject.get("two_hr").getAsJsonObject.get("Price").getAsString)
    //details.setReference_code("");
    //details.setReference_name("");
    orderCreateParams.setDetails(details)
    val createResponse = brawndo.order.create(orderCreateParams)
    System.out.println("++++++++++++++++++++++++++++++")
    System.out.println("++++++++++++++++++++++++++++++")
    System.out.println("++++++++++++++++++++++++++++++")
    System.out.println("Create Order: " + createResponse.toString)
    val created_order_id = createResponse.get("data").getAsJsonObject.get("order_id").getAsString
    System.out.println("------------------------------")
    System.out.println("Adding Tip")
    val tipParams = new TipParameters
    tipParams.setOrder_id(created_order_id)
    tipParams.setAmount(4.44)
    var tipResponse = brawndo.order.tip.create(tipParams)
    System.out.println("++++++++++++++++++++++++++++++")
    System.out.println("++++++++++++++++++++++++++++++")
    System.out.println("++++++++++++++++++++++++++++++")
    System.out.println("Created Tip: " + tipResponse.toString)
    System.out.println("------------------------------")
    System.out.println("Getting Tip")
    tipResponse = brawndo.order.tip.get(tipParams)
    System.out.println("++++++++++++++++++++++++++++++")
    System.out.println("++++++++++++++++++++++++++++++")
    System.out.println("++++++++++++++++++++++++++++++")
    System.out.println("Got Tip: " + tipResponse.toString)
    System.out.println("------------------------------")
    System.out.println("Deleting Tip")
    tipResponse = brawndo.order.tip.delete(tipParams)
    System.out.println("++++++++++++++++++++++++++++++")
    System.out.println("++++++++++++++++++++++++++++++")
    System.out.println("++++++++++++++++++++++++++++++")
    System.out.println("Deleted Tip: " + tipResponse.toString)
    System.out.println("------------------------------")
    System.out.println("Cancelling Order")
    val cancelParams = new OrderCancelParameters
    cancelParams.setOrder_id(created_order_id)
    val cancelResponse = brawndo.order.cancel(cancelParams)
    System.out.println("++++++++++++++++++++++++++++++")
    System.out.println("++++++++++++++++++++++++++++++")
    System.out.println("++++++++++++++++++++++++++++++")
    System.out.println("Cancelled Order: " + cancelResponse.toString)
    //brawndo.order.simulate("austin");
    brawndo.shutdown()
  }
}
