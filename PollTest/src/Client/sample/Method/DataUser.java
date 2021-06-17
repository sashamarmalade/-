package Client.sample.Method;

public class DataUser {

    private int ID;
    private String UserName;

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

    public void setID(int ID) {
        this.ID = ID;
    }

    public void setUserName(String userName) {
        UserName = userName;
    }

}
