package DataTransfer;

import javafx.fxml.FXML;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;

public class AllThemeData implements Serializable {

    @Serial
    private static final long serialVersionUID = 3;

    private ArrayList<ThemeConstructor> AllTheme;
    private String Msg;
    private int user_id;

    public AllThemeData(int user_id){
        this.user_id = user_id;
        AllTheme = new ArrayList<>();
        Msg = "";
    }

    public AllThemeData(ArrayList<ThemeConstructor> AllTheme){
        user_id = -1;
        this.AllTheme = AllTheme;
        Msg = "";
    }

    public AllThemeData(String Msg){
        user_id = -1;
        AllTheme = null;
        Msg = Msg;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }


    public ArrayList<ThemeConstructor> getAllTheme() {
        return AllTheme;
    }

    public void setAllTheme(ArrayList<ThemeConstructor> allTheme) {
        AllTheme = allTheme;
    }

    public String getMsg() {
        return Msg;
    }

    public void setMsg(String msg) {
        Msg = msg;
    }


}
