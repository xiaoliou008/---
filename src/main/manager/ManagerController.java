package main.manager;

import data.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.chart.PieChart;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.input.ContextMenuEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import main.Main;
import main.util.Configure;
import main.util.Controller;
import main.util.Dialog;
import main.util.Executable;
import main.util.local.DialogController;
import mysql.MySQL;

import java.io.File;
import java.sql.Date;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.concurrent.ExecutionException;

/**
 * 管理员界面的控制器
 */
public class ManagerController extends Controller {
    @FXML
    public VBox employeeVBox;
    @FXML
    public VBox displayVBox;
    @FXML
    public TableView<Employee> employeeTable;
    @FXML
    public VBox relicVBox;
    @FXML
    public TableView<Relic> relicTable;
    @FXML
    public TableView<Display> displayTable;
    @FXML
    public Pane managerPane;
    @FXML
    public ChoiceBox<RelicType> relicTypeChoiceBox;
    @FXML
    public ChoiceBox<Dynasty> relicTimeChoiceBox;
    @FXML
    public TextField relicNameTextField;
    @FXML
    public TextField employeeTextField;
    @FXML
    public ChoiceBox<String> employeeChoiceBox;
    @FXML
    public DatePicker datePicker;
    @FXML public CheckBox checkBox;

    private Employee manager;

    // 右键文物表格菜单
    private ContextMenu cmRelic = new ContextMenu();
    private Menu cmRelicEdit = new Menu("编辑");
    private Menu cmRelicSet = new Menu("设置");
    private Menu cmRelicImage = new Menu("图片");
    private MenuItem itemModifyRelic = new MenuItem("修改");
    private MenuItem itemAddRelic = new MenuItem("添加");
    private MenuItem itemDeleteRelic = new MenuItem("删除");
    private MenuItem itemRepairRelic = new MenuItem("修复");
    private MenuItem itemDisplayRelic = new MenuItem("展出");
    private MenuItem itemRelicSetImage = new MenuItem("设置图片");
    private MenuItem itemRelicShowImage = new MenuItem("显示图片");
    private MenuItem itemRelicStatisitcs = new MenuItem("统计");

    // 右键员工表格菜单
    private ContextMenu cmEmployee = new ContextMenu();
    private MenuItem itemModifyEmployee = new MenuItem("修改");
    private MenuItem itemAddEmployee = new MenuItem("添加");
    private MenuItem itemDeleteEmployee = new MenuItem("删除");

    // 右键展出表格菜单
    private ContextMenu cmDisplay = new ContextMenu();
    private MenuItem itemDeleteDisplay = new MenuItem("删除");

    public void setManager(Employee manager) {
        this.manager = manager;
    }

    @Override
    public void setMyApp(Main myApp) throws SQLException, InstantiationException, IllegalAccessException {
        this.myApp = myApp;

        // 配置文物表格
        configRelicTable();
        relicTable.getItems().setAll(RelicSQL.getAll());
        relicTable.setOnContextMenuRequested(this::contextMenuRelicHandler);

        // 右键文物表格菜单
//        cmRelic.getItems().addAll(itemModifyRelic, itemAddRelic, itemDeleteRelic,
//                new SeparatorMenuItem(), itemRepairRelic, itemDisplayRelic);
        cmRelicEdit.getItems().addAll(itemModifyRelic, itemAddRelic, itemDeleteRelic);
        cmRelicSet.getItems().addAll(itemRepairRelic, itemDisplayRelic);
        cmRelicImage.getItems().addAll(itemRelicSetImage, itemRelicShowImage, itemRelicStatisitcs);
        cmRelic.getItems().addAll(cmRelicEdit, cmRelicSet, cmRelicImage);
        relicTable.setContextMenu(cmRelic);
        itemModifyRelic.setOnAction(this::modifyItemHandler);        // 使用method reference 代替lambda表达式
        itemAddRelic.setOnAction(this::addItemHandler);
        itemDeleteRelic.setOnAction(this::deleteItemHandler);
        itemRepairRelic.setOnAction(this::repairItemHandler);
        itemDisplayRelic.setOnAction(this::displayAddItemHandler);
        itemRelicSetImage.setOnAction(this::setImageHandler);
        itemRelicShowImage.setOnAction(this::showImageHandler);
        itemRelicStatisitcs.setOnAction(this::showChartHandler);

        // 配置文物choice box
        Configure.configChoiceBox(relicTypeChoiceBox, RelicTypeSQL.getAll());
        Configure.configChoiceBox(relicTimeChoiceBox, DynastySQL.getAll());

        //配置员工表格
        configEmployeeTable();
        employeeTable.getItems().setAll(EmployeeSQL.getAll());
        employeeTable.setOnContextMenuRequested(this::contextMenuEmployeeHandler);

        // 配置员工choice box
        employeeChoiceBox.getItems().setAll("全部", "管理员", "修复员");
        employeeChoiceBox.getSelectionModel().select(0);

        // 右键员工表格菜单
        cmEmployee.getItems().addAll(itemModifyEmployee, itemAddEmployee, itemDeleteEmployee);
        employeeTable.setContextMenu(cmEmployee);
        itemModifyEmployee.setOnAction(this::modifyEmployeeHandler);        // 使用method reference 代替lambda表达式
        itemAddEmployee.setOnAction(this::addEmployeeHandler);
        itemDeleteEmployee.setOnAction(this::deleteEmployeeHandler);

        // 配置展览表格
        configDisplayTable();
        displayTable.getItems().setAll(DisplaySQL.query(MySQL.getDate()));
        displayTable.setContextMenu(cmDisplay);
        cmDisplay.getItems().addAll(itemDeleteDisplay);
        itemDeleteDisplay.setOnAction(this::deleteDisplayHandler);
        // 设置日期选择为当前日期
        datePicker.setValue(MySQL.getLocalDate());
    }

    private void deleteDisplayHandler(ActionEvent actionEvent) {
        Display display = displayTable.getSelectionModel().getSelectedItem();
        if (display == null) return;
        Dialog.showAlert("确定取消这次展出吗？", () -> {
            DisplaySQL.delete(display);
            Dialog.showAlert("成功取消展出\n文物: " + display.getRname() + "\n时间: " + display.getDtime());
        });
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
        TableColumn<Relic, String> tc_broken = new TableColumn<>("损坏");
        tc_broken.setCellValueFactory(new PropertyValueFactory<Relic, String>("broken"));
        tc_broken.setPrefWidth(50.0);
        TableColumn<Relic, Integer> tc_num = new TableColumn<>("数量");
        tc_num.setCellValueFactory(new PropertyValueFactory<Relic, Integer>("num"));
        TableColumn<Relic, String> tc_unity = new TableColumn<>("单位");
        tc_unity.setCellValueFactory(new PropertyValueFactory<Relic, String>("unity"));
        tc_unity.setPrefWidth(50.0);
        relicTable.getColumns().setAll(
                tc_rid, tc_rname, tc_tidName, tc_cidName, tc_display, tc_broken, tc_num, tc_unity);
    }

    private void configEmployeeTable() {
        TableColumn<Employee, String> tc_pid = new TableColumn<>("编号");
        tc_pid.setCellValueFactory(new PropertyValueFactory<Employee, String>("pid"));
        tc_pid.setPrefWidth(120);
        TableColumn<Employee, String> tc_pname = new TableColumn<>("姓名");
        tc_pname.setCellValueFactory(new PropertyValueFactory<Employee, String>("pname"));
        tc_pname.setPrefWidth(120);
        TableColumn<Employee, String> tc_job = new TableColumn<>("职位");
        tc_job.setCellValueFactory(new PropertyValueFactory<Employee, String>("job"));
        tc_job.setPrefWidth(120);
        employeeTable.getColumns().setAll(tc_pid, tc_pname, tc_job);
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

    public void queryBtnHandler(ActionEvent actionEvent) {
        try {
            relicTable.getItems().setAll(RelicSQL.query(
                    relicNameTextField.getText(),
                    relicTypeChoiceBox.getSelectionModel().getSelectedItem(),
                    relicTimeChoiceBox.getSelectionModel().getSelectedItem()));
        } catch (SQLException e) {
            e.printStackTrace();
            Dialog.showAlert("查询失败", Alert.AlertType.ERROR);
        }
    }

    public void modifyItemHandler(ActionEvent actionEvent) {
        if (relicTable.getSelectionModel().isEmpty()) return;
        Relic relic = relicTable.getSelectionModel().getSelectedItem();
        System.out.println(relic.getRname());
        try {
            myApp.showRelicModifyDialog(relic);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void addItemHandler(ActionEvent actionEvent) {
        try {
            myApp.showNormDialog("manager/relic/add.fxml", "添加文物信息");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void deleteItemHandler(ActionEvent actionEvent) {
        if (relicTable.getSelectionModel().isEmpty()) return;
        Relic relic = relicTable.getSelectionModel().getSelectedItem();
        System.out.println("申请删除：" + relic.getRname());
        String ask = null;
        try {
            ask = "确定删除该文物？\n名称：" + relic.getRname() +
                    "\n类别：" + relic.getTidName() +
                    "\n年代：" + relic.getCidName();
        } catch (SQLException e) {
            e.printStackTrace();
            Dialog.showAlert("数据库异常\n获取类别和年代错误", Alert.AlertType.ERROR);
        }
        Dialog.showAlert(ask, new Executable() {
            @Override
            public void exec() throws SQLException {
                RelicSQL.delete(relic);
                Dialog.showAlert("删除成功");
            }
        });
    }

    public void repairItemHandler(ActionEvent actionEvent) {
        if (relicTable.getSelectionModel().isEmpty()) return;
        Relic relic = relicTable.getSelectionModel().getSelectedItem();
        System.out.println("申请修复：" + relic.getRname());
        Dialog.showTextInput("无", "修理申请", "修理申请", "破损描述", broken -> {
            try {
                RepairSQL.insert(relic.getRid(), broken, MySQL.getTime());
                Dialog.showAlert("申请修复成功");
            } catch (SQLException e) {
                e.printStackTrace();
                Dialog.showAlert("申请修复失败", Alert.AlertType.ERROR);
            }
        });
    }

    public void displayAddItemHandler(ActionEvent actionEvent) {
        try {
            if (relicTable.getSelectionModel().isEmpty()) return;
            Relic relic = relicTable.getSelectionModel().getSelectedItem();
            if(relic.isBroken()){
                Dialog.showAlert("已经损坏的文物不能展出", Alert.AlertType.WARNING);
                return;
            }
            System.out.println("设置展出：" + relic.getRname());
            myApp.showNormDialog("util/local/datePickerDialog.fxml", "选择日期", control -> {
                ((DialogController) control).setRelic(relic);
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setImageHandler(ActionEvent actionEvent) {
        try {
            Relic relic = relicTable.getSelectionModel().getSelectedItem();
            if (relic == null) return;
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("选择文物图片");
            fileChooser.getExtensionFilters().addAll(
                    new FileChooser.ExtensionFilter("图像文件", "*.png", "*.jpg", "*.gif"),
                    new FileChooser.ExtensionFilter("所有文件", "*.*"));
            fileChooser.setInitialDirectory(new File("./src/icon"));
            File selectedFile = fileChooser.showOpenDialog(myApp.getStage());
            if (selectedFile != null) {
                RelicSQL.setImage(relic, selectedFile.getPath());
                Dialog.showAlert("设置图片成功");
            }
        } catch (Exception e) {
            Dialog.showAlert("设置图片失败", Alert.AlertType.ERROR);
            e.printStackTrace();
        }
    }

    private void showImageHandler(ActionEvent actionEvent) {
        Relic relic = relicTable.getSelectionModel().getSelectedItem();
        if (relic == null) return;
        if(relic.getImagePath() == null){
            Dialog.showAlert("该文物暂时没有图片", Alert.AlertType.WARNING);
        } else {
            String path = "file:" + relic.getImagePath();
            System.out.println(path);
            Dialog.showDialog("图片: " + relic.getRname(),
                    new Image(path, 600, 600, true, true));
        }
    }

    public void menuManageRelicHandler(ActionEvent actionEvent) {
        relicVBox.setVisible(true);
        employeeVBox.setVisible(false);
        displayVBox.setVisible(false);
    }

    public void menuEmployeeRelicHandler(ActionEvent actionEvent) {
        relicVBox.setVisible(false);
        employeeVBox.setVisible(true);
        displayVBox.setVisible(false);
    }

    public void menuDisplayRelicHandler(ActionEvent actionEvent) {
        relicVBox.setVisible(false);
        employeeVBox.setVisible(false);
        displayVBox.setVisible(true);
    }

    public void menuAboutHandler(ActionEvent actionEvent) {
        Dialog.showAlert("制作人信息", "制作人：刘静平\n班级：ACM1701\n学号：U201714624");
    }

    public void queryEmployeeHandler(ActionEvent actionEvent) {
        try {
            employeeTable.getItems().setAll(EmployeeSQL.query(employeeTextField.getText(), employeeChoiceBox.getValue()));
        } catch (SQLException e) {
            e.printStackTrace();
            Dialog.showAlert("查询失败", Alert.AlertType.ERROR);
        }
    }

    public void menuPasswordHandler(ActionEvent actionEvent) {
        Dialog.showTextInput("", "修改", "修改密码", "请输入更新后的密码", psword->{
            try {
                if(psword == null || psword.length() < 1 || psword.length() > 32){
                    Dialog.showAlert("新密码长度不符合要求");
                } else {
                    EmployeeSQL.updatePassword(this.manager, psword);
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

    public void modifyEmployeeHandler(ActionEvent actionEvent) {
        if (employeeTable.getSelectionModel().isEmpty()) return;
        Employee employee = employeeTable.getSelectionModel().getSelectedItem();
        System.out.println(employee.getPname());
        try {
            myApp.showEmployeeModifyDialog(employee);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void addEmployeeHandler(ActionEvent actionEvent) {
        try {
            myApp.showNormDialog("manager/employee/add.fxml", "添加新员工");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void deleteEmployeeHandler(ActionEvent actionEvent) {
        if (employeeTable.getSelectionModel().isEmpty()) return;
        Employee employee = employeeTable.getSelectionModel().getSelectedItem();
        System.out.println(employee.getPname());
        String ask = "确定删除该员工信息？\n姓名：" + employee.getPname() +
                "\n职位：" + employee.getJob();
        Dialog.showAlert(ask, () -> {
            EmployeeSQL.delete(employee);
            Dialog.showAlert("删除成功");
        });
    }

    private void contextMenuRelicHandler(ContextMenuEvent contextMenuEvent) {
        if (relicTable.getSelectionModel().isEmpty()) {
            itemDeleteRelic.setVisible(false);
            itemModifyRelic.setVisible(false);
        } else {
            itemDeleteRelic.setVisible(true);
            itemModifyRelic.setVisible(true);
        }
    }

    public void contextMenuEmployeeHandler(ContextMenuEvent contextMenuEvent) {
        if (employeeTable.getSelectionModel().isEmpty()) {    // SetDisable 有BUG，样式一旦disable就回不来了
            itemDeleteEmployee.setVisible(false);
            itemModifyEmployee.setVisible(false);
        } else {
            itemDeleteEmployee.setVisible(true);
            itemModifyEmployee.setVisible(true);
        }
    }

    public void queryDisplayHandler(ActionEvent actionEvent) throws SQLException {
        if (datePicker.getEditor().getText() == null) return;
        System.out.println(datePicker.getEditor().getText());
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

    public void showChartHandler(ActionEvent actionEvent){
        try {
            ObservableList<PieChart.Data> datalist = FXCollections.observableArrayList();
            ArrayList<String> list = new ArrayList<>(Arrays.asList("陶瓷", "绘画", "书法", "铭刻", "青铜器"));
            for (String s : list) {
                PieChart.Data data = new PieChart.Data(s, RelicSQL.queryType(s));
                datalist.add(data);
            }
            PieChart pieChart = new PieChart(datalist);
            Dialog.showChartDialog("类别统计", pieChart);
        } catch (SQLException e){
            e.printStackTrace();
        }
    }
}
