FROM openjdk:17

WORKDIR /app

ENV PORT=8080

ENV SPRING_DATASOURCE_URL=jdbc:postgresql://localhost:5432/drop-note
ENV SPRING_DATASOURCE_USERNAME=postgres
ENV SPRING_DATASOURCE_PASSWORD=postgres

ADD target/drop-note.jar  drop-note.jar

CMD ["java", "-jar", "drop-note.jar"]
