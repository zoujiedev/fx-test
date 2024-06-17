package org.example;

import java.util.Objects;
import javafx.animation.FadeTransition;
import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Dialog;
import javafx.scene.control.DialogEvent;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

/**
 * @Classname org.example.Main
 * @Description
 * @Date 2024-6-12 16:11
 * @Created by zoujie
 */
public class Main extends Application {

  public static void main(String[] args) {
    launch(args);
  }

  @Override
  public void start(Stage primaryStage) throws Exception {

    GridPane gr = new GridPane();
    gr.setStyle("-fx-background-color: #aed4ce");

    Label user = new Label("用户名:");
    user.setFont(Font.font(15));
    Label pass = new Label("密码:");
    pass.setFont(Font.font(15));

    TextField accountInput = new TextField();
    accountInput.setUserData("hello");
    PasswordField passwordInput = new PasswordField();
    passwordInput.setUserData("123456");

    Button login = new Button("登录");
    Button clear = new Button("清除");

    gr.add(user,0,0);
    gr.add(accountInput,1,0);
    gr.add(pass,0,1);
    gr.add(passwordInput,1,1);
    gr.add(clear,0,2);
    gr.add(login,1,2);

    gr.setHgap(5);
    gr.setVgap(15);
    gr.setAlignment(Pos.CENTER);
    GridPane.setMargin(login,new Insets(0,0,0,120));

    clear.setOnMouseClicked(new EventHandler<MouseEvent>() {
      @Override
      public void handle(MouseEvent event) {
        accountInput.clear();
        passwordInput.clear();
      }
    });

    login.setOnMouseClicked(new EventHandler<MouseEvent>() {
      @Override
      public void handle(MouseEvent event) {
        String accountInputText = accountInput.getText();
        String passwordInputText = passwordInput.getText();

        String savedAccount = (String)accountInput.getUserData();
        String savedPasswd = (String)passwordInput.getUserData();
        if(Objects.equals(accountInputText,savedAccount) && Objects.equals(passwordInputText,savedPasswd)){
          BorderPane pane = new BorderPane();
          pane.setTop(new Text("欢迎：" + savedAccount));
          Scene scene = new Scene(pane);

          Stage stage = new Stage();
          stage.setScene(scene);
          stage.setTitle("主页");
          stage.setHeight(800);
          stage.setWidth(800);
          stage.show();
          primaryStage.close();
          System.out.println("登录成功");
        }else {
          FadeTransition fadeTransition = new FadeTransition();
          fadeTransition.setDuration(Duration.seconds(0.2));
          fadeTransition.setNode(gr);
          fadeTransition.setFromValue(0);
          fadeTransition.setToValue(1);
          fadeTransition.play();

          user.setTextFill(Color.RED);
          pass.setTextFill(Color.RED);
          System.out.println("登录失败");
        }

      }
    });

    Scene scene = new Scene(gr);
    primaryStage.setScene(scene);
    primaryStage.setWidth(500);
    primaryStage.setHeight(500);
    primaryStage.setTitle("登录");
    primaryStage.show();
  }

}
