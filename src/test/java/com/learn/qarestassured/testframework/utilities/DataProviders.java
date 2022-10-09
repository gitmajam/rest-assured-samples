package com.learn.qarestassured.testframework.utilities;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.testng.ITestContext;
import org.testng.annotations.DataProvider;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;

public class DataProviders {

	/**
	 * This data provider delivers an Iterator of Maps, it returns a whole row of
	 * data from the table in the csv file for each iteration , runs the tests in a
	 * sequential manner
	 */
	@DataProvider(name = "csvReader", parallel = false)
	public static Iterator<Object[]> csvReader(Method method) {

		String path = getProviderPath(method);
		List<Object[]> list = new ArrayList<Object[]>();

		File file = new File(path);

		try {
			CSVReader reader = new CSVReader(new FileReader(file));
			String[] keys = reader.readNext();
			if (keys != null) {
				String[] dataParts;
				while ((dataParts = reader.readNext()) != null) {
					String todo = dataParts[0];
					if (todo.contentEquals("TRUE")) {
						Map<String, String> testData = new HashMap<String, String>();
						for (int i = 0; i < keys.length; i++) {
							testData.put(keys[i], dataParts[i]);
						}
						list.add(new Object[] { testData });
					}
				}
			}
			reader.close();
		} catch (FileNotFoundException e) {
			throw new RuntimeException("File " + path + " was not found.\n" + e.getStackTrace().toString());
		} catch (IOException e) {
			throw new RuntimeException("Could not read " + path + " file.\n" + e.getStackTrace().toString());
		} catch (CsvValidationException e) {
			throw new RuntimeException(
					"Could not read next line in csv file" + path + "\n" + e.getStackTrace().toString());
		}

		return list.iterator();
	}

	/**
	 * This data provider delivers an Iterator of Maps, it returns a whole row of
	 * data from the table in the csv file for each iteration , runs all the test in
	 * parallel
	 */
	@DataProvider(name = "csvReaderParallel", parallel = true)
	public static Iterator<Object[]> csvReaderParallel(Method method) {

		String path = getProviderPath(method);
		List<Object[]> list = new ArrayList<Object[]>();

		File file = new File(path);
		try {
			CSVReader reader = new CSVReader(new FileReader(file));
			String[] keys = reader.readNext();
			if (keys != null) {
				String[] dataParts;
				while ((dataParts = reader.readNext()) != null) {
					String todo = dataParts[0];
					if (todo.contentEquals("TRUE")) {
						Map<String, String> testData = new HashMap<String, String>();
						for (int i = 0; i < keys.length; i++) {
							testData.put(keys[i], dataParts[i]);
						}
						list.add(new Object[] { testData });
					}
				}
			}
			reader.close();
		} catch (FileNotFoundException e) {
			throw new RuntimeException("File " + path + " was not found.\n" + e.getStackTrace().toString());
		} catch (IOException e) {
			throw new RuntimeException("Could not read " + path + " file.\n" + e.getStackTrace().toString());
		} catch (CsvValidationException e) {
			throw new RuntimeException(
					"Could not read next line in csv file" + path + "\n" + e.getStackTrace().toString());
		}

		return list.iterator();
	}

	/**
	 * This data provider delivers an Iterator of Maps, it returns the whole table
	 * that is in the csv file. for each iteration , runs the tests in a sequential
	 * manner
	 */
	@DataProvider(name = "csvReaderMatrix", parallel = false)
	public static Iterator<Object[]> csvReaderMatrix(Method method, ITestContext testContext) {

		String path = getProviderPath(method);
		List<Object[]> list = new ArrayList<Object[]>();
		List<Map<String, String>> dataList = new ArrayList<Map<String, String>>();

		File file = new File(path);

		try {
			CSVReader reader = new CSVReader(new FileReader(file));
			String[] keys = reader.readNext();
			if (keys != null) {
				String[] dataParts;
				while ((dataParts = reader.readNext()) != null) {
					String todo = dataParts[0];
					if (todo.contentEquals("TRUE")) {
						Map<String, String> testData = new HashMap<String, String>();
						for (int i = 0; i < keys.length; i++) {
							testData.put(keys[i], dataParts[i]);
						}
						dataList.add(testData);
					}
				}
				list.add(new Object[] { dataList });
			}
			reader.close();
		} catch (FileNotFoundException e) {
			throw new RuntimeException("File " + path + " was not found.\n" + e.getStackTrace().toString());
		} catch (IOException e) {
			throw new RuntimeException("Could not read " + path + " file.\n" + e.getStackTrace().toString());
		} catch (CsvValidationException e) {
			throw new RuntimeException(
					"Could not read next line in csv file" + path + "\n" + e.getStackTrace().toString());
		}

		return list.iterator();
	}

	private static String getProviderPath(Method method) {

		String path = null;

		// Retrieving the class field called "dataProviderFilePath" from caller class
		// by reflection.

		try {
			Field field = method.getDeclaringClass().getDeclaredField("dataProviderFilePath");
			Object testObj = method.getDeclaringClass().getDeclaredConstructor().newInstance();
			path = (String) field.get(testObj);
		} catch (Exception e1) {
			e1.printStackTrace();
		}

		return path;
	}

}
