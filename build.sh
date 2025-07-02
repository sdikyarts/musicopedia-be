#!/bin/bash
# Build script for Render deployment

echo "Starting build process..."

# Make gradlew executable
chmod +x ./gradlew

# Clean and build the project
echo "Building Spring Boot application..."
./gradlew clean build -x test --no-daemon

echo "Build completed successfully!"
