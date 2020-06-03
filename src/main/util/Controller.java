package main.util;

import main.Main;

import java.sql.SQLException;

/**
 * 所有控制类的抽象父类
 */
public abstract class Controller {
    protected Main myApp;
    abstract public void setMyApp(Main myApp) throws SQLException, InstantiationException, IllegalAccessException;
}
