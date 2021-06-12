package sample.Method;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

public class Method {



    Connection DB = null;

    public Method(){
        Initialize();
    }

    public String login(String MailLogin, String Password){

        if ((MailLogin.trim().length() <= 0) || (Password.trim().length() <= 0)) return "Вы не заполнили все поля";

        this.DB = DataBaseConnecting.Connecting();
        try {
            assert DB != null: "Failed to connect to database";
            PreparedStatement ps = DB.prepareStatement("Select * from users where (EMAIL = ? or USER_NAME = ?) and PASSWORD = ?");
            ps.setString(1, MailLogin);
            ps.setString(2, MailLogin);
            ps.setString(3, Password);
            ResultSet rs =  ps.executeQuery();
            if (rs.next())
                return "";
            else
                return "Неверный логин/почта или пароль";
        }catch (Exception e){
            System.out.println(e.getMessage());
            return "Не удалось подключится к серверу";
        }
    }

    public String add_user(String Mail, String Login, String Password, String Password2){

        String Res = RegistrChecking(Mail, Login, Password, Password2);
        if (Res.length() > 0) return Res;

        this.DB = DataBaseConnecting.Connecting();
        try {
            assert DB != null: "Failed to connect to database";

            PreparedStatement ps = DB.prepareStatement("Select * from users where EMAIL = ?");
            ps.setString(1, Mail);
            ResultSet rs = ps.executeQuery();
            if (rs.next())
                return "Введенная почта уже используется, введите другую почту";

            ps = DB.prepareStatement("Select * from users where USER_NAME = ?");
            ps.setString(1, Login);
            rs = ps.executeQuery();
            if (rs.next())
                return "Введенный логин уже используется, введите другой логин";

            ps = DB.prepareStatement("Insert into users (EMAIL, USER_NAME, PASSWORD) values (?,?,?)");
            ps.setString(1, Mail);
            ps.setString(2, Login);
            ps.setString(3, Password);
            ps.execute();
            return "";

        }catch (Exception e){
            System.out.println(e.getMessage());
            return "Не удалось подключится к серверу";
        }
    }

    public String RegistrChecking (String Mail, String UserName, String Password, String Password2){

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

    public int getAllTheme(ArrayList<DataTheme> Theme){
        this.DB = DataBaseConnecting.Connecting();

        try {
            assert DB != null: "Failed to connect to database";
            PreparedStatement ps = DB.prepareStatement("Select * from theme");
            ResultSet rs =  ps.executeQuery();

            while(rs.next())
                Theme.add(new DataTheme(rs.getInt("ID"), rs.getString("TextTheme"),
                        rs.getString("Type"), rs.getTime("Time")));

            return 1;
        }catch (Exception e){
            System.out.println(e.getMessage());
            return -1;
        }
    }

    public int getTheme(DataTheme Theme, int ID){
        this.DB = DataBaseConnecting.Connecting();

        try {
            assert DB != null: "Failed to connect to database";
            PreparedStatement ps = DB.prepareStatement("Select * from theme where ID = ?");
            ps.setString(1, Integer.toString(ID));
            ResultSet rs =  ps.executeQuery();

            if (rs.next())
                 Theme.setAll(rs.getInt("ID"), rs.getString("TextTheme"),
                        rs.getString("Type"), rs.getTime("Time"));


            return 1;
        }catch (Exception e){
            System.out.println(e.getMessage());
            return -1;
        }
    }

    public int getQuestion(ArrayList<DataQuestion> DQ, int ID){
        this.DB = DataBaseConnecting.Connecting();

        try {
            assert DB != null: "Failed to connect to database";
            PreparedStatement ps = DB.prepareStatement("Select Number, Text, AnswerOptions, TypeQuestion from questions where ThemeID = ?");
            ps.setString(1, Integer.toString(ID));
            ResultSet rs =  ps.executeQuery();


            while (rs.next()) {
                try {
                    DQ.add(new DataQuestion(rs.getInt(1), rs.getString(2), rs.getString(3), DataQuestion.TypeQuestion.valueOf(rs.getString(4))));
                }catch (Exception e){
                    System.out.println(e.getMessage());
                    return -1;
                }
            }
            System.out.println(DQ);

            System.out.println(DQ.get(0).getID());
            System.out.println(DQ.get(0).getTextQuestion());
            System.out.println(DQ.get(0).getAnswerOptions());
            System.out.println(DQ.get(0).getType());

            return 1;
        }catch (Exception e){
            System.out.println(e.getMessage());
            return -1;
        }
    }

    private void Initialize(){

    }
}
