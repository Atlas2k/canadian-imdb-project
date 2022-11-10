
build: generateSQL.class

generateSQL.class: generateSQL.java
	javac generateSQL.java

run: generateSQL.class
	java -cp .:mssql-jdbc-11.2.0.jre11.jar generateSQL

clean:
	rm generateSQL.class
