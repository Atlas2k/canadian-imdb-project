import java.sql.SQLException;
import java.sql.Statement;

public class Tables {
    public  Tables(){}
    public static void createTitleTable(Statement statement) throws SQLException {
        String selectSql = "drop table if exists title";
        statement.executeQuery(selectSql);
        selectSql = "CREATE TABLE title (tconst text, titleType text, titleType text, originalTitle text, isAdult text, startYear text, endYear text,  runtimeMinutes text, genres text);";
        statement.executeQuery(selectSql);

    }
}
