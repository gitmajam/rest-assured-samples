package com.learn.qarestassure.tests;

import static io.restassured.RestAssured.given;

import java.util.Map;

import org.testng.annotations.Test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.learn.qarestassured.testframework.utilities.DataProviders;
import com.learn.qarestassured.testframework.utilities.Utilities;

import io.restassured.RestAssured;

public class DynamicJson {

	public static String dataProviderFilePath = "src/test/resources/providerFiles/dynamic-json-data.csv";

	@Test(dataProvider = "csvReaderMethod", dataProviderClass = DataProviders.class, groups = { "smoke" })
	public void addBook(Map<String, String> provider) throws JsonMappingException, JsonProcessingException {
		
		// Creating an instance of ObjectMapper class
		ObjectMapper objectMapper = new ObjectMapper();
		
		// Get ObjectNode representation of json as json is an Object
		ObjectNode objectNode = objectMapper.readValue(provider.get("body"), ObjectNode.class);
		objectNode.put("name", provider.get("name"));
		objectNode.put("isbn", provider.get("isbn"));
		objectNode.put("aisle", provider.get("aisle"));
//		ObjectNode arrayNode = (ObjectNode) objectNode.withArray("nested").get(2);
//		arrayNode.put("color", provider.get("color"));

		RestAssured.baseURI = provider.get("baseUri");
		String response = given().log().all().header("Content-Type", provider.get("contentType"))
			.body(objectNode)
			.when().log().all().post(provider.get("resource")).then().assertThat().log().all()
			.statusCode(Integer.parseInt(provider.get("statusCode")))
			.extract().response().asString();
		
		String msg = Utilities.rawToJson(response).get("Msg") == null ? Utilities.rawToJson(response).get("msg"): Utilities.rawToJson(response).get("Msg");
		System.out.println(msg);
	}

}
