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
and then run docker-compose.yaml from this folder. The docker-compose file include all needed operations to set up projects and databases.

# user-service
This service stores users data. It has all necessary CRUD operations and REST API.
The service uses PostgresSQL database. 
Service can be run with 'dev' profile (used in Docker environment) or 'local' profile (used for development or tests).

# film-service
This service stores data about films and comments about them. It has all necessary CRUD operations and REST API.
This service uses MongoDB database.
Service can be run with 'dev' profile (used in Docker environment) or 'local' profile (used for development or tests).

# cinema-service
This service stores data about cinema. It means that it has data about rooms, shows, tickets and reservations. It is possible to add and remove all mentioned data. 
It has all necessary CRUD operations and REST API. 

The service uses PostgresSQL database and Kafka framework. It has connection to user-service and film-service to get needed information. After a reservation is made or cancelling, it sends notification by Kafka to send reminder about film to user.
The service can be run with 'dev' profile (used in DOcker environment) or 'local' profile (used for development or tests).


# notification-service
This service gets information from Kafka about notification request. Using Quartz framework it sends notification to user about show 15 minutes before start. After successful/failed attempt to save a new notification, it sends response with the appropriate status by Kafka.
Service can be run with 'dev' profile (used in Docker environment) or 'local' profile (used for development or tests).

