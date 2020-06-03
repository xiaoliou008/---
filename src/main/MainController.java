package main;

import javafx.event.ActionEvent;
import main.util.Controller;

import java.sql.SQLException;

/**
 * 首页的控制器
 */
public class MainController extends Controller {
    @Override
    public void setMyApp(Main myApp) throws SQLException {
        this.myApp = myApp;
    }

    public void LoginHandler(ActionEvent actionEvent) throws Exception {
        myApp.changeScene("login/login.fxml", "登陆");
    }

    public void VisitorHandler(ActionEvent actionEvent) throws Exception {
        myApp.changeScene("visitor/visitor.fxml", "游客");
    }
}
