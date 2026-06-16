JAVA_VERSION = 21
APP_NAME = pokedex-api
PORT = 8080

.PHONY: build run

build:
	@echo "Building the application..."
	./gradlew clean build

run:
	@echo "Running the application on port $(PORT)..."
	./gradlew bootRun --args='--server.port=$(PORT)'