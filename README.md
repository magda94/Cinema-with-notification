# Cinema-with-notification

This project was created to simulate simple cinema approach.
It includes few subproject like:
- user-service
- film-service
- cinema-service
- notification-service.

Project uses two databases: PostgresSQL and MongoDB.

All services are implemented in Java 17 and SpringBoot 2.x.

To use this project, it is needed to prepare Docker environment
and then run docker-compose.yaml from docker-composes/postgres directory.

#user-service
This service stores users data. It has all necessary CRUD operations and REST API.
The service uses PostgresSQL database. 
Service can be run with 'dev' profile (used in Docker environment) or 'local' profile (used for development or tests).
