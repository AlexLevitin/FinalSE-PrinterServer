FROM openjdk:8
EXPOSE 8080
ADD target/dockertestproject.jar dockertestproject.jar
ENTRYPOINT ["java","-jar","/dockertestproject.jar"]