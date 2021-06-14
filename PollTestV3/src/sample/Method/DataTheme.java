package sample.Method;


import java.sql.Time;

public class DataTheme{

    int ID;
    String TextTheme;
    String Type;
    Time Time;
    int NumberAttempts;



    int AttemptsLeft;

    public DataTheme() {
        ID = -1;
        TextTheme = null;
        Type = null;
        Time = null;
        NumberAttempts = -1;
        AttemptsLeft = -1;
    }

    public DataTheme(int ID, String TextTheme, String Type, Time Time, int NumberAttempts){
        this.ID = ID;
        this.TextTheme = TextTheme;
        this.Type = Type;
        this.Time = Time;
        this.NumberAttempts = NumberAttempts;
        this.AttemptsLeft = -1;
    }

    public void setAll(int ID, String TextTheme, String Type, Time Time, int NumberAttempts){
        this.ID = ID;
        this.TextTheme = TextTheme;
        this.Type = Type;
        this.Time = Time;
        this.NumberAttempts = NumberAttempts;
        this.AttemptsLeft = -1;
    }

    public DataTheme(int ID, String TextTheme, String Type, Time Time, int NumberAttempts, int AttemptsLeft){
        this.ID = ID;
        this.TextTheme = TextTheme;
        this.Type = Type;
        this.Time = Time;
        this.NumberAttempts = NumberAttempts;
        this.AttemptsLeft = AttemptsLeft;
    }

    public void setAll(int ID, String TextTheme, String Type, Time Time, int NumberAttempts, int AttemptsLeft){
        this.ID = ID;
        this.TextTheme = TextTheme;
        this.Type = Type;
        this.Time = Time;
        this.NumberAttempts = NumberAttempts;
        this.AttemptsLeft = AttemptsLeft;
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

    public int getNumberAttempts() {
        return NumberAttempts;
    }

    public void setNumberAttempts(int numberAttempts) {
        NumberAttempts = numberAttempts;
    }

    public int getAttemptsLeft() {
        return AttemptsLeft;
    }

    public void setAttemptsLeft(int attemptsLeft) {
        AttemptsLeft = attemptsLeft;
    }
}
