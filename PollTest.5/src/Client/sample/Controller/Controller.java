package Client.sample.Controller;

import Client.sample.Main;
import Client.sample.Method.DataUser;
import Client.sample.Method.TestTimer;
import DataTransfer.*;
import Client.sample.Method.Method;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
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
import javafx.stage.WindowEvent;
import org.apache.commons.io.FilenameUtils;

import java.io.IOException;
import java.net.URL;
import java.sql.Time;
import java.util.ArrayList;
import java.util.Optional;
import java.util.ResourceBundle;


public class Controller implements Initializable {

    private Method Logic;

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
    private Button ButReg;

    @FXML
    private Button ButLogin;

    @FXML
    private Label LQuestMsg;

    @FXML
    private VBox VBThemeQuestions;

    @FXML
    private Button BReturn;

    private void sendData(Event event, String Resource) {
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
            e.printStackTrace();
            System.err.println("Error: " + e.getMessage());
        }
    }

    @FXML
    private void ReturnToMenu(ActionEvent event){
        sendData(event, "Visual/Main.fxml");
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

    private void CancelTheme(ActionEvent event, ThemeConstructor DT){
        if (CreateDialog("Вы уверены что хотите завершить тестирование?\n" +
                "(Внимание отмена теста также будет засчитана за попытку)")){
            Logic.getServer().OutData(new InputData(Logic.getUser().getID(), DT.getID()));

            new Thread(() -> {
                while (true) {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    if (Logic.getServer().getData() == null) continue;
                    InputData ID = (InputData) Logic.getServer().getData();
                    String Res = ID.getMsg();
                    Logic.getServer().ClearData();
                    if (Res.length() > 0)
                        Platform.runLater(() -> System.out.println(Res));
                    else
                        Platform.runLater(() -> sendData(event, "Client/sample/Visual/Main.fxml"));
                    break;
                }
            }).start();
        }
    }

    private void EndTheme(ActionEvent event,ThemeConstructor DT, ArrayList<String> Result) {

        boolean Empty = false;
        for (String r : Result)
            if (r.trim().equals("")) {
                Empty = true;
                break;
            }

        if (CreateDialog(Empty ? "Вы не ответили на все вопросы, желаете завершить тест?" :
                "Желаете завершить тест?")) {

            Logic.getServer().OutData(new InputData(Logic.getUser().getID(), DT.getID()));

            new Thread(() -> {
                while (true) {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    if (Logic.getServer().getData() == null) continue;
                    InputData ID = (InputData) Logic.getServer().getData();
                    String Res = ID.getMsg();
                    Logic.getServer().ClearData();
                    if (Res.length() > 0)
                        Platform.runLater(() -> System.out.println(Res));
                    else
                        Platform.runLater(() -> {

                            Logic.getServer().OutData(new InputAnswer(Logic.getUser().getID(), DT.getID(),String.valueOf(Result)));

                            new Thread(() -> {
                                while (true) {
                                    try {
                                        Thread.sleep(1000);
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    }
                                    if (Logic.getServer().getData() == null) continue;
                                    InputAnswer IA = (InputAnswer) Logic.getServer().getData();
                                    String Res2 = IA.getMsg();
                                    Logic.getServer().ClearData();
                                    if (Res2.length() > 0)
                                        Platform.runLater(() -> System.out.println(Res2));
                                    else
                                        Platform.runLater(() -> sendData(event, "Client/sample/Visual/Main.fxml"));
                                    break;
                                }
                            }).start();
                        });
                    break;
                }
            }).start();

        }
    }

    public void EndTime(ActionEvent event,ThemeConstructor DT, ArrayList<String> Result){
        APQuestionMenu.setVisible(false);
        CreateDialogOK("Время тестирования закончилось");
        Logic.getServer().OutData(new InputData(Logic.getUser().getID(), DT.getID()));

        new Thread(() -> {
            while (true) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if (Logic.getServer().getData() == null) continue;
                InputData ID = (InputData) Logic.getServer().getData();
                String Res = ID.getMsg();
                Logic.getServer().ClearData();
                if (Res.length() > 0)
                    Platform.runLater(() -> System.out.println(Res));
                else
                    Platform.runLater(() -> {

                        Logic.getServer().OutData(new InputAnswer(Logic.getUser().getID(), DT.getID(),String.valueOf(Result)));

                        new Thread(() -> {
                            while (true) {
                                try {
                                    Thread.sleep(1000);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                                if (Logic.getServer().getData() == null) continue;
                                InputAnswer IA = (InputAnswer) Logic.getServer().getData();
                                String Res2 = IA.getMsg();
                                Logic.getServer().ClearData();
                                if (Res2.length() > 0)
                                    Platform.runLater(() -> System.out.println(Res2));
                                else
                                    Platform.runLater(() -> sendData(event, "Client/sample/Visual/Main.fxml"));
                                break;
                            }
                        }).start();
                    });
                break;
            }
        }).start();
    }

    @FXML
    private void StartQuestions(ActionEvent event) {
        BStartQuest.setVisible(false);
        LQuestMsg.setVisible(false);
        //receiveData(APQuestions);


        Logic.getServer().OutData(new ThemeData(SelectIDTheme));

        new Thread(() -> {
            while (true) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if (Logic.getServer().getData() == null) continue;
                ThemeData TD = (ThemeData) Logic.getServer().getData();
                String Res = TD.getMsg();
                Logic.getServer().ClearData();
                if (Res.length() > 0)
                    Platform.runLater(() -> System.out.println(Res));
                else
                    Platform.runLater(() -> {

                        Logic.getServer().OutData(new QuestionData(SelectIDTheme));

                        new Thread(() -> {
                            while (true) {
                                try {
                                    Thread.sleep(1000);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                                if (Logic.getServer().getData() == null) continue;
                                QuestionData QD = (QuestionData) Logic.getServer().getData();
                                String Res2 = QD.getMsg();
                                Logic.getServer().ClearData();
                                if (Res2.length() > 0)
                                    Platform.runLater(() -> {
                                        NameTheme.setText("Не удалось подключиться к серверу");
                                        BReturn.setVisible(true);
                                    });
                                else
                                    Platform.runLater(() -> {

                                        ThemeConstructor DT = TD.getTheme();
                                        SCQuestion.setVisible(true);

                                        Button Cancel = new Button("Отменить");
                                        Cancel.setFont(new Font("Times New Roman", 16));
                                        Button End = new Button("Закончить");
                                        End.setFont(new Font("Times New Roman", 16));


                                        ArrayList<Object> AR = new ArrayList<>();
                                        VBThemeQuestions.getChildren().addAll(NodeConstructQuestion(QD.getAllQuestions(),AR));

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


                                    });
                                break;
                            }
                        }).start();



                    });
                break;
            }
        }).start();






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
        ButLogin.setStyle("-fx-background-color: #ff5e00");
        ButReg.setStyle("-fx-background-color: #632500");
        RegMail.clear();
        RegLogin.clear();
        RegPass.clear();
        RegPass2.clear();
        RegMsg.setText("");
    }

    @FXML
    private void Login(ActionEvent event) {
        if ((LogMailLog.getText().trim().length() <= 0) || (LogPass.getText().trim().length() <= 0)) {
            LogMsg.setText("Вы не заполнили все поля");
            return;
        }

        Logic.getServer().OutData(new LoginData(LogMailLog.getText(), LogPass.getText()));

        new Thread(() -> {
            while (true) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if (Logic.getServer().getData() == null) continue;
                LoginData LD = (LoginData) Logic.getServer().getData();
                String Res = LD.getMsg();
                Logic.getServer().ClearData();
                if (Res.length() > 0)
                    Platform.runLater(() -> LogMsg.setText(Res));
                else {
                    Logic.setUser(new DataUser(LD.getID(),LD.getUserName()));
                    Platform.runLater(() -> sendData(event, "Client/sample/Visual/Main.fxml"));
                }
                break;
            }
        }).start();
    }

    @FXML
    private void Registration(ActionEvent event){

        String Check = Logic.RegistrChecking(RegMail.getText(),RegLogin.getText(),RegPass.getText(), RegPass2.getText());
        if (Check.length() > 0){
            RegMsg.setText(Check);
            return;
        }

        Logic.getServer().OutData(new RegistrationData(RegMail.getText(),RegLogin.getText(),RegPass.getText()));

        new Thread(() -> {
            while (true) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if (Logic.getServer().getData() == null) continue;
                String Res = ((RegistrationData) Logic.getServer().getData()).getMsg();
                Logic.getServer().ClearData();
                if (Res.length() > 0)
                    Platform.runLater(() -> RegMsg.setText(Res));
                else
                    Platform.runLater(() -> RegMsg.setText("Вы успешно зарегистрировались"));
                break;
            }
        }).start();

    }


    @FXML
    private void RegistPanelShow(ActionEvent event){
        APReg.setVisible(true);
        APLogin.setVisible(false);
        ButReg.setStyle("-fx-background-color: #ff5e00");
        ButLogin.setStyle("-fx-background-color: #632500");
        LogMailLog.clear();
        LogPass.clear();
        LogMsg.setText("");
    }

    private Node NodeConstructTheme(ThemeConstructor DT) {

        HBox HBTheme = new HBox();
        HBTheme.setAlignment(Pos.CENTER_LEFT);
        HBTheme.setStyle("-fx-border-color: #000000; -fx-border-width: 2; -fx-background-color: #ffffff");
        HBTheme.setPadding(new Insets(10, 10, 10, 10));
        HBTheme.setSpacing(10);

        Label lID = new Label(Integer.toString(DT.getID()));
        lID.setFont(new Font("Times New Roman", 18));
        lID.setMinWidth(Region.USE_PREF_SIZE);
        lID.setMinHeight(Region.USE_PREF_SIZE);
        Label lTheme = new Label(DT.getTextTheme());
        lTheme.setPadding(new Insets(10, 0, 10, 0));
        lTheme.setFont(new Font("Times New Roman", 18));
        lTheme.setWrapText(true);

        VBox VBThemeInfo = new VBox();
        VBThemeInfo.setAlignment(Pos.CENTER_RIGHT);
        VBThemeInfo.setPadding(new Insets(10, 10, 10, 10));
        VBThemeInfo.setSpacing(10);


        int AL = -1;

        Label time = new Label(
                DT.getTime() == null ? "Нет ограничения по времени" : String.valueOf(DT.getTime()));
        time.setFont(new Font("Times New Roman", 16));
        time.setMinWidth(Region.USE_PREF_SIZE);
        time.setMinHeight(Region.USE_PREF_SIZE);
        time.setWrapText(true);


        int NA = DT.getNumberAttempts();
        if (NA != 0) AL = DT.getNumberAttempts() - DT.getAttemptsLeft();
        Label AttemptsLeft = new Label(Integer.toString(
                NA == 0 ? DT.getAttemptsLeft() : AL));
        AttemptsLeft.setFont(new Font("Times New Roman", 16));

        Label NumberAttempts = new Label(NA == 0 ? "Бесконечные попытки" : Integer.toString(DT.getNumberAttempts()));
        NumberAttempts.setFont(new Font("Times New Roman", 16));


        VBThemeInfo.getChildren().addAll(time, AttemptsLeft,NumberAttempts);


        HBTheme.getChildren().addAll(lID, lTheme, VBThemeInfo);
        int finalAL = AL;
        HBTheme.setOnMouseClicked(event -> {
            if (finalAL == 0) CreateDialogOK("У вас кончились попытки для прохождения теста");
            else
                try {
                    SelectIDTheme = DT.getID();
                    sendData(event, "Client/sample/Visual/PassageWindow.fxml");
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
        });

        return HBTheme;
    }



    private ArrayList<Node> NodeConstructQuestion(ArrayList<QuestionConstructor> AllDQ, ArrayList<Object> AllResult){
        int NumberQuestion = 1;
        ArrayList<Node> AllQuestion= new ArrayList<>();
        for (QuestionConstructor DQ : AllDQ) {
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
                    for (int i = 1; Nu < buttons.size(); i++)
                        for (int j = 1 ; j <= 2 &&  Nu < buttons.size(); j++) {
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
                    for (int i = 1; Nu < check.size(); i++)
                        for (int j = 1 ; j <= 2 &&  Nu < check.size(); j++) {
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
        VBListTheme.getChildren().clear();

        Logic.getServer().OutData(new AllThemeData(Logic.getUser().getID()));

        new Thread(() -> {
            while (true) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if (Logic.getServer().getData() == null) continue;
                AllThemeData TD = (AllThemeData) Logic.getServer().getData();
                String Res = TD.getMsg();
                Logic.getServer().ClearData();
                if (Res.length() > 0)
                    Platform.runLater(() -> RegMsg.setText(Res));
                else
                    Platform.runLater(() -> {

                        for (ThemeConstructor res : TD.getAllTheme()) {
                            VBListTheme.getChildren().add(NodeConstructTheme(res));
                        }

                    });
                break;
            }
        }).start();
    }

    private void PassageWindowsInit(){
        Logic.getServer().OutData(new ThemeData(SelectIDTheme));

        new Thread(() -> {
            while (true) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if (Logic.getServer().getData() == null) continue;
                ThemeData TD = (ThemeData) Logic.getServer().getData();
                String Res = TD.getMsg();
                Logic.getServer().ClearData();
                if (Res.length() > 0)
                    Platform.runLater(() -> System.out.println(Res));
                else
                    Platform.runLater(() -> {
                        NumberTheme.setText(Integer.toString(SelectIDTheme));
                        NameTheme.setText(TD.getTheme().getTextTheme());
                        if (TD.getTheme().getTime() == null)
                            TimeTheme.setText("Отсутствует ограничение по времени");
                        else
                            TimeTheme.setText(String.valueOf(TD.getTheme().getTime()));
                        BStartQuest.setVisible(true);
                    });
                break;
            }
        }).start();

    }

    @FXML
    public void initialize(URL url, ResourceBundle rb) {
        this.Logic = Main.Logic;
        String FileName = FilenameUtils.getBaseName(url.getPath());
        switch (FileName) {

            case "PassageWindow" -> PassageWindowsInit();
            //case "Login" -> this.Logic = new Method();
        }

    }


}
