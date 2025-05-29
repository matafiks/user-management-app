@echo off

echo Starting backend...
cd backend
start cmd /k mvnw spring-boot:run
cd ..

echo Starting frontend...
cd frontend
npm start
