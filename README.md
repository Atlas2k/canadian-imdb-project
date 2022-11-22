# Loading Tables and Data Into Server
## LoadMovies.java
* Change auth.cfg to contain your server login credentials.
* In the movies/src directory run the command "make buildLoader" to build the loader.
* In the movies/src directory run the command "make runLoader" to run the loader program.
* Press h for a help menu or simply press b to build the tables then l to begin loading.
* * Note: This will take a while :(
* After this process concludes your server has been loaded with all the data required to use the program.
*  Please feel free to quit the loader program.
* * Also NOTE: The server under user "eleissa1" and password "7867467" has been fully loaded with all data incase the process takes too long.

# Starting Server to Connect Client
## RunServer.java
* In the movies/src directory run the command "make buildServer" to build the server.
* In the movies directory run the command "make runServer" to run the server program.
* Press 2 to start the server and listen for a client connection from the Android frontend.

# Starting and Connecting the Client to Server
## 






# Important Notes
* The makefile assumes you are running Java JRE 18 as that is the class path that is in both run commands. The file for JRE 11 is provided if need be, feel free to update the makefile to use that .jar file if an older version of the JRE is being run on your machine. It is also assumed that make is installed on the user's machine.
* We realized after parsing the data sets from IMDb that if we included all US and CA releases that the .sql files would total up to around 12 gigs. Keeping that in mind we constrained our dataset to Canadian releases only which means our dataset has all movie information for media that was released in Canada which encompasses the vast majority of American media. Saying that, IMDb in their infinite wisdom decided that only US releases deserve episode information when it comes to American shows, thus our database does not contain episode information for American shows released in Canada. Canadian made shows do have episode information, so I encourage you, when testing the search show functionality, to be a patriot and search for some Canuck made shows like "Schitt's Creek" or (I cannot believe someone actually made this show) "Canada's Worst Driver". Given infinite time we could have easily had the database encompass everything. I hope this decision is understandable.