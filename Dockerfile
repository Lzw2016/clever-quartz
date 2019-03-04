FROM 172.16.29.104:8850/java:8u111-jre-alpine as dev

ADD clever-quartz-server/target/clever-quartz-server-*-SNAPSHOT.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar", "--server.port=28082", "--server.address=0.0.0.0", "--server.register=false"]
EXPOSE 28082
