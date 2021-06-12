package sample.Controller;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Bounds;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import org.apache.commons.io.FilenameUtils;

import sample.Method.*;

import java.net.URL;
import java.sql.Time;
import java.util.ArrayList;
import java.util.ResourceBundle;


public class Controller implements Initializable {

    private final Method Logic = new Method();

    public static int SelectIDTheme = -1;

    @FXML
    private AnchorPane APLogin;

    @FXML
    private AnchorPane APReg;

    @FXML
    private AnchorPane APTheme;

    @FXML
    private PasswordField LogPass;

    @FXML
    private TextField LogMailLog;

    @FXML
    private Label LogMsg;

    @FXML
    private TextField RegMail;

    @FXML
    private TextField RegLogin;

    @FXML
    private PasswordField RegPass;

    @FXML
    private PasswordField RegPass2;

    @FXML
    private Label RegMsg;

    @FXML
    private Label NumberTheme;

    @FXML
    private Label NameTheme;

    @FXML
    private Label TimeTheme;

    @FXML
    private VBox VBListTheme;

    @FXML
    private ScrollPane SCQuestion;

    @FXML
    private Button BStartQuest;

    @FXML
    private Label LQuestMsg;

    @FXML
    private VBox VBThemeQuestions;

    @FXML
    private void StartQuestions(ActionEvent event) {

        BStartQuest.setVisible(false);
        LQuestMsg.setVisible(false);
        SCQuestion.setVisible(true);

        DataTheme DT = new DataTheme();
        ArrayList<DataQuestion> DQ = new ArrayList<>();

        if (Logic.getTheme(DT, SelectIDTheme) == -1){
            NameTheme.setText("Не удалось подключиться к серверу");
            return;
        }

        if (Logic.getQuestion(DQ, DT.getID()) == -1){
            NameTheme.setText("Не удалось подключиться к серверу");
            return;
        }

        VBThemeQuestions.getChildren().addAll(NodeConstructQuestion(DQ));


        Time time = DT.getTime();
        TestTimer testTimer = new TestTimer(time, TimeTheme);
    }

    @FXML
    private void LoginPanelShow(ActionEvent event){
        APLogin.setVisible(true);
        APReg.setVisible(false);
        RegMail.clear();
        RegLogin.clear();
        RegPass.clear();
        RegPass2.clear();
        RegMsg.setText("");
    }

    @FXML
    private void Login(ActionEvent event){
        String Res = Logic.login(LogMailLog.getText(), LogPass.getText());
        if (Res.length() > 0)
            LogMsg.setText(Res);
        else {
            try {
                APLogin.getScene().getWindow().setWidth(800);
                APLogin.getScene().getWindow().setHeight(600);
                APLogin.getScene().setRoot(FXMLLoader.load(getClass().getResource("/sample/Visual/Main.fxml")));
            } catch (Exception e) {
                System.out.println(e.getMessage());
                LogMsg.setText("Ошибка загрузки программы.");
            }
        }

    }

    @FXML
    private void Registration(ActionEvent event){
        String Res = Logic.add_user(RegMail.getText(),RegLogin.getText(),RegPass.getText(), RegPass2.getText());
        if (Res.length() > 0)
            RegMsg.setText(Res);
        else
            RegMsg.setText("Вы успешно зарегистрировались");
    }

    @FXML
    private void RegistPanelShow(ActionEvent event){
        APReg.setVisible(true);
        APLogin.setVisible(false);
        LogMailLog.clear();
        LogPass.clear();
        LogMsg.setText("");
    }

    private Node NodeConstructTheme(DataTheme DT){

        HBox HBTheme = new HBox();
        HBTheme.setAlignment(Pos.CENTER_LEFT);
        HBTheme.setStyle("-fx-border-color: #000000; -fx-border-width: 2; -fx-background-color: #ffffff");
        HBTheme.setPadding(new Insets(0, 10, 0, 10));
        HBTheme.setSpacing(10);
        Label lID = new Label(Integer.toString(DT.getID()));
        lID.setFont(new Font("Times New Roman", 18));
        Label lTheme = new Label(DT.getTextTheme());
        lTheme.setFont(new Font("Times New Roman", 18));
        lTheme.setWrapText(true);
        HBTheme.getChildren().addAll(lID, lTheme);
        HBTheme.setOnMouseClicked(event->{
            try {
                SelectIDTheme = DT.getID();
                APTheme.getScene().getWindow().setWidth(800);
                APTheme.getScene().getWindow().setHeight(600);
                APTheme.getScene().setRoot(FXMLLoader.load(getClass().getResource("/sample/Visual/PassageWindow.fxml")));
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        });


        return HBTheme;
    }

    private ArrayList<Node> NodeConstructQuestion(ArrayList<DataQuestion> AllDQ){

        ArrayList<Node> AllQuestion= new ArrayList<>();
        for (DataQuestion DQ : AllDQ) {
            VBox VBQuestion = new VBox();
            VBQuestion.setAlignment(Pos.CENTER_LEFT);
            VBQuestion.setStyle("-fx-border-color: #000000; -fx-border-width: 2; -fx-background-color: #ffffff");
            VBQuestion.setPadding(new Insets(10, 10, 10, 10));
            VBQuestion.setSpacing(10);

            Label Number = new Label("Вопрос №"+Integer.toString(DQ.getID()));
            Number.setFont(new Font("Times New Roman", 18));

            Label lQuestion = new Label(DQ.getTextQuestion());
            lQuestion.setFont(new Font("Times New Roman", 18));
            lQuestion.setWrapText(true);

            VBQuestion.setAlignment(Pos.CENTER_LEFT);
            VBQuestion.getChildren().addAll(Number, lQuestion);

            switch (DQ.getType()) {
                case only_correct -> {
                    GridPane GridRadio = new GridPane();
                    GridRadio.setAlignment(Pos.CENTER);
                    ToggleGroup group = new ToggleGroup();
                    ArrayList<RadioButton> buttons = new ArrayList<>();
                    int Nu = 1;
                    for (String Answer : DQ.getAnswerOptions().split(" ")) {
                        RadioButton BA = new RadioButton(Nu + ". " + Answer);
                        BA.setToggleGroup(group);
                        BA.setPadding(new Insets(10, 20, 10, 0));
                        buttons.add(BA);
                        Nu++;
                    }
                    Nu = 0;
                    for (int i = 0; i < buttons.size() / 2; i++)
                        for (int j = buttons.size() / 2; j < buttons.size(); j++) {
                            GridRadio.add(buttons.get(Nu), i, j);
                            Nu++;
                        }
                    VBQuestion.getChildren().add(GridRadio);
                }
                case several_possible -> {
                    GridPane GridRadio = new GridPane();
                    GridRadio.setAlignment(Pos.CENTER);
                    ArrayList<CheckBox> check = new ArrayList<>();
                    int Nu = 1;
                    for (String Answer : DQ.getAnswerOptions().split(" ")) {
                        CheckBox CB = new CheckBox(Nu + ". " + Answer);
                        CB.setPadding(new Insets(10, 20, 10, 0));
                        check.add(CB);
                        Nu++;
                    }
                    Nu = 0;
                    for (int i = 0; i < check.size() / 2; i++)
                        for (int j = check.size() / 2; j < check.size(); j++) {
                            GridRadio.add(check.get(Nu), i, j);
                            Nu++;
                        }
                    VBQuestion.getChildren().add(GridRadio);
                }
                case completion_of_sentences -> VBQuestion.getChildren().add(new TextField());
                case establishing_relationships -> {
                    GridPane GP = new GridPane();
                    GP.setAlignment(Pos.CENTER);
                    GP.setPadding(new Insets(20,20,20,20));
                    String Col1 = "";
                    String Col2 = "";

                    Col1 = DQ.getAnswerOptions().split("\\|")[0];
                    Col2 = DQ.getAnswerOptions().split("\\|")[1];

                    int Nu = 0;
                    for (String i : Col1.split(" ")){
                        Label l = new Label(i);
                        l.setPadding(new Insets(20,20,20,20));

                        l.setStyle("-fx-border-color: #000000; -fx-border-width: 2; -fx-background-color: #ffffff");
                        GP.add(l,0,Nu);
                        Nu++;
                    }

                    Nu = 0;
                    for (String i : Col2.split(" ")){
                        Label l = new Label(i);
                        l.setPadding(new Insets(20,20,20,20));

                        l.setStyle("-fx-border-color: #000000; -fx-border-width: 2; -fx-background-color: #ffffff");
                        GP.add(l,1,Nu);
                        Nu++;
                    }

                    VBQuestion.getChildren().add(GP);

                }
            }


            AllQuestion.add(VBQuestion);
        }

        return AllQuestion;
    }

    @FXML
    private void IVThemeShow(MouseEvent event) {
        APTheme.setVisible(true);
        VBListTheme.getChildren().clear();
        ArrayList<DataTheme> DT = new ArrayList<>();
        if (Logic.getAllTheme(DT) == -1) System.out.println("Error!");
        for (DataTheme res : DT)
            VBListTheme.getChildren().add(NodeConstructTheme(res));

    }

    private void PassageWindowsInit(){
        DataTheme DT = new DataTheme();
        if (Logic.getTheme(DT, SelectIDTheme) == -1)
            NameTheme.setText("Не удалось подключиться к серверу");
        else {
            NumberTheme.setText(Integer.toString(DT.getID()));
            NameTheme.setText(DT.getTextTheme());
            if (DT.getTime() == null)
                TimeTheme.setText("Отсутствует ограничение по времени");
            else
                TimeTheme.setText(String.valueOf(DT.getTime()));
        }
    }


    @FXML
    public void initialize(URL url, ResourceBundle rb){
        String FileName = FilenameUtils.getBaseName(url.getPath());
        if (FileName.equals("PassageWindow")){
            PassageWindowsInit();
        }
    }

}
