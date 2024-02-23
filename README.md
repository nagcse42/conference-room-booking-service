# conference-booking-service
Service for conference rooms booking 

Build Process:
---------------
Clone project to folder
Open terminal in project root folder
Build project: mvn clean install <br>
Run project: mvn spring-boot:run  <br>

Use Below APIs to run and verify the data in H2DB by login (provided details below) <br>
  API-1) Get available conference rooms by requested time slots <br>
  API-2) Book conference room <br>
  
  
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

Swagger Details:
--------------
http://localhost:8080/swagger-ui/index.html <br>


Docker Image Process
--------------------
docker build -t conference-booking-service .


H2- DATABASE
-------------
http://localhost:8080/h2-console <br>
 	username: sa <br>
    password: password<br>
#   c o n f e r e n c e - r o o m - b o o k i n g - s e r v i c e  
 