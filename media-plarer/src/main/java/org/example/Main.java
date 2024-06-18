package org.example;

import java.util.List;
import javafx.application.Application;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleMapProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;
import javafx.util.Duration;

/**
 * @Classname ${NAME}
 * @Description
 * @Date 2024-6-18 15:46
 * @Created by zoujie
 */
public class Main extends Application {

  private Slider volSlider;
  private Slider progressSlider;

  private int curMusicIndex = 0;

  MediaPlayer player;

  List<Media> musicList;

  private SimpleBooleanProperty isPlaying = new SimpleBooleanProperty(false);

  public static void main(String[] args) {
    launch(args);
  }

  @Override
  public void start(Stage primaryStage) throws Exception {
    showIndex(primaryStage);
    musicList = new MusicLoader().loadMedia();
    initMusicPlayer(musicList);
  }

  private void initMusicPlayer(List<Media> mediaList) {
    player = new MediaPlayer(mediaList.get(curMusicIndex));
    player.setOnReady(() -> {
      player.volumeProperty().bind(volSlider.valueProperty());

      progressSlider.setMin(0);
      progressSlider.setValue(0);
      progressSlider.setMax(player.getTotalDuration().toSeconds());

      player.currentTimeProperty().addListener((observable, oldValue, newValue) -> {
            // 避免滑块正在被用户拖动时的绑定冲突
            if(!progressSlider.isValueChanging()){
              progressSlider.setValue(newValue.toSeconds());
            }
          });

      progressSlider.valueProperty().addListener((observable, oldValue, newValue) -> {
        //手动拖动时才设置media player的值
        if(progressSlider.isValueChanging()){
          player.seek(Duration.seconds(newValue.doubleValue()));
        }
      });

      progressSlider.setOnMousePressed(event -> {
        player.pause();
        isPlaying.set(false);
      });

      progressSlider.setOnMouseReleased(event -> {
        player.seek(Duration.seconds(progressSlider.getValue()));
        player.play();
        isPlaying.set(true);
      });

    });
  }

  private void showIndex(Stage primaryStage){
    AnchorPane root = new AnchorPane();
    Scene scene = new Scene(root);

    Button prevButton = new Button("上一首");
    prevButton.setOnAction(event -> {
      if(player != null){
        player.dispose();
        curMusicIndex = curMusicIndex == 0 ? curMusicIndex : --curMusicIndex % musicList.size();
        initMusicPlayer(musicList);
        player.play();
        isPlaying.set(true);
      }
    });

    Button playOrPauseButton = new Button("播放 ");
    isPlaying.addListener((observable, oldValue, newValue) -> {
      String text = newValue ? "暂停" : "播放";
      playOrPauseButton.setText(text);
    });
    playOrPauseButton.setOnAction(event -> {
      if(isPlaying.get()){
        player.pause();
      }else {
        player.play();
      }
      isPlaying.set(!isPlaying.get());;
    });

    Button nextButton = new Button("下一首");
    nextButton.setOnAction(event -> {
      if(player != null){
        player.dispose();
        curMusicIndex = (++curMusicIndex) % musicList.size();
        initMusicPlayer(musicList);
        player.play();
        isPlaying.set(true);
      }
    });
    Button stopButton = new Button("停止 ");
    stopButton.setOnAction(event -> {
      player.stop();
      isPlaying.set(false);
    });

    volSlider = new Slider(0.0,1.0,0.5);
    Label volLabel = new Label("音量");

    FlowPane buttonsPane = new FlowPane();
    buttonsPane.setPrefHeight(50);
    buttonsPane.setAlignment(Pos.BOTTOM_CENTER);
    buttonsPane.setStyle("-fx-background-color: #e2bb9d");
    buttonsPane.setHgap(10);

    buttonsPane.getChildren().addAll(prevButton,playOrPauseButton,nextButton,stopButton,volLabel,volSlider);
    AnchorPane.setLeftAnchor(buttonsPane,0.0);
    AnchorPane.setRightAnchor(buttonsPane,0.0);
    AnchorPane.setBottomAnchor(buttonsPane,0.0);

    progressSlider = new Slider(0.0,1.0,0.0);
    AnchorPane slidersPane = new AnchorPane();
    slidersPane.setStyle("-fx-background-color: #70deb7");
    slidersPane.getChildren().addAll(progressSlider);
    AnchorPane.setLeftAnchor(progressSlider,0.0);
    AnchorPane.setRightAnchor(progressSlider,0.0);

    FlowPane.setMargin(volSlider,new Insets(0,slidersPane.getPrefWidth(),0,0));
    FlowPane.setMargin(progressSlider,new Insets(0,0,0,slidersPane.getPrefWidth()));

    AnchorPane.setLeftAnchor(slidersPane,0.0);
    AnchorPane.setRightAnchor(slidersPane,0.0);
    AnchorPane.setBottomAnchor(slidersPane,buttonsPane.getPrefHeight());

    root.getChildren().addAll(buttonsPane,slidersPane);

    primaryStage.setScene(scene);
    primaryStage.setTitle("Media Player");
    primaryStage.setWidth(1000);
    primaryStage.setHeight(800);
    primaryStage.show();
  }
}