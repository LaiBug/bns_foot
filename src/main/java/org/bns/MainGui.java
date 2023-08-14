package org.bns;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.bns.pojo.PickPOJO;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class MainGui extends JFrame  implements ActionListener, KeyListener,Runnable {
    public static boolean run = false;
    private JFrame frame;
    private JPanel panel;
    private JComboBox<String> comboBox;
    private BufferedImage oldShot;

    public static Map<String, PickPOJO> buttonMap = new HashMap<String, PickPOJO>() {{
        put("quSe", new PickPOJO("当前取色"));
        put("xueYin", new PickPOJO("血隐"));
        put("xueXian", new PickPOJO("血现"));
        put("shiQu", new PickPOJO("拾取"));
        put("qieXian", new PickPOJO("切线"));
        put("jiantou", new PickPOJO("箭头"));
    }};
    Robot robot = new Robot();
    FileDialog openDia, saveDia;//定义“打开、保存”对话框
    Point mousePoint;
    Color pixel = new Color(0, 0, 0);
    JButton JRun;
    Thread bb;

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == JRun) {
            if (!run) {
                JRun.setText("运行中");
                try {
                    Thread.sleep(1000);
                    MainGui aa = new MainGui();
                    bb = new Thread(aa);
                    bb.start();
                    run = true;
                } catch (AWTException ex) {
                    throw new RuntimeException(ex);
                } catch (InterruptedException ex) {
                    throw new RuntimeException(ex);
                }

            } else {
                run = false;
                bb.stop();
                JRun.setText("运行");
                JOptionPane.showMessageDialog(null, "已停止运行", "提示", JOptionPane.INFORMATION_MESSAGE);
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
                if (buttonMap.get("quSe").getX() == -1) {
                    JOptionPane.showMessageDialog(null, "此功能:" + p.getButton().getText(), "提示", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    p.setX(buttonMap.get("quSe").getX()) ;
                    p.setY(buttonMap.get("quSe").getY())  ;
                    p.setR(buttonMap.get("quSe").getR());
                    p.setG(buttonMap.get("quSe").getG())  ;
                    p.setB( buttonMap.get("quSe").getB()) ;
                    p.getXy().setText("(" + p.getX() + "," + p.getY() + ")");
                    p.getjCol().setForeground(new Color(p.getR(), p.getG(), p.getB()));
                }
            }
        }
    }

    private void saveTxt() {
        saveDia.setVisible(true);
        String dirpath = saveDia.getDirectory();
        String fileName = saveDia.getFile() + ".txt";

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

            System.out.println("内容：" + text);
            bufw.write(text);
            bufw.close();
        } catch (Exception ex) {
            System.out.println("错：" + ex.getMessage());
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
                System.out.println(line);
                s2 += line;
            }
            JSONObject jsonObject = JSON.parseObject(s2);
            for (String key : buttonMap.keySet()) {
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
            bufr.close();
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void run() {
        while (run) {
            System.out.println("运行中");
            robot.delay(100);
            if (comparePoint(buttonMap.get("xueYin"))) {
                //龙出了 开始转圈圈直到锁定龙
                System.out.println("龙出了 开始转圈圈直到锁定龙--测试是否一直按着左键");
                boolean isLockLong = false;
                while (!isLockLong) {
                    robot.keyPress(KeyEvent.VK_RIGHT);
                    if (comparePoint(buttonMap.get("xueXian"))) {
                        System.out.println("锁定龙了 松开转圈键");
                        robot.keyRelease(KeyEvent.VK_RIGHT);//判断锁定龙了 松开转圈键
                        System.out.println("开始攻击");
                        attack();
                        pick();
                        qieXian();
                        break;
                    }
                    robot.keyRelease(KeyEvent.VK_RIGHT);
                }
            }
        }
    }
    private static BufferedImage captureRegion(int x, int y, int width, int height) throws AWTException {
        Robot robot = new Robot();
        Rectangle region = new Rectangle(x, y, width, height);
        return robot.createScreenCapture(region);
    }

    private void qieXian() {
        robot.keyPress(KeyEvent.VK_ALT);
        robot.delay(1150);
        robot.mouseMove(buttonMap.get("qieXian").getX(), buttonMap.get("qieXian").getY());
        robot.delay(150);
        robot.keyPress(KeyEvent.BUTTON1_MASK);
        robot.delay(150);
        robot.keyRelease(KeyEvent.BUTTON1_MASK);
        robot.delay(150);
        System.out.println("切2线");
        robot.mouseMove(buttonMap.get("qieXian").getX(), buttonMap.get("qieXian").getY() + 15);
        robot.delay(150);
        robot.keyPress(KeyEvent.BUTTON1_MASK);
        robot.delay(50);
        robot.keyRelease(KeyEvent.BUTTON1_MASK);
        robot.delay(150);
        robot.keyRelease(KeyEvent.VK_ALT);
        robot.delay(150);
        robot.keyPress(KeyEvent.VK_Y);
        robot.delay(50);
        robot.keyRelease(KeyEvent.VK_Y);
        robot.delay(150);
    }

    private void pick() {
        Rectangle area = new Rectangle(buttonMap.get("jiantou").getX(), buttonMap.get("jiantou").getY(), 20, 20); // 指定区域的坐标和大小
        BufferedImage oldshot = robot.createScreenCapture(area);
        System.out.println("保存击杀后箭头方向");

        int isPick = 0;
        while (isPick < 10) {
            System.out.println("开始寻找箱子");
            robot.delay(1000);
            if (comparePoint(buttonMap.get("shiQu"))) {
                System.out.println("找到箱子了，捡起来");
                executeKeyAndTime(KeyEvent.VK_F, 500);//拾取
                executeKeyAndTime(KeyEvent.VK_F, 500);//拾取
            }
            System.out.println("不在脸上，开始第"+(isPick+1)+"次左转圈寻找");
            executeKeyAndTime(KeyEvent.VK_F, 1000);
            robot.delay(2000);
            System.out.println("获取当前箭头方向");
            BufferedImage newshot= robot.createScreenCapture(area);
            while (BnsUtils.calculateSimilarity(newshot, oldshot)<0.9) {
                if (comparePoint(buttonMap.get("shiQu"))) {
                    System.out.println("找到箱子了，捡起来");
                    executeKeyAndTime(KeyEvent.VK_F, 500);//拾取
                    executeKeyAndTime(KeyEvent.VK_F, 500);//拾取
                    System.out.println("修正方向");

                    while (BnsUtils.calculateSimilarity(newshot, oldshot)<0.9) {
                        System.out.println("当前箭头方向和击杀后箭头方向不匹配，继续修正方向");
                        executeKeyAndTime(KeyEvent.VK_F, 1000);
                        robot.delay(1000);
                        newshot= robot.createScreenCapture(area);
                    }
                    System.out.println("修正方向完成");
                    return;
                }
                System.out.println("第"+(isPick+1)+"次转圈继续按左转键");
                executeKeyAndTime(KeyEvent.VK_F, 1000);
                robot.delay(2000);
            }
            System.out.println("第"+(isPick+1)+"次转圈继续按左转键");
            executeKeyAndTime(KeyEvent.VK_W, 2000);
            isPick++;
            System.out.println("第"+(isPick)+"次转圈找不到箱子前进" + isPick + "点");
        }
    }

    private void attack() {
        while (comparePoint(buttonMap.get("xueXian"))) {
            System.out.println("攻击中");
            executeKeyAndTime(KeyEvent.VK_1, 500);
            executeKeyAndTime(KeyEvent.VK_G, 500);
            executeKeyAndTime(KeyEvent.VK_R, 500);
            executeKeyAndTime(KeyEvent.VK_T, 500);
        }
        System.out.println("龙G了，停止攻击");
    }

    private void executeKeyAndTime(int key, int i) {
        robot.keyPress(key);
        if (i != 0) {
            robot.delay(i);
        }
        robot.keyRelease(key);
    }

    private boolean comparePoint(PickPOJO pojo) {

        pixel = robot.getPixelColor(pojo.getX(), pojo.getY());
        //模糊匹配颜色  rgb差值小于100
        Integer diffR = BnsUtils.diff(pojo.getR(), pixel.getRed());
        Integer diffG = BnsUtils.diff(pojo.getG(), pixel.getGreen());
        Integer diffB = BnsUtils.diff(pojo.getB(), pixel.getBlue());
        if ((diffR + diffG + diffB) < 100) {
            System.out.println("匹配到：" + pojo.getButton().getText());
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
                buttonMap.get("quSe").setX ( mousePoint.x);
                buttonMap.get("quSe").setY ( mousePoint.y);
                buttonMap.get("quSe").setR ( pixel.getRed());
                buttonMap.get("quSe").setG ( pixel.getGreen());
                buttonMap.get("quSe").setB (pixel.getBlue());
                buttonMap.get("quSe").getXy().setText("(" + buttonMap.get("quSe").getX() + "," + buttonMap.get("quSe").getY() + ")");
                buttonMap.get("quSe").getjCol().setForeground(new Color(buttonMap.get("quSe").getR(), buttonMap.get("quSe").getG(), buttonMap.get("quSe").getB()));
            } catch (Exception ext) {
                ext.printStackTrace();
            }
        }
        if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
            this.run = false;
            JRun.setText("运行");
            JOptionPane.showMessageDialog(null, "已停止运行", "提示", JOptionPane.INFORMATION_MESSAGE);
        }

    }

    public static void main(String[] args) throws AWTException {
        MainGui mainGui = new MainGui();
    }

    public MainGui() throws AWTException {
        frame = new JFrame("My GUI");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(250, 350);
        // 创建面板
        panel = new JPanel();
        panel.setLayout(new GridBagLayout());


        openDia = new FileDialog(this, "打开", FileDialog.LOAD);
        saveDia = new FileDialog(this, "保存", FileDialog.SAVE);

        GridBagConstraints constraints = new GridBagConstraints();
        constraints.gridx = 0;
        constraints.anchor = GridBagConstraints.WEST;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.insets = new Insets(5, 5, 5, 5);
        int i=0;
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
        String[] options = {"Option 1", "Option 2", "Option 3"};
        comboBox = new JComboBox(options);
        constraints.gridy = 7;
        constraints.gridx = 0;
        constraints.gridwidth = 2;
        panel.add(comboBox, constraints);
        frame.getContentPane().add(panel);
        // 设置主窗口可见
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setMenuBar();
    }

    public void setMenuBar()

    {

        JMenuBar myBar=new JMenuBar();

        JMenu helpMenu=new JMenu("帮助");

        JMenuItem help_About=new JMenuItem("载入");
        JMenuItem save=new JMenuItem("保存");

        frame.setJMenuBar(myBar);

        myBar.add(helpMenu);

        helpMenu.add(help_About);
        helpMenu.add(save);
        save.addActionListener(this);
        help_About.addActionListener(this);

    }
    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
    }

}