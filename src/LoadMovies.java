import java.sql.SQLException;
import java.util.Scanner;
import java.io.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.util.Properties;

public class LoadMovies {

    Statement statement;
    Connection connection;

    public static void main(String[] args) throws SQLException {
        Scanner console = new Scanner(System.in);
        System.out.print("Welcome! Type h for help. ");
        System.out.print("db > ");
        String line = console.nextLine();
        String[] parts;
        LoadMovies instance = new LoadMovies();

        while (line != null && !line.equals("q")) {
            parts = line.split("\\s+");
            if (parts[0].equals("h"))
                printHelp();

            else if (parts[0].equals("c")) {
                instance.createConnection();
            }

            else if (parts[0].equals("b")) {
                instance.dropTables();
                instance.createTables();
            }

            else if (parts[0].equals("l")) {
                instance.loadTables();
            }

            System.out.print("db > ");
            line = console.nextLine();

        }
        console.close();
    }

    public LoadMovies() {
        statement = null;
        connection = null;
    }

    private static void printHelp() {
        System.out.println("Movies Data Base Loader Functionality:");
        System.out.println("Commands:");
        System.out.println("h - Get help");
        System.out.println("c Connect to the SQL server");
        System.out.println("b Build all tables onto server");
        System.out.println("l Load all .sql files onto server");
        System.out.println("");
        System.out.println("q - Exit the program");
        System.out.println("---- end help ----- ");
    }

    private void createConnection() {
        Properties prop = new Properties();
        String fileName = "auth.cfg";
        try {
            FileInputStream configFile = new FileInputStream(fileName);
            prop.load(configFile);
            configFile.close();
        } catch (FileNotFoundException ex) {
            System.out.println("Could not find config file.");
            System.exit(1);
        } catch (IOException ex) {
            System.out.println("Error reading config file.");
            System.exit(1);
        }
        String username = (prop.getProperty("username"));
        String password = (prop.getProperty("password"));

        if (username == null || password == null) {
            System.out.println("Username or password not provided.");
            System.exit(1);
        }

        String connectionUrl = "jdbc:sqlserver://uranium.cs.umanitoba.ca:1433;"
                + "database=cs3380;"
                + "user=" + username + ";"
                + "password=" + password + ";"
                + "encrypt=false;"
                + "trustServerCertificate=false;"
                + "loginTimeout=30;";
        try {
            this.connection = DriverManager.getConnection(connectionUrl);
            this.statement = connection.createStatement();
            System.out.println("Succesfully Connected to Server!");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void loadTables() {
        System.out.println("Loading Data From Scripts Into Server. Please Wait...");
        System.out.println("Loading the media table...");
        loadFromFile("title.akas.sql");
        loadFromFile("title.basics.sql");
        loadFromFile("title.ratings.sql");
        try {
            connection.createStatement().execute("Delete from media where titleId = 0;");
            connection.createStatement().execute("Delete from media where title is NULL;");
        } catch (SQLException e) {

            e.printStackTrace();
        }

        System.out.println("Loading the genres table and realtion...");
        loadFromFile("genres.title.basics.sql");
        loadFromFile("partOf.title.basics.sql");

        System.out.println("Loading the having (episodes) table...");
        loadFromFile("title.episode.sql");

        // Loading the people table
        System.out.println("Loading the people table relations...");
        loadFromFile("name.basics.sql");
        loadFromFile("knownFor.name.basics.sql");
        loadFromFile("jobs.name.basics.sql");
        loadFromFile("works.name.basics.sql");

        // Loading cast and character data
        System.out.println("Loading the cast and character tables...");
        loadFromFile("title.principals.sql");
        loadFromFile("characters.title.principals.sql");

        // Loading platform data
        System.out.println("Loading the platform table and relations...");
        loadFromFile("platform.sql");
        loadFromFile("availableOn.sql");
    }

    public void loadFromFile(String file) {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(file));
            String line = reader.readLine();
            // assumes each query is its own line
            System.out.println(line);
            while (line != null) {
                this.connection.createStatement().execute(line);
                line = reader.readLine();
            }
            reader.close();
        } catch (IOException | SQLException e) {

            e.printStackTrace();
        }
    }

    public void dropTables() throws SQLException {
        try {
            String selectSql = "drop table if exists knownFor";
            this.connection.createStatement().execute(selectSql);
            selectSql = "drop table if exists works";
            this.connection.createStatement().execute(selectSql);
            selectSql = "drop table if exists partOf";
            this.connection.createStatement().execute(selectSql);
            selectSql = "drop table if exists have";
            this.connection.createStatement().execute(selectSql);
            selectSql = "drop table if exists genre";
            this.connection.createStatement().execute(selectSql);
            selectSql = "drop table if exists jobs";
            this.connection.createStatement().execute(selectSql);
            selectSql = "drop table if exists workedOn";
            this.connection.createStatement().execute(selectSql);
            selectSql = "drop table if exists characters";
            this.connection.createStatement().execute(selectSql);
            selectSql = "drop table if exists availableOn";
            this.connection.createStatement().execute(selectSql);
            selectSql = "drop table if exists platform";
            this.connection.createStatement().execute(selectSql);
            selectSql = "drop table if exists people";
            this.connection.createStatement().execute(selectSql);
            selectSql = "drop table if exists media";
            this.connection.createStatement().execute(selectSql);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void createTables() {
        try {
            String selectSql = "CREATE TABLE media(titleId INTEGER, title text, isAdult BIT, imdbRating float, language text, startYear INTEGER, endYear INTEGER, runTime INTEGER, seasonNumber INTEGER, episodeNumber INTEGER, Primary Key(titleId));";
            this.connection.createStatement().execute(selectSql);
            selectSql = "CREATE TABLE genre(genreId INTEGER, genreName text, Primary Key(genreId));";
            this.connection.createStatement().execute(selectSql);
            selectSql = "CREATE TABLE partOf(genreId INTEGER, titleId INTEGER, FOREIGN KEY(genreId) REFERENCES genre(genreId), FOREIGN KEY(titleId) REFERENCES media(titleId));";
            this.connection.createStatement().execute(selectSql);
            selectSql = "CREATE TABLE have(titleIdEpisode INTEGER, titleIdShow INTEGER, FOREIGN KEY(titleIdEpisode) REFERENCES media(titleId), FOREIGN KEY(titleIdShow) REFERENCES media(titleId));";
            this.connection.createStatement().execute(selectSql);
            selectSql = "CREATE TABLE people(personId INTEGER, name text, dateOfBirth INTEGER, dateOfPassing INTEGER, Primary Key(personId));";
            this.connection.createStatement().execute(selectSql);
            selectSql = "CREATE TABLE knownFor(personId INTEGER, titleId INTEGER, FOREIGN KEY(personId) REFERENCES people(personId),FOREIGN KEY (titleId) REFERENCES media(titleId));";
            this.connection.createStatement().execute(selectSql);
            selectSql = "CREATE TABLE jobs(jobId INTEGER, jobName text, Primary Key(jobId));";
            this.connection.createStatement().execute(selectSql);
            selectSql = "CREATE TABLE works (personId INTEGER, jobId INTEGER, FOREIGN KEY(personId) REFERENCES people(personId), FOREIGN KEY (jobId) REFERENCES jobs(jobId));";
            this.connection.createStatement().execute(selectSql);
            selectSql = "CREATE TABLE workedOn (titleId INTEGER, personId INTEGER, position text, FOREIGN KEY(personId) REFERENCES people(personId),FOREIGN KEY (titleId) REFERENCES media(titleId));";
            this.connection.createStatement().execute(selectSql);
            selectSql = "CREATE TABLE characters(titleId INTEGER, personId INTEGER, characterId INTEGER IDENTITY(1,1), character text, FOREIGN KEY(personId) REFERENCES people(personId),FOREIGN KEY (titleId) REFERENCES media(titleId), Primary Key(titleId, personId, characterId));";
            this.connection.createStatement().execute(selectSql);
            selectSql = "CREATE TABLE platform (platformId INTEGER, platformName text, Primary Key (platformId));";
            this.connection.createStatement().execute(selectSql);
            selectSql = "CREATE TABLE availableOn (platformId INTEGER, titleId INTEGER, dateAdded text, FOREIGN KEY(platformId) REFERENCES platform(platformId), FOREIGN KEY (titleId) REFERENCES media(titleId));";
            this.connection.createStatement().execute(selectSql);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
