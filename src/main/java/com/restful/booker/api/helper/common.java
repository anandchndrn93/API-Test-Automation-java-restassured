package com.restful.booker.api.helper;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class common {

	public static JSONArray readjson(String path) {
		JSONParser jsonParser = new JSONParser();
		JSONArray userList = null;
		try (FileReader reader = new FileReader(path)) {
			// Read JSON file
			Object obj = jsonParser.parse(reader);

			userList = (JSONArray) obj;

			// Iterate over employee array

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		}

		return userList;
	}
}
