package com.learn.qarestassured.tests;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

import java.util.Map;

import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import com.learn.qarestassured.testframework.utilities.DataProviders;
import com.learn.qarestassured.testframework.utilities.Utilities;

import io.restassured.RestAssured;

public class ApiTest {

	public static String dataProviderFilePath = "src/test/resources/providerFiles/api-data.csv";

	@Test(dataProvider = "csvReader", dataProviderClass = DataProviders.class, groups = { "smoke" })
	public void apiTest(Map<String, String> provider) {

		// given - all input details
		RestAssured.baseURI = provider.get("baseUri");
		
		String postResponse = given().log().all()
			.queryParam("key", provider.get("key"))
			.header("Content-Type", provider.get("contentType"))
			.body(provider.get("body"))
	
			// when - Submit the API
			.when().post(provider.get("resource"))
	
			// then - validate response
			.then().assertThat()
			.statusCode(Integer.parseInt(provider.get("statusCode")))
			.body("scope", equalTo(provider.get("scope")))
			.header("server", provider.get("server"))
			.extract().response().asString();

		// read jason element
		String place_id = Utilities.rawToJson(postResponse).getString("place_id");

		// updating the place name
		given().log().all()
			.queryParam("key", provider.get("statusCode"))
			.header("Content-Type", provider.get("contentType"))
			.body("{\n" + "\"place_id\":\"" + place_id + "\",\n" + "\"address\":\"" + provider.get("newAddress") + "\",\n"
						+ "\"key\":\"qaclick123\"\n" + "}")
			.when().put("/maps/api/place/update/json")
			.then().assertThat().log().all()
			.statusCode(200)
			.body("msg", equalTo("Address successfully updated"));

		String getResponse = given().log().all()
			.queryParam("key", provider.get("key"))
			.queryParam("place_id", place_id)
			.when().get("/maps/api/place/get/json")
			.then().assertThat().log().all()
			.statusCode(200)
			.extract().response().asString();

		String actualAddress = Utilities.rawToJson(getResponse).getString("address");
		System.out.println("get actual Address: " + actualAddress);

		SoftAssert softAssert = new SoftAssert();
		softAssert.assertEquals(provider.get("newAddress"), actualAddress);
		softAssert.assertAll();
	}

}
