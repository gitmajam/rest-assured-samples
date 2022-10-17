package com.learn.qarestassured.testframework.utilities;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import io.restassured.path.json.JsonPath;

public class Utilities {

		public static JsonPath rawToJson(String response) {
			JsonPath js1 = new JsonPath(response);
			return js1;
		}
		
		public static ObjectNode rawToJsonObject(String response ) {
			
			// Creating an instance of ObjectMapper class
			ObjectMapper objectMapper = new ObjectMapper();
			
			// Get ObjectNode representation of json as json is an Object
			ObjectNode objectNode = null;
			
			try {
				objectNode = objectMapper.readValue(response, ObjectNode.class);
			} catch (JsonMappingException em) {
				em.printStackTrace();
			}catch (JsonProcessingException ep) {
				ep.printStackTrace();
			}
			
			return objectNode;
		}
		
//		ObjectNode arrayNode = (ObjectNode) objectNode.withArray("nested").get(2);
//		arrayNode.put("color", provider.get("color"));
}
