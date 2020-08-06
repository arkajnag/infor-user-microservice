# infor-user-microservice
Microservice designed using Spring Boot-Embedded H2 DB to create and handle User Related Services.

- Register new Users.
- Search existing Users.
- Proper Error Handling on Failure.

Different endpoints being exposed are:
Create New User: [http://localhost:3001/rest/user/new/user]
Search All Users: [http://localhost:3001/rest/user/all]
Search User by Userid: [http://localhost:3001/rest/user/userid/{userid}]
Search User by Username: [http://loalhost:3001/rest/user/username/{username}]
Generate Report of All Users: [http://localhost:3001/rest/user/all/reports/{reportType}]
[reportType: html or pdf]
[Reports will be generated inside Project->Reports directory]

