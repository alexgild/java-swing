package FIT_16207_Gild_Init.ru.nsu.model;

import FIT_16207_Gild_Init.ru.nsu.Constants;

import java.awt.image.*;


public class Filters {
    private BufferedImage image;
    private int[][] pixels;


    public Filters(BufferedImage image) {
        this.image = image;
        getPixels();
    }

    private void getPixels() {
        pixels = new int[image.getHeight()][image.getWidth()];

        for(int i = 0; i < image.getHeight(); i++) {
            for(int j = 0; j < image.getWidth(); j++) {
                pixels[i][j] = image.getRGB(i, j);
            }
        }
    }

    public BufferedImage blur() {
        final int[] blurMatrix = {0, 1, 0, 1, 2, 1, 0, 1, 0};
        ImageConvolution filter = new ImageConvolution(image, 3, 3, 6, blurMatrix);
        return filter.getOutputImage();
    }

    public BufferedImage sharpen() {
        final int[] sharpenMatrix = {0, -1, 0, -1, 5, -1, 0, -1, 0};
        ImageConvolution filter = new ImageConvolution(image, 3, 3, 1, sharpenMatrix);
        return filter.getOutputImage();
    }

    public BufferedImage stamping() {
        final int[] stampingMatrix = {0, 1, 0, -1, 0, 1, 0, -1, 0};
        ImageConvolution filter = new ImageConvolution(image, 3, 3, 1, stampingMatrix);
        return stampingCorrection(filter.getOutputImage());
    }

    public BufferedImage blackAndWhite() {
        double r, g, b, a;
        BufferedImage newImage = new BufferedImage(image.getWidth(), image.getHeight(), java.awt.image.BufferedImage.TYPE_INT_RGB);
        for (int y = 0; y < image.getHeight(); y++) {
            for (int x = 0; x < image.getWidth(); x++) {
                int rgba = image.getRGB(x, y);

                r = ((rgba) & 0xff) * Constants.BW_R_CONSTANT;
                g = ((rgba >> 8) & 0xff) * Constants.BW_G_CONSTANT;
                b = ((rgba >> 16) & 0xff) * Constants.BW_B_CONSTANT;
                a = (rgba >> 24) & 0xff;

                r = r + g + b;
                g = r;
                b = r;
                rgba = ((int)a << 24) + ((int)b << 16) + ((int)g << 8) + (int)r;
                newImage.setRGB(x, y, rgba);
            }
        }
        return newImage;
    }

    public BufferedImage negative() {
        int r, g, b, a;
        BufferedImage newImage = new BufferedImage(image.getWidth(), image.getHeight(), java.awt.image.BufferedImage.TYPE_INT_RGB);
        for (int y = 0; y < image.getHeight(); y++) {
            for (int x = 0; x < image.getWidth(); x++) {
                int rgba = image.getRGB(x, y);
                r = (rgba) & 0xff;
                g = (rgba >> 8) & 0xff;
                b = (rgba >> 16) & 0xff;
                a = (rgba >> 24) & 0xff;

                r = 255 - r;
                g = 255 - g;
                b = 255 - b;
                rgba = (a << 24) + (b << 16) + (g << 8) + r;
                newImage.setRGB(x, y, rgba);
            }
        }
        return newImage;
    }

    private BufferedImage stampingCorrection(BufferedImage stampingImage) {
        double r, g, b, a;
        BufferedImage newImage = new BufferedImage(stampingImage.getWidth(), stampingImage.getHeight(), java.awt.image.BufferedImage.TYPE_INT_RGB);
        for (int y = 0; y < stampingImage.getHeight(); y++) {
            for (int x = 0; x < stampingImage.getWidth(); x++) {
                int rgba = stampingImage.getRGB(x, y);

                r = ((rgba) & 0xff) + 128;
                g = ((rgba >> 8) & 0xff) + 128;
                b = ((rgba >> 16) & 0xff) + 128;
                a = (rgba >> 24) & 0xff + 128;

                rgba = ((int)a << 24) + ((int)b << 16) + ((int)g << 8) + (int)r;
                newImage.setRGB(x, y, rgba);
            }
        }
        return newImage;
    }

    public BufferedImage gammaCorrection(double gamma) {
        double r, g, b, a;
        BufferedImage newImage = new BufferedImage(image.getWidth(), image.getHeight(), java.awt.image.BufferedImage.TYPE_INT_RGB);
        for (int y = 0; y < image.getHeight(); y++) {
            for (int x = 0; x < image.getWidth(); x++) {
                int rgba = image.getRGB(x, y);

                r = (rgba) & 0xff;
                g = (rgba >> 8) & 0xff;
                b = (rgba >> 16) & 0xff;
                a = (rgba >> 24) & 0xff;

                r = 255*Math.pow(r/255.0, 1.0/gamma);
                g = 255*Math.pow(g/255.0, 1.0/gamma);
                b = 255*Math.pow(b/255.0, 1.0/gamma);

                rgba = ((int)a << 24) + ((int)b << 16) + ((int)g << 8) + (int)r;
                newImage.setRGB(x, y, rgba);
            }
        }
        return newImage;
    }

}
