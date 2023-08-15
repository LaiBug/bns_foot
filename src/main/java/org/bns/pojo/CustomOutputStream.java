//package org.bns.pojo;
//
//import javax.swing.*;
//import java.awt.*;
//import java.io.OutputStream;
//
//public class CustomOutputStream extends OutputStream {
//    private JTextArea textArea;
//
//    public CustomOutputStream(JTextArea textArea) {
//        this.textArea = textArea;
//    }
//
//    @Override
//    public void write(int b) {
//        // 将字节转换为字符串并追加到 JTextArea 中
////        StringBuilder aa=new StringBuilder();
////        aa.append(String.valueOf((char) b));
////        textArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
//        textArea.append(String.valueOf((char) b));
////        textArea.getDocument().putProperty("charset", "UTF-8");
//
//        // 将 JTextArea 滚动到最后一行
////        textArea.setCaretPosition(textArea.getDocument().getLength());
//    }
//}
