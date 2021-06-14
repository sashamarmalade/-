package sample.Controller;

import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import org.apache.commons.io.FilenameUtils;

import sample.Method.*;

import java.io.IOException;
import java.net.URL;
import java.sql.Time;
import java.util.ArrayList;
import java.util.Optional;
import java.util.ResourceBundle;


public class Controller implements Initializable {

    private Method Logic = new Method();

    public static int SelectIDTheme = -1;

    @FXML
    private AnchorPane APLogin;

    @FXML
    private AnchorPane APReg;

    @FXML
    private AnchorPane APMain;

    @FXML
    private AnchorPane APLogReg;

    @FXML
    private AnchorPane APTheme;

    @FXML
    private AnchorPane APQuestions;

    @FXML
    private AnchorPane APQuestionMenu;

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
    private Button BReturn;

    private void sendData(Event event,String Resource){
        Node node = (Node) event.getSource();
        Stage stage = (Stage) node.getScene().getWindow();
        stage.close();
        try {
            Parent root = FXMLLoader.load(getClass().getClassLoader().getResource(Resource));
            stage.setUserData(Logic);
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();

        } catch (IOException e) {
            System.err.println("Error: " + e.getMessage());
        }
    }

    public void receiveData(Node node) {
        Logic = (Method)node.getScene().getWindow().getUserData();
    }

    @FXML
    private void ReturnToMenu(ActionEvent event){
        sendData(event,"sample/Visual/Main.fxml");
    }

    private void CreateDialogOK(String Text){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Информация");
        alert.setHeaderText(null);
        alert.setContentText(Text);

        alert.showAndWait();
    }

    private boolean CreateDialog(String Text){
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Информация");
        alert.setHeaderText(null);
        alert.setContentText(Text);

        Optional<ButtonType> result = alert.showAndWait();
        return result.get() == ButtonType.OK;
    }

    private void CancelTheme(ActionEvent event, DataTheme DT){
        if (CreateDialog("Вы уверены что хотите завершить тестирование?\n" +
                "(Внимание отмена теста также будет засчитана за попытку)")){
            Logic.InputDataDB(DT.getID());
            sendData(event,"sample/Visual/Main.fxml");
        }
    }

    private void EndTheme(ActionEvent event,DataTheme DT, ArrayList<String> Result) {

        boolean Empty = false;
        for (String r : Result)
            if (r.trim().equals("")) {
                Empty = true;
                break;
            }

        if (CreateDialog(Empty ? "Вы не ответили на все вопросы, желаете завершить тест?" :
                "Желаете завершить тест?")) {
            Logic.InputDataDB(DT.getID());
            Logic.InputAnswerDB(DT.getID(),String.valueOf(Result));
            sendData(event, "sample/Visual/Main.fxml");
        }
    }

    public void EndTime(ActionEvent event,DataTheme DT, ArrayList<String> Result){
        APQuestionMenu.setVisible(false);
        CreateDialogOK("Время тестирования закончилось");
        Logic.InputDataDB(DT.getID());
        Logic.InputAnswerDB(DT.getID(),String.valueOf(Result));
        sendData(event, "sample/Visual/Main.fxml");
    }

    @FXML
    private void StartQuestions(ActionEvent event) {
        BStartQuest.setVisible(false);
        LQuestMsg.setVisible(false);

        receiveData(APQuestions);
        DataTheme DT = new DataTheme();
        if (Logic.getTheme(DT, SelectIDTheme) == -1){
            NameTheme.setText("Не удалось подключиться к серверу");
            BReturn.setVisible(true);
            return;
        }

        ArrayList<DataQuestion> DQ = new ArrayList<>();
        if (Logic.getQuestion(DQ, DT.getID()) == -1){
            NameTheme.setText("Не удалось подключиться к серверу");
            BReturn.setVisible(true);
            return;
        }

        SCQuestion.setVisible(true);

        Button Cancel = new Button("Отменить");
        Cancel.setFont(new Font("Times New Roman", 16));
        Button End = new Button("Закончить");
        End.setFont(new Font("Times New Roman", 16));



        ArrayList<Object> AR = new ArrayList<>();
        VBThemeQuestions.getChildren().addAll(NodeConstructQuestion(DQ,AR));

        Cancel.setOnAction(e-> CancelTheme(e,DT));
        End.setOnAction(e -> EndTheme(e, DT, ConvertResult(AR)));

        HBox EndMenu = new HBox();
        EndMenu.setPadding(new Insets(20,20,20,20));
        EndMenu.getChildren().addAll(Cancel, End);
        EndMenu.setAlignment(Pos.CENTER);
        EndMenu.setSpacing(20);

        VBThemeQuestions.getChildren().addAll(EndMenu);

        if (DT.getTime() != null) {
            Time time = DT.getTime();
            new TestTimer(time, TimeTheme, this, DT, AR, event);
        }
    }

    public ArrayList<String> ConvertResult(ArrayList<Object> AR){
        ArrayList<String> Result = new ArrayList<>();
        for (Object o: AR)
            switch (o.getClass().getSimpleName()) {

                case "ToggleGroup" -> {
                    RadioButton RB = (RadioButton)((ToggleGroup) o).getSelectedToggle();
                    Result.add(RB == null? " ": RB.getText());
                }
                case "ArrayList" -> {
                    StringBuilder group = new StringBuilder();
                    for (CheckBox CB: (ArrayList<CheckBox>) o)
                        if (CB.isSelected())
                            group.append(CB.getText()).append(" ");
                        Result.add(String.valueOf(group));
                }
                case "TextField" -> Result.add(((TextField) o).getText());
            }

        return Result;
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
        else sendData(event,"sample/Visual/Main.fxml");

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
        HBTheme.setAlignment(Pos.CENTER);
        HBTheme.setStyle("-fx-border-color: #000000; -fx-border-width: 2; -fx-background-color: #ffffff");
        HBTheme.setPadding(new Insets(0, 10, 0, 10));
        HBTheme.setSpacing(10);

        Label lID = new Label(Integer.toString(DT.getID()));
        lID.setFont(new Font("Times New Roman", 18));
        Label lTheme = new Label(DT.getTextTheme());
        lTheme.setFont(new Font("Times New Roman", 18));
        lTheme.setWrapText(true);

        VBox VBThemeInfo = new VBox();
        VBThemeInfo.setAlignment(Pos.CENTER_RIGHT);
        VBThemeInfo.setPadding(new Insets(10, 20, 10, 20));
        VBThemeInfo.setSpacing(10);

        int AL = -1;
        if (DT.getType().equals("Test")) {

            Label time = new Label(
                    DT.getTime() == null ? "Нет ограничения по времени" : String.valueOf(DT.getTime()));
            time.setFont(new Font("Times New Roman", 16));

            int NA = DT.getNumberAttempts();
            if (NA != 0) AL = DT.getNumberAttempts() - DT.getAttemptsLeft();
            Label AttemptsLeft = new Label(Integer.toString(
                    NA == 0? DT.getAttemptsLeft():AL));
            AttemptsLeft.setFont(new Font("Times New Roman", 16));

            Label NumberAttempts = new Label( NA == 0? "Бесконечные попытки":Integer.toString(DT.getNumberAttempts()));
            NumberAttempts.setFont(new Font("Times New Roman", 16));

            VBThemeInfo.getChildren().addAll(time, AttemptsLeft, NumberAttempts);
        }


        HBTheme.getChildren().addAll(lID, lTheme,VBThemeInfo);
        int finalAL = AL;
        HBTheme.setOnMouseClicked(event->{
            if (finalAL == 0) CreateDialogOK("У вас кончились попытки для прохождения теста");
            else
            try {
                SelectIDTheme = DT.getID();
                sendData(event, "sample/Visual/PassageWindow.fxml");
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        });

        return HBTheme;
    }

    private ArrayList<Node> NodeConstructQuestion(ArrayList<DataQuestion> AllDQ, ArrayList<Object> AllResult){
        int NumberQuestion = 1;
        ArrayList<Node> AllQuestion= new ArrayList<>();
        for (DataQuestion DQ : AllDQ) {
            VBox VBQuestion = new VBox();
            VBQuestion.setAlignment(Pos.CENTER_LEFT);
            VBQuestion.setStyle("-fx-border-color: #000000; -fx-border-width: 2; -fx-background-color: #ffffff");
            VBQuestion.setPadding(new Insets(10, 10, 10, 10));
            VBQuestion.setSpacing(10);

            Label Number = new Label("Вопрос №"+Integer.toString(NumberQuestion));
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
                    AllResult.add(group);
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
                    AllResult.add(check);
                    VBQuestion.getChildren().add(GridRadio);
                }
                case completion_of_sentences -> {
                    TextField Text = new TextField();
                    AllResult.add(Text);
                    VBQuestion.getChildren().add(Text);
                }
            }


            AllQuestion.add(VBQuestion);
            NumberQuestion++;
        }

        return AllQuestion;
    }

    @FXML
    private void IVThemeShow(MouseEvent event) {
        receiveData(APMain);
        VBListTheme.getChildren().clear();
        ArrayList<DataTheme> DT = new ArrayList<>();
        if (Logic.getAllTheme(DT) == -1) System.out.println("Error!");
        for (DataTheme res : DT)
            VBListTheme.getChildren().add(NodeConstructTheme(res));
    }

    private void PassageWindowsInit(){
        DataTheme DT = new DataTheme();
        if (Logic.getTheme(DT, SelectIDTheme) == -1) {
            NameTheme.setText("Не удалось подключиться к серверу");
            return;
        }

        NumberTheme.setText(Integer.toString(DT.getID()));
        NameTheme.setText(DT.getTextTheme());
        if (DT.getTime() == null)
            TimeTheme.setText("Отсутствует ограничение по времени");
        else
            TimeTheme.setText(String.valueOf(DT.getTime()));

    }

    @FXML
    public void initialize(URL url, ResourceBundle rb){
        String FileName = FilenameUtils.getBaseName(url.getPath());

        switch (FileName){
            case "PassageWindow" -> PassageWindowsInit();

        }



    }

}
