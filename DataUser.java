package sample.Method;

public class DataUser {

    private final int ID;
    private final String UserName;

    public DataUser(int ID, String UserName){
        this.ID = ID;
        this.UserName = UserName;
    }

    public int getID() {
        return ID;
    }

    public String getUserName() {
        return UserName;
    }
}
