package com.rest;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Restful Calls
 * <pre>
 *     IMPORTANT: For the best results, provide both last and first name,
 *     because if you provide only one of the two, search will stop as first match is found. Therefore, the
 *     1st of Johns and the first of Smiths will be returned.
 * </pre>
 */
@Path("students")
public final class RestfulCaller {

	private static final String FN_START = "<br>";
	private static final String FN_END = "</br>\n";
	private static final String LN_START = "<br>";
	private static final String LN_END = "</br>\n";
	private static final String GPA_START = "<br>";
	private static final String GPA_END = "</br>\n";
	private static final String LIST_START = "<li>";
	private static final String LIST_END = "</li>\n";
	private static final String EMAIL_START = "<br>";
	private static final String EMAIL_END = "</br>\n";
	private static final String HTML_START = "<!DOCTYPE html>\n"
											 + "<html lang=\"en\">\n"
											 + "<head>\n"
											 + "</head>\n"
											 + "<body>\n";
	private static final String HTML_END = "</body>\n"
										   + "</html>";

	/**
	 * Search Point returning:
	 * First Name
	 * Last Name
	 * GPA
	 *
	 * @return JSON object with listed information
	 */
	@GET
	@Path("/summary/json")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getStudentSummaryJSON(@QueryParam("lastName") String lastName, @QueryParam("firstName") String firstName) {
		final JSONObject studentSummary = getStudentSummaryInformation(firstName, lastName);
		studentSummary.remove("classesAndGrades");
		final String result = studentSummary.toString();

		return !result.equals("{}") ? Response.status(200).entity(result).build() : Response.status(204).build();
	}

	/**
	 * Search Point returning:
	 * First Name
	 * Last Name
	 * GPA
	 *
	 * @return XML object with listed information
	 */
	@GET
	@Path("/summary/xml")
	@Produces(MediaType.APPLICATION_XML)
	public Response getStudentSummaryXML(@QueryParam("lastName") String lastName, @QueryParam("firstName") String firstName) {
		final JSONObject studentSummary = getStudentSummaryInformation(firstName, lastName);
		studentSummary.remove("classesAndGrades");
		final String result = studentSummary.toString();
		final StringBuilder respXml = new StringBuilder(HTML_START);
		if (!result.equals("{}")) {
			respXml.append(FN_START).append("firstName: ").append(studentSummary.getString("firstName")).append(FN_END);
			respXml.append(LN_START).append("lastName :").append(studentSummary.getString("lastName")).append(LN_END);
			respXml.append(GPA_START).append("gpa :").append(studentSummary.getString("gpa")).append(GPA_END);
			respXml.append(EMAIL_START).append("email :").append(studentSummary.getString("email")).append(EMAIL_END);
			respXml.append(HTML_END);
		}

		return !result.equals("{}") ? Response.status(200).entity(respXml.toString()).build() : Response.status(204).build();
	}

	/**
	 * Search Point returning:
	 * First Name
	 * Last Name
	 * email address
	 * GPA
	 * List of classes with grades
	 *
	 * @return JSON object with listed information.
	 */
	@GET
	@Path("/detail/json")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getStudentDetailsJSON(@QueryParam("lastName") String lastName, @QueryParam("firstName") String firstName) {
		final JSONObject studentSummary = getStudentSummaryInformation(firstName, lastName);
		final String result = studentSummary.toString();

		return !result.equals("{}") ? Response.status(200).entity(result).build() : Response.status(204).build();
	}

	/**
	 * Search Point returning:
	 * First Name
	 * Last Name
	 * email address
	 * GPA
	 * List of classes with grades
	 *
	 * @return XML object with listed information.
	 */
	@GET
	@Path("/detail/xml")
	@Produces(MediaType.APPLICATION_XML)
	public Response getStudentDetailsXML(@QueryParam("lastName") String lastName, @QueryParam("firstName") String firstName) throws IOException {
		final JSONObject studentSummary = getStudentSummaryInformation(firstName, lastName);
		final String result = studentSummary.toString();
		final StringBuilder respXml = new StringBuilder(HTML_START);
		if (!result.equals("{}")) {
			final Map<String, String> courseAndGradeMap = convertJSONToMap(studentSummary.getJSONObject("classesAndGrades"));

			respXml.append(FN_START).append("firstName: ").append(studentSummary.getString("firstName")).append(FN_END);
			respXml.append(LN_START).append("lastName: ").append(studentSummary.getString("lastName")).append(LN_END);
			respXml.append(GPA_START).append("gpa : ").append(studentSummary.getString("gpa")).append(GPA_END);
			respXml.append(EMAIL_START).append("email :").append(studentSummary.getString("email")).append(EMAIL_END);
			for (Map.Entry<String, String> entry : courseAndGradeMap.entrySet()) {
				respXml.append(LIST_START).append("Class and grade: ").append(entry.getKey()).append(":").append(entry.getValue()).append(LIST_END);
			}
			respXml.append(HTML_END);
		}
		return !result.equals("{}") ? Response.status(200).entity(respXml.toString()).build() : Response.status(204).build();
	}

	private JSONObject getStudentSummaryInformation(final String firstName, final String lastName) {
		final JSONObject info = getJSONResources();

		return findStudent(info, firstName, lastName);
	}

	private JSONObject getJSONResources() throws JSONException {
		final InputStream input = getClass().getClassLoader().getResourceAsStream("json/student_data.json");
		final String fileSource = convertStreamToString(input);
		return new JSONObject(fileSource);
	}

	private String convertStreamToString(final java.io.InputStream is) {
		return new BufferedReader(new InputStreamReader(is)).lines().collect(Collectors.joining("\n"));
	}

	private JSONObject findStudent(final JSONObject data, final String firstName, final String lastName) {
		final JSONArray students = data.getJSONArray("students");
		final JSONObject summary = new JSONObject();
		final JSONObject classes = data.getJSONObject("classes");
		final Object curriculum;
		for (Object info : students) {
			//Case 1: We have both first and last name.
			if (StringUtils.isNotEmpty(firstName) && StringUtils.isNotEmpty(lastName)) {
				if (((JSONObject) info).getString("first").equals(firstName)) {
					if (((JSONObject) info).getString("last").equals(lastName)) {
						summary.put("firstName", firstName);
						summary.put("lastName", lastName);
						summary.put("email", ((JSONObject) info).getString("email"));
						curriculum = ((JSONObject) info).get("studentClasses");
						summary.put("gpa", calculateGPA(curriculum));
						summary.put("classesAndGrades", getClassesAndGrades(classes, curriculum));
						break;
					}
				}
			}
			//Case 2: We have first name, but no last name.
			if (StringUtils.isNotEmpty(firstName) && StringUtils.isEmpty(lastName)) {
				if (((JSONObject) info).getString("first").equals(firstName)) {
					summary.put("firstName", firstName);
					summary.put("lastName", ((JSONObject) info).getString("first"));
					summary.put("email", ((JSONObject) info).getString("email"));
					curriculum = ((JSONObject) info).get("studentClasses");
					summary.put("gpa", calculateGPA(curriculum));
					summary.put("classesAndGrades", getClassesAndGrades(classes, curriculum));
					break;
				}
			}

			//Case 3: We have last name, but no first name.
			if (StringUtils.isNotEmpty(lastName) && StringUtils.isEmpty(lastName)) {
				if (((JSONObject) info).getString("first").equals(firstName)) {
					summary.put("firstName", ((JSONObject) info).getString("first"));
					summary.put("lastName", lastName);
					summary.put("email", ((JSONObject) info).getString("email"));
					curriculum = ((JSONObject) info).get("studentClasses");
					summary.put("gpa", calculateGPA(curriculum));
					summary.put("classesAndGrades", getClassesAndGrades(classes, curriculum));
					break;
				}
			}
		}
		return summary;
	}

	private String calculateGPA(final Object curriculum) {
		JSONArray curriculumInfo = (JSONArray) curriculum;
		double gpa = 0;
		int numberOfClasses = 1;
		for (Object clax : curriculumInfo) {
			int grade = ((JSONObject) clax).getInt("grade");
			gpa += grade;
			numberOfClasses++;
		}
		gpa = gpa / numberOfClasses;
		final DecimalFormat df = new DecimalFormat("#.00", DecimalFormatSymbols.getInstance(Locale.US));

		return df.format(gpa);
	}

	private JSONObject getClassesAndGrades(final JSONObject classes, final Object curriculum) {
		final JSONObject classAndGrade = new JSONObject();
		JSONArray curriculumInfo = (JSONArray) curriculum;
		for (Object clax : curriculumInfo) {
			int grade = ((JSONObject) clax).getInt("grade");
			int id = ((JSONObject) clax).getInt("id");
			switch ("" + id) {
				case "1":
					classAndGrade.put(classes.getString("1"), "" + grade);
					break;
				case "2":
					classAndGrade.put(classes.getString("2"), "" + grade);
					break;
				case "3":
					classAndGrade.put(classes.getString("3"), "" + grade);
					break;
				case "4":
					classAndGrade.put(classes.getString("4"), "" + grade);
					break;
				case "5":
					classAndGrade.put(classes.getString("5"), "" + grade);
					break;
				case "6":
					classAndGrade.put(classes.getString("6"), "" + grade);
					break;
				case "7":
					classAndGrade.put(classes.getString("7"), "" + grade);
					break;
				default:
					break;
			}
		}
		return classAndGrade;
	}

	private Map<String, String> convertJSONToMap(final JSONObject json) throws IOException {
		final ObjectMapper mapper = new ObjectMapper();
		// convert JSON string to Map
		return mapper.readValue(json.toString(), new TypeReference<Map<String, String>>() {});
	}
}
