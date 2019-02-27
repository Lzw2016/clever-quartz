FROM 192.168.159.136:5000/java:8u111-jre-alpine as dev

ADD clever-quartz-server/target/clever-quartz-server-*-SNAPSHOT.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar", "--server.port=9066", "--server.address=0.0.0.0", "--server.register=false"]
