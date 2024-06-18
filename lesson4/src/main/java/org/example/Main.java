package org.example;

import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.NodeOrientation;
import javafx.scene.Scene;
import javafx.scene.control.Accordion;
import javafx.scene.control.Button;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

/**
 * @Classname ${NAME}
 * @Description
 * @Date 2024-6-18 9:35
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


    TitledPane tp1 = new TitledPane("TitledPane1",new Button("button1"));
    tp1.setAnimated(false);
//    tp1.setCollapsible(false);
    tp1.setExpanded(false);

    TitledPane tp2 = new TitledPane("TitledPane2",new Button("button2"));
    tp2.setExpanded(false);
    tp2.setNodeOrientation(NodeOrientation.RIGHT_TO_LEFT);
    tp2.setGraphic(new Button("sss"));

    //titledPane 放到这个组件中，则每次只能展开一个
    Accordion accordion = new Accordion();
    accordion.getPanes().addAll(tp1,tp2);
    accordion.expandedPaneProperty().addListener((observable, oldValue, newValue) -> {
      //隐藏时，则该值为null
      if(newValue == null){
        System.out.println(oldValue.getText() + "收起");
      }else {
        System.out.println(newValue.getText() + "展开");
      }
    });

    HBox hBox = new HBox();
    hBox.setPrefHeight(30);
    hBox.setStyle("-fx-background-color: blue");

    AnchorPane.setLeftAnchor(hBox,0.0);
    AnchorPane.setRightAnchor(hBox,0.0);
    AnchorPane.setTopAnchor(hBox,0.0);


    hBox.getChildren().addAll(accordion);


    root.getChildren().addAll(hBox);


    primaryStage.setScene(scene);
    primaryStage.setTitle("fx");
    primaryStage.setWidth(800);
    primaryStage.setHeight(500);
    primaryStage.show();

  }
}