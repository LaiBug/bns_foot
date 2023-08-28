package org.bns.util;
import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

////首先获取屏幕截图并加载物品掉落的图像模板。然后，使用图像匹配算法（基于像素的差异比较）对屏幕截图进行遍历，计算每个位置与模板的相似度得分。最后找到最佳匹配位置，并计算出物品掉落的坐标。
//
//注意，在使用该代码之前，请将template.png替换为你自己准备的物品掉落图像模板，并确保图像模板与屏幕截图具有相同的颜色和尺寸。
//
//运行示例代码后，将会在控制台输出物品掉落的坐标。你可以根据实际情况对输出的坐标进行进一步的处理和使用。
public class RPGItemDrop {
    public static void main(String[] args) {
        try {
            // 获取屏幕大小
            Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
            int screenWidth = (int) screenSize.getWidth();
            int screenHeight = (int) screenSize.getHeight();

            // 创建屏幕图像对象
            Robot robot = new Robot();
            BufferedImage screenCapture = robot.createScreenCapture(new Rectangle(screenWidth, screenHeight));

            // 加载物品掉落图像模板
            File templateFile = new File("template.png");
            BufferedImage templateImage = ImageIO.read(templateFile);

            // 进行图像匹配
            int resultWidth = screenWidth - templateImage.getWidth() + 1;
            int resultHeight = screenHeight - templateImage.getHeight() + 1;
            float[][] matchResult = new float[resultWidth][resultHeight];
            for (int x = 0; x < resultWidth; x++) {
                for (int y = 0; y < resultHeight; y++) {
                    matchResult[x][y] = compareImages(screenCapture.getSubimage(x, y, templateImage.getWidth(), templateImage.getHeight()), templateImage);
                }
            }

            // 寻找最佳匹配位置
            float bestMatch = 0;
            Point bestMatchPosition = new Point();
            for (int x = 0; x < resultWidth; x++) {
                for (int y = 0; y < resultHeight; y++) {
                    if (matchResult[x][y] > bestMatch) {
                        bestMatch = matchResult[x][y];
                        bestMatchPosition.setLocation(x, y);
                    }
                }
            }

            // 计算物品掉落坐标
            int itemDropX = bestMatchPosition.x + templateImage.getWidth() / 2;
            int itemDropY = bestMatchPosition.y + templateImage.getHeight() / 2;

            System.out.println("物品掉落坐标：(" + itemDropX + ", " + itemDropY + ")");
        } catch (AWTException | IOException e) {
            e.printStackTrace();
        }
    }

    // 图像比较方法（基于像素的差异比较）
    private static float compareImages(BufferedImage image1, BufferedImage image2) {
        int width = image1.getWidth();
        int height = image1.getHeight();

        // 确保两幅图像尺寸相同
        if (width != image2.getWidth() || height != image2.getHeight()) {
            return 0;
        }

        long diff = 0;
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int rgb1 = image1.getRGB(x, y);
                int rgb2 = image2.getRGB(x, y);
                int r1 = (rgb1 >> 16) & 0xff;
                int g1 = (rgb1 >> 8) & 0xff;
                int b1 = rgb1 & 0xff;
                int r2 = (rgb2 >> 16) & 0xff;
                int g2 = (rgb2 >> 8) & 0xff;
                int b2 = rgb2 & 0xff;
                diff += Math.abs(r1 - r2) + Math.abs(g1 - g2) + Math.abs(b1 - b2);
            }
        }

        // 计算相似度得分
        float similarityScore = 1.0f - (float) diff / (width * height * 3 * 255);
        return similarityScore;
    }
}