
buildLoader: LoadMovies.class

LoadMovies.class: LoadMovies.java
	javac LoadMovies.java

runLoader: LoadMovies.class
	java -cp .:mssql-jdbc-11.2.0.jre18.jar LoadMovies

cleanLoader:
	rm LoadMovies.class
