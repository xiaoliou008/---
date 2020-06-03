package main.login;

import data.Employee;
import data.EmployeeSQL;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import main.Main;
import main.util.Controller;
import main.util.Dialog;

import java.sql.SQLException;

public class LoginController extends Controller {

    @FXML
    private TextField textField;

    @FXML
    private PasswordField passwordField;

    @FXML
    void LoginHandler(ActionEvent event) throws Exception {
        String pid = textField.getText();
        String psword = passwordField.getText();
        if(pid == null || pid.length() < 1){
            Dialog.showAlert("请输入员工编号", Alert.AlertType.WARNING);
        } else if(psword == null || psword.length() < 1){
            Dialog.showAlert("请输入员工密码", Alert.AlertType.WARNING);
        } else {
            Employee employee = EmployeeSQL.login(pid, psword);
            if(employee != null){
                if(employee.getJob().equals("管理员"))
                    myApp.managerLogin(employee);
                else if(employee.getJob().equals("修复员"))
                    myApp.employeeLogin(employee);
                else Dialog.showAlert("职位不可登陆", Alert.AlertType.INFORMATION);
            } else {        // 密码错误
                Dialog.showAlert("用户名或密码错误", Alert.AlertType.ERROR);
            }
        }
    }

    @Override
    public void setMyApp(Main myApp) throws SQLException, InstantiationException, IllegalAccessException {
        this.myApp = myApp;
    }
}
