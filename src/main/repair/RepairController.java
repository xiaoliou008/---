package main.repair;

import data.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.ContextMenuEvent;
import javafx.scene.layout.VBox;
import main.Main;
import main.util.Configure;
import main.util.Controller;
import main.util.Dialog;
import mysql.MySQL;

import java.sql.Date;
import java.sql.SQLException;

public class RepairController extends Controller {
    @FXML public CheckBox checkBox;
    @FXML
    private VBox relicVBox;
    @FXML
    private TableView<Repair> repairTable;
    @FXML
    private TextField relicNameTextField;
    @FXML
    private ChoiceBox<RelicType> relicTypeChoiceBox;
    @FXML
    private DatePicker datePicker;

    private Employee employee;

    // 右键菜单
    private ContextMenu cmRepair = new ContextMenu();
    private MenuItem itemQueryRepair = new MenuItem("查看详情");
    private MenuItem itemReceiveRepair = new MenuItem("接受申请");
    private MenuItem itemFinishRepair = new MenuItem("完成修复");

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    @FXML
    void menuAboutHandler(ActionEvent event) {
        Dialog.showAlert("制作人信息", "制作人：刘静平\n班级：ACM1701\n学号：U201714624");
    }

    @FXML
    void queryBtnHandler(ActionEvent event) throws SQLException {
        if(checkBox.isSelected()){      // 不考虑时间
            repairTable.getItems().setAll(RepairSQL.query(relicNameTextField.getText(), relicTypeChoiceBox.getValue()));
        }else {
            repairTable.getItems().setAll(RepairSQL.query(relicNameTextField.getText(),
                    relicTypeChoiceBox.getValue(), datePicker.getEditor().getText()));
        }
    }

    @Override
    public void setMyApp(Main myApp) throws SQLException, InstantiationException, IllegalAccessException {
        this.myApp = myApp;
        Configure.configChoiceBox(relicTypeChoiceBox, RelicTypeSQL.getAll());
        configRepairTable();
        // 设置日期选择为当前日期
        datePicker.setValue(MySQL.getLocalDate());
        queryBtnHandler(new ActionEvent());

        // 配置右键菜单
        cmRepair.getItems().addAll(itemQueryRepair, itemReceiveRepair, itemFinishRepair);
        repairTable.setContextMenu(cmRepair);
        repairTable.setOnContextMenuRequested(this::contextMenuRequestedHandler);
        itemQueryRepair.setOnAction(this::queryRepairHandler);
        itemReceiveRepair.setOnAction(this::receiveRepairHandler);
        itemFinishRepair.setOnAction(this::finishRepairHandler);

    }

    private void finishRepairHandler(ActionEvent actionEvent) {
        Dialog.showTextInput("无", "完成修复", "修复结果", "请输入修复结果", s->{
            try {
                RepairSQL.finish(s, repairTable.getSelectionModel().getSelectedItem());
                Dialog.showAlert("结果", "成功完成修复");
            } catch (SQLException e) {
                e.printStackTrace();
                Dialog.showAlert("操作失败", Alert.AlertType.ERROR);
            }
        });
    }

    private void receiveRepairHandler(ActionEvent actionEvent) {
        Dialog.showAlert("确定接受修复申请？", ()->{
            try {
                RepairSQL.receive(employee, repairTable.getSelectionModel().getSelectedItem());
                Dialog.showAlert("结果", "成功接受修复申请");
            } catch (SQLException e) {
                e.printStackTrace();
                Dialog.showAlert("接受修复申请失败", Alert.AlertType.ERROR);
            }
        });
    }

    private void queryRepairHandler(ActionEvent actionEvent) {
        Repair repair = repairTable.getSelectionModel().getSelectedItem();
        if(repair.getEtime() == null)
            Dialog.showAlert("破损状况", repair.getBroke());
        else
            Dialog.showAlert("修复状况", repair.getRestore());
    }

    private void contextMenuRequestedHandler(ContextMenuEvent contextMenuEvent) {

        if (repairTable.getSelectionModel().isEmpty()) {
            cmRepair.getItems().clear();
        } else {
            cmRepair.getItems().setAll(itemQueryRepair);
            Repair repair = repairTable.getSelectionModel().getSelectedItem();
            if(repair.getEtime() != null){      // 修复完成
                return;
            }
            if (repair.getPid() == null) {      // 未接受
                cmRepair.getItems().addAll(itemReceiveRepair);
                return;
            } else if (repair.getPid().equals(employee.getPid())) {
//                System.out.println("debug#");
                cmRepair.getItems().addAll(itemFinishRepair);
                return;
            }
        }
    }

    private void configRepairTable() {
        double width = 120;
        TableColumn<Repair, String> tc_eid = new TableColumn<>("编号");
        tc_eid.setCellValueFactory(new PropertyValueFactory<Repair, String>("eid"));
        tc_eid.setPrefWidth(50);
        TableColumn<Repair, Date> tc_rname = new TableColumn<>("文物名称");
        tc_rname.setCellValueFactory(new PropertyValueFactory<Repair, Date>("rname"));
        tc_rname.setPrefWidth(width);
        TableColumn<Repair, String> tc_stime = new TableColumn<>("申请时间");
        tc_stime.setCellValueFactory(new PropertyValueFactory<Repair, String>("stime"));
        tc_stime.setPrefWidth(width);
        TableColumn<Repair, String> tc_result = new TableColumn<>("修复状态");
        tc_result.setCellValueFactory(new PropertyValueFactory<Repair, String>("result"));
        TableColumn<Repair, String> tc_etime = new TableColumn<>("修复时间");
        tc_etime.setCellValueFactory(new PropertyValueFactory<Repair, String>("etime"));
        tc_etime.setPrefWidth(width);
        TableColumn<Repair, String> tc_pname = new TableColumn<>("修复员");
        tc_pname.setCellValueFactory(new PropertyValueFactory<Repair, String>("pname"));
        repairTable.getColumns().setAll(tc_eid, tc_rname, tc_stime, tc_result, tc_etime, tc_pname);
    }

    public void menuPasswordHandler(ActionEvent actionEvent) {
        Dialog.showTextInput("", "修改", "修改密码", "请输入更新后的密码", psword->{
            try {
                if(psword == null || psword.length() < 1 || psword.length() > 32){
                    Dialog.showAlert("新密码长度不符合要求");
                } else {
                    EmployeeSQL.updatePassword(this.employee, psword);
                    Dialog.showAlert("修改密码成功");
                }
            } catch (SQLException e) {
                e.printStackTrace();
                Dialog.showAlert("修改密码失败", Alert.AlertType.ERROR);
            }
        });
    }

    public void menuLogoutHandler(ActionEvent actionEvent) throws Exception {
        myApp.changeScene("main.fxml", "博物馆", ctrl -> {});
    }

    public void checkBoxHandler(ActionEvent actionEvent) {
        if(checkBox.isSelected()){
            datePicker.setDisable(true);
        } else {
            datePicker.setDisable(false);
        }
    }
}
