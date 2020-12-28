# API-Test-Automation-java-restassured
This is a sample API test automation framework using java restassured library. The APIs automated are take from https://restful-booker.herokuapp.com/apidoc/index.html. 

Methods automated:POST,PUT,PATCH,DELETE,GET

Extend reports and log4j is integrated with the framework

/restfull-booker_restassured/testng.xml file can be used to run the tests

logs can be found in folder /restfull-booker_restassured/logs

reports can be found in /restfull-booker_restassured/test-output/reports

/restfull-booker_restassured/src/main/resources/testdata/newUser.json file is the test data. This can be edited as required. Make sure to pass valid booking ID. booking ids can be found using GetBookingIDs api from https://restful-booker.herokuapp.com/apidoc/index.html. 
