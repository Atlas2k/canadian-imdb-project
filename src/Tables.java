import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Tables {

    //FOREIGN KEY("sid") REFERENCES "store"("id"),
    public  Tables(){}


    public static void dropTables(Statement statement, ResultSet resultSet) throws SQLException {
        String selectSql = "drop table if exists title";
        statement.execute(selectSql);
        selectSql = "drop table if exists genre";
        statement.execute(selectSql);
        selectSql = "drop table if exists partOf";
        statement.execute(selectSql);
        selectSql = "drop table if exists movies";
        statement.execute(selectSql);
        selectSql = "drop table if exists shows";
        statement.execute(selectSql);
        selectSql = "drop table if exists episode";
        statement.execute(selectSql);
        selectSql = "drop table if exists have";
        statement.execute(selectSql);
        selectSql = "drop table if exists people";
        statement.execute(selectSql);
        selectSql = "drop table if exists knownFor";
        statement.execute(selectSql);
        selectSql = "drop table if exists jobs";
        statement.execute(selectSql);
        selectSql = "drop table if exists works";
        statement.execute(selectSql);
        selectSql = "drop table if exists workedOn";
        statement.execute(selectSql);
        selectSql = "drop table if exists characters";
        statement.execute(selectSql);
        selectSql = "drop table if exists platform";
        statement.execute(selectSql);
        selectSql = "drop table if exists availableOn";
        statement.execute(selectSql);

    }





    public static void createTables(Statement statement) throws SQLException {
        String selectSql = "CREATE TABLE media (titleid text primarykey IDENTITY(1,1), title text not null, isAdult boolean not null, imdbRating double not null, language text not null);";
        statement.executeQuery(selectSql);
        selectSql = "CREATE TABLE genre (genreid text primary key IDENTITY(1,1), genreName text not null);";
        statement.executeQuery(selectSql);
        selectSql = "CREATE TABLE partOf (genreid text primary key IDENTITY(1,1), titleid text primarykey IDENTITY(1,1), FOREIGN KEY(genreid) REFERENCES genre(genreid),FOREIGN KEY(titleid) REFERENCES title(titleid));";
        statement.executeQuery(selectSql);
        selectSql = "CREATE TABLE movies (titleid text primarykey IDENTITY(1,1), title text not null, isAdult boolean not null, imdbRating double not null, language text not null, genreid text primary key IDENTITY(1,1), runtime Integer not null);";
        statement.executeQuery(selectSql);
        selectSql = "CREATE TABLE shows (titleid text primarykey IDENTITY(1,1), title text not null, isAdult boolean not null, imdbRating double not null, language text not null, genreid text primary key IDENTITY(1,1), endDate Integer not null);";
        statement.executeQuery(selectSql);
        selectSql = "CREATE TABLE episode (titleid text primarykey IDENTITY(1,1), title text not null, isAdult boolean not null, imdbRating double not null, language text not null, genreid text primary key IDENTITY(1,1), seasonNumber Integer not null, episodeNumber Integer not null);";
        statement.executeQuery(selectSql);
        selectSql = "CREATE TABLE have (titleidEpisode text primarykey IDENTITY(1,1),titleidShow text primarykey IDENTITY(1,1),FOREIGN KEY(titleidEpisode) REFERENCES episode(titleid),FOREIGN KEY(titleidShow) REFERENCES show(titleid) );";
        statement.executeQuery(selectSql);
        selectSql = "CREATE TABLE people (personId text primary key  IDENTITY(1,1), name text not null, dateOfBirth Integer not null, dateOfPassing Integer not null);";
        statement.executeQuery(selectSql);
        selectSql = "CREATE TABLE knownFor (personId text primary key  IDENTITY(1,1), titleid text primarykey IDENTITY(1,1),FOREIGN KEY(personId) REFERENCES person(id),FOREIGN KEY(personId) REFERENCES title(titleid));";
        statement.executeQuery(selectSql);
        selectSql = "CREATE TABLE jobs (jobId text primary key IDENTITY(1,1), jobName text not null);";
        statement.executeQuery(selectSql);
        selectSql = "CREATE TABLE works (personId text primary key  IDENTITY(1,1), jobId text primary key IDENTITY(1,1));";
        statement.executeQuery(selectSql);
        selectSql = "CREATE TABLE workedOn (titleid text primarykey IDENTITY(1,1), personId text primary key  IDENTITY(1,1), character text not null, position text not null);";
        statement.executeQuery(selectSql);
        selectSql = "CREATE TABLE characters (titleid text primarykey IDENTITY(1,1), personId text primary key  IDENTITY(1,1), character text not null);";
        statement.executeQuery(selectSql);
        selectSql = "CREATE TABLE platform (platformId text primary key IDENTITY(1,1), platformName text not null  IDENTITY(1,1));";
        statement.executeQuery(selectSql);
        selectSql = "CREATE TABLE availableOn (platformId text primary key IDENTITY(1,1), titleid text primarykey IDENTITY(1,1), dateAdded Integer not null);";
        statement.executeQuery(selectSql);

    }
}
