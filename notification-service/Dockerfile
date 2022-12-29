FROM openjdk:17.0.1
ADD build/libs/notification-service-0.0.1-SNAPSHOT.jar notification-service-0.0.1-SNAPSHOT.jar
EXPOSE 8083
CMD java -jar -Dspring.profiles.active=dev notification-service-0.0.1-SNAPSHOT.jar