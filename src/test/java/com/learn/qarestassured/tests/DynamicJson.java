package com.learn.qarestassured.tests;

import static io.restassured.RestAssured.given;

import java.util.Map;

import org.testng.annotations.Test;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.learn.qarestassured.testframework.utilities.DataProviders;
import com.learn.qarestassured.testframework.utilities.Utilities;

import io.restassured.RestAssured;

public class DynamicJson {

	public static String dataProviderFilePath = "src/test/resources/providerFiles/dynamic-json-data.csv";

	@Test(dataProvider = "csvReaderMethod", dataProviderClass = DataProviders.class, groups = { "smoke" })
	public void addBook(Map<String, String> provider){
		
		ObjectNode bodyAddBookNode = Utilities.rawToJsonObject(provider.get("bodyAddBook"));
		bodyAddBookNode.put("name", provider.get("name"));
		bodyAddBookNode.put("isbn", provider.get("isbn"));
		bodyAddBookNode.put("aisle", provider.get("aisle"));

		RestAssured.baseURI = provider.get("baseUri");
		
		String response = given().log().all().header("Content-Type", provider.get("contentType"))
			.body(bodyAddBookNode)
			.when().post(provider.get("resourceAddBook")).then().log().all().assertThat()
			.statusCode(Integer.parseInt(provider.get("statusCode")))
			.extract().response().asString();
		
		// get the book ID from the response
		
		String bookID = Utilities.rawToJsonObject(response).get("ID").textValue();
		
		// edit the delete body with the current ID
		
		ObjectNode bodyDeleteBookNode = Utilities.rawToJsonObject(provider.get("bodyDeleteBook"));
		bodyDeleteBookNode.put("ID", bookID);
		
		// delete the book we just created
		
		given().log().all().header("Content-Type", provider.get("contentType"))
		.body(bodyDeleteBookNode)
		.when().delete(provider.get("resourceDeleteBook"))
		.then().log().all().assertThat().statusCode(200);
	}

}
