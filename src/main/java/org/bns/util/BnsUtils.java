package org.bns.util;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
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
    public static boolean matchImage(BufferedImage source, BufferedImage target, double threshold) {
        int sourceWidth = source.getWidth();
        int sourceHeight = source.getHeight();
        int targetWidth = target.getWidth();
        int targetHeight = target.getHeight();

        // 比较像素的区域范围
        int xStart = Math.min(sourceWidth - targetWidth, 0);
        int yStart = Math.min(sourceHeight - targetHeight, 0);
        int xEnd = Math.max(sourceWidth - targetWidth, 0);
        int yEnd = Math.max(sourceHeight - targetHeight, 0);

        // 遍历源图像的区域进行匹配
        for (int y = yStart; y <= yEnd; y++) {
            for (int x = xStart; x <= xEnd; x++) {
                double similarity = compareImages(source, target, x, y);
                if (similarity >= threshold) {
                    return true;
                }
            }
        }

        return false;
    }


    public static void saveBufferedImage(BufferedImage image, String formatName, String outputFilePath) {
        try {
            File outputFile = new File(outputFilePath);
            ImageIO.write(image, formatName, outputFile);
            System.out.println("图片保存成功！");
        } catch (IOException e) {
            System.out.println("保存图片时出错：" + e.getMessage());
        }
    }

    public static double compareImages(BufferedImage source, BufferedImage target, int x, int y) {
        int targetWidth = target.getWidth();
        int targetHeight = target.getHeight();

        // 比较像素的个数
        int totalPixels = targetWidth * targetHeight;

        // 统计匹配的像素个数
        int matchedPixels = 0;

        // 遍历目标图像进行像素比较
        for (int j = 0; j < targetHeight; j++) {
            for (int i = 0; i < targetWidth; i++) {
                int sourcePixel = source.getRGB(x + i, y + j);
                int targetPixel = target.getRGB(i, j);

                // 判断像素是否匹配
                if (sourcePixel == targetPixel) {
                    matchedPixels++;
                }
            }
        }

        // 计算相似度
        double similarity = (double) matchedPixels / totalPixels;

        return similarity;
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