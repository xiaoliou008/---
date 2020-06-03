package main.util;

import javafx.collections.FXCollections;
import javafx.scene.control.ChoiceBox;
import javafx.util.StringConverter;

import java.util.List;

/**
 * 配置各种控件
 */
public class Configure {
    /**
     * 配置choiceBox控件
     * @param choiceBox
     * @param list
     * @param <E>
     * @throws IllegalAccessException
     * @throws InstantiationException
     */
    public static <E extends NameAccessible> void configChoiceBox(ChoiceBox<E> choiceBox, List<E> list)
            throws IllegalAccessException, InstantiationException {
        choiceBox.setConverter(new StringConverter<E>() {
            @Override
            public String toString(NameAccessible nameAccessible) {
                return nameAccessible.isAll() ? "全部" : nameAccessible.getName();
            }

            @Override
            public E fromString(String s) {
                return null;
            }
        });
        // 配置内容
        choiceBox.setItems(FXCollections.observableList(list));
        Class<E> cla = (Class<E>) list.get(0).getClass();
        choiceBox.getItems().add(cla.newInstance());

        choiceBox.getSelectionModel().selectLast();     // 默认选择最后的“全部”
    }
}
