package sample.Method;

public class DataQuestion {

    public enum TypeQuestion{
        only_correct,
        several_possible,
        completion_of_sentences,
        establishing_relationships
    }

    int ID;
    String TextQuestion;
    String AnswerOptions;
    TypeQuestion Type;

    public DataQuestion(){
        ID = -1;
        TextQuestion = null;
        AnswerOptions = null;
        Type = null;
    }

    public DataQuestion(int ID, String TextQuestion, String AnswerOptions, TypeQuestion Type){
        this.ID = ID;
        this.TextQuestion = TextQuestion;
        this.AnswerOptions = AnswerOptions;
        this.Type = Type;
    }

    public void setAll(int ID, String TextQuestion, String AnswerOptions, TypeQuestion Type){
        this.ID = ID;
        this.TextQuestion = TextQuestion;
        this.AnswerOptions = AnswerOptions;
        this.Type = Type;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getTextQuestion() {
        return TextQuestion;
    }

    public void setTextQuestion(String textQuestion) {
        TextQuestion = textQuestion;
    }

    public String getAnswerOptions() {
        return AnswerOptions;
    }

    public void setAnswerOptions(String answerOptions) {
        AnswerOptions = answerOptions;
    }

    public TypeQuestion getType() {
        return Type;
    }

    public void setType(TypeQuestion type) {
        Type = type;
    }

}
