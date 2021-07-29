# Topicus Track&Trace

This is the server for the team 5 track & trace application. :zap:
Developed at the university of Twente for Topicus.

## Grading Section
### Live Version :dancer:
[Go to the app](http://node13337-topicus-5.paas.hosted-by-previder.com/)

### Database Info :closed_lock_with_key:
SQL code for Schema:
src/main/resources/pgsql/createSchema.pgsql
```
database name: dab_di20212b_99
development schema: track_and_trace_db
production schema: track_and_trace_prod
testing schema: track_and_trace_testing

```

### login information of example account:
email | password | role
--- | --- | ---
bram@otte.biz | password | customer
hayel@otte.biz | password | customer
staff@topic.nl | admin | service provider

<br>

### UML diagrams:
```
Class diagram: https://lucid.app/lucidchart/20a62b12-baf3-4e67-9591-32f1e0b5a424/edit?shared=true&page=0_0#
Use Case diagram: https://lucid.app/lucidchart/62f926aa-0f51-44c0-aade-9d47372894ee/view?page=0_0#

```

### Reports:
```
Testing report: https://docs.google.com/document/d/15j7JzOm_oeZzOws6Z7Rgk_UirZaK7pVYKRoyEScjJVU/edit#heading=h.eb9ojugmw81r
Security report: https://docs.google.com/document/d/11lQJzoN6xd1tsDpUIxkPuNqGzIrIT7tjCWEYYtk6aMg/edit
Project report: https://docs.google.com/document/d/1w35vQ8eBR0b2Nc4MOQvekATnd1yYDaEDERTStkvv7os/edit?usp=sharing

Also, pdf versions of the above mentioned reports are going to be in the directory report.
```

### Trello

[Trello](https://trello.com/b/chnPehD1/2021-m4-project-topicustracktrace-team-5)

## Getting Started :muscle:

These instructions will get you a copy of the project up and running on your local machine for development and testing purposes. See deployment for notes on how to deploy the project on a live system.

### Prerequisites :runner:
* [Java11](https://www.oracle.com/java/technologies/javase-downloads.html)
* [PostgreSQL](https://www.postgresql.org/) - The DBMS used.
* [Maven3](https://maven.apache.org/) - Dependency Management for java
* [Node.js and NPM](https://nodejs.org/en/) - Runtime and Dependency Management for Javascript
* [Yarn](https://yarnpkg.com/) - Dependency Management for Javascript

### Installing :nut_and_bolt:

A step by step series of examples that tell you how to get a development env running

Maven will install dependencies as they are needed for commands.
But yarn first needs to be told to install them:
```sh
yarn
```` 
Now we can build the code:
```sh
yarn build
```
and upload the target/ROOT.war file to a tomcat server

After running the server you can get data from the api.
Disclaimer: this request will fail without a token.
```sh
curl http://localhost:8080/api/user/1  
```

## Watching/Serving files 👀💁‍♂️
Automatically build and serve the app on file change:
```sh
yarn serve
```
Automatically run tests on file change:
```sh
yarn test-watch
```

## Running the tests :tada:

Here is how to run the automated tests for this system.

### Unit Tests
To run unit tests we use the maven `test` lifecycle.
```sh
yarn test
```

### Integration Tests
Make sure you have the client side build.
And run the testing server: (note running this command will not build the client code).
```sh
yarn serve-test
```

then you can run the integration tests:
```sh
yarn integration
```

### Checkstyle

To run checkstyle on the project we use maven to create 
the checkstyle report.
```
mvn checkstyle:checkstyle
```

## Deployment :ship:
### deploy to Previder
Create a tomcat server with tomcat version 8.5.64
Unfold the server tab until you see Deployments
Hover over deployments and click "Deploy Archive".
Click browse, select target/Root.war and click open.
Set Context to ROOT and click deploy.


### To deploy to Heroku:
make sure the heroku cli is installed
login to heroku:
```sh
heroku login
```
and deploy
```
mvn heroku:deploy-war
```

### Our Trello Board
The URL for our trello board:
https://trello.com/b/chnPehD1/2021-m4-project-topicustracktrace-team-5

## Built With :wrench:

* [PostgreSQL](https://www.postgresql.org/) - The DBMS used.
* [Maven](https://maven.apache.org/) - Dependency Management for java
* [Node.js and NPM](https://nodejs.org/en/) - Runtime and Dependency Management for Javascript
* [Yarn](https://yarnpkg.com/) - Dependency Management for Javascript
* [Heroku](https://www.heroku.com) - Deploy target

## Versioning :date:

We use [SemVer](http://semver.org/) for versioning. For the versions available, see the [tags on this repository](https://git.snt.utwente.nl/mod4-project/webapp/-/tags).

## Authors :pencil2:

+ **Mark Bruderer**
+ **Hayel Akel**
+ **Bram Otte**
+ **Minh Vo**
+ **Noah Koerselman**
+ **Phuoc Ho**

### Mentor:
**Gergana Georgieva**

### Supervisor:
**Tannaz Zameni**
