package ConnectionHelper;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionHelper {

    public  static  Connection createConnection(){
        Connection con;
        try {
             con= DriverManager.getConnection("jdbc:mysql://localhost:3306/BankOpr","root","root");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return con;
    }
}
