# Restful-API Implementation
This is an example of Restful API using JAX_RS and Jersey. In order to run this code you will need the following:

<li> <a https://www.jetbrains.com/idea/download/#section=mac>IntelliJ IDEA</a>full edition.</li>
<li> Tomcat installed before running local version and/or unit test.</li>

# Configuring Tomcat:

There are a plenty guides on the web relating to this issue. here are sample screenshots of values that I have used in this project:

<img src="..art/raw/Tomcat Config .png" alt="Tomcat">
<br>
<img src="a..rt/raw/War Config .png" alt="WAR">

# API Calls:

After running on localhost:8080, the following calls can be executed:

http://localhost:8080/restful_war_exploded/rest/students/summary/json?firstName=John&lastName=Smith
http://localhost:8080/restful_war_exploded/rest/students/summary/xml?firstName=John&lastName=Smith
http://localhost:8080/restful_war_exploded/rest/students/detail/xml?firstName=John&lastName=Smith
http://localhost:8080/restful_war_exploded/rest/students/detail/xml?firstName=John&lastName=Smith

Sample JSON file sits in the resources folder and providing both first and last name in the search will assure for the greatest accuracy.
If only one input parameter is being provided, the first record with that information will be displayed.

Sample response:
<br>
JSON:
```
{
"firstName": "John",
"lastName": "Smith",
"gpa": "2.57",
"email": "johnsmith@mailinator.com"
}
```
XML:
```
<html lang="en">
<head> </head>
<body>
<br>firstName: John</br>
<br>lastName :Smith</br>
<br>gpa :2.57</br>
<br>email :johnsmith@mailinator.com</br>
</body>
</html>
```


