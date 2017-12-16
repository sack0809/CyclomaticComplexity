# CyclomaticComplexity"



Code is written in the JAVA REST APIs.

CLient calculates the Complexity per file and returns the cumulative complexity per commit.
Server plots the graph between Commit Ids and Complexity.


This is the client and Server API.
Repo Path needs to be replaced with the Local Paths;

Below URLs
Server URL for cloning and Calculating the commit
http://localhost:8080/ServerRESTAPI/rest/CyclometicServer/main
http://localhost:8080/ServerRESTAPI/rest/CyclometicServer/clone

Client URL:

http://localhost:9080/ClientRESTAPI/rest/CyclometicClient/calculate
This will calculate the Cyclometic Complexity and returns the value to server, Then server builds the graph per commit and cumulative complexity

Code is deployed on the Tomcat Server Version8.5 and 9.0
