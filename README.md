# Task App REST API

Task app API with user authentication. Made in Spring Boot with a Postgres database.

## Example Requests

* Organize tasks by projects
* Mark a task completed with a simple GET request
    * `???????????????`

## Setup

* Clone the repository

    * run `git clone https://github.com/qelery/??????????.git` in the terminal

* Create Postgres database
    * log into progress then run `CREATE DATABASE taskapp;`

* Change postgres username and password per your installation
    * open `src/main/resources/application.properties` from the repository
    * change `spring.datasource.username` and `spring.datasource.password` per your postgres installation

* Run the program
    * run `./mvnw spring-boot:run` in the terminal
    * or use your favorite IDE


## Usage

* Must register on endpoint `/auth/users/register` before using any of the private endpoints
* Login on endpoint `auth/users/login`
    * Will receive JWT in response
* Must send Bearer Token (JWT) in authorization header with each request on private endpoints
### Endpoints

#### Project endpoints
Method |Endpoint | Functionality| 
------------ |------------ | ------------- | 
GET | /api/projects | Lists all projects | 
POST | /api/projects | Creates a new project | 
GET | /api/projects/{projectId} | Gets a single project with the supplied id | 
PUT | /api/projects/{projectId} | Updates a project with the supplied id |
PATCH | /api/projects/{projectId} | Updates a project; Supports patch semantics |
DELETE | /api/projects/{projectId} | Deletes a project with the supplied id |
<br>

#### Task endpoints
Method |Endpoint | Functionality| 
------------ |------------ | ------------- | 
GET | /api/projects/tasks/all | List all tasks regardless of project |
GET | /api/projects/{projectId}/tasks | List tasks in the given project |
POST | /api/projects/{projectId}/tasks | Creates a new task in the given project | 
GET | /api/projects/{projectId}/tasks/{taskId}| Gets a single task from the given project |
PUT | /api/projects/{projectId}/tasks/{taskId}| Updates a task in the given project | 
PATCH | /api/projects/{projectId}/tasks/{taskId}| Updates a task in the given project; Supports patch semantics | 
DELETE | /api/projects/{projectId}/tasks/{itemId} | Deletes a task in the given project | 
PUT | /api/projects/{projectId}/tasks/{taskId}/complete | Marks the specified task complete |
<br>

#### Auth endpoints
Method |Endpoint | Functionality| 
------------ |------------ | ------------- | 
POST | /auth/users/register | Registers a user |
POST | /auth/users/login |Logs a user in | 
<br>

#### Project JSON representation

```
[
   {
       "id": 2,
       "name": "Surprise birthday party",
       "description": "Surprise birthday party for Batman on 7-2",
       "tasks": [
           {
               "id": 4,
               "name": "Buy cake",
               "description": "Pick up the cake from the bakery on First Street",                
               "dueDate": "2021-07-20",
               "priority": "NORMAL",
               "status": "PENDING"
           },
           {
               "id": 5,
               "name": "Send out invitations",
               "description": "Make an event on Facebook. Be sure not to include 
                        Batman on the invitation",
               "dueDate": "2021-07-11",
               "priority": "HIGH",
               "status": "COMPLETED"
           }
       ]
   }
]
```
<br>

#### User JSON representation

```
{
    "email":"example@google.com"
    "password":"",
}
```
<br>

### Fields
Name |Type
------------ |------------ | 
id | integer - identifier for that particular entity in the database  | 
name | string - name of the project or task |
description | string - description of the project or task |
dueDate | date - format (yyyy-mm-dd)
priority | enum - high / normal / low — case-insensitive
status | enum - pending / completed — case-insensitive
<br>

### Query Parameters

Parameter | Accepted Values
------------ |------------ | 
overdue | true / false |
status | pending / completed — case-insensitive | 
priority | low / normal / high — case-insensitive | 
limit | any number |
sort | e.g. sort=dueDate,desc &nbsp;&nbsp; / &nbsp;&nbsp; sort=name,asc &nbsp;&nbsp; / &nbsp;&nbsp; sort=priority,asc |
dueBefore | yyyy-mm-dd |
dueAfter | yyyy-mm-dd |

## ERD Diagram
![erd diagram](images/erd.png)