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

    ResultSet resultSet;
    Statement statement;
    // Connect to your database.
    // Replace server name, username, and password with your credentials

    public generateSQL() {
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

        this.resultSet = null;

        try {Connection connection = DriverManager.getConnection(connectionUrl);

             this.statement = connection.createStatement();
             Tables.dropTables(statement);
             Tables.createTables(statement);
            String selectSql = "insert into people(personId,name,dateOfBirth,dateOfPassing) values (023523, 'Mayokun', 2001, null);";
            statement.execute(selectSql);
             //queries.searchActor(resultSet,statement);

        }

        catch (SQLException e) {
             e.printStackTrace();
        }

    }

    public String yourSearch(String input) throws SQLException {
        queries query = new queries();
        if(input.split(" ")[0].compareTo("a")==0){
            return query.searchID(resultSet,statement,input.split(" ")[1]);
        } // a is for searching by id

        if(input.split(" ")[0].compareTo("b")==0){
            return query.searchName(resultSet,statement,input.split(" ")[1]);
        } // a is for searching by id

        return null;
    }

}