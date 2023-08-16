package org.bns;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import org.bns.pojo.PickPOJO;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.io.*;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;


public class BnsFish extends JFrame  implements ActionListener, KeyListener,Runnable {
    public static boolean run=false;
    public static BnsFish mainGui;
    private JFrame frame;
    private JPanel panel;
    private static Timer timer;

    private static PickPOJO quse =new PickPOJO("当前取色");

    public static Map<String, PickPOJO> buttonMap = new HashMap<String, PickPOJO>() {{
        put("paoGan1", new PickPOJO("抛竿1"));
        put("paoGan2", new PickPOJO("抛竿2"));
        put("zhongYu1", new PickPOJO("上钩1"));
        put("zhongYu2", new PickPOJO("上钩2"));
    }};
    Robot robot = new Robot();
    static  JTextArea logTextArea ;
    static JScrollPane scrollPane;
    FileDialog openDia, saveDia;//定义“打开、保存”对话框
    Point mousePoint;
    Color pixel = new Color(0, 0, 0);
    JButton JRun;
    QQLoginApp qqLoginApp;

    public BnsFish() throws AWTException {

        frame = new JFrame("BnsFish");
        Image icon = Toolkit.getDefaultToolkit().getImage(BnsFish.class.getResource("/stat2.png"));
        JFrame.setDefaultLookAndFeelDecorated(true);

//        ImageIcon icon = new ImageIcon("/stat2.ico"); // 替换为实际图标文件的路径
        frame.setSize(450, 350);
        frame.setIconImage(icon);

        // 创建面板
        panel = new JPanel();
        panel.setLayout(new GridBagLayout());


        openDia = new FileDialog(this, "打开", FileDialog.LOAD);
        saveDia = new FileDialog(this, "保存", FileDialog.SAVE);
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.gridx = 0;
        constraints.anchor = GridBagConstraints.WEST;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.insets = new Insets(-10, 5, 30, 5);

        constraints.gridy = 0;
        constraints.gridx = 0;
        panel.add(quse.getjCol(), constraints);
        constraints.gridy = 0;
        constraints.gridx = 1;
        panel.add(quse.getXy(), constraints);
        constraints.gridy = 0;
        constraints.gridx = 2;
        panel.add( new JLabel("当前取色"), constraints);
        quse.getButton().addKeyListener(this);
        quse.getButton().addActionListener(this);


        constraints.insets = new Insets(5, 5, 5, 5);

        int i=1;
        for (String key : buttonMap.keySet()) {
            constraints.gridy = i;
            constraints.gridx = 0;
            panel.add(buttonMap.get(key).getjCol(), constraints);
            constraints.gridy = i;
            constraints.gridx = 1;
            panel.add(buttonMap.get(key).getXy(), constraints);
            constraints.gridy = i;
            constraints.gridx = 2;
            panel.add(buttonMap.get(key).getButton(), constraints);
            buttonMap.get(key).getButton().addKeyListener(this);
            buttonMap.get(key).getButton().addActionListener(this);

            i++;
        }
//        String[] options = {"Option 1", "Option 2", "Option 3"};
//        comboBox = new JComboBox(options);
//        constraints.gridy = i;
//        constraints.gridx = 0;
//        constraints.gridwidth = 2;
//        panel.add(comboBox, constraints);
        logTextArea=new JTextArea(10,27);
        logTextArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        logTextArea.setEditable(false);

        scrollPane = new JScrollPane(logTextArea);

//        scrollPane.setPreferredSize(new Dimension(300, 200)); // 设置宽度和高度
        constraints.gridy = 0;
        constraints.gridx = 4;
        constraints.gridheight=i+1;
        panel.add(scrollPane, constraints);
        logTextArea.addKeyListener(this);
//        logTextArea.addAncestorListener(this);
        scrollPane.addKeyListener(this);

        JRun=new JButton("运行");
        JRun.addActionListener(this);
        JRun.addKeyListener(this);
        constraints.gridy = i+1;
        constraints.gridx = 0;
        constraints.gridwidth=7;
        panel.add(JRun, constraints);

        frame.getContentPane().add(panel);
        setMenuBar();

        // 设置主窗口可见
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        LocalDate currentDate = LocalDate.now();  // 获取当前日期

        LocalDate targetDate = LocalDate.of(2023, 10, 31);  // 目标日期为2023年10月31日
        if (currentDate.isAfter(targetDate)) {
            JOptionPane.showMessageDialog(frame, "已超免费使用期限，如需继续使用请联系-VX：  或者QQ：", "提示", JOptionPane.INFORMATION_MESSAGE);
            BnsUtils.logPrint(logTextArea,"3秒后自动关闭");
            robot.delay(3000);
            System.exit(0);
        } else {
            JOptionPane.showMessageDialog(frame, "免费钓鱼工具，绑定QQ授权使用，2023-10-31后失效，联系方式-VX：  或者QQ：", "提示", JOptionPane.INFORMATION_MESSAGE);
        }
        qqLoginApp=new QQLoginApp();
        qqLoginApp.getQQLogin();

        final Timer timer = new Timer();
        TimerTask task = new TimerTask() {
            public void run() {
                String url=qqLoginApp.aaa();
                if(!url.contains("i.qq.com")){
                    if(url.contains("454299035")) {
                        timer.cancel();
                        qqLoginApp.isAuth=true;
                        System.out.println("当前页面的URL地址：" + url);
                        qqLoginApp.jframe.dispose();
                    }else {
                        timer.cancel();
                        qqLoginApp.isAuth=false;
                        System.out.println("未授权QQ：请联系管理员：VX" + url);
                        System.exit(0);
                    }
                }
            }
        };
        // 启动定时器
        timer.scheduleAtFixedRate(task, 2000,2000);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == JRun) {
            if (JRun.getText().equals("运行")) {
                try {
                    if(!qqLoginApp.isAuth){
                        JOptionPane.showMessageDialog(frame, "未授权", "提示", JOptionPane.INFORMATION_MESSAGE);
                        System.exit(0);
                    }
                    JRun.setText("运行中");
                    run=true;
                    timer = new Timer();
                    TimerTask task = new TimerTask() {
                        public void run() {
                            // 执行定时任务的逻辑
                            mainGui.run();
                        }
                    };
                    // 启动定时器
                    timer.scheduleAtFixedRate(task, 2000,2000);
                    BnsUtils.logPrint(logTextArea,"开始运行");
                } catch (Exception ex) {
                    run=false;
                    robot.delay(2000);
                    BnsUtils.logPrint(logTextArea,"运行错误，已停止");
                    timer.cancel();
                    throw new RuntimeException(ex);
                }
            } else {
                 stopFish();
            }
        }
        if(e.getActionCommand()=="载入"){
            importTxt();

        }else if(e.getActionCommand()=="保存"){
            saveTxt();
        }
        for (String key : buttonMap.keySet()) {
            if (e.getSource() == buttonMap.get(key).getButton()) {
                PickPOJO p = buttonMap.get(key);
                if (quse.getX() == -1) {
                    JOptionPane.showMessageDialog(null, "此功能:" + p.getButton().getText(), "提示", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    p.setX(quse.getX()) ;
                    p.setY(quse.getY())  ;
                    p.setR(quse.getR());
                    p.setG(quse.getG())  ;
                    p.setB( quse.getB()) ;
                    p.getXy().setText("(" + p.getX() + "," + p.getY() + ")");
                    p.getjCol().setForeground(new Color(p.getR(), p.getG(), p.getB()));
                }
            }
        }
    }
    private void stopFish(){
        run=false;
        timer.cancel();
        robot.delay(2000);

        BnsUtils.logPrint(logTextArea,"已停止运行");
        JRun.setText("运行");
        JOptionPane.showMessageDialog(null, "已停止运行", "提示", JOptionPane.INFORMATION_MESSAGE);
    }
    private void saveTxt() {
        saveDia.setVisible(true);
        String dirpath = saveDia.getDirectory();
        String fileName = saveDia.getFile() ;

        if (dirpath == null || fileName == null) {
            return;
        }
        File file = new File(dirpath, fileName);
        try {
            BufferedWriter bufw = new BufferedWriter(new FileWriter(file));
            JSONObject aa = new JSONObject();
            for (String key : buttonMap.keySet()) {
                aa.put(key, buttonMap.get(key).getX() + "," + buttonMap.get(key).getY() + "," + buttonMap.get(key).getR() + "," + buttonMap.get(key).getG() + "," + buttonMap.get(key).getB());
            }
            String text = JSONObject.toJSONString(aa);

            BnsUtils.logPrint(logTextArea,"保存成功" );
            bufw.write(text);
            bufw.close();
        } catch (Exception ex) {
            BnsUtils.logPrint(logTextArea,"报错：" + ex.getMessage());
            ex.printStackTrace();
            ex.getMessage();
        }
    }

    private void importTxt() {
        openDia.setVisible(true);
        String dirpath = openDia.getDirectory();
        String fileName = openDia.getFile();

        if (dirpath == null || fileName == null) {
            return;
        }
        File file = new File(dirpath, fileName);
        try {
            BufferedReader bufr = new BufferedReader(new FileReader(file));
            String line = "";
            String s2 = "";
            while ((line = bufr.readLine()) != null) {
                s2 += line;
            }
            JSONObject jsonObject = JSON.parseObject(s2);
            for (String key : buttonMap.keySet()) {
                if(jsonObject.get(key)!=null){
                    String str = jsonObject.get(key).toString();
                    String[] sites = str.split(",");
                    if (sites[0] != null && !sites[0].equals("null")) {
                        buttonMap.get(key).setX(Integer.valueOf(sites[0]));
                        buttonMap.get(key).setY(Integer.valueOf(sites[1]));
                        buttonMap.get(key).setR(Integer.valueOf(sites[2]));
                        buttonMap.get(key).setG(Integer.valueOf(sites[3]));
                        buttonMap.get(key).setB(Integer.valueOf(sites[4]));
                        buttonMap.get(key).getXy().setText("(" + sites[0] + "," + sites[1] + ")");
                        buttonMap.get(key).getjCol().setForeground(new Color(buttonMap.get(key).getR(), buttonMap.get(key).getG(), buttonMap.get(key).getB()));
                    }
                }

            }
            BnsUtils.logPrint(logTextArea,"导入成功");
            bufr.close();
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void run() {
            BnsUtils.logPrint(logTextArea,"钓鱼中");
            robot.delay(100);
            int fishKey=KeyEvent.VK_7;
            while(!comparePoint(buttonMap.get("paoGan1"))
                    &&!comparePoint(buttonMap.get("paoGan2"))
                    &&!comparePoint(buttonMap.get("zhongYu1"))
                    &&!comparePoint(buttonMap.get("zhongYu2"))){
                BnsUtils.logPrint(logTextArea,"使用鱼饵7或8");
                robot.keyPress(fishKey);
                robot.delay(500);
                robot.keyRelease(fishKey);
                robot.delay(500);
                if (fishKey==KeyEvent.VK_8){
                    stopFish();
                    BnsUtils.logPrint(logTextArea,"请检查物品栏7和8是否有鱼饵");
                    return;
                }
                fishKey=KeyEvent.VK_8;
            }
           robot.delay(888);
            while ((comparePoint(buttonMap.get("paoGan1"))&&comparePoint(buttonMap.get("paoGan2")))
                    ||(comparePoint(buttonMap.get("zhongYu1"))&&comparePoint(buttonMap.get("zhongYu2")))) {
                BnsUtils.logPrint(logTextArea,"垂钓中");
                robot.delay(100);
                if(comparePoint(buttonMap.get("zhongYu1"))&&comparePoint(buttonMap.get("zhongYu2"))){
                    BnsUtils.logPrint(logTextArea,"上钩了");
                    robot.delay(500);
                    robot.keyPress(KeyEvent.VK_F);
                    robot.delay(500);
                    robot.keyRelease(KeyEvent.VK_F);
                }
            }
    }

    private boolean comparePoint(PickPOJO pojo) {

        pixel = robot.getPixelColor(pojo.getX(), pojo.getY());
        //模糊匹配颜色  rgb差值小于100
        Integer diffR = BnsUtils.diff(pojo.getR(), pixel.getRed());
        Integer diffG = BnsUtils.diff(pojo.getG(), pixel.getGreen());
        Integer diffB = BnsUtils.diff(pojo.getB(), pixel.getBlue());
        if ((diffR + diffG + diffB) < 100&&run) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        if (e.getKeyCode() == 18) {
            try {
                mousePoint = MouseInfo.getPointerInfo().getLocation();
                pixel = robot.getPixelColor(mousePoint.x, mousePoint.y);
                quse.setX ( mousePoint.x);
                quse.setY ( mousePoint.y);
                quse.setR ( pixel.getRed());
                quse.setG ( pixel.getGreen());
                quse.setB (pixel.getBlue());
                quse.getXy().setText("(" + quse.getX() + "," + quse.getY() + ")");
                quse.getjCol().setForeground(new Color(quse.getR(), quse.getG(), quse.getB()));
            } catch (Exception ext) {
                ext.printStackTrace();
            }
        }
        if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
           timer.cancel();
            JRun.setText("运行");
            JOptionPane.showMessageDialog(null, "已停止运行", "提示", JOptionPane.INFORMATION_MESSAGE);
        }

    }

    public static void main(String[] args) throws AWTException {
         mainGui = new BnsFish();
    }
    public void setMenuBar()

    {

        JMenuBar myBar=new JMenuBar();

        JMenu helpMenu=new JMenu("帮助");

        JMenuItem help_About=new JMenuItem("载入");
        JMenuItem save=new JMenuItem("保存");


        myBar.add(helpMenu);

        helpMenu.add(help_About);
        helpMenu.add(save);
        save.addActionListener(this);
        help_About.addActionListener(this);
        frame.setJMenuBar(myBar);


    }
    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
    }

}