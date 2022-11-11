import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class queries {

    public queries(){

    }

    public String searchActor(ResultSet resultSet,Statement statement ) throws SQLException {
        String selectSql = "SELECT * from people where personId = 023523;";
        resultSet = statement.executeQuery(selectSql);

        while (resultSet.next()) {
            return resultSet.getString(1) +
                    " " + resultSet.getString(2) +
                    " lives in " + resultSet.getString(3);
        }

        return null;
    }



}
