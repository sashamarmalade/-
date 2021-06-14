package sample.Method;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

public class Method {

    DataUser User = null;
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
            if (rs.next()) {
                User = new DataUser(rs.getInt("ID"), rs.getString("USER_NAME"));
                rs.close();
                return "";
            }else {
                rs.close();
                return "Неверный логин/почта или пароль";
            }
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
            ps.close();
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



            while(rs.next()) {
                PreparedStatement ps2 = DB.prepareStatement("Select сompleted_number from сompleted_topics where theme_id = ? and user_id = ?");
                ps2.setString(1, Integer.toString(rs.getInt("ID")));
                ps2.setString(2, Integer.toString(User.getID()));
                ResultSet rs2 =  ps2.executeQuery();

                Theme.add(new DataTheme(rs.getInt("ID"), rs.getString("TextTheme"),
                        rs.getString("Type"), rs.getTime("Time"), rs.getInt("number_attempts"), rs2.next()?rs2.getInt(1):0));
                rs2.close();
            }
            rs.close();
            return 1;
        }catch (Exception e){
            System.out.println(e.getMessage());
            return -1;
        }
    }

    public int getTheme(DataTheme Theme, int theme_id){
        this.DB = DataBaseConnecting.Connecting();

        try {
            assert DB != null: "Failed to connect to database";
            PreparedStatement ps = DB.prepareStatement("Select * from theme where ID = ?");
            ps.setString(1, Integer.toString(theme_id));
            ResultSet rs =  ps.executeQuery();

            if (rs.next())
                Theme.setAll(rs.getInt("ID"), rs.getString("TextTheme"),
                        rs.getString("Type"), rs.getTime("Time"), rs.getInt("number_attempts"));
            rs.close();
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
            PreparedStatement ps = DB.prepareStatement("select Text, AnswerOptions, TypeQuestion from questions where ThemeID = ?");
            ps.setString(1, Integer.toString(ID));
            ResultSet rs =  ps.executeQuery();


            while (rs.next()) {
                try {
                    DQ.add(new DataQuestion(rs.getString(1), rs.getString(2), DataQuestion.TypeQuestion.valueOf(rs.getString(3))));
                }catch (Exception e){
                    System.out.println(e.getMessage());
                    return -1;
                }
            }
            rs.close();
            return 1;
        }catch (Exception e){
            System.out.println(e.getMessage());
            return -1;
        }
    }

    public int InputDataDB(int theme_id){
        this.DB = DataBaseConnecting.Connecting();
        int CompletedTopicsId = -1;
        int CompletedNumber = 0;

        try {
            assert DB != null: "Failed to connect to database";

            PreparedStatement ps = DB.prepareStatement("Select ID, сompleted_number from сompleted_topics where user_id =? and theme_id = ?");
            ps.setString(1, Integer.toString(User.getID()));
            ps.setString(2, Integer.toString(theme_id));
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                CompletedTopicsId = rs.getInt(1);
                CompletedNumber = rs.getInt(2);
            }
            rs.close();

            CompletedNumber++;

            if (CompletedTopicsId == -1){
                ps = DB.prepareStatement("Insert into сompleted_topics (user_id, theme_id, сompleted_number) values (?,?,?)");
                ps.setString(1, String.valueOf(User.getID()));
                ps.setString(2, String.valueOf(theme_id));
                ps.setString(3, String.valueOf(CompletedNumber));
                ps.execute();
                ps.close();
            }else {
                ps = DB.prepareStatement("UPDATE сompleted_topics SET сompleted_number = ? where ID = ?");
                ps.setString(1, String.valueOf(CompletedNumber));
                ps.setString(2, String.valueOf(CompletedTopicsId));
                ps.executeUpdate();
                ps.close();
            }




            return 1;

        }catch (Exception e){
            System.out.println(e.getMessage());
            return -1;
        }
    }

    public int InputAnswerDB(int theme_id, String Answer){
        this.DB = DataBaseConnecting.Connecting();

        try {
            assert DB != null: "Failed to connect to database";

            PreparedStatement ps = DB.prepareStatement("Insert into user_answers (user_id, theme_id, Answers) values (?,?,?)");
            ps.setString(1, String.valueOf(User.getID()));
            ps.setString(2, String.valueOf(theme_id));
            ps.setString(3, Answer);
            ps.execute();
            ps.close();
            return 1;

        }catch (Exception e){
            System.out.println(e.getMessage());
            return -1;
        }
    }

    private void Initialize(){

    }
}
