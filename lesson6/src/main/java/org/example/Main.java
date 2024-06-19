package org.example;

import java.net.URL;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Slider;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.media.AudioClip;
import javafx.stage.Stage;

/**
 * @Classname ${NAME}
 * @Description
 * @Date 2024-6-18 11:06
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

    URL resource = this.getClass().getClassLoader().getResource("audio/rainy-day.mp3");
    AudioClip clip = new AudioClip(resource.toExternalForm());

    Button play = new Button("播放");
    play.setOnMouseClicked(event -> clip.play());
    Button stop = new Button("停止");
    stop.setOnMouseClicked(event -> clip.stop());

    Slider slider = new Slider(0,1,0.5);
    slider.valueProperty().addListener(
        (observable, oldValue, newValue) -> {
          clip.setVolume(newValue.doubleValue());
          if(clip.isPlaying()){
            clip.play();
          }
        });

    slider.setOnMouseDragOver(event -> {
      System.out.println("setOnMouseDragOver");
      Slider source = (Slider)event.getSource();
      clip.setVolume(source.getValue());
      if(clip.isPlaying()){
        clip.play();
      }
    });

    slider.setOnMouseClicked(event -> {
      Slider source = (Slider)event.getSource();
      clip.setVolume(source.getValue());
      if(clip.isPlaying()){
        clip.stop();
        clip.play();
      }
    });


    HBox box = new HBox();
    box.setPrefHeight(20);
    box.setAlignment(Pos.CENTER);
    box.getChildren().addAll(play,stop);
    AnchorPane.setTopAnchor(box,0.0);
    AnchorPane.setLeftAnchor(box,0.0);
    AnchorPane.setRightAnchor(box,0.0);

    HBox sbox = new HBox();
    sbox.getChildren().addAll(slider);
    sbox.setAlignment(Pos.CENTER);
    AnchorPane.setTopAnchor(sbox,box.getPrefHeight() + 20);
    AnchorPane.setLeftAnchor(sbox,0.0);
    AnchorPane.setRightAnchor(sbox,0.0);

    root.getChildren().addAll(box,sbox);

    primaryStage.setScene(scene);
    primaryStage.setHeight(500);
    primaryStage.setWidth(500);
    primaryStage.show();
  }
}