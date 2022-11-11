
build: movies.class

movies.class: movies.java
	javac movies.java

run: movies.class
	java -cp .:mssql-jdbc-11.2.0.jre18.jar movies

clean:
	rm movies.class
	rm Tables.class
	rm generateSQL.class
	rm queries.class
