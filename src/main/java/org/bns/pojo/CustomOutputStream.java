package org.bns.pojo;

import javax.swing.*;
import java.io.OutputStream;

public class CustomOutputStream extends OutputStream {
    private JTextArea textArea;

    public CustomOutputStream(JTextArea textArea) {
        this.textArea = textArea;
    }

    @Override
    public void write(int b) {
        // 将字节转换为字符串并追加到 JTextArea 中
        textArea.append(String.valueOf((char) b));
        // 将 JTextArea 滚动到最后一行
        textArea.setCaretPosition(textArea.getDocument().getLength());
    }
}
