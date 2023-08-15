package org.bns;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class BnsUtils {


    public static int diff(Integer a, Integer b) {
        if (a >= b) {
            return a - b;
        } else {
            return b - a;
        }
    }

    public static double calculateSimilarity(BufferedImage image1, BufferedImage image2) {
        // 计算相似度的算法，根据实际需求选择合适的算法
        // 这里使用简单的像素比较算法
        int width = image1.getWidth();
        int height = image1.getHeight();
        int numPixels = width * height;
        int matchingPixels = 0;

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int rgb1 = image1.getRGB(x, y);
                int rgb2 = image2.getRGB(x, y);

                if (rgb1 == rgb2) {
                    matchingPixels++;
                }
            }
        }

        return (double) matchingPixels / numPixels;
    }

    public static void logPrint(JTextArea logTextArea, String logStr) {
        logTextArea.append(logStr + System.lineSeparator());
        logTextArea.setCaretPosition(logTextArea.getDocument().getLength());
    }
}