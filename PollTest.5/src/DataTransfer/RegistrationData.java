package DataTransfer;

import java.io.Serial;
import java.io.Serializable;

public class RegistrationData implements Serializable {

    @Serial
    private static final long serialVersionUID = 2;

    private String Mail,  Login,  Password, Msg;

    public RegistrationData(String Mail,String Login,String Password){
        this.Msg = "";
        this.Mail = Mail;
        this.Login = Login;
        this.Password = Password;
    }

    public RegistrationData(String Msg){
        this.Msg = Msg;
    }

    public String getMsg(){
        return Msg;
    }

    public String getMail() {
        return Mail;
    }

    public String getLogin() {
        return Login;
    }

    public String getPassword() {
        return Password;
    }


}
