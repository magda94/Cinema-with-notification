FROM openjdk:17.0.1
ADD build/libs/film-service-0.0.1-SNAPSHOT.jar film-service-0.0.1-SNAPSHOT.jar
EXPOSE 8081
CMD java -jar -Dspring.profiles.active=dev film-service-0.0.1-SNAPSHOT.jar