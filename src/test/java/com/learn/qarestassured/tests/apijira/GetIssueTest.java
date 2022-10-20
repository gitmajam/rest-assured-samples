package com.learn.qarestassured.tests.apijira;

import static io.restassured.RestAssured.given;

import java.io.File;
import java.util.Map;

import org.testng.Assert;
import org.testng.ITestContext;
import org.testng.annotations.Test;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.learn.qarestassured.testframework.utilities.DataProviders;
import com.learn.qarestassured.testframework.utilities.Utilities;

import io.restassured.RestAssured;
import io.restassured.filter.session.SessionFilter;

public class GetIssueTest {

	public static String dataProviderFilePath = "src/test/resources/providerFiles/jira-api-data.csv";

	@Test(dataProvider = "csvReaderMethod", dataProviderClass = DataProviders.class, groups = { "smoke" })
	public void getIssue(ITestContext context, Map<String, String> provider) {

		// parameters shared by other test in the same test
		SessionFilter session = (SessionFilter)context.getAttribute("session");
		String key = (String)context.getAttribute("key");
		
		RestAssured.baseURI = provider.get("baseUri");

		// get issue, passing session object
		String getResponse = given().log().all()
				.pathParam("key", key)
				.queryParam("fields", "summary")
				.queryParam("fields", "description")
				.filter(session) // passing the session as a result of login operation
				.when().get(provider.get("resourceToTest"))
				.then().log().all().assertThat().statusCode(200)
				.extract().response().asString();
		
		String description = Utilities.rawToJsonObject(getResponse).with("fields").get("description").textValue();
		
		Assert.assertEquals(description, provider.get("description"), "description value doesn't match");


	}
}
