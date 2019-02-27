FROM java:8u111-jre-alpine as dev

ADD clever-quartz-server/target/clever-quartz-server-0.0.1-SNAPSHOT.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar", "--server.port=9066", "--server.address=0.0.0.0", "--server.register=false"]
