package main;

import data.Employee;
import data.Relic;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import main.manager.ManagerController;
import main.manager.employee.ModifyEmployeeController;
import main.manager.relic.AddDialogController;
import main.manager.relic.ModifyDialogController;
import main.repair.RepairController;
import main.util.Controller;
import main.util.ControllerConfigure;
import main.util.Dialog;
import main.util.StageSetable;
import mysql.MySQL;

import java.io.IOException;
import java.sql.SQLException;

public class Main extends Application {
    private Stage stage, newStage;

    public Stage getStage() {
        return stage;
    }

    @Override
    public void start(Stage primaryStage) throws Exception{
        stage = primaryStage;
        FXMLLoader loader = new FXMLLoader(getClass().getResource("main.fxml"));
        Parent root = loader.load();
        Controller controller = loader.getController();
        controller.setMyApp(this);

        primaryStage.setTitle("博物馆");
        primaryStage.getIcons().add(new Image("/icon/博物馆.png"));
//        primaryStage.setScene(new Scene(root, 300, 275));
        primaryStage.setScene(new Scene(root));
        primaryStage.setX(300);
        primaryStage.setY(50);
        primaryStage.show();

        // 连接数据库
        MySQL.getConnection();

        // 配置窗口退出监听
        primaryStage.setOnCloseRequest(windowEvent -> {
            try {
                MySQL.close();
                System.out.println("关闭窗口");
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });


        // 调试管理员界面
//        changeScene("manager/manager.fxml", "管理员");
//        managerLogin(new Employee("1", "刘静平", "ljp", "管理员"));
        // 调试修复员
//        changeScene("repair/repair.fxml", "修复员");
//        employeeLogin(new Employee("2", "修复员", "xfy", "修复员"));

        // 调试对话框
//        Dialog.showTextInput("default", "title");

    }

    /**
     * 切换场景，例如医生登陆、病人登陆后的场景切换
     * @param targetFXML
     * @param title
     * @throws IOException
     * @throws SQLException
     */
    public void changeScene(String targetFXML, String title) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(targetFXML));
        Pane root = loader.load();

        Controller controller = loader.getController();
        controller.setMyApp(this);

        stage.setScene(new Scene(root));
        stage.setTitle(title);
        stage.show();
    }

    /**
     * 切换场景，例如医生登陆、病人登陆后的场景切换更多配置
     * @param targetFXML
     * @param title
     * @throws IOException
     * @throws SQLException
     */
    public void changeScene(String targetFXML, String title, ControllerConfigure configure) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(targetFXML));
        Pane root = loader.load();

        Controller controller = loader.getController();
        controller.setMyApp(this);
        configure.config(controller);

        stage.setScene(new Scene(root));
        stage.setTitle(title);
        stage.show();
    }

    /**
     * 新建窗口
     * @param targetFXML
     * @param title
     */
    public Controller createStage(String targetFXML, String title) throws Exception {
        newStage = new Stage();
        FXMLLoader loader = new FXMLLoader(getClass().getResource(targetFXML));
        Pane root = loader.load();
        Controller controller = loader.getController();
        controller.setMyApp(this);
        newStage.setScene(new Scene(root));
        newStage.setTitle(title);
        return controller;
    }

    /**
     * 修改文物信息时显示的对话框
     */
    public void showRelicModifyDialog(Relic relic) throws Exception {
        ModifyDialogController controller = (ModifyDialogController)createStage(
                "manager/relic/modify.fxml", "修改文物信息");
        controller.setStage(newStage);
        controller.configNode(relic);
        newStage.show();
    }

    public void showEmployeeModifyDialog(Employee employee) throws Exception{
        ModifyEmployeeController controller = (ModifyEmployeeController)createStage(
                "manager/employee/modify.fxml", "修改员工信息");
        controller.setStage(newStage);
        controller.setEmployee(employee);
        newStage.show();
    }

    /**
     * 添加信息时显示的对话框
     */
    public void showNormDialog(String targetFXML, String title) throws Exception {
        StageSetable controller = (StageSetable)createStage(targetFXML, title);
        controller.setStage(newStage);
        newStage.show();
    }

    /**
     * 添加信息时显示的对话框（更多配置）
     */
    public void showNormDialog(String targetFXML, String title, ControllerConfigure configure) throws Exception {
        StageSetable controller = (StageSetable)createStage(targetFXML, title);
        controller.setStage(newStage);
        configure.config((Controller)controller);
        newStage.show();
    }

    /**
     * 添加信息时返回的对话框
     */
    public StageSetable getNormDialog(String targetFXML, String title) throws Exception {
        StageSetable controller = (StageSetable)createStage(targetFXML, title);
        controller.setStage(newStage);
        return controller;
    }

    public void managerLogin(Employee manager) throws Exception {
        changeScene("manager/manager.fxml", "博物馆管理员: " + manager.getPname(), ctrl->{
            ((ManagerController)ctrl).setManager(manager);
        });
    }

    public void employeeLogin(Employee employee) throws Exception {
        changeScene("repair/repair.fxml", "文物修复员: " + employee.getPname(), ctrl->{
            ((RepairController)ctrl).setEmployee(employee);
        });
    }

    public static void main(String[] args) {
        launch(args);
    }
}
