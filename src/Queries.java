import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Queries {

    public Queries(){

    }

    public String searchID(ResultSet resultSet,Statement statement, String id ) throws SQLException {
        String selectSql = "SELECT * from people where personId = "+ id;
        resultSet = statement.executeQuery(selectSql);

        while (resultSet.next()) {
            return resultSet.getString(1) +
                    " " + resultSet.getString(2) +
                    " lives in " + resultSet.getString(3);
        }

        return null;
    }

    public String searchName(ResultSet resultSet,Statement statement, String name ) throws SQLException {
        String selectSql ="SELECT * from people where name = '"+ name+"';";
        resultSet = statement.executeQuery(selectSql);

        while (resultSet.next()) {
            return resultSet.getString(1) +
                    " " + resultSet.getString(2) +
                    " lives in " + resultSet.getString(3);
        }

        return null;
    }



}
