
buildLoader: LoadDatabase.class

LoadDatabase.class: LoadDatabase.java
	javac LoadDatabase.java

runLoader: LoadDatabase.class
	java -cp .:mssql-jdbc-11.2.0.jre18.jar LoadDatabase

cleanLoader:
	rm LoadDatabase.class

buildServer: RunServer.class

RunServer.class: RunServer.java
	javac RunServer.java

runServer: RunServer.class
	java -cp .:mssql-jdbc-11.2.0.jre18.jar RunServer

cleanServer:
	rm generateSQL.class
	rm ClientCommandHandler.class
	rm ClientConnection.class
	rm UserCommandHandler.class
	rm RunServer.class
	rm Server.class
	rm StandardIO.class