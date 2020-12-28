package com.restful.booker.api.helper;

import java.io.File;
import java.io.FileReader;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class MyConfig {
	private static MyConfig myConfig = null;
	public static Map<String, String> bundle;

	private MyConfig() {
	}

	public static Map<String, String> getBundle() {
		// env = environment;
		if (myConfig == null) {
			myConfig = new MyConfig();
			bundle = readAllConfig();
		} else {
			// System.out.println("MyConfig is already instantiated!!");
		}
		return bundle;
	}

	private static Map<String, String> readAllConfig() {

		String[] allFiles = { "src/main/resources/config/", "src/main/resources/payload/" };

		Properties prop = new Properties();
		for (String filePath : allFiles) {
			// System.out.println("Source file : " + filePath);
			File file = new File(filePath);
			// get all properties file
			File[] matchingFiles = file.listFiles(new FilenameFilter() {
				public boolean accept(File dir, String name) {
					return name.endsWith(".properties");
				}
			});

			try {
				for (File matchineFile : matchingFiles) {
					System.out.println("Current file loading : " + matchineFile.getAbsolutePath());
					prop.load(new FileReader(matchineFile)); // read properties file
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		System.out.println(prop.keySet());
		return new HashMap(prop);
	}

	public static void main(String... args) {
		System.out.println(MyConfig.getBundle());
	}
}
