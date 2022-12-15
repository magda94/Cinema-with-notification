FROM openjdk:17.0.1
ADD build/libs/cinema-service-0.0.1-SNAPSHOT.jar cinema-service-0.0.1-SNAPSHOT.jar
EXPOSE 8082
CMD java -jar -Dspring.profiles.active=dev cinema-service-0.0.1-SNAPSHOT.jar