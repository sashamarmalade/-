package sample.Controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import sample.Method.*;

public class Controller {

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

    public void LoginPanelShow(){
        APLogin.setVisible(true);
        APRegistr.setVisible(false);
    }

    public void Signin(){

    }


    public void RegistrPanelShow(){
        APRegistr.setVisible(true);
        APLogin.setVisible(false);
    }



    @FXML
    public void initialize(){

        new Method();

    }
}
