package main.manager.employee;

import data.Employee;
import data.EmployeeSQL;
import data.Relic;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import main.Main;
import main.util.Controller;
import main.util.Dialog;
import main.util.StageSetable;

import java.sql.SQLException;

public class ModifyEmployeeController extends Controller implements StageSetable {
    private Stage stage;
    private Employee employee;
    @FXML
    private TextField nameTextField;

    @FXML
    private ChoiceBox<String> jobChoiceBox;

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
        try {
            EmployeeSQL.update(employee.getPid(), nameTextField.getText(), jobChoiceBox.getValue());
            Dialog.showAlert("修改成功");
        } catch (SQLException e) {
            e.printStackTrace();
            Dialog.showAlert("修改失败", Alert.AlertType.ERROR);
        } finally {
            stage.close();
        }
    }

    @Override
    public void setMyApp(Main myApp) throws SQLException, InstantiationException, IllegalAccessException {
        this.myApp = myApp;
        jobChoiceBox.getItems().setAll("管理员", "修复员");
    }

    @Override
    public void setStage(Stage stage) throws Exception {
        this.stage = stage;
    }

    public void setEmployee(Employee employee){
        this.employee = employee;
        nameTextField.setText(employee.getPname());
        jobChoiceBox.getSelectionModel().select(employee.getJob());
    }
}
