package DataTransfer;

import java.io.Serial;
import java.io.Serializable;

public class ThemeData implements Serializable {

    @Serial
    private static final long serialVersionUID = 5;

    private ThemeConstructor Theme;
    private String Msg;
    private int theme_id;

    public ThemeData(int user_id){
        this.theme_id = user_id;
        Theme = null;
        Msg = "";
    }

    public ThemeData(ThemeConstructor Theme){
        theme_id = -1;
        this.Theme = Theme;
        Msg = "";
    }

    public ThemeData(String Msg){
        theme_id = -1;
        Theme = null;
        Msg = Msg;
    }

    public int getTheme_id() {
        return theme_id;
    }

    public void setTheme_id(int theme_id) {
        this.theme_id = theme_id;
    }

    public ThemeConstructor getTheme() {
        return Theme;
    }

    public void setTheme(ThemeConstructor Theme) {
        this.Theme = Theme;
    }

    public String getMsg() {
        return Msg;
    }

    public void setMsg(String msg) {
        Msg = msg;
    }


}
