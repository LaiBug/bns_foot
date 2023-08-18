package org.bns;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.bns.pojo.PickPOJO;
import org.bns.util.BnsUtils;
import org.bns.util.Constants;
import org.bns.util.QQLoginApp;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.TimerTask;
import java.util.Timer;


public class MainGui extends JFrame  implements ActionListener, KeyListener,Runnable {


    public static boolean run=false;
    public static MainGui mainGui;
    private JFrame frame;
    private JPanel panel;
    private JComboBox<String> comboBox;

    private static Timer timer;
    private static boolean secondLineSuccess=false;
    private static boolean firstLineSuccess=true;

    private static int kill=0;

    private static PickPOJO quse =new PickPOJO("当前取色");

    private static BufferedImage beginLine;

    public static Map<String, PickPOJO> buttonMap = new HashMap<String, PickPOJO>() {{
        put("xueYin", new PickPOJO("血隐",1));
        put("xueXian", new PickPOJO("血现",1));
        put("shiQu", new PickPOJO("拾取",1));
        put("y", new PickPOJO("Y键取色",1));
        put("jiantou", new PickPOJO("箭头",2));
        put("pindao", new PickPOJO("频道",2));
        put("1xian", new PickPOJO("1线",2));
        put("2xian", new PickPOJO("2线",2));

    }};

    Robot robot = new Robot();

    static  JTextArea logTextArea ;
    static JScrollPane scrollPane;
    FileDialog openDia, saveDia;//定义“打开、保存”对话框
    Point mousePoint;
    Color pixel = new Color(0, 0, 0);
    JButton JRun;

    QQLoginApp qqLoginApp;

    public MainGui() throws AWTException {

        frame = new JFrame("BnsBoss");
        Image icon = Toolkit.getDefaultToolkit().getImage(BnsFish.class.getResource("/stat2.png"));
        JFrame.setDefaultLookAndFeelDecorated(true);

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 400);
        frame.setIconImage(icon);
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                System.out.println("Window is closing");
                System.exit(0);
            }
        });
        // 创建面板
        panel = new JPanel();
        panel.setLayout(new GridBagLayout());


        openDia = new FileDialog(this, "打开", FileDialog.LOAD);
        saveDia = new FileDialog(this, "保存", FileDialog.SAVE);
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.anchor = GridBagConstraints.WEST;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.insets = new Insets(-10, 5, 30, 5);

        constraints.gridy = 0;
        constraints.gridx = 2;
        panel.add(quse.getjCol(), constraints);
        constraints.gridy = 0;
        constraints.gridx = 3;
        panel.add(quse.getXy(), constraints);
        constraints.gridy = 0;
        constraints.gridx = 4;
        panel.add( new JLabel("当前取色"), constraints);
        quse.getButton().addKeyListener(this);
        quse.getButton().addActionListener(this);


        constraints.insets = new Insets(5, 5, 5, 5);

        int x=0;
        for (String key : buttonMap.keySet()) {
            if(buttonMap.get(key).getFlag()==2){
                constraints.gridy = 1;
                constraints.gridx = x;
                x++;
                panel.add(buttonMap.get(key).getXy(), constraints);
//                buttonMap.get(key).getXy().setBackground(new Color(52, 152, 219));
//                buttonMap.get(key).getXy().setOpaque(true);
                constraints.gridy = 1;
                constraints.gridx = x;
                panel.add(buttonMap.get(key).getButton(), constraints);
                buttonMap.get(key).getButton().setBackground(new Color(52, 152, 219)); // 设置按钮的背景颜色
                buttonMap.get(key).getButton().setForeground(Color.WHITE); // 设置按钮的前景色
//                buttonMap.get(key).getButton().setBackground(Color.BLUE); // 设置按钮的背景颜色
//                buttonMap.get(key).getButton().setBorder(BorderFactory.createRaisedBevelBorder());

//                buttonMap.get(key).getButton().setFont(new Font("Arial", Font.BOLD, 16)); // 设置按钮的字体样式

                buttonMap.get(key).getButton().addKeyListener(this);
                buttonMap.get(key).getButton().addActionListener(this);
                x++;
            }
        }
        int y=2;
        for (String key : buttonMap.keySet()) {
            if(buttonMap.get(key).getFlag()==1){
                constraints.gridy = y;
                constraints.gridx = 0;
                panel.add(buttonMap.get(key).getjCol(), constraints);
                constraints.gridy = y;
                constraints.gridx = 1;
                panel.add(buttonMap.get(key).getXy(), constraints);
                constraints.gridy = y;
                constraints.gridx = 2;
                panel.add(buttonMap.get(key).getButton(), constraints);
                buttonMap.get(key).getButton().addKeyListener(this);
                buttonMap.get(key).getButton().addActionListener(this);
                y++;
            }
        }

        String[] options = {"Option 1", "Option 2", "Option 3"};
        comboBox = new JComboBox(options);
        constraints.gridy = y;
        constraints.gridx = 0;
        constraints.gridwidth = 3;
        panel.add(comboBox, constraints);
        logTextArea=new JTextArea(10,27);
        logTextArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        logTextArea.setEditable(false);

        scrollPane = new JScrollPane(logTextArea);

//        scrollPane.setPreferredSize(new Dimension(300, 200)); // 设置宽度和高度
        constraints.gridy = 2;
        constraints.gridx = 3;
        constraints.gridheight=5;
        constraints.gridwidth=x-3;

        panel.add(scrollPane, constraints);
        logTextArea.addKeyListener(this);
//        logTextArea.addAncestorListener(this);
        scrollPane.addKeyListener(this);

        JRun=new JButton("运行");
        JRun.addActionListener(this);
        JRun.addKeyListener(this);
        constraints.gridy = y+1;
        constraints.gridx = 0;
        constraints.gridwidth=3;
        panel.add(JRun, constraints);

        frame.getContentPane().add(panel);
        setMenuBar();

        // 设置主窗口可见
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        LocalDate currentDate = LocalDate.now();  // 获取当前日期

        LocalDate targetDate = LocalDate.of(Constants.LIMIT_YEAR, Constants.LIMIT_MONTH, Constants.LIMIT_DAY);  // 目标日期为2023年10月31日
        if (currentDate.isAfter(targetDate)) {
            JOptionPane.showMessageDialog(frame, "已超免费使用期限，如需继续使用请联系\nVX："+Constants.ADMIN_VX+"  或者QQ："+Constants.ADMIN_QQ, "提示", JOptionPane.INFORMATION_MESSAGE);
            BnsUtils.logPrint(logTextArea,"3秒后自动关闭");
            robot.delay(3000);
            System.exit(0);
        } else {
            JOptionPane.showMessageDialog(frame, "免费工具，以后需要绑定QQ授权使用，"+Constants.LIMIT_YEAR+
                    "-"+ Constants.LIMIT_MONTH+"-"+Constants.LIMIT_DAY+"后失效\n联系方式VX："+Constants.ADMIN_VX+"  或者QQ："+Constants.ADMIN_QQ, "提示", JOptionPane.INFORMATION_MESSAGE);
        }
        importTxt("./","boss.txt");
    }
    private void authLogin() {
        qqLoginApp=new QQLoginApp();
        qqLoginApp.getQQLogin();
        final Timer timer = new Timer();
        TimerTask task = new TimerTask() {
            public void run() {
                String url=qqLoginApp.aaa();
                if(!url.contains("i.qq.com")){
                    if(url.contains(Constants.USER_QQ)) {
                        timer.cancel();
                        qqLoginApp.isAuth=true;
                        System.out.println("当前页面的URL地址：" + url);
                        qqLoginApp.jframe.dispose();
                    }else {
                        timer.cancel();
                        qqLoginApp.isAuth=false;
                        System.out.println("未授权QQ!请联系管理员VX:" + Constants.ADMIN_VX+",QQ:"+Constants.ADMIN_QQ);
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
                    if(qqLoginApp!=null&&!qqLoginApp.isAuth){
                        JOptionPane.showMessageDialog(frame, "未授权", "提示", JOptionPane.INFORMATION_MESSAGE);
                        System.exit(0);
                    }
                    Rectangle area = new Rectangle(buttonMap.get("pindao").getX(), buttonMap.get("pindao").getY(), 15, 20); // 指定区域的坐标和大小
                    beginLine = robot.createScreenCapture(area);
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
                    robot.delay(500);
                    BnsUtils.logPrint(logTextArea,"运行错误，已停止");
                    timer.cancel();
                    throw new RuntimeException(ex);
                }
            } else {
                stopRun();
            }
        }
        if(e.getActionCommand()=="载入"){
            openDia.setVisible(true);
            String dirpath = openDia.getDirectory();
            String fileName = openDia.getFile();
            importTxt(dirpath,fileName);

        }else if(e.getActionCommand()=="保存"){
            saveTxt();
        }
        for (String key : buttonMap.keySet()) {
            if (e.getSource() == buttonMap.get(key).getButton()) {
                PickPOJO p = buttonMap.get(key);
                if (quse.getX() == 0&&quse.getX() == 0) {
                    JOptionPane.showMessageDialog(frame, "请按ALT取色" , "提示", JOptionPane.INFORMATION_MESSAGE);
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

    private void stopRun() {
        run=false;
        timer.cancel();
        robot.delay(500);

        BnsUtils.logPrint(logTextArea,"已停止运行");
        JRun.setText("运行");
        JOptionPane.showMessageDialog(frame, "已停止运行", "提示", JOptionPane.INFORMATION_MESSAGE);

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

            BnsUtils.logPrint(logTextArea,"内容：" + text);
            bufw.write(text);
            bufw.close();
        } catch (Exception ex) {
            BnsUtils.logPrint(logTextArea,"错：" + ex.getMessage());
            ex.printStackTrace();
            ex.getMessage();
        }
    }

    private void importTxt(String dirpath, String fileName) {
        if (dirpath == null || fileName == null) {
            return;
        }
        File file = new File(dirpath, fileName);
        try {
            BufferedReader bufr = new BufferedReader(new FileReader(file));
            String line = "";
            String s2 = "";
            while ((line = bufr.readLine()) != null) {
                BnsUtils.logPrint(logTextArea,line);
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
            bufr.close();
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void run() {
            BnsUtils.logPrint(logTextArea,"运行中");
            if (comparePoint(buttonMap.get("xueYin"))) {
                executeKeyAndTime(KeyEvent.VK_RIGHT,500);
                BnsUtils.logPrint(logTextArea,"龙出了 开始转圈圈直到锁定龙");
            }else   if (comparePoint(buttonMap.get("xueXian"))) {
                BnsUtils.logPrint(logTextArea,"锁定龙了");
                attack();
                pick();
                chooseLine();
                chooseLine();
            }
            Rectangle area = new Rectangle(buttonMap.get("pindao").getX(), buttonMap.get("pindao").getY(), 15, 20); // 指定区域的坐标和大小
            BufferedImage nowLine = robot.createScreenCapture(area);
             boolean containsImage = BnsUtils.matchImage(nowLine, beginLine,0.9);//百分90模糊匹配判断是否在2线
            if(kill!=1&&!containsImage){
                qieXian("1xian");
                kill=0;
            }
    }

    private void chooseLine() {
        Rectangle area = new Rectangle(buttonMap.get("pindao").getX(), buttonMap.get("pindao").getY(), 15, 20); // 指定区域的坐标和大小
        BufferedImage nowLine = robot.createScreenCapture(area);
        String formatName = "jpg";  // 图片格式，这里以 jpg 格式为例
        BnsUtils.saveBufferedImage(nowLine,formatName, "nowLine.jpg");
        BnsUtils.saveBufferedImage(beginLine,formatName,"beginLine.jpg");
        try {
            boolean containsImage = BnsUtils.matchImage(nowLine, beginLine,0.9);//百分90模糊匹配判断是否在2线
            if(containsImage){
                qieXian("2xian");
                System.out.println("在1线，切2线");

            }else {
                qieXian("1xian");
                System.out.println("不在1线，切1线");
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private boolean qieXian(String line) {
        robot.keyPress(KeyEvent.VK_ALT);
        robot.delay(1150);
        robot.mouseMove(buttonMap.get("pindao").getX(), buttonMap.get("pindao").getY());
        robot.delay(150);
        robot.keyPress(KeyEvent.BUTTON1_MASK);
        robot.delay(150);
        robot.keyRelease(KeyEvent.BUTTON1_MASK);
        robot.delay(1150);
        BnsUtils.logPrint(logTextArea,"切"+line);
        robot.mouseMove(buttonMap.get(line).getX(), buttonMap.get(line).getY() );
        robot.delay(1150);
        robot.keyPress(KeyEvent.BUTTON1_MASK);
        robot.delay(150);
        robot.keyRelease(KeyEvent.BUTTON1_MASK);
        robot.delay(1150);
        robot.keyRelease(KeyEvent.VK_ALT);
        robot.delay(1150);
        if (comparePoint(buttonMap.get("y"))){
            robot.keyPress(KeyEvent.VK_Y);
            robot.delay(550);
            robot.keyRelease(KeyEvent.VK_Y);
            robot.delay(1150);
            return true;
        }
        return false;

    }

    private void pick() {
        BnsUtils.logPrint(logTextArea,"保存击杀后箭头方向");
        int isPick = 0;
        while (isPick < 1) {
            Rectangle area = new Rectangle(buttonMap.get("jiantou").getX(), buttonMap.get("jiantou").getY(), 20, 20); // 指定区域的坐标和大小
            BufferedImage oldshot = robot.createScreenCapture(area);
            BnsUtils.logPrint(logTextArea,"开始第"+(isPick+1)+"寻找箱子");
            robot.delay(500);
            if (comparePoint(buttonMap.get("shiQu"))) {
                BnsUtils.logPrint(logTextArea,"找到箱子了，捡起来");
                robot.delay(1000);
                executeKeyAndTime(KeyEvent.VK_F, 500);//拾取
                executeKeyAndTime(KeyEvent.VK_F, 500);//拾取
                return;
            }
            BnsUtils.logPrint(logTextArea,"不在脸上，左转圈寻找");
            executeKeyAndTime(KeyEvent.VK_LEFT, 1500);
            BnsUtils.logPrint(logTextArea,"左转1.5秒");
            robot.delay(500);
            BnsUtils.logPrint(logTextArea,"获取当前箭头方向");
            BufferedImage newshot= robot.createScreenCapture(area);
            while (BnsUtils.calculateSimilarity(newshot, oldshot)<0.9&&run) {
                if (comparePoint(buttonMap.get("shiQu"))) {
                    BnsUtils.logPrint(logTextArea,"找到箱子了，捡起来");
                    executeKeyAndTime(KeyEvent.VK_F, 500);//拾取
                    executeKeyAndTime(KeyEvent.VK_F, 500);//拾取
                    BnsUtils.logPrint(logTextArea,"修正方向");

                    while (BnsUtils.calculateSimilarity(newshot, oldshot)<0.9) {
                        if(!run){
                            return;
                        }
                        BnsUtils.logPrint(logTextArea,"当前箭头方向和击杀后箭头方向不匹配，继续修正方向");
                        executeKeyAndTime(KeyEvent.VK_LEFT, 1000);
                        robot.delay(500);
                        newshot= robot.createScreenCapture(area);
                    }
                    BnsUtils.logPrint(logTextArea,"修正方向完成");
                    return;
                }
                BnsUtils.logPrint(logTextArea,"第"+(isPick+1)+"次转圈继续按左转键");
                executeKeyAndTime(KeyEvent.VK_LEFT, 1000);
//                robot.delay(500);
                newshot= robot.createScreenCapture(area);

            }
            BnsUtils.logPrint(logTextArea,"第"+(isPick+1)+"次转圈结束，找不到箱子，前进一步继续寻找");
            if(!run){
                return;
            }
            executeKeyAndTime(KeyEvent.VK_W, 2000);
            isPick++;
        }
    }

    private void attack() {
        BnsUtils.logPrint(logTextArea,"开始攻击");
        while (comparePoint(buttonMap.get("xueXian"))&&run) {
            BnsUtils.logPrint(logTextArea,"攻击中");
            executeKeyAndTime(KeyEvent.VK_1, 500);
            executeKeyAndTime(KeyEvent.VK_G, 500);
            executeKeyAndTime(KeyEvent.VK_R, 500);
            executeKeyAndTime(KeyEvent.VK_T, 500);
        }
        BnsUtils.logPrint(logTextArea,"龙G了，停止攻击");
        kill++;

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
        if ((diffR + diffG + diffB) < 100&&run) {
            BnsUtils.logPrint(logTextArea,"匹配到：" + pojo.getButton().getText());
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
            JOptionPane.showMessageDialog(frame, "已停止运行", "提示", JOptionPane.INFORMATION_MESSAGE);
        }

    }

    public static void main(String[] args) throws AWTException {
         mainGui = new MainGui();
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