package org.example.stages;

import java.awt.AWTException;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.List;
import javafx.application.Platform;
import javafx.embed.swing.SwingFXUtils;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.WritableImage;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javax.imageio.ImageIO;

/**
 * @Classname ScreenSnapshutSelectStage
 * @Description
 * @Date 2024-6-17 11:01
 * @Created by zoujie
 */
public class ScreenSnapshotSelectStage implements StageView{

  private Stage stage;

  private double startX;
  private double startY;
  private double endX;
  private double endY;

  private HBox selectedRec;

  private Label selectedRecInfo;

  public ScreenSnapshotSelectStage(){
    this.stage = new Stage();
    this.stage.initStyle(StageStyle.TRANSPARENT);
  }

  public void show(Stage parent){
    AnchorPane root = new AnchorPane();

    root.setStyle("-fx-background-color: #b5b5b5");
    //设置布局文件透明
    root.setOpacity(0.2);

    drag(root);

    Scene scene = new Scene(root);
    //设置场景图透明
    scene.setFill(Paint.valueOf("#ffffff00"));

    //esc 关闭窗口
    scene.setOnKeyReleased(event -> {
      KeyCode code = event.getCode();
      if(KeyCode.ESCAPE.equals(code)){
         stage.close();
      }
    });


    //设置窗体透明
    stage.setFullScreen(true);
    stage.setFullScreenExitHint("");
    stage.setScene(scene);
    stage.show();
  }

  private void drag(AnchorPane pane){
    pane.setOnMousePressed(event -> {
      pane.getChildren().clear();

      ScreenSnapshotSelectStage.this.startX = event.getScreenX();
      ScreenSnapshotSelectStage.this.startY = event.getScreenY();

      selectedRec = new HBox();
      selectedRec.setBorder(
          new Border(
              new BorderStroke(Paint.valueOf("#CD3700"),
              BorderStrokeStyle.SOLID,
              null,
              new BorderWidths(2))));

      selectedRecInfo = new Label();
      selectedRecInfo.setPrefHeight(30);
      selectedRecInfo.setPrefWidth(170);
      selectedRecInfo.setAlignment(Pos.CENTER);
      selectedRecInfo.setStyle("-fx-background-color: black");
      selectedRecInfo.setTextFill(Color.WHITE);
      selectedRecInfo.setVisible(false);

      double recInfoFromTop = startY == 0 ? startY : startY - selectedRecInfo.getPrefHeight();
      double recInfoFromLeft = startX;

      AnchorPane.setTopAnchor(selectedRecInfo,recInfoFromTop);
      AnchorPane.setLeftAnchor(selectedRecInfo,recInfoFromLeft);

      AnchorPane.setLeftAnchor(selectedRec,startX);
      AnchorPane.setTopAnchor(selectedRec,startY);
      pane.getChildren().addAll(selectedRec,selectedRecInfo);
    });

    pane.setOnMouseDragged(event -> {
      endX = event.getScreenX();
      endY = event.getScreenY();

      double width = endX - startX;
      double height = endY - startY;

      Platform.runLater(() -> {
        selectedRec.setPrefWidth(width);
        selectedRec.setPrefHeight(height);


        selectedRecInfo.setText("宽度：" + width + " 高度：" + height);
        selectedRecInfo.setVisible(true);
      });
    });


    pane.setOnMouseReleased(event -> {
      Button finishBu = new Button("完成");
      finishBu.setPrefHeight(30);
      finishBu.setPrefWidth(60);
      finishBu.setOnMouseClicked(mouseEven -> {
        try {
          //关闭窗口
          stage.close();

          BufferedImage screenCapture = snapshotScreen();

          //放进剪切板
          ClipboardContent clipboardContent = new ClipboardContent();
          WritableImage fxImage = SwingFXUtils.toFXImage(screenCapture, null);
          clipboardContent.putImage(fxImage);
          Clipboard.getSystemClipboard().setContent(clipboardContent);
        } catch (AWTException e) {
          e.printStackTrace();
        }
      });

      Button saveBu = new Button("保存");
      saveBu.setPrefHeight(30);
      saveBu.setPrefWidth(60);
      saveBu.setOnMouseClicked(saveEvent -> {
        stage.close();
        try {
          BufferedImage bufferedImage = snapshotScreen();

          Stage chooseStage = new Stage();
          FileChooser chooser = new FileChooser();
          chooser.setTitle("选择文件");
          chooser.getExtensionFilters().addAll(new ExtensionFilter("图片格式",".jpg"),new ExtensionFilter("图片格式",".png"));
          chooser.setInitialFileName("screen_snapshot_" + System.currentTimeMillis());
          chooser.setInitialDirectory(new File("E://tmp"));

          File file = chooser.showSaveDialog(chooseStage);
          if(file == null){
            return;
          }

          ExtensionFilter selectedExtensionFilter = chooser.getSelectedExtensionFilter();
          List<String> extensions = selectedExtensionFilter.getExtensions();
          String format = extensions.get(0).replace(".","");

          file.createNewFile();

          ImageIO.write(bufferedImage, format, file);
        }catch (Exception e){
          e.printStackTrace();
        }
      });

      AnchorPane.setTopAnchor(finishBu,endY - finishBu.getPrefHeight() - 1);
      AnchorPane.setLeftAnchor(finishBu,endX - finishBu.getPrefWidth() - 1);

      AnchorPane.setTopAnchor(saveBu,endY - saveBu.getPrefHeight() - 1);
      AnchorPane.setLeftAnchor(saveBu,endX - finishBu.getPrefWidth() - saveBu.getPrefWidth() - 1);

      pane.getChildren().addAll(finishBu,saveBu);
    });
  }

  private BufferedImage snapshotScreen() throws AWTException {
    //截图
    int width = (int) (endX - startX);
    int height = (int) (endY - startY);
    Rectangle rectangle = new Rectangle((int) startX,(int) startY,width,height);
    return new Robot().createScreenCapture(rectangle);
  }
}
