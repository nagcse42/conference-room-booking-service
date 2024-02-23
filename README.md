# conference-booking-service
Service for conference rooms booking 

Build Process:
---------------
Clone project to folder
Open terminal in project root folder
Build project: mvn clean install <br>
Run project: mvn spring-boot:run  <br>

Tech Stack:
-----------
Java 17
Spring 3.X
H2 In Memory DB


1) Get Available conference rooms API:<br>
URL :  GET : http://localhost:8080/api/v1/conference-rooms?startTime=10:00&endTime=10:30 <br>
Header : Content-Type : application/json <br>

2) Book conference room API : <br>
 URL > POST : http://localhost:8080/api/v1/bookings/bookConferenceRoom <br>
     Header : user : username <br>
     		  Content-Type : application/json	<br>
     Body : { <br>
   			"startTime": "10:00:00", <br>
		    "endTime": "10:30:00", <br>
		    "participantsCount": 2 <br>
			} <br>
 <br>

Open API Details:
--------------
http://localhost:8080/api-docs <br>

Swagger Details:
--------------
http://localhost:8080/swagger-ui/index.html#/ <br>

Docker Image Build Commands
--------------------
docker build -t conference-booking-service .


H2- DATABASE
-------------
http://localhost:8080/h2-console <br>
 	username: sa <br>
    password: password<br>