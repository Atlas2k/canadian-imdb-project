
buildLoader: LoadMovies.class

LoadMovies.class: LoadMovies.java
	javac LoadMovies.java

runLoader: LoadMovies.class
	java -cp .:mssql-jdbc-11.2.0.jre18.jar LoadMovies

cleanLoader:
	rm LoadMovies.class

buildServer: RunServer.class

RunServer.class: RunServer.java
	javac RunServer.java

runServer: RunServer.class
	java -cp .:mssql-jdbc-11.2.0.jre18.jar RunServer

cleanServer:
