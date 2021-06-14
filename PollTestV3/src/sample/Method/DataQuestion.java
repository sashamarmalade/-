package sample.Method;

public class DataQuestion {

    public enum TypeQuestion{
        only_correct,
        several_possible,
        completion_of_sentences
    }

    String TextQuestion;
    String AnswerOptions;
    TypeQuestion Type;

    public DataQuestion(){

        TextQuestion = null;
        AnswerOptions = null;
        Type = null;
    }

    public DataQuestion(String TextQuestion, String AnswerOptions, TypeQuestion Type){
        this.TextQuestion = TextQuestion;
        this.AnswerOptions = AnswerOptions;
        this.Type = Type;
    }

    public void setAll(String TextQuestion, String AnswerOptions, TypeQuestion Type){
        this.TextQuestion = TextQuestion;
        this.AnswerOptions = AnswerOptions;
        this.Type = Type;
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
