package main.manager.employee;

import data.EmployeeSQL;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import main.Main;
import main.util.Controller;
import main.util.Dialog;
import main.util.StageSetable;

import java.sql.SQLException;

public class AddEmployeeController extends Controller implements StageSetable {
    private Stage stage;
    @FXML
    private TextField nameTextField;
    @FXML
    private ChoiceBox<String> employeeChoiceBox;
    @FXML
    private PasswordField passwordField;

    @FXML
    void CancelHandler(ActionEvent event) {
        stage.close();
    }

    @FXML
    void OKHandler(ActionEvent event) {
        if(nameTextField.getText() == null || nameTextField.getText().length() < 2){
            Dialog.showAlert("姓名过短", Alert.AlertType.ERROR);
            return;
        }
        if(passwordField.getText() == null || passwordField.getText().length() < 1){
            Dialog.showAlert("请初始化员工密码", Alert.AlertType.ERROR);
            return;
        }
        try {
            EmployeeSQL.insert(nameTextField.getText(), passwordField.getText(), employeeChoiceBox.getValue());
            Dialog.showAlert("添加新员工成功");
        } catch (SQLException e) {
            e.printStackTrace();
            Dialog.showAlert("添加新员工失败", Alert.AlertType.ERROR);
        } finally {
            stage.close();
        }
    }

    @Override
    public void setMyApp(Main myApp) throws SQLException, InstantiationException, IllegalAccessException {
        this.myApp = myApp;
        employeeChoiceBox.getItems().setAll("管理员", "修复员");
        employeeChoiceBox.getSelectionModel().select(1);
    }

    @Override
    public void setStage(Stage stage) throws Exception {
        this.stage = stage;
    }
}
