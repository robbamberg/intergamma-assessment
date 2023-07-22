Intergamma Assessment
---

### Introduction
This is a Kotlin Spring Boot application for a simple API doing CRUD on a stock system.

It is possible to add, update, delete and fetch stock entities. If you place a reservation on a stock item then it will automatically cancel after 5 minutes if it was not sold in that time.

### Local Development
This application uses an embedded in memory h2 database. It uses flyway for db migrations.

You can build and test the application using gradle:

`./gradlew clean build`

Now we can run application via:

`./gradlew bootRun`

Application runs and is accessible on: http://localhost:8080/

API specification can be found in `stock_api.yaml`. THe generated HTML from this specification can be found in `documentation/api-documentation.html`

### Authorisation
For demo purposes we have the SecurityConfiguration configuration in Config.tk. 
This disables  Basic Authorisation and CSRF on the API, so it can be tested without issues locally.

Removing the file will enable Basic authorisation (password in properties) and will add CSRF security on POST/PUT requests.