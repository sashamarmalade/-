package DataTransfer;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;

public class QuestionData implements Serializable {

    @Serial
    private static final long serialVersionUID = 6;

    private int ID;
    private String Msg;
    private ArrayList<QuestionConstructor> AllQuestions;

    public QuestionData(int ID){
        this.ID = ID;
        Msg = "";
        AllQuestions = new ArrayList<>();
    }

    public QuestionData(ArrayList<QuestionConstructor> QC){
        ID = -1;
        Msg = "";
        this.AllQuestions = QC;
    }

    public QuestionData(String Msg){
        ID = -1;
        this.Msg = Msg;
        AllQuestions = null;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getMsg() {
        return Msg;
    }

    public void setMsg(String msg) {
        Msg = msg;
    }

    public ArrayList<QuestionConstructor> getAllQuestions() {
        return AllQuestions;
    }

    public void setAllQuestions(ArrayList<QuestionConstructor> allQuestions) {
        this.AllQuestions = allQuestions;
    }

}
