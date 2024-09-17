import org.junit.Assert;

import java.sql.*;

public class Test {



    public static void main(String[] args) throws SQLException {
        Connection connection;
        PreparedStatement statement;
        ResultSet resultSet;

        String query = "select * from tags where tag_id = ?";

        String url = "jdbc:postgresql://18.159.52.24:5434/postgres";
        String username = "cashwiseuser";
        String password = "cashwisepass";

        connection = DriverManager.getConnection(url, username, password);

            statement = connection.prepareStatement(query);
            statement.setInt(1, Integer.parseInt("916"));

            resultSet = statement.executeQuery();

            while(resultSet.next()){
                System.out.println(resultSet.getString("name_tag"));
            }


    }
}
