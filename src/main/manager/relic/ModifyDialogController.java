package main.manager.relic;

import data.*;
import javafx.event.ActionEvent;
import javafx.scene.control.Alert;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import main.Main;
import main.util.Configure;
import main.util.Controller;
import main.util.Dialog;
import main.util.StageSetable;

import java.sql.SQLException;

public class ModifyDialogController extends Controller implements StageSetable {
    private Stage stage;
    private Relic relic;
    public TextField relicNameTextField;
    public ChoiceBox relicTypeChoiceBox;
    public ChoiceBox relicDynastyChoiceBox;

    @Override
    public void setMyApp(Main myApp) throws SQLException, InstantiationException, IllegalAccessException {
        this.myApp = myApp;
    }

    public void setStage(Stage stage) throws SQLException, InstantiationException, IllegalAccessException {
        this.stage = stage;
    }

    public void configNode(Relic relic) throws SQLException, InstantiationException, IllegalAccessException {
        this.relic = relic;
        relicNameTextField.setText(relic.getRname());
        Configure.configChoiceBox(relicTypeChoiceBox, RelicTypeSQL.getAll());
        relicTypeChoiceBox.getSelectionModel().select(RelicTypeSQL.queryTID(relic.getTid()));
        Configure.configChoiceBox(relicDynastyChoiceBox, DynastySQL.getAll());
        relicDynastyChoiceBox.getSelectionModel().select(DynastySQL.queryCID(relic.getCid()));
    }

    public void OKHandler(ActionEvent actionEvent) {
        if(relicNameTextField.getText() == null || relicNameTextField.getText().length() < 1){
            Dialog.showAlert("请输入合法的文物名称", Alert.AlertType.ERROR);
            return;
        }       // 暂时没考虑年代和类型选择有问题
        try {
            RelicSQL.update(relic.getRid(), relicNameTextField.getText(),
                    (RelicType) relicTypeChoiceBox.getSelectionModel().getSelectedItem(),
                    (Dynasty) relicDynastyChoiceBox.getSelectionModel().getSelectedItem());
            Dialog.showAlert("修改成功！");
            stage.close();
        } catch (SQLException e) {
            Dialog.showAlert("文物信息修改失败！", Alert.AlertType.WARNING);
//            e.printStackTrace();
        }
    }

    public void CancelHandler(ActionEvent actionEvent) {
        stage.close();
    }
}
