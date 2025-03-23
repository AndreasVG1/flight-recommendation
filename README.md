# flight-recommendation
A Spring Boot application for selecting flights and recommending seats on the plane.

## Prerequisites
1. Ensure you have Java 23 with `java -version`
2. Git
3. If you don't have Maven installed, use the command `chmod +x ./mvnw` in a Linux terminal on Git Bash, inside the project directory. This allows the application to be ran without installing Maven.

## How to run
1. Clone the repository `git clone https://github.com/AndreasVG1/flight-recommendation.git`
2. Go into the project directory `cd flight-recommendation`
3. Run `./mvnw spring-boot:run`
4. Access the app on `http://localhost:8080/flights`
5. API available on `http://localhost:8080/api/flights`

## Searching for flights
Currently, users can search for flights by the following filters:
1. Destination airport
2. Flight price
3. Flight duration

## Selecting seats on the plane
Users can select their seats or have the application recommend them by the following preferences. Currently users can only select one at a time:
1. Window seat
2. Near the exit
3. More leg room
