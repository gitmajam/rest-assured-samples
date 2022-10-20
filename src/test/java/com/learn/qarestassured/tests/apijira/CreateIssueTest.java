package com.learn.qarestassured.tests.apijira;

import static io.restassured.RestAssured.given;

import java.util.Map;

import org.testng.ITestContext;
import org.testng.annotations.Test;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.learn.qarestassured.testframework.utilities.DataProviders;
import com.learn.qarestassured.testframework.utilities.Utilities;

import io.restassured.RestAssured;
import io.restassured.filter.session.SessionFilter;

public class CreateIssueTest {

	public static String dataProviderFilePath = "src/test/resources/providerFiles/jira-api-data.csv";

	@Test(dataProvider = "csvReaderMethod", dataProviderClass = DataProviders.class, groups = { "smoke" })
	public void createIssue(ITestContext context, Map<String, String> provider) {

		// SessionFilter object that will store the session id
		SessionFilter session = new SessionFilter();

		// parse Json body and change some fields
		ObjectNode loginBodyNode = Utilities.rawToJsonObject(provider.get("loginBody"));
		loginBodyNode.put("username", provider.get("username"));
		loginBodyNode.put("password", provider.get("password"));

		RestAssured.baseURI = provider.get("baseUri");

		// login
		String loginResponse = given().log().all()
				.header("Content-Type", provider.get("contentType"))
				.body(loginBodyNode)
				.filter(session)
				.when().post(provider.get("resourceLogin"))
				.then().log().all().assertThat().statusCode(200)
				.extract().response().asString();

		// parse Json body and change some fields
		ObjectNode createBodyNode = Utilities.rawToJsonObject(provider.get("body"));
		createBodyNode.with("fields").put("summary", provider.get("summary"));
		createBodyNode.with("fields").put("description", provider.get("description"));

		// create issue
		String createResponse = given().log().all()
				.header("Content-Type", provider.get("contentType"))
				.body(createBodyNode)
				.filter(session) // passing the session as a result of login operation
				.when().post(provider.get("resourceToTest"))
				.then().log().all().assertThat().statusCode(201)
				.extract().response().asString();
		
		// sharing the parameters with other test in the same test
		context.setAttribute("session", session);
		context.setAttribute("key", Utilities.rawToJsonObject(createResponse).get("key").textValue());
	}
}
