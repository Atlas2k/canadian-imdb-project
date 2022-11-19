import java.io.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

public class generateSQL {

    ResultSet resultSet;
    Statement statement;
    Connection connection;

    // Connect to your database.
    // Replace server name, username, and password with your credentials
    public static void main(String[] args) {
        generateSQL thisThing = new generateSQL();
        System.out.println(thisThing.searchMovie("m A Beautiful Mind"));

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

        this.resultSet = null;

        try {
            this.connection = DriverManager.getConnection(connectionUrl);
            this.statement = connection.createStatement();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public String searchMovie(String clientCommand) {
        String movieName = clientCommand.split(" ", 2)[1];
        String builtResult = "";
        try {
            PreparedStatement searchMovie = connection.prepareStatement("SELECT * from media where title like ?;");
            searchMovie.setString(1, movieName);
            ResultSet resultSet = searchMovie.executeQuery();
            while (resultSet.next()) {
                if (builtResult.length() != 0) {
                    builtResult += "------------------------------------" + "\n";
                } else {
                    builtResult += "Movies matching the name " + movieName + ":\n";
                }
                if (resultSet.getString("endYear") == null) {
                    builtResult += resultSet.getString("title") + "\n";
                    builtResult += "Release Data: " + resultSet.getInt("startYear") + "\n";
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
                        builtResult += castSet.getString("name") + ", " + castSet.getString("position");
                        if (castSet.getString("character") != null) {
                            builtResult += " as " + castSet.getString("character");
                        }
                        builtResult += "\n";
                        while (castSet.next()) {
                            builtResult += castSet.getString("name") + ", " + castSet.getString("position");
                            if (castSet.getString("character") != null) {
                                builtResult += " as " + castSet.getString("character");
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
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return builtResult;
    }

    public String searchShow(String clientCommand) {
        String builtResult = "";
        return builtResult;
    }
}
