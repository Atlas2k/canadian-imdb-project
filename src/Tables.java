import java.sql.SQLException;
import java.sql.Statement;

public class Tables {

    //FOREIGN KEY("sid") REFERENCES "store"("id"),
    public  Tables(){}


    public static void dropTables(Statement statement) throws SQLException {
        String selectSql = "drop table if exists knownFor";
        statement.execute(selectSql);
        selectSql = "drop table if exists works";
        statement.execute(selectSql);
        selectSql = "drop table if exists partOf";
        statement.execute(selectSql);
        selectSql = "drop table if exists have";
        statement.execute(selectSql);
        selectSql = "drop table if exists genre";
        statement.execute(selectSql);
        selectSql = "drop table if exists jobs";
        statement.execute(selectSql);
        selectSql = "drop table if exists workedOn";
        statement.execute(selectSql);
        selectSql = "drop table if exists characters";
        statement.execute(selectSql);
        selectSql = "drop table if exists availableOn";
        statement.execute(selectSql);
        selectSql = "drop table if exists platform";
        statement.execute(selectSql);
        selectSql = "drop table if exists people";
        statement.execute(selectSql);
        selectSql = "drop table if exists media";
        statement.execute(selectSql);
    }


    public static void createTables(Statement statement) throws SQLException {
        String selectSql = "CREATE TABLE media(titleId INTEGER, title text, isAdult BIT, imdbRating float, language text, startYear INTEGER, endYear INTEGER, runTime INTEGER, seasonNumber INTEGER, episodeNumber INTEGER, Primary Key(titleId));";
        statement.execute(selectSql);
        selectSql = "CREATE TABLE genre(genreId INTEGER, genreName text, Primary Key(genreId));";
        statement.execute(selectSql);
        selectSql = "CREATE TABLE partOf(genreId INTEGER, titleId INTEGER, FOREIGN KEY(genreId) REFERENCES genre(genreId), FOREIGN KEY(titleId) REFERENCES media(titleId));";
        statement.execute(selectSql);
        selectSql = "CREATE TABLE have(titleIdEpisode INTEGER, titleIdShow INTEGER, FOREIGN KEY(titleIdEpisode) REFERENCES media(titleId), FOREIGN KEY(titleIdShow) REFERENCES media(titleId));";
        statement.execute(selectSql);
        selectSql = "CREATE TABLE people(personId INTEGER, name text, dateOfBirth INTEGER, dateOfPassing INTEGER, Primary Key(personId));";
        statement.execute(selectSql);
        selectSql = "CREATE TABLE knownFor(personId INTEGER, titleId INTEGER, FOREIGN KEY(personId) REFERENCES people(personId),FOREIGN KEY (titleId) REFERENCES media(titleId));";
        statement.execute(selectSql);
        selectSql = "CREATE TABLE jobs(jobId INTEGER, jobName text, Primary Key(jobId));";
        statement.execute(selectSql);
        selectSql = "CREATE TABLE works (personId INTEGER, jobId INTEGER, FOREIGN KEY(personId) REFERENCES people(personId), FOREIGN KEY (jobId) REFERENCES jobs(jobId));";
        statement.execute(selectSql);
        selectSql = "CREATE TABLE workedOn (titleId INTEGER, personId INTEGER, position text, FOREIGN KEY(personId) REFERENCES people(personId),FOREIGN KEY (titleId) REFERENCES media(titleId));"; 
        statement.execute(selectSql);
        selectSql = "CREATE TABLE characters(titleId INTEGER, personId INTEGER, characterId INTEGER IDENTITY(1,1), character text, FOREIGN KEY(personId) REFERENCES people(personId),FOREIGN KEY (titleId) REFERENCES media(titleId), Primary Key(titleId, personId, characterId));";
        statement.execute(selectSql);
        selectSql = "CREATE TABLE platform (platformId INTEGER, platformName text, Primary Key (platformId));";
        statement.execute(selectSql);
        selectSql = "CREATE TABLE availableOn (platformId INTEGER, titleId INTEGER, dateAdded INTEGER, FOREIGN KEY(platformId) REFERENCES platform(platformId), FOREIGN KEY (titleId) REFERENCES media(titleId));";
        statement.execute(selectSql);
    }


}
