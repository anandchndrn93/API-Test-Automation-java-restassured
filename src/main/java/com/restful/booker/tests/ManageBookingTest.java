package com.restful.booker.tests;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import static com.restful.booker.api.helper.MyConfig.getBundle;

import com.aventstack.extentreports.Status;
import com.restful.booker.api.helper.Listners;
import com.restful.booker.api.helper.common;
import com.restful.booker.modules.Modules;

import io.restassured.response.Response;

public class ManageBookingTest {
	Modules mod;
	private static Logger log = LogManager.getLogger(ManageBookingTest.class.getName());
	public static ThreadLocal<JSONObject[]> thread = new ThreadLocal<JSONObject[]>();

	@BeforeClass
	public void testSetup() {
		mod = new Modules();
	}

	@Test(dataProvider = "userProvider")
	public void createBookingTest(JSONObject user) {
		// creating an new booking
		Response res = mod.createBooking(user.get("firstname").toString(), user.get("lastname").toString(),
				Integer.valueOf(user.get("totalprice").toString()), Boolean.valueOf(user.get("depositpaid").toString()),
				user.get("checkin").toString(), user.get("checkout").toString(),
				user.get("additionalneeds").toString());
		Assert.assertEquals(res.getStatusCode(), 200, " The API status code does not match expected");
		Listners.getReporter().log(Status.PASS, "API returned correct status code" + res.getStatusCode());
		log.debug("API returned correct status code" + res.getStatusCode());
		int bookingID = res.path("bookingid");
		log.debug("new user created with booking id " + bookingID);
		Listners.getReporter().log(Status.PASS, "new user created with booking id " + bookingID);
	}

	@Test(dataProvider = "userProvider")
	public void updateBookingTest(JSONObject user) {
		//updating a booking
		Response res = mod.updateBooking(user.get("firstname").toString(), user.get("lastname").toString(),
				Integer.valueOf(user.get("totalprice").toString()), Boolean.valueOf(user.get("depositpaid").toString()),
				user.get("checkin").toString(), user.get("checkout").toString(), user.get("additionalneeds").toString(),
				Integer.valueOf(user.get("bookingid").toString()));
		try {
			Assert.assertEquals(res.getStatusCode(), 200, " The API status code does not match expected");
			Listners.getReporter().log(Status.PASS, "API returned correct status code" + res.getStatusCode());
			log.debug("API returned correct status code" + res.getStatusCode());
			log.debug("user with booking id " + user.get("bookingid").toString() + " was updated");
			Listners.getReporter().log(Status.PASS,
					"user with booking id " + user.get("bookingid").toString() + " was updated");
		} catch (AssertionError AE) {
			if (res.getStatusCode() == 405) {
				log.error("the entered booking id does not exist");
				Listners.getReporter().log(Status.FAIL, "the entered booking id does not exist");
				Assert.fail("the entered booking id does not exist");
			} else
				AE.printStackTrace();
		}

	}

	@Test(dataProvider = "userProvider")
	public void patchBookingTest(JSONObject user) {
		//perform partial update on a booking
		Response res = mod.patchBooking(user.get("firstname").toString(), user.get("lastname").toString(),
				Integer.valueOf(user.get("bookingid").toString()));
		try {
			Assert.assertEquals(res.getStatusCode(), 200, " The API status code does not match expected");
			Listners.getReporter().log(Status.PASS, "API returned correct status code" + res.getStatusCode());
			log.debug("API returned correct status code" + res.getStatusCode());
			log.debug("user with booking id " + user.get("bookingid").toString() + " was updated");
			Listners.getReporter().log(Status.PASS,
					"user with booking id " + user.get("bookingid").toString() + " was updated");
		} catch (AssertionError AE) {
			if (res.getStatusCode() == 405) {
				log.error("the entered booking id does not exist");
				Listners.getReporter().log(Status.FAIL, "the entered booking id does not exist");
				Assert.fail("the entered booking id does not exist");
			} else
				AE.printStackTrace();
		}
	}

	@Test(dataProvider = "userProvider", dependsOnMethods = { "patchBookingTest", "updateBookingTest" })
	public void deleteBookingTest(JSONObject user) {
		// delete a booking
		Response res = mod.deleteBooking(Integer.valueOf(user.get("bookingid").toString()));
		try {
			Assert.assertEquals(res.getStatusCode(), 201, " The API status code does not match expected");
			Listners.getReporter().log(Status.PASS, "API returned correct status code" + res.getStatusCode());
			log.debug("API returned correct status code" + res.getStatusCode());
		} catch (AssertionError AE) {
			if (res.getStatusCode() == 405) {
				log.error("the entered booking id does not exist");
				Listners.getReporter().log(Status.FAIL, "the entered booking id does not exist");
				Assert.fail("the entered booking id does not exist");
			} else
				AE.printStackTrace();
		}
	}

	@DataProvider
	public JSONObject[] userProvider() {
		JSONArray users = common.readjson(getBundle().get("booker.user.json.path"));
		JSONObject[] testObjArray = new JSONObject[users.size()];
		for (int i = 0; i < users.size(); i++) {
			JSONObject object = (JSONObject) users.get(i);
			JSONObject user = (JSONObject) object.get("user");
			testObjArray[i] = user;
		}
		thread.set(testObjArray);
		return thread.get();
	}
}
