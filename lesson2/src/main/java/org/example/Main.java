package org.example;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.CheckMenuItem;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.RadioMenuItem;
import javafx.scene.control.ToggleGroup;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

/**
 * @Classname Main
 * @Description
 * @Date 2024-6-13 17:23
 * @Created by zoujie
 */
public class Main extends Application {

  public static void main(String[] args) {
    launch(args);
  }

  @Override
  public void start(Stage primaryStage) throws Exception {
    MenuBar menuBar = new MenuBar();

    Menu m1 = new Menu("菜单1");
    MenuItem item1 = new MenuItem("选项1");
    item1.setAccelerator(KeyCombination.valueOf("ctrl+i"));
    item1.setOnAction(new EventHandler<ActionEvent>() {
      @Override
      public void handle(ActionEvent event) {
        System.out.println("item1 on action");
      }
    });

    MenuItem item2 = new MenuItem("选项2");
    MenuItem item3 = new MenuItem("选项3");

    //菜单嵌套
    Menu menu = new Menu("拆分menu");
    menu.getItems().addAll(new MenuItem("内置选项1"),new MenuItem("内置选项2"));

    m1.getItems().addAll(item1,menu);
    Menu m2 = new Menu("菜单2");
    m2.getItems().addAll(item2);
    Menu m3 = new Menu("菜单3");
    m3.getItems().addAll(item3);

    //单选
    ToggleGroup tg = new ToggleGroup();
    RadioMenuItem ritem1 = new RadioMenuItem("ritem1");
    RadioMenuItem ritem2 = new RadioMenuItem("ritem2");
    RadioMenuItem ritem3 = new RadioMenuItem("ritem3");
    ritem1.setSelected(true);
//    ritem1.setToggleGroup(tg);
//    ritem2.setToggleGroup(tg);
//    ritem3.setToggleGroup(tg);
    tg.getToggles().addAll(ritem1,ritem2,ritem3);

    m2.getItems().addAll(ritem1,ritem2,ritem3);


    //多选
    CheckMenuItem citem1 = new CheckMenuItem("选择1");
    CheckMenuItem citem2 = new CheckMenuItem("选择2");
    CheckMenuItem citem3 = new CheckMenuItem("选择3");
    m3.getItems().addAll(citem1,citem2,citem3);


    menuBar.getMenus().addAll(m1,m2,m3);

    AnchorPane.setLeftAnchor(menuBar,0.0);
    AnchorPane.setRightAnchor(menuBar,0.0);



    AnchorPane anchorPane = new AnchorPane();
    Scene scene = new Scene(anchorPane);

    anchorPane.getChildren().add(menuBar);


    primaryStage.setScene(scene);
    primaryStage.setWidth(500);
    primaryStage.setHeight(500);
    primaryStage.show();
  }
}
