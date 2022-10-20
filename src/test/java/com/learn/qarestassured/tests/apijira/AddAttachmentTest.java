package com.learn.qarestassured.tests.apijira;

import static io.restassured.RestAssured.given;

import java.io.File;
import java.util.Map;

import org.testng.ITestContext;
import org.testng.annotations.Test;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.learn.qarestassured.testframework.utilities.DataProviders;
import com.learn.qarestassured.testframework.utilities.Utilities;

import io.restassured.RestAssured;
import io.restassured.filter.session.SessionFilter;

public class AddAttachmentTest {

	public static String dataProviderFilePath = "src/test/resources/providerFiles/jira-api-data.csv";

	@Test(dataProvider = "csvReaderMethod", dataProviderClass = DataProviders.class, groups = { "smoke" })
	public void addAttachment(ITestContext context, Map<String, String> provider) {

		// parameters shared by other test in the same test
		SessionFilter session = (SessionFilter)context.getAttribute("session");
		String key = (String)context.getAttribute("key");
		
		String path = System.getProperty("user.dir") + File.separator + "src" + File.separator + "test"
				+ File.separator + "resources" + File.separator + "media" + File.separator+ "pdf" + File.separator
				+ "certs.pdf";
		
		RestAssured.baseURI = provider.get("baseUri");

		// add attachment, passing session object
		String editResponse1 = given().log().all()
				.pathParam("key", key)
				.header("X-Atlassian-Token","no-check")
				.header("Content-Type", provider.get("contentType"))
				.multiPart("file", new File(path))
				.filter(session) // passing the session as a result of login operation
				.when().post(provider.get("resourceToTest"))
				.then().log().all().assertThat().statusCode(200)
				.extract().response().asString();


	}
}
