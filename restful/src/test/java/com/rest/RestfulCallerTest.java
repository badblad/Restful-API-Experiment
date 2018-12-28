package com.rest;

import com.jayway.restassured.RestAssured;
import static com.jayway.restassured.RestAssured.expect;
import groovyx.net.http.ContentType;
import org.junit.Before;
import org.junit.Test;

public class RestfulCallerTest {

	@Before
	public void setUp() {
		RestAssured.basePath = "http://localhost:8080";
	}

	@Test
	public void getStudentSummaryJSON() {
		expect().statusCode(200).contentType(ContentType.JSON).when()
				.get("http://localhost:8080/restful_war_exploded/rest/students/summary/json?firstName=John&lastName=Smith");
	}

	@Test
	public void getInvalidStudentSummaryJSON() {
		expect().statusCode(204).contentType(ContentType.JSON).when()
				.get("http://localhost:8080/restful_war_exploded/rest/students/summary/json?firstName=Lilo&lastName=Johnes");
	}

	@Test
	public void getStudentDetailJSON() {
		expect().statusCode(200).contentType(ContentType.JSON).when()
				.get("http://localhost:8080/restful_war_exploded/rest/students/detail/json?firstName=John&lastName=Smith");
	}

	@Test
	public void getInvalidStudentDetailJSON() {
		expect().statusCode(204).contentType(ContentType.JSON).when()
				.get("http://localhost:8080/restful_war_exploded/rest/students/detail/json?firstName=Lilo&lastName=Johnes");
	}

	@Test
	public void getStudentSummaryXML() {
		expect().statusCode(200).contentType(ContentType.XML).when()
				.get("http://localhost:8080/restful_war_exploded/rest/students/summary/xml?firstName=John&lastName=Smith");
	}

	@Test
	public void getInvalidStudentSummaryXML() {
		expect().statusCode(204).contentType(ContentType.XML).when()
				.get("http://localhost:8080/restful_war_exploded/rest/students/summary/xml?firstName=Lilo&lastName=Johnes");
	}

	@Test
	public void getStudentDetailXML() {
		expect().statusCode(200).contentType(ContentType.XML).when()
				.get("http://localhost:8080/restful_war_exploded/rest/students/detail/xml?firstName=John&lastName=Smith");
	}

	@Test
	public void getInvalidStudentDetailXML() {
		expect().statusCode(204).contentType(ContentType.XML).when()
				.get("http://localhost:8080/restful_war_exploded/rest/students/detail/xml?firstName=Lilo&lastName=Johnes");
	}
}