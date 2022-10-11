package com.learn.qarestassure.tests;

import java.util.List;
import java.util.Map;

import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import com.learn.qarestassured.testframework.utilities.DataProviders;
import com.learn.qarestassured.testframework.utilities.Utilities;

public class CoursesApiTest {

	public static String dataProviderFilePath = "src/test/resources/providerFiles/api-courses.csv";

	@Test(dataProvider = "csvReader", dataProviderClass = DataProviders.class, groups = { "smoke" })
	public void verifyCoursesData(Map<String, String> provider) {

		// Print No of courses returned by API
		System.out.println("No of courses: " + Utilities.rawToJson(provider.get("body")).getInt("courses.size()"));

		// Print Purchase Amount
		System.out.println(
				"Purchase amount: " + Utilities.rawToJson(provider.get("body")).getInt("dashboard.purchaseAmount"));

		// Print Title of the first course
		System.out.println("first course: " + Utilities.rawToJson(provider.get("body")).getString("courses[0].title"));

		// Print All course titles and their respective Prices
		List<Map<String, ?>> courses = Utilities.rawToJson(provider.get("body")).get("courses");
		courses.forEach(m -> System.out.println("title: " + m.get("title") + " price: " + m.get("price")));

		// Print no of copies sold by RPA Course
		List<Map> copies = Utilities.rawToJson(provider.get("body")).param("name", "RPA")
				.get("courses.findAll {courses -> courses.title == name}");
		copies.forEach(m -> System.out.println("No of copies sold by RPA Course: " + m.get("copies")));

		// Verify if Sum of all Course prices matches with Purchase Amount
		Integer purchaseAmount = Utilities.rawToJson(provider.get("body")).get("dashboard.purchaseAmount");

		List<Map> cursos = Utilities.rawToJson(provider.get("body")).get("courses");
		Integer sumPrices = cursos.stream().map(m -> (Integer) m.get("copies") * (Integer) m.get("price"))
				.reduce(0, Integer::sum);

		SoftAssert softAssert = new SoftAssert();
		softAssert.assertEquals(purchaseAmount, sumPrices);
		softAssert.assertAll();
	}

}
