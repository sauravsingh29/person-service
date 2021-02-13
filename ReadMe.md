# [person-service]()

## Description
This application is test API project written into spring boot. It's a sample Restful api.

## Features
1. Save a person.
2. Get all person with pagination request.
3. Get a person by id.
4. Get/Search a person by firstname or lastname.
5. Update a person using person id.
6. Delete a person using person id.

System generates Person Id to make sure <b>uniqueness</b> when save a person using <b>H2 In memory database</b>. 

## Run locally
##### Checkout/Clone project
    ```shell script
    git clone https://github.com/sauravsingh29/person-service.git
    ```
#### With Docker
1. Build Docker image
    ```shell
    cd person-service
    # Build jar file.
    mvn clean install 
    # Build Docker image 
    docker build -t person-service .
    ```
2. Check Docker
    ```shell
    docker images
    ```
3. Run Docker iamge
    ```shell
    docker run -p 8080:8080 person-service
    ```
    
#### With maven
1. Import project to STS or IntelliJ as maven project.
   >OR
2. Goto project root folder
    ```shell script
    > cd person-service
    # To run the application from command line
    > mvn spring-boot:run
    # To run test
    > mvn test
    ```
    
## Documentation
Api documentation has been done using Open Api and will be available [Swagger UI](http://localhost:8080/swagger-ui.html).

For getting security in place for API Basic Authentication has been used to secure.
1. username - <b>admin</b>
2. password - <b>password</b>
