package com.restful.booker.tests;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.aventstack.extentreports.Status;
import com.restful.booker.api.helper.Listners;
import com.restful.booker.modules.Modules;

import io.restassured.response.Response;

public class GetBookingTest {
	Modules mod;
	private static Logger log = LogManager.getLogger(GetBookingTest.class.getName());

	@BeforeClass(alwaysRun = true)
	public void testSetup() {
		mod = new Modules();
	}

	@Test(groups = { "regression", "sanity" })
	public void getBooKingIdsTest() {
		// checking getBooKingIds API response
		Listners.getReporter().log(Status.INFO, " checking getBooKingIds API response");
		log.info(" checking getBooKingIds API response");
		Response res = mod.getBookingIds();
		Assert.assertEquals(res.getStatusCode(), 200, " The API status code does not match expected");
		Listners.getReporter().log(Status.PASS, "API returned correct status code" + res.getStatusCode());
		log.debug("API returned correct status code" + res.getStatusCode());
	}

	@Test(groups = { "regression", "sanity" })
	public void getBooKingTest() {
		// checking getbooking api response
		Listners.getReporter().log(Status.INFO, " checking getBooKing API response");
		log.info(" checking getBooKing API response");
		Response res = mod.getBooking(3);
		try {
			Assert.assertEquals(res.getStatusCode(), 200, " The API status code does not match expected");
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

		try {
			Assert.assertEquals(res.path("firstname").getClass(), String.class);
			log.debug("first name is String");
			Listners.getReporter().log(Status.PASS, "first name is String");
		} catch (AssertionError AE) {
			log.error("first name is not String");
			Listners.getReporter().log(Status.FAIL, "first name is not String");
		}

		try {
			Assert.assertEquals(res.path("lastname").getClass(), String.class);
			log.debug("lastname name is String");
			Listners.getReporter().log(Status.PASS, "lastname name is String");
		} catch (AssertionError AE) {
			log.error("lastname name is not String");
			Listners.getReporter().log(Status.FAIL, "lastname name is not String");
		}

		try {
			Assert.assertEquals(res.path("totalprice").getClass(), Integer.class);
			log.debug("totalprice is Integer");
			Listners.getReporter().log(Status.PASS, "totalprice is Integer");
		} catch (AssertionError AE) {
			Listners.getReporter().log(Status.FAIL, "totalprice is not Integer");
			log.error("totalprice is not Integer");
		}

		try {
			Assert.assertEquals(res.path("depositpaid").getClass(), Boolean.class);
			log.debug("depositpaid is boolean");
			Listners.getReporter().log(Status.PASS, "depositpaid is boolean");
		} catch (AssertionError AE) {
			log.error("depositpaid is not Boolean");
			Listners.getReporter().log(Status.FAIL, "depositpaid is not Boolean");
		}

		try {
			Assert.assertEquals(res.path("bookingdates.checkin").getClass(), String.class);
			log.debug("checkin date is string");
			Listners.getReporter().log(Status.PASS, "checkin date is string");
		} catch (AssertionError AE) {
			log.error("checkin date is not String");
			Listners.getReporter().log(Status.FAIL, "checkin date is not String");
		}

		try {
			Assert.assertEquals(res.path("bookingdates.checkout").getClass(), String.class);
			log.debug("checkout date is string");
			Listners.getReporter().log(Status.PASS, "checkout date is string");
		} catch (AssertionError AE) {
			log.error("checkout date is not String");
			Listners.getReporter().log(Status.FAIL, "checkout date is not String");
		}

		try {
			Assert.assertEquals(res.path("additionalneeds").getClass(), String.class);
			log.debug("additionalneeds is string");
			Listners.getReporter().log(Status.PASS, "additionalneeds is string");
		} catch (AssertionError AE) {
			log.error("additionalneeds is not String");
			Listners.getReporter().log(Status.FAIL, "additionalneeds is not String");
		}
	}

	@Test(groups = { "regression" })
	public void getBooKingInvalidIDTest() {
		// checking getbooking api response on passing invalid id
		Listners.getReporter().log(Status.INFO, " checking getBooKing API respnse");
		log.info(" checking getBooKing API response");
		Response res = mod.getBooking(100);
		Assert.assertEquals(res.getStatusCode(), 404, " The API status code does not match expected");
		Listners.getReporter().log(Status.PASS, "API returned correct status code" + res.getStatusCode());
		log.debug("API returned correct status code" + res.getStatusCode());
	}
}
