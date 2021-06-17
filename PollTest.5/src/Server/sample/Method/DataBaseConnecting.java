package Server.sample.Method;

import Server.sample.Main;

import java.sql.Connection;
import java.sql.DriverManager;

public class DataBaseConnecting {
    public static Connection Connecting(){
        try {
            return DriverManager.getConnection("jdbc:mysql://"+ Main.Config.get("DataBaseHost:")+"/answer",Main.Config.get("User:"),Main.Config.get("Password:"));
        }catch(Exception e){
            System.out.println(e.getMessage());
            return null;
        }
    }
}
