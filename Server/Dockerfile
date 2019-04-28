FROM java:8
COPY Server.java Records.java DatabaseOperations.java ./
COPY mysql-connector-java-8.0.16.jar ./libs/
ENV CLASSPATH .:./libs/mysql-connector-java-8.0.16.jar
RUN javac -cp .:./libs/mysql-connector-java-8.0.16.jar ./*.java
EXPOSE 59001
ENTRYPOINT ["java"]
CMD ["Server"]
