package com.tribu.qarestassure.tests;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;

public class Basics {
	
	@Test
	public void apiTest() {
		
		//given - all input details
		RestAssured.baseURI = "https://rahulshettyacademy.com";
		String postResponse = given().log().all().queryParam("key", "qaclick123")
		.header("Content-Type", "application/json")
		.body("{\n"
				+ "  \"location\": {\n"
				+ "    \"lat\": -38.383494,\n"
				+ "    \"lng\": 33.427362\n"
				+ "  },\n"
				+ "  \"accuracy\": 50,\n"
				+ "  \"name\": \"Jaime house\",\n"
				+ "  \"phone_number\": \"(+91) 983 893 3937\",\n"
				+ "  \"address\": \"29, side layout, cohen 09\",\n"
				+ "  \"types\": [\n"
				+ "    \"shoe park\",\n"
				+ "    \"shop\"\n"
				+ "  ],\n"
				+ "  \"website\": \"http://google.com\",\n"
				+ "  \"language\": \"French-IN\"\n"
				+ "}")
		
		//when - Sibmit the API
		.when().post("/maps/api/place/add/json")
		
		//then - validate response
		.then().assertThat().log().all().statusCode(200).body("scope", equalTo("APP"))
		.header("server", "Apache/2.4.41 (Ubuntu)")
		.extract().response().asString();
		
		JsonPath postResponseJason = new JsonPath(postResponse);
		String place_id = postResponseJason.getString("place_id");
		
		//updating the place name
		String newAddress = "Cr. 67# 73-168 Barranquilla";
		given().log().all().queryParam("key", "qaclick123")
		.header("Content-Type", "application/json")
		.body("{\n"
				+ "\"place_id\":\"" + place_id + "\",\n"
				+ "\"address\":\"" + newAddress + "\",\n"
				+ "\"key\":\"qaclick123\"\n"
				+ "}")
		.when().put("/maps/api/place/update/json")
		.then().assertThat().log().all().statusCode(200).body("msg",equalTo("Address successfully updated"));

		String getResponse = given().log().all().queryParam("key", "qaclick123").queryParam("place_id", place_id)
		.when().get("/maps/api/place/get/json")
		.then().assertThat().log().all().statusCode(200).extract().response().asString();
		
		JsonPath getResponseJason = new JsonPath(getResponse);
		String actualAddress = getResponseJason.getString("address");
		System.out.println("get actual Address: " +  actualAddress);
		
		SoftAssert softAssert = new SoftAssert();
		
		softAssert.assertEquals(newAddress, actualAddress);
		softAssert.assertAll();
	}

}
