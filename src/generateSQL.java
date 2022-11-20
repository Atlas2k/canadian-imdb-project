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
        System.out.println(thisThing.searchEpisode("s Look where you want to go"));

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

    public String processReturn(String clientCommand) {
        String preparedString = clientCommand.trim();
        preparedString = preparedString.replace("\"", "'");
        return preparedString;
    }

    public String searchMovie(String clientCommand) {
        String builtResult = "Movies matching the name " + clientCommand.split(" ", 2)[1] + ":\n";
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
                        builtResult += processReturn(castSet.getString("name")) + ", " + castSet.getString("position");
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
        } catch (SQLException e) {
            e.printStackTrace();
        }

        if (builtResult.length() == originalLength) {
            builtResult += "No Result Found!\n";
        }
        return builtResult;
    }

    public String searchShow(String clientCommand) {
        String builtResult = "Shows matching the name " + clientCommand.split(" ", 2)[1] + ":\n";
        int originalLength = builtResult.length();
        clientCommand = processCommand(clientCommand);
        String showName = clientCommand.split(" ", 2)[1];
        try {
            PreparedStatement searchShow = connection.prepareStatement("SELECT * from media where title like ?;");
            searchShow.setString(1, "%" + showName + "%");
            ResultSet resultSet = searchShow.executeQuery();
            while (resultSet.next()) {
                if ((resultSet.getString("endYear") == "0"
                        || (resultSet.getString("endYear") != "1" && resultSet.getString("endYear") != null))) {
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
        return builtResult;
    }

    public String searchEpisode(String clientCommand) {
        String builtResult = "Episodes matching the name " + clientCommand.split(" ", 2)[1] + ":\n";
        int originalLength = builtResult.length();
        clientCommand = processCommand(clientCommand);
        String episodeName = clientCommand.split(" ", 2)[1];
        try {
            PreparedStatement searchEpisode = connection.prepareStatement("SELECT * from media where title like ?;");
            searchEpisode.setString(1, "%" + episodeName + "%");
            ResultSet resultSet = searchEpisode.executeQuery();
            while (resultSet.next()) {
                if (resultSet.getInt("endYear") == 1) {
                    builtResult += processReturn(resultSet.getString("title")) + "\n";
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
        } catch (SQLException e) {
            e.printStackTrace();
        }

        if (builtResult.length() == originalLength) {
            builtResult += "No Result Found!\n";
        }
        return builtResult;

    }
}
