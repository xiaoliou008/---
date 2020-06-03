package main.manager.relic;

import data.*;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import main.Main;
import main.util.Configure;
import main.util.Controller;
import main.util.Dialog;
import main.util.StageSetable;

import java.sql.Array;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;

public class AddDialogController extends Controller implements StageSetable {
    private Stage stage;
    @FXML
    private TextField relicNameTextField;
    @FXML
    private ChoiceBox<RelicType> relicTypeChoiceBox;
    @FXML
    private ChoiceBox<Dynasty> relicDynastyChoiceBox;
    @FXML
    private ChoiceBox<Integer> relicNumChoiceBox;
    @FXML
    private TextField relicUnityTextField;

    @FXML
    void CancelHandler(ActionEvent event) {
        stage.close();
    }

    @FXML
    void OKHandler(ActionEvent event) {
        if(relicNameTextField.getText() == null || relicNameTextField.getText().length() < 1){
            Dialog.showAlert("请输入合法的文物名称", Alert.AlertType.ERROR);
            return;
        }
        try {
            RelicSQL.insert(relicNameTextField.getText(),
                    relicTypeChoiceBox.getSelectionModel().getSelectedItem(),
                    relicDynastyChoiceBox.getSelectionModel().getSelectedItem(),
                    relicUnityTextField.getText(),
                    relicNumChoiceBox.getValue()
                    );
            Dialog.showAlert("添加成功");
            stage.close();
        } catch (SQLException e) {
            e.printStackTrace();
            Dialog.showAlert("添加失败", Alert.AlertType.ERROR);
        }
    }

    @Override
    public void setMyApp(Main myApp) throws SQLException, InstantiationException, IllegalAccessException {
        this.myApp = myApp;
        Configure.configChoiceBox(relicTypeChoiceBox, RelicTypeSQL.getAll());
        relicTypeChoiceBox.getSelectionModel().select(0);
        Configure.configChoiceBox(relicDynastyChoiceBox, DynastySQL.getAll());
        relicDynastyChoiceBox.getSelectionModel().select(0);
        relicNumChoiceBox.setItems(FXCollections.observableList(new ArrayList<Integer>(Arrays.asList(1,2,3,4,5,6))));
        relicNumChoiceBox.getSelectionModel().select(0);
    }

    @Override
    public void setStage(Stage stage) throws Exception {
        this.stage = stage;
    }
}
