# Spring Data ElasticSearch Example

## Introduction

This example demonstrates how to use Spring Data ElasticSearch to do simple CRUD operation.

You can find the tutorial about this example at the below link:

[https://blog.madadipouya.com](https://blog.madadipouya.com)

For this example, a Book controller created that allows to do the following operations with ElasticSearch:

- Get list of all books
- Create a book
- Update a book by Id
- Delete a book by Id
- Search for a book by ISBN


## How to run

The first thing to do is to start ElasticSearch. For that you can use the `docker-compose` file in this project
and run it like this:

```bash
$ docker-compose -f docker-compose up -d
``` 

It brings ElasticSearch up on a single node cluster with the cluser name `elasticsearch`.

Then you can run the application like below:

```bash
$ ./mvnw spring-boot:run
```

If your ElasticSearch URI is not `localhost` and/or the cluster name is different simply override one or both of the following environment variable:

- `ES_URI`
- `ES_CLUSTER_NAME`

Once everything is up and running open the browser and go to [http://localhost:8080](http://localhost:8080). You should see Swagger to interact with.