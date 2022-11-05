import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

public class generateSQL {

    // Connect to your database.
    // Replace server name, username, and password with your credentials
    public static void main(String[] args) {

        Properties prop = new Properties();
        String fileName = "C:\\Users\\akint\\OneDrive\\Desktop\\movies\\src\\auth.cfg";
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

        if (username == null || password == null){
            System.out.println("Username or password not provided.");
            System.exit(1);
        }

        String connectionUrl =
                "jdbc:sqlserver://uranium.cs.umanitoba.ca:1433;"
                        + "database=cs3380;"
                        + "user=" + username + ";"
                        + "password="+ password +";"
                        + "encrypt=false;"
                        + "trustServerCertificate=false;"
                        + "loginTimeout=30;";

        ResultSet resultSet = null;
        Tables tables = new Tables();
        try {Connection connection = DriverManager.getConnection(connectionUrl);
             Statement statement = connection.createStatement();

             dropTables(statement);
             createTables(statement);
             //Inserting into tables.

        }

        catch (SQLException e) {
             e.printStackTrace();
        }
    }

    public static void dropTables(Statement statement) throws SQLException {
        String selectSql = "drop table if exists title";
        statement.executeQuery(selectSql);
        selectSql = "drop table if exists genre";
        statement.executeQuery(selectSql);
        selectSql = "drop table if exists partOf";
        statement.executeQuery(selectSql);
        selectSql = "drop table if exists movies";
        statement.executeQuery(selectSql);
        selectSql = "drop table if exists shows";
        statement.executeQuery(selectSql);
        selectSql = "drop table if exists episode";
        statement.executeQuery(selectSql);
        selectSql = "drop table if exists have";
        statement.executeQuery(selectSql);
        selectSql = "drop table if exists people";
        statement.executeQuery(selectSql);
        selectSql = "drop table if exists knownFor";
        statement.executeQuery(selectSql);
        selectSql = "drop table if exists jobs";
        statement.executeQuery(selectSql);
        selectSql = "drop table if exists works";
        statement.executeQuery(selectSql);
        selectSql = "drop table if exists workedOn";
        statement.executeQuery(selectSql);
        selectSql = "drop table if exists characters";
        statement.executeQuery(selectSql);
        selectSql = "drop table if exists platform";
        statement.executeQuery(selectSql);
        selectSql = "drop table if exists availableOn";
        statement.executeQuery(selectSql);

    }
    public static void createTables(Statement statement) throws SQLException {
        String selectSql = "CREATE TABLE media (titleid text, title text, releaseDate text, isAdult text, imdbRating text, language text);";
        statement.executeQuery(selectSql);
        selectSql = "CREATE TABLE genre (genreid text, genreName text);";
        statement.executeQuery(selectSql);
        selectSql = "CREATE TABLE partOf (genreid text, titleid text);";
        statement.executeQuery(selectSql);
        selectSql = "CREATE TABLE movies (titleid text, title text, releaseDate text, isAdult text, imdbRating text, language text, genreId text, runtime text);";
        statement.executeQuery(selectSql);
        selectSql = "CREATE TABLE shows (titleid text, title text, releaseDate text, isAdult text, imdbRating text, language text, genreid text, endDate text);";
        statement.executeQuery(selectSql);
        selectSql = "CREATE TABLE episode (titleid text, title text, releaseDate text, isAdult text, imdbRating text, language text, genreid text, seasonNumber text, episodeNumber text);";
        statement.executeQuery(selectSql);
        selectSql = "CREATE TABLE have (forign key for titleid and show == titleid );";
        statement.executeQuery(selectSql);
        selectSql = "CREATE TABLE people (personId text, name text, dateOfBirth text, dateOfPassing text);";
        statement.executeQuery(selectSql);
        selectSql = "CREATE TABLE knownFor (personId text, titleid text);";
        statement.executeQuery(selectSql);
        selectSql = "CREATE TABLE jobs (jobId text, jobName text);";
        statement.executeQuery(selectSql);
        selectSql = "CREATE TABLE works (genreid text, genreName text);";
        statement.executeQuery(selectSql);
        selectSql = "CREATE TABLE workedOn (titleid text, personId text, character text, position text);";
        statement.executeQuery(selectSql);
        selectSql = "CREATE TABLE characters (character text, titleid text, personId text);";
        statement.executeQuery(selectSql);
        selectSql = "CREATE TABLE platform (platformId text, platFormID text);";
        statement.executeQuery(selectSql);
        selectSql = "CREATE TABLE availableOn (platFormID text, titleid text, dateAdded text);";
        statement.executeQuery(selectSql);

    }


}
