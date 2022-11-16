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
        String fileName = "C:\\Users\\akint\\OneDrive\\Desktop\\COMP3380Proj\\Server\\src\\auth.cfg";
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
            System.out.println("Exporting ....");
            // Tables.dropTables(statement);
            // Tables.createTables(statement);
            loadTables();

        }
        catch (SQLException | IOException e) {
             e.printStackTrace();
        }

    }

    public String yourSearch(String input) throws SQLException {
        queries query = new queries();
        Parameters p = new Parameters();
        handleErrors(input.split(" ")[1],p);
        if(input.split(" ")[0].compareTo("a")==0){
            return query.searchID(resultSet,statement,p.getActorName());
        } // a is to seaarch for information about an author.

        return null;
    }

    public void handleErrors(String parameters, Parameters p){
        String[] para = parameters.split(",");
        for(int i =0; i<para.length; i++){
            if(para[i].split(":")[0].compareTo("Search")==0){
                p.setSearch(para[i].split(":")[1]);
            }

            if(para[i].split(":")[0].compareTo("AuthorName")==0){
                p.setActorName((para[i].split(":")[1]));
            }

            if(para[i].split(":")[0].compareTo("Shows")==0){
                p.setActorName((para[i].split(":")[1]));
            }
            if(para[i].split(":")[0].compareTo("Episodes")==0){
                p.setActorName((para[i].split(":")[1]));
            }
        }
    }

    public void loadTables() throws IOException, SQLException {
        System.out.println("Loading Data From Scripts Into Server. Please Wait...");
        System.out.println("Loading the media table...");
        // loadData("title.akas.sql");
        // loadData("title.basics.sql");
        // loadData("title.ratings.sql");
        // connection.createStatement().execute("Delete from media where titleId = 0;");
        // connection.createStatement().execute("Delete from media where title is NULL;");

        System.out.println("Loading the genres table and realtion...");
        // loadData("genres.title.basics.sql");
        // loadData("partOf.title.basics.sql");

        System.out.println("Loading the having (episodes) table...");
        // loadData("title.episode.sql");

        // Loading the people table
        System.out.println("Loading the people table relations...");
        // loadData("name.basics.sql");
        // loadData("knownFor.name.basics.sql");
        // loadData("jobs.name.basics.sql");
        // loadData("works.name.basics.sql");

        // Loading cast and character data
        System.out.println("Loading the cast and character tables...");
        //        loadData("title.principals.sql");
        //        loadData("characters.title.principals.sql");

        // Loading platform data
        System.out.println("Loading the platform table and relations...");
        //         loadData("platform.sql");
        //         loadData("availableOn.sql");
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
        reader.close();
    }
}
