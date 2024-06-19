package org.example;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;

/**
 * @Classname ${NAME}
 * @Description
 * @Date 2024-6-18 14:48
 * @Created by zoujie
 */
public class Main extends Application {

  private boolean isPlaying = false;

  private boolean isMute = false;

  public static void main(String[] args) {
    launch(args);
  }

  @Override
  public void start(Stage primaryStage) throws Exception {
    AnchorPane root = new AnchorPane();
    Scene scene = new Scene(root);

    String netMedia = "http://music.163.com/song/media/outer/url?id=2074987490.mp3";
    Media media = new Media(netMedia);
    MediaPlayer player = new MediaPlayer(media);

    Button play = new Button("播放");
    play.setOnMouseClicked(event -> {
      if(isPlaying){
        player.pause();
        play.setText("播放");
      }else {
        player.play();
        play.setText("暂停");
      }
      isPlaying = !isPlaying;
    });
    Button stop = new Button("停止");
    stop.setOnMouseClicked(event -> {
          player.stop();
          play.setText("播放");
          isPlaying = false;
        });
    Button mute = new Button("静音");
    mute.setOnAction(event -> {
      if(isPlaying){
        isMute = !isMute;
        player.setMute(isMute);
      }
    });

    HBox box = new HBox();
    box.setPrefHeight(20);
    box.setAlignment(Pos.CENTER);
    box.getChildren().addAll(play,stop,mute);
    AnchorPane.setTopAnchor(box,0.0);
    AnchorPane.setLeftAnchor(box,0.0);
    AnchorPane.setRightAnchor(box,0.0);

    root.getChildren().add(box);

    primaryStage.setScene(scene);
    primaryStage.setWidth(500);
    primaryStage.setHeight(500);
    primaryStage.show();
  }
}