package sample.Controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import sample.Method.*;

public class Controller {

    Method Logic = null;

    @FXML
    public Button ButLogin;

    @FXML
    public Button ButRegistr;

    @FXML
    public AnchorPane APLogin;

    @FXML
    public AnchorPane APRegistr;

    @FXML
    public PasswordField LogPass;

    @FXML
    public TextField LogMailLog;

    @FXML
    public Label LogMsg;

    @FXML
    public Button LogButEnter;

    @FXML
    public Label LogResPass;

    @FXML
    public TextField RegMail;

    @FXML
    public TextField RegLogin;

    @FXML
    public PasswordField RegPass;

    @FXML
    public PasswordField RegPass2;

    @FXML
    public Label RegMsg;

    @FXML
    public Button RegButRegist;

    @FXML
    private void LoginPanelShow(ActionEvent event){
        APLogin.setVisible(true);
        APRegistr.setVisible(false);
    }

    @FXML
    private void Login(ActionEvent event){
        String Res = Logic.login(LogMailLog.getText(), LogPass.getText());
        if (Res.length() > 0)
            LogMsg.setText(Res);
        else
            LogMsg.setText("Успешно");

    }

    @FXML
    private void Registration(ActionEvent event){
        String Res = Logic.add_user(RegMail.getText(),RegLogin.getText(),RegPass.getText(), RegPass2.getText());
        if (Res.length() > 0)
            RegMsg.setText(Res);
        else
            RegMsg.setText("Успешно");
    }


    @FXML
    private void RegistPanelShow(ActionEvent event){
        APRegistr.setVisible(true);
        APLogin.setVisible(false);
    }



    @FXML
    public void initialize(){

       Logic = new Method();

    }
}
