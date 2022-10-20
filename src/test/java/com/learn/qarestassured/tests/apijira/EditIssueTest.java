package com.learn.qarestassured.tests.apijira;

import static io.restassured.RestAssured.given;

import java.util.Map;

import org.testng.ISuite;
import org.testng.ITestContext;
import org.testng.annotations.Test;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.learn.qarestassured.testframework.utilities.DataProviders;
import com.learn.qarestassured.testframework.utilities.Utilities;

import io.restassured.RestAssured;
import io.restassured.filter.session.SessionFilter;
import io.restassured.response.Response;

public class EditIssueTest {

	public static String dataProviderFilePath = "src/test/resources/providerFiles/jira-api-data.csv";

	@Test(dataProvider = "csvReaderMethod", dataProviderClass = DataProviders.class, groups = { "smoke" })
	public void editIssue(ITestContext context, Map<String, String> provider) {

		// parameters shared by other test in the same test
		SessionFilter session = (SessionFilter)context.getAttribute("session");
		String key = (String)context.getAttribute("key");

		// parse Json body and change some fields
		ObjectNode editBodyNode = Utilities.rawToJsonObject(provider.get("body"));
		
		RestAssured.baseURI = provider.get("baseUri");

		// edit issue, passing session object
		String editResponse1 = given().log().all()
				.pathParam("key", key)
				.header("Content-Type", provider.get("contentType"))
				.body(editBodyNode)
				.filter(session) // passing the session as a result of login operation
				.when().put(provider.get("resourceToTest"))
				.then().log().all().assertThat().statusCode(204)
				.extract().response().asString();
		
//		System.out.println("summary" + editBodyNode.with("update").withArray("summary").get(0).with("summary"), null, null).toPrettyString());
		
		// edit issue, passing id value
		String editResponse2 = given().log().all()
				.pathParam("key", key)
				.header("Content-Type", provider.get("contentType"))
				.header("Cookie", "JSESSIONID=" + session.getSessionId())
				.body(editBodyNode)
				.when().put(provider.get("resourceToTest"))
				.then().log().all().assertThat().statusCode(204)
				.extract().response().asString();

	}
}
