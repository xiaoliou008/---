package main.visitor;

import data.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.layout.VBox;
import main.Main;
import main.util.Configure;
import main.util.Controller;
import main.util.Dialog;
import mysql.MySQL;

import java.sql.Date;
import java.sql.SQLException;

public class VisitorController extends Controller {
    @FXML
    public VBox displayVBox;
    @FXML
    public VBox relicVBox;
    @FXML
    public TableView<Relic> relicTable;
    @FXML
    public TableView<Display> displayTable;
    @FXML
    public ChoiceBox<RelicType> relicTypeChoiceBox;
    @FXML
    public ChoiceBox<Dynasty> relicTimeChoiceBox;
    @FXML
    public TextField relicNameTextField;
    @FXML public DatePicker datePicker;
    @FXML public CheckBox checkBox;

    // 右键菜单
    MenuItem menuImage = new MenuItem("显示图片");
    ContextMenu cmRelic = new ContextMenu(menuImage);

    @Override
    public void setMyApp(Main myApp) throws SQLException, InstantiationException, IllegalAccessException {
        this.myApp = myApp;

        // 配置文物表格
        configRelicTable();
        relicTable.getItems().setAll(RelicSQL.getAll());
        relicTable.setContextMenu(cmRelic);
        menuImage.setOnAction(this::showImageHandler);

        // 配置文物choice box
        Configure.configChoiceBox(relicTypeChoiceBox, RelicTypeSQL.getAll());
        Configure.configChoiceBox(relicTimeChoiceBox, DynastySQL.getAll());

        // 配置展览表格
        configDisplayTable();
        displayTable.getItems().setAll(DisplaySQL.query(MySQL.getDate()));
        // 设置日期选择为当前日期
        datePicker.setValue(MySQL.getLocalDate());
    }

    private void showImageHandler(ActionEvent actionEvent) {
        Relic relic = relicTable.getSelectionModel().getSelectedItem();
        if (relic == null) return;
        if(relic.getImagePath() == null){
            Dialog.showAlert("该文物暂时没有图片", Alert.AlertType.WARNING);
        } else {
            String path = "file:" + relic.getImagePath();
            Dialog.showDialog("图片: " + relic.getRname(),
                    new Image(path, 600, 600, true, true));
        }
    }

    private void configRelicTable() {
        TableColumn<Relic, String> tc_rid = new TableColumn<>("编号");
        tc_rid.setCellValueFactory(new PropertyValueFactory<Relic, String>("rid"));
        TableColumn<Relic, String> tc_rname = new TableColumn<>("名称");
        tc_rname.setCellValueFactory(new PropertyValueFactory<Relic, String>("rname"));
        TableColumn<Relic, String> tc_tidName = new TableColumn<>("分类");
        tc_tidName.setCellValueFactory(new PropertyValueFactory<Relic, String>("tidName"));
        TableColumn<Relic, String> tc_cidName = new TableColumn<>("朝代");
        tc_cidName.setCellValueFactory(new PropertyValueFactory<Relic, String>("cidName"));
        TableColumn<Relic, String> tc_display = new TableColumn<>("展出");
        tc_display.setCellValueFactory(new PropertyValueFactory<Relic, String>("display"));
        tc_display.setPrefWidth(50.0);
        TableColumn<Relic, Integer> tc_num = new TableColumn<>("数量");
        tc_num.setCellValueFactory(new PropertyValueFactory<Relic, Integer>("num"));
        TableColumn<Relic, String> tc_unity = new TableColumn<>("单位");
        tc_unity.setCellValueFactory(new PropertyValueFactory<Relic, String>("unity"));
        tc_unity.setPrefWidth(50.0);
        relicTable.getColumns().setAll(
                tc_rid, tc_rname, tc_tidName, tc_cidName, tc_display, tc_num, tc_unity);
    }

    private void configDisplayTable() {
        TableColumn<Display, String> tc_rname = new TableColumn<>("文物名称");
        tc_rname.setCellValueFactory(new PropertyValueFactory<Display, String>("rname"));
        tc_rname.setPrefWidth(120);
        TableColumn<Display, Date> tc_date = new TableColumn<>("展出日期");
        tc_date.setCellValueFactory(new PropertyValueFactory<Display, Date>("dtime"));
        tc_date.setPrefWidth(120);
        TableColumn<Display, String> tc_place = new TableColumn<>("展馆名称");
        tc_place.setCellValueFactory(new PropertyValueFactory<Display, String>("place"));
        tc_place.setPrefWidth(120);
        displayTable.getColumns().setAll(tc_rname, tc_date, tc_place);
    }

    public void queryBtnHandler(ActionEvent actionEvent) throws SQLException {
        relicTable.getItems().setAll(RelicSQL.query(
                relicNameTextField.getText(),
                (RelicType) relicTypeChoiceBox.getSelectionModel().getSelectedItem(),
                (Dynasty) relicTimeChoiceBox.getSelectionModel().getSelectedItem()));
    }

    public void menuVisitorRelicHandler(ActionEvent actionEvent) {
        relicVBox.setVisible(true);
        displayVBox.setVisible(false);
    }

    public void menuDisplayRelicHandler(ActionEvent actionEvent) {
        relicVBox.setVisible(false);
        displayVBox.setVisible(true);
    }

    public void menuAboutHandler(ActionEvent actionEvent) {
        main.util.Dialog.showAlert("制作人：刘静平\n班级：ACM1701\n学号：U201714624");
    }

    public void queryDisplayHandler(ActionEvent actionEvent) throws SQLException {
        if(checkBox.isSelected()){
            displayTable.getItems().setAll(DisplaySQL.getAll());
        }else {
            displayTable.getItems().setAll(DisplaySQL.query(datePicker.getEditor().getText()));
        }
    }

    public void checkBoxHandler(ActionEvent actionEvent) {
        if(checkBox.isSelected()){
            datePicker.setDisable(true);
        } else {
            datePicker.setDisable(false);
        }
    }
}
