Travel API Client
=================

# Environment Setup

1. Install JDK 17

   Make sure that JAVA_HOME env variable referred to JDK 17 and its 'bin' directory added to PATH env variable


2. Install docker


3. Install docker compose

# Run service

Run from the project root directory `docker-compose -f ./scripts/docker-compose.yaml up`

Run from the project root directory `./gradlew bootRun` (`./gradlew.bat bootRun` on Windows OS)

# Check metrics

Open `http://localhost:9000/actuator/prometheus` to see Prometheus metrics

Optionally you can configure Grafana dashboard UI
