package org.example;

import java.awt.AWTException;
import java.awt.Image;
import java.awt.Menu;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.Toolkit;
import java.awt.TrayIcon;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.net.URL;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.example.cons.MouseCons;
import org.example.stages.ScreenSnapshotSelectStage;

/**
 * @Classname ${NAME}
 * @Description
 * @Date 2024-6-17 9:53
 * @Created by zoujie
 */
public class Main extends Application {

  private volatile TrayIcon trayIcon;

  private Stage primaryStage;

  private ScreenSnapshotSelectStage screenSnapshotSelectStage;

  public static void main(String[] args) {
    Application.launch(args);
  }


  @Override
  public void start(Stage primaryStage) throws Exception {
    screenSnapshotSelectStage = new ScreenSnapshotSelectStage();

    initTrap();
    this.primaryStage = primaryStage;
    primaryStage.setIconified(true);
    primaryStage.setWidth(0);
    primaryStage.setHeight(0);
    primaryStage.setOpacity(0);
    primaryStage.show();
    //todo 这里主窗口不show的话，后续关闭截图stage，后就无法点击再显示截图窗口，应该是程序判断所有stage都退出了，则不会执行platform.runLater
  }

  private void initTrap() throws AWTException {
    SystemTray tray = SystemTray.getSystemTray();

    URL resource = this.getClass().getClassLoader().getResource("icon/icon.png");
    Image image = Toolkit.getDefaultToolkit().createImage(resource);

    String tip = "";
    MenuItem item = new MenuItem("exit");
    item.addActionListener(e -> {
      tray.remove(trayIcon);
      Platform.exit();
    });

    PopupMenu menu = new PopupMenu("menu");
    menu.add(item);

    this.trayIcon = new TrayIcon(image,tip,menu);

    trayIcon.addMouseListener(new MouseAdapter() {
      @Override
      public void mouseClicked(MouseEvent e) {
        if(MouseCons.MOUSE_BUTTON_PRIMARY == e.getButton()){
          Platform.runLater(() -> {
            try {
              screenSnapshotSelectStage.show(primaryStage);
            }catch (Exception exception){
              exception.printStackTrace();
            }
          });
        }
      }
    });
    tray.add(trayIcon);
  }
}