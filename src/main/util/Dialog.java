package main.util;

import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import main.Main;
import main.util.local.DialogController;

import java.sql.SQLException;

public class Dialog {
    public static void showAlert(String s){
        Alert alert = new Alert(Alert.AlertType.INFORMATION, s);
        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                alert.close();
            }
        });
    }

    public static void showAlert(String head, String s){
        Alert alert = new Alert(Alert.AlertType.INFORMATION, s);
        alert.setHeaderText(head);
        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                alert.close();
            }
        });
    }

    public static void showAlert(String s, Alert.AlertType type){
        Alert alert = new Alert(type, s);
        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                alert.close();
            }
        });
    }

    public static void showAlert(String s, Executable executor){
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, s);
        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                try {
                    executor.exec();
                } catch (SQLException e) {
//                    e.printStackTrace();
                    showAlert("执行失败", Alert.AlertType.ERROR);
                } finally {
                    alert.close();
                }
            } else {
                alert.close();
            }
        });
    }

    public static void showTextInput(String defaultValue, String title, String head, String content, StringExecutable e){
        TextInputDialog dialog = new TextInputDialog(defaultValue);
        dialog.setTitle(title);
        dialog.setHeaderText(head);
        dialog.setContentText(content);
        dialog.getDialogPane().getChildren().add(new Button("test"));
        dialog.showAndWait().ifPresent(response -> {
            e.exec(response);
            dialog.close();
        });
    }

    public static void showDialog(String title, Image image){
        javafx.scene.control.Dialog<Image> dialog = new javafx.scene.control.Dialog<>();
        dialog.setTitle(title);
        dialog.getDialogPane().setContent(new ImageView(image));
        // 至少要加一个按钮，否则Dialog无法关闭
        dialog.getDialogPane().getButtonTypes().add(new ButtonType("确认", ButtonBar.ButtonData.OK_DONE));
        dialog.show();
    }

    private static void showDatePickerDialog(Main app) throws Exception {
    }
}
