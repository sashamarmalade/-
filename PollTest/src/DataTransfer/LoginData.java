package DataTransfer;

import java.io.Serial;
import java.io.Serializable;

public class LoginData implements Serializable {

    @Serial
    private static final long serialVersionUID = 1;

    private String Msg, MailLogin, Password;
    private String UserName;
    private int ID;

    public LoginData(String MailLogin, String Password){
        Msg = "";
        this.MailLogin = MailLogin;
        this.Password = Password;
    }

    public LoginData(String Msg, int ID, String UserName){
        this.Msg = Msg;
        this.ID = ID;
        this.UserName = UserName;
    }

    public String getMsg() {
        return Msg;
    }

    public void setMsg(String msg) {
        Msg = msg;
    }

    public String getMailLogin() {
        return MailLogin;
    }

    public String getPassword() {
        return Password;
    }

    public String getUserName() {
        return UserName;
    }

    public int getID() {
        return ID;
    }


}
