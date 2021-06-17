package Client.sample.Method;

import java.io.*;
import java.net.Socket;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;


import java.io.*;
import java.net.Socket;


public class Method {

    DataUser User = null;
    private Server server = null;

    public void setUser(DataUser User) {
        this.User = User;
    }

    public DataUser getUser() {
        return User;
    }

    public Server getServer() {
        return server;
    }

    public Method() {
        Initialize();
    }

    public String RegistrChecking(String Mail, String UserName, String Password, String Password2) {

        if ((Mail.trim().length() <= 0) || (UserName.trim().length() <= 0) ||
                (Password.trim().length() <= 0) || (Password2.trim().length() <= 0))
            return "Вы не заполнили все поля";

        if (Mail.length() > 50) return "Почта не должна быть длинее 50 символов";

        if (Mail.contains(" ")) return "Почта не должна содержать пробелов";

        if (UserName.length() > 16) return "Логин не должно быть длинее 16 символов";

        if (UserName.contains(" ")) return "Логин не должна содержать пробелов";

        if (!UserName.matches("[a-zA-Z0-9\\d\\s\\p{Punct}]+"))
            return "Логин может состоять только из английских букв," +
                    " цифр и знаков пункуаций";

        if (Password.length() > 24) return "Пароль не должна быть длинее 24 символов";

        if ((!Password.equals(Password2))) return "Пароли не совпадают";

        return "";
    }

    private void Initialize() {
        String IP = "127.0.0.1";
        int Port = 1111;

        try {
            BufferedReader br = new BufferedReader(new FileReader("src/Client/sample/Config.txt"));

            String line;
            while (true) {
                if ((line = br.readLine()) != null) {
                    boolean Empty = line.substring(line.lastIndexOf(":") + 1).trim().length() != 0;
                    if (!Empty) continue;

                    if (line.contains("IP:"))
                        IP = line.substring(line.lastIndexOf(":") + 1).trim();

                    if (line.contains("Port:"))
                        Port = Integer.parseInt(line.substring(line.lastIndexOf(":") + 1).trim());
                } else break;

            }
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }

        try {
            Socket s = new Socket(IP, Port);
            System.out.println("Local port: " + s.getLocalPort());
            System.out.println("Remote port: " + s.getPort());

            server = new Server(s);
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());

        }
    }
}


