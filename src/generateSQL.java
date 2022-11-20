import java.io.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

public class generateSQL {

    Statement statement;
    Connection connection;

    // Connect to your database.
    // Replace server name, username, and password with your credentials
    public static void main(String[] args) {
        generateSQL thisThing = new generateSQL();
        System.out.println(thisThing.recommendByGenreAndPlatform("s comedy, Netflix"));

    }

    public generateSQL() {
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
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public String processCommand(String clientCommand) {
        String preparedString = clientCommand.trim();
        preparedString = preparedString.replace("'", "\"");
        return preparedString;
    }

    public String processJobReturn(String returnValue) {
        String preparedString = returnValue.replace("_", " ");
        return preparedString;
    }

    public String processReturn(String clientCommand) {
        String preparedString = clientCommand.trim();
        preparedString = preparedString.replace("\"", "'");
        return preparedString;
    }

    public String searchMovie(String clientCommand) {
        String builtResult = "";
        if (clientCommand.trim().split(" ", 2).length > 1) {
            builtResult = "Movies matching the name " + clientCommand.split(" ", 2)[1].trim() + ":\n";
            int originalLength = builtResult.length();
            clientCommand = processCommand(clientCommand);
            String movieName = clientCommand.split(" ", 2)[1];
            try {
                PreparedStatement searchMovie = connection.prepareStatement("SELECT * from media where title like ?;");
                searchMovie.setString(1, "%" + movieName + "%");
                ResultSet resultSet = searchMovie.executeQuery();
                while (resultSet.next()) {
                    if (resultSet.getString("endYear") == null) {
                        builtResult += processReturn(resultSet.getString("title")) + "\n";
                        builtResult += "Release Date: " + resultSet.getInt("startYear") + "\n";
                        builtResult += "Runtime: " + resultSet.getInt("runtIme") + "\n";
                        if (resultSet.getBoolean("isAdult") == true) {
                            builtResult += "Rating: Adult\n";
                        } else {
                            builtResult += "Rating: Everyone\n";
                        }
                        builtResult += "IMDb Rating: " + resultSet.getFloat("imdbRating") + "\n";

                        // Adding Genre Info
                        int titleId = resultSet.getInt("titleId");
                        String sqlCommand = "select genreName from media ";
                        sqlCommand += "join partOf on media.titleId = partOf.titleId ";
                        sqlCommand += "join genre on partOf.genreId = genre.genreId ";
                        sqlCommand += "where media.titleId = ?";
                        searchMovie = connection.prepareStatement(sqlCommand);
                        searchMovie.setInt(1, titleId);
                        ResultSet genreResultSet = searchMovie.executeQuery();
                        if (genreResultSet.next()) {
                            builtResult += "Genre: " + genreResultSet.getString("genreName");
                            while (genreResultSet.next()) {
                                builtResult += ", " + genreResultSet.getString("genreName");
                            }
                            builtResult += "\n";
                        }

                        // Adding Cast and Character Information
                        sqlCommand = "select people.name, workedOn.position, characters.character from media ";
                        sqlCommand += "left join workedOn on media.titleId = workedOn.titleId ";
                        sqlCommand += "left join people on workedOn.personId = people.personId ";
                        sqlCommand += "left join characters on media.titleId = characters.titleId ";
                        sqlCommand += "and people.personId = characters.personId where media.titleId = ?";
                        searchMovie = connection.prepareStatement(sqlCommand);
                        searchMovie.setInt(1, titleId);
                        ResultSet castSet = searchMovie.executeQuery();
                        if (castSet.next()) {
                            builtResult += "Cast:\n";
                            builtResult += processReturn(castSet.getString("name")) + ", "
                                    + castSet.getString("position");
                            if (castSet.getString("character") != null) {
                                builtResult += " as " + processReturn(castSet.getString("character"));
                            }
                            builtResult += "\n";
                            while (castSet.next()) {
                                builtResult += castSet.getString("name") + ", " + castSet.getString("position");
                                if (castSet.getString("character") != null) {
                                    builtResult += " as " + processReturn(castSet.getString("character"));
                                }
                                builtResult += "\n";
                            }
                        }

                        // Adding Available On Information
                        sqlCommand = "select platform.platformName, availableOn.dateAdded from media ";
                        sqlCommand += "join availableOn on media.titleId = availableOn.titleId ";
                        sqlCommand += "join platform on availableOn.platformId = platform.platformId ";
                        sqlCommand += "where media.titleId = ?;";
                        searchMovie = connection.prepareStatement(sqlCommand);
                        searchMovie.setInt(1, titleId);
                        ResultSet platformSet = searchMovie.executeQuery();
                        if (platformSet.next()) {
                            builtResult += "Available On:\n";
                            builtResult += platformSet.getString("platformName");
                            if (platformSet.getString("dateAdded") != "") {
                                builtResult += " since " + platformSet.getString("dateAdded");
                            }
                            builtResult += "\n";
                            while (platformSet.next()) {
                                builtResult += platformSet.getString("platformName");
                                if (platformSet.getString("dateAdded") != "") {
                                    builtResult += " since " + platformSet.getString("dateAdded");
                                }
                                builtResult += "\n";
                            }
                        }
                        builtResult += "------------------------------------" + "\n";
                    }
                }
                if (builtResult.length() == originalLength) {
                    builtResult += "No Result Found!\n";
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else {
            builtResult = "Cannot return result for empty string!\n";
        }
        return builtResult;
    }

    public String searchShow(String clientCommand) {
        String builtResult = "";
        if (clientCommand.trim().split(" ", 2).length > 1) {
            builtResult = "Shows matching the name " + clientCommand.split(" ", 2)[1].trim() + ":\n";
            int originalLength = builtResult.length();
            clientCommand = processCommand(clientCommand);
            String showName = clientCommand.split(" ", 2)[1];
            try {
                PreparedStatement searchShow = connection.prepareStatement("SELECT * from media where title like ?;");
                searchShow.setString(1, "%" + showName + "%");
                ResultSet resultSet = searchShow.executeQuery();
                while (resultSet.next()) {
                    if ((resultSet.getInt("endYear") == 0
                            || (resultSet.getInt("endYear") != 1 && resultSet.getString("endYear") != null))) {
                        builtResult += processReturn(resultSet.getString("title")) + "\n";
                        builtResult += "Start Date: " + resultSet.getInt("startYear") + "\n";
                        if (resultSet.getInt("endYear") != 0) {
                            builtResult += "End Date: " + resultSet.getInt("endYear") + "\n";
                        } else {
                            builtResult += "End Date: Ongoing" + "\n";
                        }
                        if (resultSet.getBoolean("isAdult") == true) {
                            builtResult += "Rating: Adult\n";
                        } else {
                            builtResult += "Rating: Everyone\n";
                        }
                        builtResult += "IMDb Rating: " + resultSet.getFloat("imdbRating") + "\n";

                        // Adding Genre Info
                        int titleId = resultSet.getInt("titleId");
                        String sqlCommand = "select genreName from media ";
                        sqlCommand += "join partOf on media.titleId = partOf.titleId ";
                        sqlCommand += "join genre on partOf.genreId = genre.genreId ";
                        sqlCommand += "where media.titleId = ?";
                        searchShow = connection.prepareStatement(sqlCommand);
                        searchShow.setInt(1, titleId);
                        ResultSet genreResultSet = searchShow.executeQuery();
                        if (genreResultSet.next()) {
                            builtResult += "Genre: " + genreResultSet.getString("genreName");
                            while (genreResultSet.next()) {
                                builtResult += ", " + genreResultSet.getString("genreName");
                            }
                            builtResult += "\n";
                        }

                        // Adding Cast and Character Information
                        sqlCommand = "select people.name, workedOn.position, characters.character from media ";
                        sqlCommand += "left join workedOn on media.titleId = workedOn.titleId ";
                        sqlCommand += "left join people on workedOn.personId = people.personId ";
                        sqlCommand += "left join characters on media.titleId = characters.titleId ";
                        sqlCommand += "and people.personId = characters.personId where media.titleId = ?";
                        searchShow = connection.prepareStatement(sqlCommand);
                        searchShow.setInt(1, titleId);
                        ResultSet castSet = searchShow.executeQuery();
                        if (castSet.next()) {
                            builtResult += "Cast:\n";
                            if (castSet.getString("name") != null) {
                                builtResult += processReturn(castSet.getString("name")) + ", "
                                        + castSet.getString("position");
                                if (castSet.getString("character") != null) {
                                    builtResult += " as " + processReturn(castSet.getString("character"));
                                }
                            }
                            builtResult += "\n";
                            while (castSet.next()) {
                                if (castSet.getString("name") != null) {
                                    builtResult += processReturn(castSet.getString("name")) + ", "
                                            + castSet.getString("position");

                                    if (castSet.getString("character") != null) {
                                        builtResult += " as " + processReturn(castSet.getString("character"));
                                    }
                                }
                                builtResult += "\n";
                            }
                        }

                        // Adding Available On Information
                        sqlCommand = "select platform.platformName, availableOn.dateAdded from media ";
                        sqlCommand += "join availableOn on media.titleId = availableOn.titleId ";
                        sqlCommand += "join platform on availableOn.platformId = platform.platformId ";
                        sqlCommand += "where media.titleId = ?;";
                        searchShow = connection.prepareStatement(sqlCommand);
                        searchShow.setInt(1, titleId);
                        ResultSet platformSet = searchShow.executeQuery();
                        if (platformSet.next()) {
                            builtResult += "Available On:\n";
                            builtResult += platformSet.getString("platformName");
                            if (platformSet.getString("dateAdded") != "") {
                                builtResult += " since " + platformSet.getString("dateAdded");
                            }
                            builtResult += "\n";
                            while (platformSet.next()) {
                                builtResult += platformSet.getString("platformName");
                                if (platformSet.getString("dateAdded") != "") {
                                    builtResult += " since " + platformSet.getString("dateAdded");
                                }
                                builtResult += "\n";
                            }
                        }

                        // Adding Some Episode Information
                        sqlCommand = "select top 5 episodes.* from have ";
                        sqlCommand += "join media shows on have.titleIdShow = shows.titleId ";
                        sqlCommand += "join media episodes on have.titleIdEpisode = episodes.titleId ";
                        sqlCommand += "where shows.titleId = ? ";
                        sqlCommand += "order by imdbRating desc;";
                        searchShow = connection.prepareStatement(sqlCommand);
                        searchShow.setInt(1, titleId);
                        ResultSet episodeSet = searchShow.executeQuery();
                        if (episodeSet.next()) {
                            builtResult += "Some Available Episodes:\n";
                            builtResult += "Episode Name: " + processReturn(episodeSet.getString("title")) + "\n";
                            builtResult += "Season: " + episodeSet.getInt("seasonNumber") + "\n";
                            builtResult += "Episode: " + episodeSet.getInt("episodeNumber") + "\n";
                            builtResult += "IMDb Rating: " + resultSet.getFloat("imdbRating") + "\n";
                            builtResult += "--\n";
                            while (episodeSet.next()) {
                                builtResult += "Episode Name: " + processReturn(episodeSet.getString("title")) + "\n";
                                builtResult += "Season: " + episodeSet.getInt("seasonNumber") + "\n";
                                builtResult += "Episode: " + episodeSet.getInt("episodeNumber") + "\n";
                                builtResult += "IMDb Rating: " + resultSet.getFloat("imdbRating") + "\n";
                                builtResult += "--\n";
                            }
                        }
                        builtResult += "------------------------------------" + "\n";
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            if (builtResult.length() == originalLength) {
                builtResult += "No Result Found!\n";
            }
        } else {
            builtResult = "Cannot return result for empty string!\n";
        }
        return builtResult;
    }

    public String searchEpisode(String clientCommand) {
        String builtResult = "";
        if (clientCommand.trim().split(" ", 2).length > 1) {
            builtResult = "Episodes matching the name " + clientCommand.split(" ", 2)[1].trim() + ":\n";
            int originalLength = builtResult.length();
            clientCommand = processCommand(clientCommand);
            String episodeName = clientCommand.split(" ", 2)[1];
            try {
                PreparedStatement searchEpisode = connection
                        .prepareStatement("SELECT * from media where title like ?;");
                searchEpisode.setString(1, "%" + episodeName + "%");
                ResultSet resultSet = searchEpisode.executeQuery();
                while (resultSet.next()) {
                    if (resultSet.getInt("endYear") == 1) {
                        builtResult += processReturn(resultSet.getString("title")) + "\n";

                        // Getting parent show information if available
                        int titleId = resultSet.getInt("titleId");
                        String sqlCommand = "select show.title, show.titleId from have ";
                        sqlCommand += "join media show on show.titleId = have.titleIdShow ";
                        sqlCommand += "join media episode on episode.titleId = have.titleIdEpisode ";
                        sqlCommand += "where episode.titleId = ?;";
                        searchEpisode = connection.prepareStatement(sqlCommand);
                        searchEpisode.setInt(1, titleId);
                        ResultSet parentSet = searchEpisode.executeQuery();
                        if (parentSet.next()) {
                            builtResult += "Parent Show: " + processReturn(parentSet.getString("title")) + "\n";
                        }
                        builtResult += "Release Date: " + resultSet.getInt("startYear") + "\n";
                        builtResult += "Season: " + resultSet.getInt("seasonNumber") + "\n";
                        builtResult += "Episode: " + resultSet.getInt("episodeNumber") + "\n";
                        if (resultSet.getBoolean("isAdult") == true) {
                            builtResult += "Rating: Adult\n";
                        } else {
                            builtResult += "Rating: Everyone\n";
                        }
                        builtResult += "IMDb Rating: " + resultSet.getFloat("imdbRating") + "\n";

                        // Adding Genre Info
                        sqlCommand = "select genreName from media ";
                        sqlCommand += "join partOf on media.titleId = partOf.titleId ";
                        sqlCommand += "join genre on partOf.genreId = genre.genreId ";
                        sqlCommand += "where media.titleId = ?";
                        searchEpisode = connection.prepareStatement(sqlCommand);
                        searchEpisode.setInt(1, titleId);
                        ResultSet genreResultSet = searchEpisode.executeQuery();
                        if (genreResultSet.next()) {
                            builtResult += "Genre: " + genreResultSet.getString("genreName");
                            while (genreResultSet.next()) {
                                builtResult += ", " + genreResultSet.getString("genreName");
                            }
                            builtResult += "\n";
                        }

                        // Adding Cast and Character Information
                        sqlCommand = "select people.name, workedOn.position, characters.character from media ";
                        sqlCommand += "left join workedOn on media.titleId = workedOn.titleId ";
                        sqlCommand += "left join people on workedOn.personId = people.personId ";
                        sqlCommand += "left join characters on media.titleId = characters.titleId ";
                        sqlCommand += "and people.personId = characters.personId where media.titleId = ?";
                        searchEpisode = connection.prepareStatement(sqlCommand);
                        searchEpisode.setInt(1, titleId);
                        ResultSet castSet = searchEpisode.executeQuery();
                        if (castSet.next()) {
                            builtResult += "Cast:\n";
                            if (castSet.getString("name") != null) {
                                builtResult += processReturn(castSet.getString("name")) + ", "
                                        + castSet.getString("position");
                                if (castSet.getString("character") != null) {
                                    builtResult += " as " + processReturn(castSet.getString("character"));
                                }
                            }
                            builtResult += "\n";
                            while (castSet.next()) {
                                if (castSet.getString("name") != null) {
                                    builtResult += processReturn(castSet.getString("name")) + ", "
                                            + castSet.getString("position");

                                    if (castSet.getString("character") != null) {
                                        builtResult += " as " + processReturn(castSet.getString("character"));
                                    }
                                }
                                builtResult += "\n";
                            }
                        }

                        // Adding Available On Information
                        sqlCommand = "select platform.platformName, availableOn.dateAdded from media ";
                        sqlCommand += "join availableOn on media.titleId = availableOn.titleId ";
                        sqlCommand += "join platform on availableOn.platformId = platform.platformId ";
                        sqlCommand += "where media.titleId = ?;";
                        searchEpisode = connection.prepareStatement(sqlCommand);
                        searchEpisode.setInt(1, parentSet.getInt("titleId"));
                        ResultSet platformSet = searchEpisode.executeQuery();
                        if (platformSet.next()) {
                            builtResult += "Available On:\n";
                            builtResult += platformSet.getString("platformName");
                            if (platformSet.getString("dateAdded") != "") {
                                builtResult += " since " + platformSet.getString("dateAdded");
                            }
                            builtResult += "\n";
                            while (platformSet.next()) {
                                builtResult += platformSet.getString("platformName");
                                if (platformSet.getString("dateAdded") != "") {
                                    builtResult += " since " + platformSet.getString("dateAdded");
                                }
                                builtResult += "\n";
                            }
                        }
                        builtResult += "------------------------------------" + "\n";
                    }
                }
                if (builtResult.length() == originalLength) {
                    builtResult += "No Result Found!\n";
                }

            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else {
            builtResult = "Cannot return result for empty string!\n";
        }
        return builtResult;
    }

    public String searchPerson(String clientCommand) {
        String builtResult = "";
        if (clientCommand.trim().split(" ", 2).length > 1) {
            builtResult = "People in the Motion Picture Industry matching the name "
                    + clientCommand.split(" ", 2)[1].trim()
                    + ":\n";
            int originalLength = builtResult.length();
            clientCommand = processCommand(clientCommand);
            String personName = clientCommand.split(" ", 2)[1];
            try {
                PreparedStatement searchPerson = connection.prepareStatement("SELECT * from people where name like ?;");
                searchPerson.setString(1, "%" + personName + "%");
                ResultSet resultSet = searchPerson.executeQuery();
                while (resultSet.next()) {
                    builtResult += processReturn(resultSet.getString("name")) + "\n";
                    if (resultSet.getInt("dateOfBirth") != 0) {
                        builtResult += "Year of birth: " + resultSet.getInt("dateOfBirth") + "\n";
                    } else {
                        builtResult += "Year of birth: Unknown\n";
                    }
                    if (resultSet.getInt("dateOfPassing") != 0) {
                        builtResult += "Year of passing: " + resultSet.getInt("dateOfPassing") + "\n";
                    }
                    int personId = resultSet.getInt("personId");

                    // Get Job
                    String sqlCommand = "select jobs.jobName from works ";
                    sqlCommand += "join jobs on jobs.jobId = works.jobId ";
                    sqlCommand += "join people on people.personId = works.personId ";
                    sqlCommand += "where people.personId = ?;";
                    searchPerson = connection.prepareStatement(sqlCommand);
                    searchPerson.setInt(1, personId);
                    ResultSet jobsSet = searchPerson.executeQuery();
                    if (jobsSet.next()) {
                        if (!jobsSet.getString("jobName").equals("null")) {
                            builtResult += "Jobs: " + processJobReturn(jobsSet.getString("jobName"));
                        }
                        while (jobsSet.next()) {
                            if (!jobsSet.getString("jobName").equals("null")) {
                                builtResult += ", " + processJobReturn(jobsSet.getString("jobName"));
                            }
                        }
                        builtResult += "\n";
                    }

                    // Known for information
                    sqlCommand = "select media.title, workedOn.position, characters.character from knownFor ";
                    sqlCommand += "join media on media.titleId = knownFor.titleId ";
                    sqlCommand += "join people on people.personId = knownFor.personId ";
                    sqlCommand += "join workedOn on workedOn.titleId = media.titleId ";
                    sqlCommand += "and workedOn.personId = people.personId ";
                    sqlCommand += "left join characters on media.titleId = characters.titleId ";
                    sqlCommand += "and people.personId = characters.personId ";
                    sqlCommand += "where people.personId = ?;";
                    searchPerson = connection.prepareStatement(sqlCommand);
                    searchPerson.setInt(1, personId);
                    ResultSet knownForSet = searchPerson.executeQuery();
                    if (knownForSet.next()) {
                        builtResult += "Known for: "
                                + processReturn(
                                        knownForSet.getString("title") + ", " + knownForSet.getString("position"));
                        if (knownForSet.getString("character") != null) {
                            builtResult += " as " + processReturn(knownForSet.getString("character"));
                        }
                        builtResult += "\n";
                    }
                    builtResult += "------------------------------------" + "\n";
                }
                if (builtResult.length() == originalLength) {
                    builtResult += "No Result Found!\n";
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else {
            builtResult = "Cannot return result for empty string!\n";
        }
        return builtResult;
    }

    public String allMediaForPerson(String clientCommand) {
        String builtResult = "";
        if (clientCommand.trim().split(" ", 2).length > 1) {
            builtResult = "All media worked on by people matching the name "
                    + clientCommand.split(" ", 2)[1].trim()
                    + ":\n";
            int originalLength = builtResult.length();
            clientCommand = processCommand(clientCommand);
            String personName = clientCommand.split(" ", 2)[1];
            try {
                PreparedStatement searchPerson = connection.prepareStatement("SELECT * from people where name like ?;");
                searchPerson.setString(1, "%" + personName + "%");
                ResultSet resultSet = searchPerson.executeQuery();
                while (resultSet.next()) {
                    builtResult += processReturn(resultSet.getString("name")) + "\n";
                    int personId = resultSet.getInt("personId");

                    // Get all media
                    String sqlCommand = "select media.titleId, media.endYear, media.title, workedOn.position, characters.character from workedOn ";
                    sqlCommand += "join media on media.titleId = workedOn.titleId ";
                    sqlCommand += "join people on people.personId = workedOn.personId ";
                    sqlCommand += "left join characters on media.titleId = characters.titleId ";
                    sqlCommand += "and people.personId = characters.personId ";
                    sqlCommand += "where people.personId = ?;";
                    searchPerson = connection.prepareStatement(sqlCommand);
                    searchPerson.setInt(1, personId);
                    ResultSet mediaSet = searchPerson.executeQuery();
                    builtResult += "Worked On:\n";
                    while (mediaSet.next()) {
                        builtResult += processReturn(mediaSet.getString("title"));

                        // Get parent show if this is an episode
                        if (mediaSet.getInt("endYear") == 1) {
                            int titleId = mediaSet.getInt("titleId");
                            sqlCommand = "select show.title, show.titleId from have ";
                            sqlCommand += "join media show on show.titleId = have.titleIdShow ";
                            sqlCommand += "join media episode on episode.titleId = have.titleIdEpisode ";
                            sqlCommand += "where episode.titleId = ?;";
                            searchPerson = connection.prepareStatement(sqlCommand);
                            searchPerson.setInt(1, titleId);
                            ResultSet parentSet = searchPerson.executeQuery();
                            if (parentSet.next()) {
                                builtResult += " from " + processReturn(parentSet.getString("title"));
                            }
                        }
                        builtResult += ", " + mediaSet.getString("position");
                        if (mediaSet.getString("character") != null) {
                            builtResult += " as " + processReturn(mediaSet.getString("character"));
                        }
                        builtResult += "\n";
                    }
                    builtResult += "------------------------------------" + "\n";
                }
                if (builtResult.length() == originalLength) {
                    builtResult += "No Result Found!\n";
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            if (builtResult.length() == originalLength) {
                builtResult += "No Result Found!\n";
            }
        } else {
            builtResult = "Cannot return result for empty string!\n";
        }
        return builtResult;
    }

    public String allPeoplePlayingACharacter(String clientCommand) {
        String builtResult = "";
        if (clientCommand.trim().split(" ", 2).length > 1) {
            builtResult = "All people who acted as a character called "
                    + clientCommand.split(" ", 2)[1].trim()
                    + ":\n";
            int originalLength = builtResult.length();
            clientCommand = processCommand(clientCommand);
            String characterName = clientCommand.split(" ", 2)[1];
            try {
                String sqlCommand = "select media.endYear, media.title, people.name, characters.character from characters ";
                sqlCommand += "join media on characters.titleId = media.titleId ";
                sqlCommand += "join people on characters.personId = people.personId ";
                sqlCommand += "where character like ? and (media.endYear != 1 or media.endYear is null); ";
                PreparedStatement searchCharacter = connection.prepareStatement(sqlCommand);
                searchCharacter.setString(1, characterName);
                ResultSet resultSet = searchCharacter.executeQuery();
                while (resultSet.next()) {
                    builtResult += processReturn(resultSet.getString("name")) + " in the ";
                    if (resultSet.getString("endYear") != null) {
                        builtResult += "show ";
                    } else {
                        builtResult += "movie ";
                    }
                    builtResult += processReturn(resultSet.getString("title")) + "\n";
                }
                if (builtResult.length() == originalLength) {
                    builtResult += "No Result Found!\n";
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            if (builtResult.length() == originalLength) {
                builtResult += "No Result Found!\n";
            }
        } else {
            builtResult = "Cannot return result for empty string!\n";
        }
        return builtResult;
    }

    public String availableOnSet(String clientCommand) {
        String builtResult = "";
        if (clientCommand.trim().split(" ", 2).length > 1) {
            builtResult = "All media avialable on all of "
                    + clientCommand.split(" ", 2)[1]
                    + ":\n";
            int originalLength = builtResult.length();
            clientCommand = processCommand(clientCommand);
            String platforms = clientCommand.split(" ", 2)[1];
            String[] platformList = platforms.split(",");
            try {
                String sqlCommand = "with showOnPlatform as ( ";
                sqlCommand += "select media.titleId, platform.platformName from availableOn ";
                sqlCommand += "join media on media.titleId = availableOn.titleId ";
                sqlCommand += "join platform on platform.platformId = availableOn.platformId) ";
                sqlCommand += "select title, endYear from media where titleId in ( ";
                sqlCommand += "select titleId from showOnPlatform ";
                sqlCommand += "where platformName like ? intersect ";
                sqlCommand += "select titleId from showOnPlatform ";
                sqlCommand += "where platformName like ? intersect ";
                sqlCommand += "select titleId from showOnPlatform ";
                sqlCommand += "where platformName like ? intersect ";
                sqlCommand += "select titleId from showOnPlatform ";
                sqlCommand += "where platformName like ?);";
                PreparedStatement searchPlatforms = connection.prepareStatement(sqlCommand);
                for (int i = 1; i <= 4; i++) {
                    searchPlatforms.setString(i, platformList[(i - 1) % platformList.length].trim());
                }
                ResultSet resultSet = searchPlatforms.executeQuery();
                while (resultSet.next()) {
                    if (resultSet.getString("endYear") != null) {
                        builtResult += "The show ";
                    } else {
                        builtResult += "The movie ";
                    }
                    builtResult += processReturn(resultSet.getString("title")) + "\n";
                }
                if (builtResult.length() == originalLength) {
                    builtResult += "No Result Found!\n";
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            if (builtResult.length() == originalLength) {
                builtResult += "No Result Found!\n";
            }
        } else {
            builtResult = "Cannot return result for empty string!\n";
        }
        return builtResult;
    }

    public String mediaByPersonAndPlatform(String clientCommand) {
        String builtResult = "";
        if (clientCommand.trim().split(" ", 2).length > 1) {
            String personAndPlatform = clientCommand.split(" ", 2)[1];
            String[] personAndPlatformList = personAndPlatform.split(",");
            builtResult = "All media by " + personAndPlatformList[0].trim()
                    + " on " + personAndPlatformList[1].trim() + ":\n";
            int originalLength = builtResult.length();
            personAndPlatformList[0] = processCommand(personAndPlatformList[0]);
            try {
                String sqlCommand = "select title, endYear from workedOn  ";
                sqlCommand += "join media on workedOn.titleId = media.titleId ";
                sqlCommand += "join people on workedOn.personId = people.personId ";
                sqlCommand += "join availableOn on media.titleId = availableOn.titleId ";
                sqlCommand += "join platform on availableOn.platformId = platform.platformId ";
                sqlCommand += "where people.name like ? and platform.platformName like ?; ";
                PreparedStatement searchPlatforms = connection.prepareStatement(sqlCommand);
                searchPlatforms.setString(1, personAndPlatformList[0].trim());
                searchPlatforms.setString(2, personAndPlatformList[1].trim());
                ResultSet resultSet = searchPlatforms.executeQuery();
                while (resultSet.next()) {
                    if (resultSet.getString("endYear") != null) {
                        builtResult += "The show ";
                    } else {
                        builtResult += "The movie ";
                    }
                    builtResult += processReturn(resultSet.getString("title")) + "\n";
                }
                if (builtResult.length() == originalLength) {
                    builtResult += "No Result Found!\n";
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            if (builtResult.length() == originalLength) {
                builtResult += "No Result Found!\n";
            }
        } else {
            builtResult = "Cannot return result for empty string!\n";
        }
        return builtResult;
    }

    public String recommendByGenreAndPlatform(String clientCommand) {
        String builtResult = "";
        if (clientCommand.trim().split(" ", 2).length > 1) {
            String genreAndPlatform = clientCommand.split(" ", 2)[1];
            String[] genreAndPlatformList = genreAndPlatform.split(",");
            builtResult = "Recommendation for " + genreAndPlatformList[0].trim()
                    + " media on " + genreAndPlatformList[1].trim() + ":\n";
            int originalLength = builtResult.length();
            try {
                String sqlCommand = "select top 10 title, endYear, imdbRating from media ";
                sqlCommand += "join partOf on partOf.titleId = media.titleId ";
                sqlCommand += "join genre on genre.genreId = partOf.genreId ";
                sqlCommand += "join availableOn on media.titleId = availableOn.titleId ";
                sqlCommand += "join platform on availableOn.platformId = platform.platformId ";
                sqlCommand += "where genreName like ? and platformName like ? ";
                sqlCommand += "order by imdbRating desc;";
                PreparedStatement searchPlatforms = connection.prepareStatement(sqlCommand);
                searchPlatforms.setString(1, genreAndPlatformList[0].trim());
                searchPlatforms.setString(2, genreAndPlatformList[1].trim());
                ResultSet resultSet = searchPlatforms.executeQuery();
                while (resultSet.next()) {
                    if (resultSet.getString("endYear") != null) {
                        builtResult += "The show ";
                    } else {
                        builtResult += "The movie ";
                    }
                    builtResult += processReturn(resultSet.getString("title")) + " with an IMDb rating of "
                            + resultSet.getFloat("imdbRating") + "\n";
                }
                if (builtResult.length() == originalLength) {
                    builtResult += "No Result Found!\n";
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            if (builtResult.length() == originalLength) {
                builtResult += "No Result Found!\n";
            }
        } else {
            builtResult = "Cannot return result for empty string!\n";
        }
        return builtResult;
    }
}
