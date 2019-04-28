FROM java:8
COPY Server/*.java ./
COPY Server/mysql-connector-java-8.0.16.jar ./libs/
ENV CLASSPATH .:Server/libs/mysql-connector-java-8.0.16.jar
RUN javac -cp .:Server/libs/mysql-connector-java-8.0.16.jar ./*.java
EXPOSE 59001
ENTRYPOINT ["java"]
CMD ["Server"]
