package FIT_16207_Gild_Init.ru.nsu.model;

import FIT_16207_Gild_Init.ru.nsu.Constants;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;

public class Resizer {
    static BufferedImage changeSize(BufferedImage image, int newWidth, int newHeight, int zoneXStart, int zoneYStart) {
        Image tmp = image.getScaledInstance(newWidth, newHeight, Image.SCALE_SMOOTH);
        BufferedImage resized = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = resized.createGraphics();
        g2d.drawImage(tmp, zoneXStart, zoneYStart, resized.getWidth(), resized.getHeight(), null);
        g2d.dispose();
        return resized;
    }

    public static BufferedImage resize(BufferedImage image) {
        if(image.getWidth() >= Constants.ZONE_SIZE && image.getWidth() >= image.getHeight()) {
            return changeSize(image, Constants.ZONE_SIZE, (image.getHeight() * Constants.ZONE_SIZE)/image.getWidth(),
                    Constants.ZONE_LOAD_LEFT, Constants.ZONE_Y_START);
        }
        if(image.getHeight() >= Constants.ZONE_SIZE && image.getHeight() > image.getWidth()) {
            return changeSize(image,(image.getWidth() * Constants.ZONE_SIZE)/image.getHeight(), Constants.ZONE_SIZE,
                    Constants.ZONE_LOAD_LEFT, Constants.ZONE_Y_START);
        }
        return image;
    }

    public static BufferedImage increase(BufferedImage image) {
        BufferedImage newImage = image.getSubimage(image.getWidth()/4, image.getHeight()/4, image.getWidth()/2, image.getHeight()/2);
        return changeSize(newImage, Constants.ZONE_SIZE, Constants.ZONE_SIZE, 0, 0);
    }


    static int getNewWidth(int imageOldWidth, int imageOldHeight) {
        if(imageOldWidth >= Constants.ZONE_SIZE && imageOldWidth >= imageOldHeight) {
            return Constants.ZONE_SIZE;
        }
        if(imageOldHeight >= Constants.ZONE_SIZE) {
            return (imageOldWidth * Constants.ZONE_SIZE)/imageOldHeight;
        }
        else return imageOldWidth;
    }

    static int getNewHeight(int imageOldHeight, int imageOldWidth) {
        if(imageOldWidth >= Constants.ZONE_SIZE && imageOldWidth >= imageOldHeight) {
            return (imageOldHeight * Constants.ZONE_SIZE)/imageOldWidth;
        }
        if(imageOldHeight >= Constants.ZONE_SIZE) {
            return Constants.ZONE_SIZE;
        }
        return imageOldHeight;
    }

    public static BufferedImage rotate(BufferedImage imgDetails, int angle) {
        BufferedImage rotateImg = new BufferedImage(imgDetails.getWidth(), imgDetails.getHeight(), BufferedImage.TYPE_INT_ARGB);
        double locationX = Math.round((float)imgDetails.getWidth()/2);
        double locationY = Math.round((float)imgDetails.getHeight()/2);
        AffineTransform affineTransform = AffineTransform.getRotateInstance(Math.toRadians(angle), locationX, locationY);
        AffineTransformOp transformOp = new AffineTransformOp(affineTransform, AffineTransformOp.TYPE_BILINEAR);
        transformOp.filter(imgDetails, rotateImg);
        return rotateImg;
    }
}
