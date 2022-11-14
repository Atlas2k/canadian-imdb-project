import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Tables {

    //FOREIGN KEY("sid") REFERENCES "store"("id"),
    public  Tables(){}


    public static void dropTables(Statement statement) throws SQLException {
//        String selectSql = "drop table if exists knownFor";
//        statement.execute(selectSql);
//        selectSql = "drop table if exists works";
//        statement.execute(selectSql);
//        selectSql = "drop table if exists partOf";
//        statement.execute(selectSql);
//        selectSql = "drop table if exists have";
//        statement.execute(selectSql);
//        selectSql = "drop table if exists media";
//        statement.execute(selectSql);
//        selectSql = "drop table if exists genre";
//        statement.execute(selectSql);
//        selectSql = "drop table if exists movies";
//        statement.execute(selectSql);
//        selectSql = "drop table if exists shows";
//        statement.execute(selectSql);
//        selectSql = "drop table if exists episode";
//        statement.execute(selectSql);
//        selectSql = "drop table if exists people";
//        statement.execute(selectSql);
//        selectSql = "drop table if exists jobs";
//        statement.execute(selectSql);
//        selectSql = "drop table if exists workedOn";
//        statement.execute(selectSql);
//        selectSql = "drop table if exists characters";
//        statement.execute(selectSql);
//        selectSql = "drop table if exists platform";
//        statement.execute(selectSql);
//        selectSql = "drop table if exists availableOn";
//        statement.execute(selectSql);

    }


    public static void createTables(Statement statement) throws SQLException {
//        String selectSql = "CREATE TABLE media(titleid INTEGER, title text, isAdult BIT, imdbRating float, language text, startYear INTEGER, endYear INTEGER, runTime INTEGER, seasonNumber INTEGER, episodeNumber INTEGER, Primary Key(titleid));";
//        statement.execute(selectSql);
//        selectSql = "CREATE TABLE genre(genreid INTEGER IDENTITY(1,1), genreName text, Primary Key(genreid));";
//        statement.execute(selectSql);
//        selectSql = "CREATE TABLE partOf(genreid INTEGER, titleid INTEGER, FOREIGN KEY(genreid) REFERENCES genre(genreid), FOREIGN KEY(titleid) REFERENCES media(titleid));";
//        statement.execute(selectSql);
//        selectSql = "CREATE TABLE have(titleidEpisode INTEGER, titleidShow INTEGER, FOREIGN KEY(titleidEpisode) REFERENCES media(titleid), FOREIGN KEY(titleidShow) REFERENCES media(titleid));";
//        statement.execute(selectSql);
//
//        selectSql = "CREATE TABLE people(personId INTEGER, name text, dateOfBirth INTEGER, dateOfPassing INTEGER, Primary Key(personId));";
//        statement.execute(selectSql);
//
//        selectSql = "CREATE TABLE knownFor(personId INTEGER, titleid INTEGER, FOREIGN KEY(personId) REFERENCES people(personId),FOREIGN KEY (personId) REFERENCES media(titleid));";
//        statement.execute(selectSql);
//        selectSql = "CREATE TABLE jobs(jobId INTEGER IDENTITY(1,1), jobName text not null, Primary Key(jobId));";
//        statement.execute(selectSql);
//        selectSql = "CREATE TABLE works (personId INTEGER, jobId INTEGER, FOREIGN KEY(personId) REFERENCES people(personId),FOREIGN KEY (jobId) REFERENCES jobs(jobId));";
//        statement.execute(selectSql);
//        selectSql = "CREATE TABLE workedOn (titleid INTEGER, personId INTEGER, character text, position text);";
//        statement.execute(selectSql);
//        selectSql = "CREATE TABLE characters(titleid INTEGER, personId INTEGER, character text);";
//        statement.execute(selectSql);
//        selectSql = "CREATE TABLE platform (platformId INTEGER IDENTITY(1,1), platformName text not null, Primary Key (platformId));";
//        statement.execute(selectSql);
//        selectSql = "CREATE TABLE availableOn (platformId INTEGER, titleid text, dateAdded INTEGER);";
//        statement.execute(selectSql);

    }


}
