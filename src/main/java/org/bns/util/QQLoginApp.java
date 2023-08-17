package org.bns.util;
import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.scene.Scene;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;

import javax.swing.*;
import java.awt.*;

public class QQLoginApp extends  JFrame{
    private static final int WIDTH = 800;
    private static final int HEIGHT = 600;
    private JFXPanel jfxPanel;

    static WebView webView;
     public static JFrame jframe;
    static WebEngine webEngine;
    public static boolean isAuth=false;
    public static void getQQLogin() {

        QQLoginApp browserSimulator = new QQLoginApp();
        browserSimulator.init();
    }
    private void init() {
        jframe = new JFrame("浏览器");
        jfxPanel = new JFXPanel();
        jframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jframe.add(jfxPanel, BorderLayout.CENTER);
        jframe.setSize(WIDTH, HEIGHT);
        jframe.setLocationRelativeTo(null);
        jframe.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosed(java.awt.event.WindowEvent windowEvent) {
                System.out.println("子窗口的返回值为：" + isAuth);
            }
        });

        jframe.setVisible(true);

        // 启动 JavaFX
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                 webView = new WebView();
                 webEngine = webView.getEngine();
                webEngine.load("https://i.qq.com/");
                Scene scene = new Scene(webView, WIDTH, HEIGHT);
                jfxPanel.setScene(scene);
            }
        });
    }

    public static String aaa(){
        String url =webEngine.getLocation();
        System.out.println(url);
        try {
            Robot robot=new Robot();
            robot.delay(1000);
            url =webEngine.getLocation();
            System.out.println(url);

            robot.delay(1100);
            url =webEngine.getLocation();
            System.out.println(url);
            return url;
        } catch (AWTException e) {
            throw new RuntimeException(e);
        }
    }

}