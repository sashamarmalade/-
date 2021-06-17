package DataTransfer;

import java.io.Serial;
import java.io.Serializable;

public class UserMsg implements Serializable {

    @Serial
    private static final long serialVersionUID = 10;

    private String Msg;

    public UserMsg(String Msg){
        this.Msg = Msg;
    }

    public String getMsg() {
        return Msg;
    }
}
