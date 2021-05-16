package sample.Method;


import java.sql.Time;

public class DataTheme{

    int ID;
    String TextTheme;
    String Type;
    Time Time;

    public DataTheme() {
        ID = 0;
        TextTheme = null;
        Type = null;
        Time = null;
    }

    public DataTheme(int ID, String TextTheme, String Type, Time Time){
        this.ID = ID;
        this.TextTheme = TextTheme;
        this.Type = Type;
        this.Time = Time;
    }

    public void setAll(int ID, String TextTheme, String Type, Time Time){
        this.ID = ID;
        this.TextTheme = TextTheme;
        this.Type = Type;
        this.Time = Time;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public int getID() {
        return ID;
    }

    public void setTextTheme(String textTheme) {
        TextTheme = textTheme;
    }


    public String getTextTheme(){
        return TextTheme;
    }

    public String getType() {
        return Type;
    }

    public void setType(String type) {
        Type = type;
    }

    public Time getTime() {
        return Time;
    }

    public void setTime(Time time) {
        Time = time;
    }
}
