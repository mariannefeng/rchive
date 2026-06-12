FROM clojure:lein AS builder
WORKDIR /app
COPY project.clj .
RUN lein deps
COPY src/ src/
COPY resources/ resources/
RUN lein uberjar

FROM eclipse-temurin:21-jre
WORKDIR /app
COPY --from=builder /app/target/rchive-0.0.1-standalone.jar app.jar
RUN mkdir -p data
CMD ["java", "-jar", "app.jar"]
