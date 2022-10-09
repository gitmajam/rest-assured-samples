package com.learn.qarestassured.testframework.utilities;

import io.restassured.path.json.JsonPath;

public class Utilities {

		public static JsonPath rawToJson(String response) {
			JsonPath js1 = new JsonPath(response);
			return js1;
		}
}
