import java.io.*;
import java.sql.Connection;
import java.sql.DriverManager;
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

        try {this.connection = DriverManager.getConnection(connectionUrl);

             this.statement = connection.createStatement();
             Tables.dropTables(statement);
             Tables.createTables(statement);

            System.out.println("Exporting ....");
            //titlesAkas
           // loadData("C:\\Users\\akint\\OneDrive\\Desktop\\COMP3380Proj\\title.Akas.sql");
            //tile name, isadult, language ,start year, end year
            // loadData("C:\\Users\\akint\\OneDrive\\Desktop\\COMP3380Proj\\title.basics.sql");
            //IMDB ratings
            //loadData("C:\\Users\\akint\\OneDrive\\Desktop\\COMP3380Proj\\title.ratings.sql");
//            connection.createStatement().execute("Delete from media where titleid = 0;");
//            connection.createStatement().execute("Delete from media where titleid is NULL;");







            loadData("C:\\Users\\akint\\OneDrive\\Desktop\\COMP3380Proj\\partOf.title.basics.sql");



//            String selectSql = "insert into media (titleid, title, language) values (0, null, en);";
//            statement.execute(selectSql);

             //queries.searchActor(resultSet,statement);

        }

        catch (SQLException | IOException e) {
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


    public void loadData(String script) throws IOException, SQLException {
        BufferedReader reader = new BufferedReader(new FileReader(script));
        String line = reader.readLine();
        // assumes each query is its own line
        System.out.println(line);
        while (line != null) {
            this.connection.createStatement().execute(line);
            line = reader.readLine();
        }
    }

}