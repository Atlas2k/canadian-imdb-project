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

        try {
            // The newInstance() call is a work around for some
            // broken Java implementations
            Class.forName("com.mysql.jdbc.Driver");
        } catch (Exception ex) {
            // handle the error
        }

        try {Connection connection = DriverManager.getConnection(connectionUrl);
             Statement statement = connection.createStatement();

             Tables.dropTables(statement);
             Tables.createTables(statement);
             //Inserting into tables.

        }

        catch (SQLException e) {
             e.printStackTrace();
        }
    }



}
