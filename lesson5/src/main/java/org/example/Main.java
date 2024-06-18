package org.example;

import javafx.application.Application;
import javafx.geometry.NodeOrientation;
import javafx.geometry.Pos;
import javafx.geometry.Side;
import javafx.scene.Scene;
import javafx.scene.control.Accordion;
import javafx.scene.control.Button;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TitledPane;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * @Classname ${NAME}
 * @Description
 * @Date 2024-6-18 10:01
 * @Created by zoujie
 */
public class Main extends Application {

  public static void main(String[] args) {
    launch(args);
  }

  @Override
  public void start(Stage primaryStage) throws Exception {
    AnchorPane root = new AnchorPane();
    Scene scene = new Scene(root);

    Tab t1 = new Tab("tab1");
    VBox t1b = new VBox();
    t1b.setPrefHeight(200);
    t1b.setPrefWidth(200);
    t1b.setStyle("-fx-background-color: #3dd563");
    t1b.setAlignment(Pos.CENTER);
    t1b.getChildren().addAll(new Button("t1b1"),new Button("t1b2"));
    t1.setContent(t1b);

    Tab t2 = new Tab("tab2");
    HBox t2b = new HBox();
    t2b.setPrefHeight(200);
    t2b.setPrefWidth(200);
    t2b.setStyle("-fx-background-color: #abc45e");
    t2b.setAlignment(Pos.CENTER);
    t2b.getChildren().addAll(new Button("t2b1"),new Button("t2b2"));
    t2.setContent(t2b);

    Tab t3 = new Tab("tab3");
    VBox t3b = new VBox();
    t3b.setPrefHeight(200);
    t3b.setPrefWidth(200);
    t3b.setStyle("-fx-background-color: #cb9769");
    t3b.setAlignment(Pos.CENTER);
    t3b.getChildren().addAll(new Button("t3b1"),new Button("t3b2"));
    t3.setContent(t3b);

    //设置图标
    ImageView imageView = new ImageView("xxx");
    imageView.setFitHeight(25);
    imageView.setFitWidth(25);

    t3.setGraphic(imageView);

    //设置是否可以关闭
    t3.setClosable(false);

    TabPane tp = new TabPane();

    //列表在右侧竖着时，图标不会跟着旋转
    //注意该设置必须在show方法前，否则不会生效
    tp.setRotateGraphic(false);

    tp.getTabs().addAll(t1,t2,t3);

    //设置朝向
//    tp.setSide(Side.RIGHT);
    root.getChildren().addAll(tp);

    primaryStage.setScene(scene);
    primaryStage.setTitle("fx");
    primaryStage.setWidth(800);
    primaryStage.setHeight(500);
    primaryStage.show();

  }
}