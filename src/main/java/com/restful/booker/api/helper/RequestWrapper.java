package com.restful.booker.api.helper;

import static com.restful.booker.api.helper.MyConfig.getBundle;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.github.dzieciou.testing.curl.CurlLoggingRestAssuredConfigFactory;
import com.github.dzieciou.testing.curl.Options;
import com.restful.booker.api.endpoints.Endpoints;

import io.restassured.RestAssured;
import io.restassured.config.RestAssuredConfig;
import io.restassured.response.Response;

public class RequestWrapper {
	public static Logger log = LogManager.getLogger(RequestWrapper.class.getName());
	String cookie = null;
	private static RequestWrapper instance = new RequestWrapper();
	RestAssuredConfig config;

	public static String getCallerBaseUri() {
		String baseURI = getBundle().get("baseurl");
		return baseURI;
	}

	private RequestWrapper() {
		cookie = getToken();
		RestAssured.baseURI = getCallerBaseUri();
		config = CurlLoggingRestAssuredConfigFactory.createConfig(optionBuilder());
	}

	public static RequestWrapper getInstance() {
		return instance;
	}

	String resolveKeys(String key) {
		String patternString1 = "(%%)(.+?)(%%)";
		Pattern pattern = Pattern.compile(patternString1);
		Matcher matcher = pattern.matcher(key);

		while (matcher.find()) {
			String foundString = matcher.group(1) + matcher.group(2) + matcher.group(3);

			String replaceWith = getBundle().getOrDefault(matcher.group(2), foundString);
			key = key.replaceAll(foundString, Matcher.quoteReplacement(replaceWith));
		}
		return key;
	}

	public String getToken() {
		log.info("Login in as admin");
		RestAssured.baseURI = getBundle().get("baseurl");
		getBundle().put("booking.login.user", getBundle().get("booking.auth.username"));
		getBundle().put("booking.login.password", getBundle().get("booking.auth.password"));
		Response res = postRequest(Endpoints.AUTH, null, getBundle().get("payload.booking.login"));
		String cookies = "token=" + res.path("token");
		return cookies;
	}

	public Response getRequest(String endpoint, Map<String, String> headers) {
		endpoint = resolveKeys(endpoint);
		String URL = getCallerBaseUri() + endpoint;
		log.debug("Performing GET on : " + URL);
		if (headers == null)
			headers = new HashMap<String, String>();
		headers.put("Content-type", "application/json");
		headers.put("Accept", "application/json");
		if (cookie != null)
			headers.put("Cookie", cookie);
		Response resp = RestAssured.given().config(config).cookie(cookie).headers(headers).when().log().all(false)
				.get(endpoint);
		log.debug("response: " + resp.asString());
		return resp;
	}

	public Response getWithFormDataRequest(String endpoint, Map<String, String> headers,
			Map<String, String> formParams) {
		endpoint = resolveKeys(endpoint);
		String URL = getCallerBaseUri() + endpoint;
		log.info("Performing GET on : " + URL);
		if (headers == null)
			headers = new HashMap<String, String>();
		headers.put("content-type", "application/json");
		headers.put("Accept", "application/json");
		if (cookie != null)
			headers.put("Cookie", cookie);
		Response resp = RestAssured.given().config(config).cookie(cookie).headers(headers)
				.config(io.restassured.RestAssured.config()
						.encoderConfig(io.restassured.config.EncoderConfig.encoderConfig()
								.appendDefaultContentCharsetToContentTypeIfUndefined(false)))
				.queryParams(formParams).when().log().all(false).get(endpoint);
		log.debug("response : " + resp.asString());
		return resp;

	}

	public Response postRequest(String endpoint, Map<String, String> headers, String payload) {
		endpoint = resolveKeys(endpoint);
		String URL = getCallerBaseUri() + endpoint;
		log.info("Performing POST on : " + URL);
		if (headers == null)
			headers = new HashMap<String, String>();
		headers.put("Accept", "application/json");
		headers.put("content-type", "application/json");
		if (cookie != null)
			headers.put("Cookie", cookie);
		payload = resolveKeys(payload);
		log.debug("request : " + payload);
		Response resp = RestAssured.given().config(config).contentType("ContentType.JSON").headers(headers)
				.body(payload).when().log().all(false).post(endpoint);
		log.debug("Response : " + resp.asString());
		return resp;
	}

	public Response putRequest(String endpoint, Map<String, String> headers, String payload) {
		endpoint = resolveKeys(endpoint);
		String URL = getCallerBaseUri() + endpoint;
		log.info("Performing PUT on : " + URL);
		if (headers == null)
			headers = new HashMap<String, String>();
		headers.put("Accept", "application/json");
		headers.put("content-type", "application/json");
		if (cookie != null)
			headers.put("Cookie", cookie);
		payload = resolveKeys(payload);
		log.debug("Payload : " + payload);
		Response resp = RestAssured.given().config(config).config(config).headers(headers).body(payload).when().log()
				.all(false).put(endpoint);
		log.debug("Response : " + resp.asString());
		return resp;
	}

	public Response deleteRequest(String endpoint, Map<String, String> headers) {
		endpoint = resolveKeys(endpoint);
		String URL = getCallerBaseUri() + endpoint;
		log.info("Performing Delete on : " + URL);
		if (headers == null)
			headers = new HashMap<String, String>();
		headers.put("content-type", "application/json");
		if (cookie != null)
			headers.put("Cookie", cookie);
		Response resp = RestAssured.given().config(config).headers(headers).when().log().all(false).delete(endpoint);
		log.debug("Response : " + resp.asString());
		return resp;
	}

	public Response patchRequest(String endpoint, Map<String, String> headers, String payload) {
		endpoint = resolveKeys(endpoint);
		String URL = getCallerBaseUri() + endpoint;
		log.info("Performing PUT on : " + URL);
		if (headers == null)
			headers = new HashMap<String, String>();
		headers.put("Accept", "application/json");
		headers.put("content-type", "application/json");
		if (cookie != null)
			headers.put("Cookie", cookie);
		payload = resolveKeys(payload);
		log.debug("Payload : " + payload);
		Response resp = RestAssured.given().config(config).headers(headers).body(payload).when().log().all(false)
				.patch(endpoint);
		log.debug("Response : " + resp.asString());
		return resp;
	}

	public Options optionBuilder() {
		Options options = Options.builder().printMultiliner()
				.updateCurl(curl -> curl.removeHeader("Host").removeHeader("User-Agent").removeHeader("Connection"))
				.build();
		return options;
	}
}
