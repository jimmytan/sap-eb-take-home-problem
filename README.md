# Introduction
This project is for the SAP take home problem.

We the North American team who are split across Boulder and Montreal often enjoy spending time in the outdoors. Some of us would like to bike while some of us are happy just walking, some of us like to challenge ourselves with difficult hikes while some of us would like to ease in slowly.

The good part is that the Boulder county releases data about the trails as open data. Your assignment is to help us find a trail that suits our requirement.

# Architecture
The project is hosted to AWS. The data can be accessed by the Web API. 

The system includes an API Gateway which takes the get trail request from the clients and forward the request to the 
get trail Lambda. Currently, there is no database used in the system. The trails data are stored as csv file and loaded 
to the system when the Lambda is started.


# Implementation
## Request

The url to get trails is /trails

Here are the query parameter the client can apply. All of the query parameters are optionals. The system validates the parameter's
value if it is applied.
* picnic: the value will filter out the trails which have picnic area. the possible values are Yes and No.
* restrooms: the value will filter out the trails which have restroomss. the possible values are Yes and No.
* trailClass: the value will filter out the trails class. the possible values are T1, T2 and T3
* pageSize: the clients can decide how many trails can be returned. By default it is 10.
* page: the system supports the pagenation. By default it is 1.

# Response
The response body contains an object which has the following info
* trails: the list of trails in JSON format

## Further implementation
### Authentication and Authorization
although the data is public, we may want to add authentication and authorization for the request. 
At least we need to make sure we have the throttling policy applied on gateway and lambda. 

### Infra
Currently, the infra is manually created and deployed. Normally we should create a pipeline to create the infra as code.
In the current setup, the system does not have database. Normally system should have a database to store external data for fast query.
### Testing
In this version the system only has unit tests. Normally we should have integration test for each check in code. Furthermore, we can have canary test which calls the api to make sure the system is in health.

### Metrics
We need to setup the metrics for the business insight and operation which triggers the alarm for the system

### Use dependency injection framework such as Dagger Guice
Due to the size of the project, I do not use the dependency injection framework. 

