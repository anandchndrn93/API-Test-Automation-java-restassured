package com.restful.booker.api.helper;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.simple.JSONObject;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;

public class Listners implements ITestListener {
	ExtentReports extent = ExtentReport.getReport();
	ExtentTest test;
	public static ThreadLocal<ExtentTest> thread = new ThreadLocal<ExtentTest>();
	private static Logger log = LogManager.getLogger(Listners.class.getName());

	@Override
	public void onTestStart(ITestResult result) {
		// TODO Auto-generated method stub
		// on start of test start extent reporting
		String testCaseName = getTestName(result);
		test = extent.createTest(testCaseName);
		thread.set(test);
		log.info("starting Test Case: " + result.getMethod().getMethodName());
		getReporter().log(Status.INFO, "starting Test Case: " + result.getMethod().getMethodName());
		ITestListener.super.onTestStart(result);
	}

	@Override
	public void onTestSuccess(ITestResult result) {
		// TODO Auto-generated method stub
		getReporter().log(Status.PASS, "Test Passed");
		log.debug("Test Passed");
		ITestListener.super.onTestSuccess(result);
	}

	@Override
	public void onTestFailure(ITestResult result) {
		// TODO Auto-generated method stub
		log.fatal(result.getThrowable());
		result.getThrowable().printStackTrace();
		getReporter().log(Status.FAIL, "Test Failed Abruptly");
		String methodName = result.getMethod().getMethodName();
		log.fatal("Test Failed Abruptly. Test name: " + methodName);
		ITestListener.super.onTestFailure(result);
	}

	@Override
	public void onTestSkipped(ITestResult result) {
		// TODO Auto-generated method stub
		String testCaseName = result.getMethod().getMethodName();
		getReporter().log(Status.SKIP, "Test was Skipped");
		getReporter().skip(result.getThrowable());
		log.fatal("Test was skipped!. Test name: " + testCaseName);
		ITestListener.super.onTestSkipped(result);
	}

	@Override
	public void onFinish(ITestContext context) {
		// TODO Auto-generated method stub
		extent.flush();
		ITestListener.super.onFinish(context);
	}

	public static ExtentTest getReporter() {
		return thread.get();
	}

	private String getTestName(ITestResult result) {
		Object param = "";
		String testCaseName;

		if (result.getParameters().length != 0) {
			for (Object parameter : result.getParameters()) {
				param = parameter;
			}
			String test = ((JSONObject) param).get("testname").toString();
			testCaseName = result.getMethod().getMethodName() + "-" + test;
		} else
			testCaseName = result.getMethod().getMethodName();

		return testCaseName;
	}

}
