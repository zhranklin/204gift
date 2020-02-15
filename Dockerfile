FROM hub.c.163.com/qingzhou/powerful-base-java
ADD target/gift-0.0.1-SNAPSHOT.jar /app.jar
ENTRYPOINT java $JAVA_OPTS -jar /app.jar