package FIT_16207_Gild_Init.ru.nsu.model;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Resizer {
    public static BufferedImage changeSize(BufferedImage image, int newWidth, int newHeight) {
        Image tmp = image.getScaledInstance(newWidth, newHeight, Image.SCALE_SMOOTH);
        BufferedImage resized = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = resized.createGraphics();
        g2d.drawImage(tmp, 0, 0, resized.getWidth(), resized.getHeight(), null);
        g2d.dispose();
        return resized;
    }
}
