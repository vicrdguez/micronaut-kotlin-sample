FROM openjdk:14-alpine

RUN mkdir /app

COPY build/libs/auth-api-*-all.jar app/auth-api.jar
WORKDIR /app

CMD ["java", "-Dcom.sun.management.jmxremote", "-Xmx128m", "-jar", "auth-api.jar"]