//package org.bns.pojo;
//
//import java.awt.BorderLayout;
//import java.io.OutputStream;
//import java.io.PrintStream;
//import javax.swing.JFrame;
//import javax.swing.JPanel;
//import javax.swing.JScrollPane;
//import javax.swing.JTextArea;
//
//public class LogViewer extends JPanel {
//    private JTextArea logTextArea;
//
//    public LogViewer() {
//        setLayout(new BorderLayout());
//
//        logTextArea = new JTextArea();
//        logTextArea.setEditable(false);
//
//        // 创建一个自定义的 PrintStream，用于重定向 System.out 到 logTextArea
//        PrintStream printStream = new PrintStream(new CustomOutputStream(logTextArea));
//        System.setOut(printStream);
//        System.setErr(printStream);
//
//        JScrollPane scrollPane = new JScrollPane(logTextArea);
//        add(scrollPane, BorderLayout.CENTER);
//    }
//
//    public static void main(String[] args) {
//        JFrame frame = new JFrame("Log Viewer");
//        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//
//        LogViewer logViewer = new LogViewer();
//        frame.add(logViewer);
//
//        frame.setSize(400, 300);
//        frame.setVisible(true);
//
//        // 示例：打印一些日志信息
//        for (int i = 0; i < 10; i++) {
//            System.out.println("Log message " + i);
//            try {
//                Thread.sleep(1000); // 模拟耗时操作
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//        }
//    }
//
//    // 自定义 OutputStream，将输出内容追加到 JTextArea 中
//    private static class CustomOutputStream extends OutputStream {
//        private JTextArea textArea;
//
//        public CustomOutputStream(JTextArea textArea) {
//            this.textArea = textArea;
//        }
//
//        @Override
//        public void write(int b) {
//            // 将字节转换为字符串并追加到 JTextArea 中
//            textArea.append(String.valueOf((char) b));
//            // 将 JTextArea 滚动到最后一行
//            textArea.setCaretPosition(textArea.getDocument().getLength());
//        }
//    }
//}