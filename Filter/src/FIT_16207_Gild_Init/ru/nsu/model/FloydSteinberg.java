package FIT_16207_Gild_Init.ru.nsu.model;

import FIT_16207_Gild_Init.ru.nsu.Constants;

import java.awt.image.BufferedImage;

public class FloydSteinberg {
    private int redNumber;
    private int greenNumber;
    private int blueNumber;
    private BufferedImage image;

    public FloydSteinberg(BufferedImage image, int redNumber, int greenNumber, int blueNumber) {
        this.image = image;

        this.redNumber = redNumber;
        this.greenNumber = greenNumber;
        this.blueNumber = blueNumber;
    }

    public BufferedImage dither() {
        BufferedImage newImage = new BufferedImage(Constants.ZONE_SIZE, Constants.ZONE_SIZE, java.awt.image.BufferedImage.TYPE_INT_RGB);
        int r, g, b;
        int rNew, gNew, bNew;
        int rError = 0;
        int gError = 0;
        int bError = 0;
        int currentRgb;

        for(int y = 0; y < image.getHeight(); y++) {
            for(int x = 0; x < image.getWidth(); x++) {
                int rgb = image.getRGB(x, y);
                r = (rgb) & 0xff;
                g = (rgb >> 8) & 0xff;
                b = (rgb >> 16) & 0xff;

                rNew = channelCheck((int)getClosestColor(r+rError, redNumber));
                gNew = channelCheck((int)getClosestColor(g+gError, greenNumber));
                bNew = channelCheck((int)getClosestColor(b+bError, blueNumber));

                currentRgb = (bNew << 16) + (gNew << 8) + rNew;
                newImage.setRGB(x, y, currentRgb);

                rError = r + rError - rNew;
                gError = g + gError - gNew;
                bError = b + bError - bNew;

                if(x < image.getWidth() - 1) {
                    currentRgb = ditheringError(x+1, y, 7, rError, gError, bError);
                    newImage.setRGB(x+1, y, currentRgb);
                }
                if(x >= 1 && y < image.getHeight() - 1) {
                    currentRgb = ditheringError(x-1, y+1, 3, rError, gError, bError);
                    newImage.setRGB(x-1, y+1, currentRgb);
                }
                if(y < image.getHeight() - 1) {
                    currentRgb = ditheringError(x, y+1, 5, rError, gError, bError);
                    newImage.setRGB(x, y+1, currentRgb);
                }
                if(x < image.getWidth() - 1 && y < image.getHeight() - 1) {
                    currentRgb = ditheringError(x+1, y+1, 1, rError, gError, bError);
                    newImage.setRGB(x+1, y+1, currentRgb);
                }
            }
        }

        return newImage;
    }

    private double getClosestColor(int oldColor, int colorNumbers) {
        double quanties = (double)255 / (double)(colorNumbers - 1);
        return Math.round((double)oldColor/quanties)*quanties;
    }

    private int channelCheck(int channel) {
        if(channel > 0xff) {
            return 0xff;
        } else if(channel < 0) {
            return 0;
        }
        return channel;
    }

    private int ditheringError(int x, int y, int divisior, int redError, int greenError, int blueError) {
        int rgb = image.getRGB(x, y);
        int r, g, b;

        r = (rgb) & 0xff;
        g = (rgb >> 8) & 0xff;
        b = (rgb >> 16) & 0xff;

        r += (((double)divisior/16.0) * redError);
        g += (((double)divisior/16.0) * greenError);
        b += (((double)divisior/16.0) * blueError);

        r = channelCheck(r);
        g = channelCheck(g);
        b = channelCheck(b);

        rgb = (b << 16) + (g << 8) + r;
        return rgb;
    }
}
