# Spring Data Elasticsearch Example

## Introduction

This example demonstrates how to use Spring Data Elasticsearch to do simple CRUD operation.

You can find the tutorial about this example at the below link:

[https://geekyhacker.com/2019/05/01/getting-started-with-spring-data-elasticsearch/](https://geekyhacker.com/2019/05/01/getting-started-with-spring-data-elasticsearch/)

For this example, a Book controller created that allows to do the following operations with Elasticsearch:

- Get list of all books
- Create a book
- Update a book by Id
- Delete a book by Id
- Search for a book by ISBN
- Fuzzy search for books by author and title


## How to run

The first thing to do is to start Elasticsearch. For that you can use the `docker-compose` file in this project
and run it like this:

```bash
$ docker-compose -f docker-compose up -d
``` 

It brings Elasticsearch up on a single node cluster with the cluser name `elasticsearch`.

Then you can run the application like below:

```bash
$ ./mvnw spring-boot:run
```

If your Elasticsearch URI is not `localhost` and/or the cluster name is different simply override one or both of the following environment variable:

- `ES_URI`
- `ES_CLUSTER_NAME`

Once everything is up and running open the browser and go to [http://localhost:8080](http://localhost:8080). You should see Swagger to interact with.

## Run testcontainers tests

The integration tests written relying on [testcontainers](https://www.testcontainers.org/) to spin up Elasticsearch on the spot to run tests against.

To know more about container read this tutorial:
[https://geekyhacker.com/2019/05/08/integration-test-with-testcontainers-in-java/](https://geekyhacker.com/2019/05/08/integration-test-with-testcontainers-in-java/)

To run them just execute below command:

```bash
$ mvn clean verify
```

Make sure you have your docker running.
