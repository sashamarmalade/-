package DataTransfer;

import java.io.Serial;
import java.io.Serializable;

public class InputAnswer implements Serializable {

    @Serial
    private static final long serialVersionUID = 9;
    private int user_id, theme_id;
    private String Msg, Answers;

    public InputAnswer(int user_id, int theme_id, String Answers){
        this.user_id = user_id;
        this.theme_id = theme_id;
        this.Answers = Answers;
        Msg = "";

    }

    public InputAnswer(String Msg){
        user_id = -1;
        theme_id = -1;
        Answers = "";
        this.Msg = Msg;

    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public int getTheme_id() {
        return theme_id;
    }

    public void setTheme_id(int theme_id) {
        this.theme_id = theme_id;
    }

    public String getMsg() {
        return Msg;
    }

    public void setMsg(String msg) {
        Msg = msg;
    }

    public String getAnswers() {
        return Answers;
    }

    public void setAnswers(String answers) {
        Answers = answers;
    }

}
