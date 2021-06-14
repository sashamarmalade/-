package sample.Method;

import java.sql.Connection;
import java.sql.DriverManager;

public class DataBaseConnecting {
    Connection connect = null;

    public static Connection Connecting(){
        try {
            return DriverManager.getConnection("jdbc:mysql://localhost/answer","user","");
        }catch(Exception e){
            System.out.println(e.getMessage());
            return null;
        }
    }
}
