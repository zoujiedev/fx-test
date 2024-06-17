package org.example;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CustomMenuItem;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Screen;
import javafx.stage.Stage;

/**
 * @Classname ${NAME}
 * @Description
 * @Date 2024-6-14 10:37
 * @Created by zoujie
 */
public class Main extends Application {

  public static void main(String[] args) {
    launch(args);
  }

  @Override
  public void start(Stage primaryStage) throws Exception {
    AnchorPane pane = new AnchorPane();

    MenuBar bar = new MenuBar();
    Menu m1 = new Menu("菜单1");
    Menu m2 = new Menu("菜单2");
    Menu m3 = new Menu("菜单3");
    Menu m4 = new Menu("菜单4");
    Menu m5 = new Menu("菜单5");
    Menu m6 = new Menu("菜单6");
    bar.getMenus().addAll(m1,m2,m3,m4,m5,m6);

    AnchorPane.setRightAnchor(bar,0.0);
    AnchorPane.setLeftAnchor(bar,0.0);

    //自定义menu
    CustomMenuItem cmit = new CustomMenuItem();
    Button bu1 = new Button("按钮1");
    cmit.setContent(bu1);
    m1.getItems().add(cmit);


    MenuButton mb = new MenuButton("mb");
    AnchorPane.setTopAnchor(mb,25.0);
    AnchorPane.setLeftAnchor(mb,5.0);

    MenuItem item1 = new MenuItem("item1");
    MenuItem item2 = new MenuItem("item2");
    mb.getItems().addAll(item1,item2);


    pane.getChildren().addAll(bar,mb);
    Scene scene = new Scene(pane);
    primaryStage.setScene(scene);
    primaryStage.setWidth(500);
    primaryStage.setHeight(500);
    primaryStage.show();
  }
}