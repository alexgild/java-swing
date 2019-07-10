package FIT_16207_Gild_Init.ru.nsu.model;

import FIT_16207_Gild_Init.ru.nsu.Constants;

import java.awt.*;
import java.awt.image.BufferedImage;

public class ImageZone {
    public static BufferedImage getZone(BufferedImage image, int xStart, int yStart) {
        if (image.getWidth() == Constants.ZONE_SIZE && image.getHeight() == Constants.ZONE_SIZE) {
            return image;
        }
        int newImageSize = Constants.ZONE_SIZE;
        BufferedImage newImage = image.getSubimage(xStart, yStart, newImageSize, newImageSize);
        return Resizer.changeSize(newImage, Constants.ZONE_SIZE, Constants.ZONE_SIZE, 0, 0);
    }

    public static void drawSquare(BufferedImage image, int x, int y) {
        int newImageSize = Constants.ZONE_SIZE;
        float[] dash = {5.0f, 5.0f};
        BasicStroke dashed = new BasicStroke(2.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 10f, dash, 0.0f);
        Graphics2D g2 = image.createGraphics();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);

        g2.setPaint(Color.gray);

        g2.setStroke(dashed);
        g2.drawLine(x, y, x + newImageSize, y);
        g2.drawLine(x + newImageSize, y, x + newImageSize, y + newImageSize);
        g2.drawLine(x + newImageSize, y + newImageSize, x, y + newImageSize);
        g2.drawLine(x, y + newImageSize, x, y);

        g2.dispose();
    }

    public static int calculateLeftFrameCornerX(int mouseX, int imageOldWidth, int imageOldHeight) {
        int imageNewWidth = Resizer.getNewWidth(imageOldWidth, imageOldHeight);
        mouseX = recalculateMouseClick(mouseX, imageOldWidth, imageNewWidth);

        int newImageSize = Constants.ZONE_SIZE;
        int offset = Constants.ZONE_SIZE / 2;
        if (mouseX < offset) {
            return 0;
        }
        if (mouseX + offset >= imageOldWidth) {
            return (imageOldWidth - newImageSize - 1);
        }
        return (mouseX - offset);
    }


    public static int calculateLeftFrameCornerY(int mouseY, int imageOldHeight, int imageOldWidth) {
        int imageNewHeight = Resizer.getNewHeight(imageOldHeight, imageOldWidth);
        mouseY = recalculateMouseClick(mouseY, imageOldHeight, imageNewHeight);

        int newImageSize = Constants.ZONE_SIZE;
        int offset = Constants.ZONE_SIZE / 2;
        if (mouseY < offset) {
            return 0;
        }
        if (mouseY + offset >= imageOldHeight) {
            return (imageOldHeight - newImageSize - 1);
        }
        return (mouseY - offset);
    }

    private static int recalculateMouseClick(int mouse, int imageOldSize, int imageNewSize) {
        return Math.round((float) (mouse * imageOldSize) / (float) imageNewSize);

    }
}
