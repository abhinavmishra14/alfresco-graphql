# GraphQL endpoint for Alfresco

This is an ACS project for Alfresco SDK 4.0, tested against ACS 6.2.0 community.

To know more about GraphQL : https://graphql.org/

Declare the following endpoints:
- http://localhost:8080/alfresco/graphql : GraphQL endpoint read only.
- http://localhost:8080/alfresco/graphql_mutation : GraphQL endpoint that allows mutations.
- http://localhost:8080/alfresco/graphiql : UI to create queries. Test with that one.

Interesting files:
- The GraphQL schema:  [alfresco.graphqls](src/main/resources/alfresco/module/graphql/alfresco.graphqls)
- Sample query: [query.graphql](src/test/resources/query.graphql)
- Sample results: [expectedResponse.json](src/test/resources/expectedResponse.json)

# Author

Made for fun and education by Adrien SAUVEZ, Alfresco expert @Smile company (https://www.smile.eu).
Special thanks for Covid 19 for having forced me to stay at home and therefore give me time to work on this project..

# Install

- `git clone git@github.com:asauvez/alfresco-graphql.git`
- `cd alfresco-graphql`
- `./run.sh build_start`
- http://localhost:8080/alfresco/graphiql
```
query {
  node {
    sitesHome {
      childByName(name: "swsdp") {
        title
        childByName(name: "documentLibrary") {
          childrenContains {
            nodeRef,
            name,
            title
          }
        }
      }
    }
  }
}
```

Can be integrated in an existing Alfresco by declaring:
```
<dependency>
	<groupId>fr.smile.alfresco.graphql</groupId>
	<artifactId>graphql</artifactId>
	<version>1.0-SNAPSHOT</version>
</dependency>
```

# TODO
- mutation
	- protection against unwanted mutation
	- add directly on node
		delete node
		add or remove aspect
		addChild : Node
	test
	create
	delete

- close resultset ?
	https://github.com/graphql-java/graphql-java/issues/1863
- gérer plusieurs consommation du flux par RetryngTransaction ?
- query by path
- Optimisation ?
- Subscription
	https://www.graphql-java.com/documentation/v14/subscriptions/
	SubmissionPublisher

