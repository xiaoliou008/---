package main.util.local;

import data.Display;
import data.DisplaySQL;
import data.Relic;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.stage.Stage;
import main.Main;
import main.util.Controller;
import main.util.Dialog;
import main.util.StageSetable;
import mysql.MySQL;

import java.sql.SQLException;

public class DialogController extends Controller implements StageSetable {
    @FXML public DatePicker datePicker;
    @FXML public ChoiceBox<String> choiceBox;
    private Stage stage;
    private Relic relic;

    public void setRelic(Relic relic) {
        this.relic = relic;
    }

    @Override
    public void setMyApp(Main myApp) throws SQLException, InstantiationException, IllegalAccessException {
        this.myApp = myApp;
        datePicker.setValue(MySQL.getLocalDate());
        choiceBox.getItems().addAll("展厅A", "展厅B", "展厅C", "展厅D", "展厅E", "展厅F");
        choiceBox.getSelectionModel().selectFirst();
    }

    @Override
    public void setStage(Stage stage) throws Exception {
        this.stage = stage;
    }

    public void OKHandler(ActionEvent actionEvent){
        if(choiceBox.getSelectionModel().isEmpty() || datePicker.getEditor().getText() == null) return;
        try {
            DisplaySQL.insert(relic.getRid(), datePicker.getEditor().getText(), choiceBox.getValue());
            Dialog.showAlert("设置展出成功");
            stage.close();
        } catch (SQLException e) {
//            e.printStackTrace();
            Dialog.showAlert("该文物已经在这天展出", Alert.AlertType.WARNING);
        }
    }

    public void CancelHandler(ActionEvent actionEvent) {
        stage.close();
    }
}
