package Server.sample.Method;

import DataTransfer.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

public class DataBaseWork {

    public Object login(String MailLogin, String Password){
        Connection DB = DataBaseConnecting.Connecting();
        try {
            assert DB != null: "Failed to connect to database";
            PreparedStatement ps = DB.prepareStatement("Select * from users where (EMAIL = ? or USER_NAME = ?) and PASSWORD = ?");
            ps.setString(1, MailLogin);
            ps.setString(2, MailLogin);
            ps.setString(3, Password);
            ResultSet rs =  ps.executeQuery();

            if (rs.next())
                return new LoginData("",rs.getInt("ID"), rs.getString("USER_NAME"));
            else
                return new LoginData("Неверный логин/почта или пароль",-1, null);

        }catch (Exception e){
            System.out.println(e.getMessage());
            return new LoginData("Не удалось подключится к серверу",-1, null);
        }
    }

    public Object add_user(String Mail, String Login, String Password){
        Connection DB = DataBaseConnecting.Connecting();

        try {
            assert DB != null: "Failed to connect to database";

            PreparedStatement ps = DB.prepareStatement("Select * from users where EMAIL = ?");
            ps.setString(1, Mail);
            ResultSet rs = ps.executeQuery();
            if (rs.next())
                return new RegistrationData("Введенная почта уже используется, введите другую почту");

            ps = DB.prepareStatement("Select * from users where USER_NAME = ?");
            ps.setString(1, Login);
            rs = ps.executeQuery();
            if (rs.next())
                return new RegistrationData("Введенный логин уже используется, введите другой логин");

            ps = DB.prepareStatement("Insert into users (EMAIL, USER_NAME, PASSWORD) values (?,?,?)");
            ps.setString(1, Mail);
            ps.setString(2, Login);
            ps.setString(3, Password);
            ps.execute();
            ps.close();
            return new RegistrationData("");

        }catch (Exception e){
            System.out.println(e.getMessage());
            return "Не удалось подключится к серверу";
        }
    }

    public Object getAllTheme(int User){
        Connection DB = DataBaseConnecting.Connecting();

        ArrayList<ThemeConstructor> Theme = new ArrayList<>();
        try {
            assert DB != null: "Failed to connect to database";
            PreparedStatement ps = DB.prepareStatement("Select * from theme");
            ResultSet rs =  ps.executeQuery();

            while(rs.next()) {
                PreparedStatement ps2 = DB.prepareStatement("Select сompleted_number from сompleted_topics where theme_id = ? and user_id = ?");
                ps2.setString(1, Integer.toString(rs.getInt("ID")));
                ps2.setString(2, Integer.toString(User));
                ResultSet rs2 =  ps2.executeQuery();

                Theme.add(new ThemeConstructor(rs.getInt("ID"), rs.getString("TextTheme"),
                        rs.getString("Type"), rs.getTime("Time"), rs.getInt("number_attempts"), rs2.next()?rs2.getInt(1):0));
                rs2.close();
            }
            rs.close();
            return new AllThemeData(Theme);
        }catch (Exception e){
            System.out.println(e.getMessage());
            return new AllThemeData("Ошибка при формировании тем.");
        }
    }

    public Object getTheme(int theme_id){
        Connection DB = DataBaseConnecting.Connecting();

        ThemeConstructor TC = new ThemeConstructor();
        try {
            assert DB != null: "Failed to connect to database";
            PreparedStatement ps = DB.prepareStatement("Select * from theme where ID = ?");
            ps.setString(1, Integer.toString(theme_id));
            ResultSet rs =  ps.executeQuery();

            if (rs.next())
                TC.setAll(rs.getInt("ID"), rs.getString("TextTheme"),
                        rs.getString("Type"), rs.getTime("Time"), rs.getInt("number_attempts"));
            rs.close();
            return new ThemeData(TC);
        }catch (Exception e){
            System.out.println(e.getMessage());
            return new ThemeData("Ошибка при формировании темы.");
        }
    }

    public Object getQuestion(int ID){
        Connection DB = DataBaseConnecting.Connecting();
        ArrayList<QuestionConstructor> DQ = new ArrayList<>();

        try {
            assert DB != null: "Failed to connect to database";
            PreparedStatement ps = DB.prepareStatement("select Text, AnswerOptions, TypeQuestion from questions where ThemeID = ?");
            ps.setString(1, Integer.toString(ID));
            ResultSet rs =  ps.executeQuery();


            while (rs.next()) {
                try {
                    DQ.add(new QuestionConstructor(rs.getString(1), rs.getString(2), QuestionConstructor.TypeQuestion.valueOf(rs.getString(3))));
                }catch (Exception e){
                    System.out.println(e.getMessage());
                    return new QuestionData("Ошибка при формировании вопросов");
                }
            }
            rs.close();
            return new QuestionData(DQ);
        }catch (Exception e){
            System.out.println(e.getMessage());
            return new QuestionData("Ошибка при формировании вопросов");
        }
    }

    public Object InputDataDB(int user_id, int theme_id){
        Connection DB = DataBaseConnecting.Connecting();
        int CompletedTopicsId = -1;
        int CompletedNumber = 0;

        try {
            assert DB != null: "Failed to connect to database";

            PreparedStatement ps = DB.prepareStatement("Select ID, сompleted_number from сompleted_topics where user_id =? and theme_id = ?");
            ps.setString(1, Integer.toString(user_id));
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
                ps.setString(1, String.valueOf(user_id));
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

            return new InputData("");

        }catch (Exception e){
            System.out.println(e.getMessage());
            return new InputData("Ошибка при отправке данных на сервер.");
        }
    }

    public Object InputAnswerDB(int user_id, int theme_id, String Answer){
        Connection DB = DataBaseConnecting.Connecting();
        try {
            assert DB != null: "Failed to connect to database";

            PreparedStatement ps = DB.prepareStatement("Insert into user_answers (user_id, theme_id, Answers) values (?,?,?)");
            ps.setString(1, String.valueOf(user_id));
            ps.setString(2, String.valueOf(theme_id));
            ps.setString(3, Answer);
            ps.execute();
            ps.close();
            return new InputAnswer("");

        }catch (Exception e){
            System.out.println(e.getMessage());
            return new InputAnswer("Ошибка при отправке вопросов на сервер.");
        }
    }
}
