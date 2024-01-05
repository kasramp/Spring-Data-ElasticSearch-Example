# Spring Data Elasticsearch Example with Spring Boot 3 and ElasticSearch 8

## Introduction

This example demonstrates how to use Spring Data Elasticsearch to do simple CRUD operations.

You can find the tutorial about this example at this link: [Getting started with Spring Data Elasticsearch](https://www.geekyhacker.com/getting-started-with-spring-data-elasticsearch/)

For this example, we created a Book controller that allows doing the following operations with Elasticsearch:

- Get the list of all books
- Create a book
- Update a book by Id
- Delete a book by Id
- Search for a book by ISBN
- Fuzzy search for books by author and title

## How to run

### Spring Boot 3.1

Since this project uses Spring Boot 3.1, all you need to do is to run the application:

```bash
$ ./mvnw spring-boot:run
```

Spring Boot automatically detects the `docker-compose` file and starts the Elasticsearch container.

### Spring Boot 3.0 and prior

The older version of this project uses Spring Boot 3.0 which does not have Spring Boot Docker Compose integration.

To run an older version of the application, the first thing to do is to start Elasticsearch. For that, you can use the `docker-compose` file in this project  and run it like this:

```bash
$ docker-compose -f docker-compose up -d
``` 

It brings Elasticsearch up on a single node cluster with the cluster name `elasticsearch`.

Then you can run the application like below:

```bash
$ ./mvnw spring-boot:run
```

If your Elasticsearch URI is not `localhost` is different simply override the following environment variable:

- `ES_URI`

Once everything is up and running open the browser and go to [http://localhost:8080](http://localhost:8080). You should see Swagger to interact with.

## Run Testcontainers tests

The integration tests are written relying on [Testcontainers](https://www.testcontainers.org/) to spin up Elasticsearch on the spot and run tests against it.
To know more about container testing read this tutorial: [Integration test with Testcontainers in Java](https://www.geekyhacker.com/integration-test-with-testcontainers-in-java/)

To run the integration test (using Testcontainers) just run the below command:

```bash
$ mvn clean verify
```

Make sure you have your docker running.
