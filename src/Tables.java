import java.sql.SQLException;
import java.sql.Statement;

public class Tables {
    public  Tables(){}
    public static void createTitleTable(Statement statement) throws SQLException {
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




        selectSql = "CREATE TABLE media (titleid text, title text, releaseDate text, isAdult text, imdbRating text, language text);";
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
