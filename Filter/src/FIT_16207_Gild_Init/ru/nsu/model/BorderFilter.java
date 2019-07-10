package FIT_16207_Gild_Init.ru.nsu.model;

import java.awt.image.BufferedImage;

public class BorderFilter {

    private static BufferedImage borderOperator(BufferedImage image, int border, int[] xMatrix, int[] yMatrix){
        ImageConvolution filter = new ImageConvolution(image, (int)Math.sqrt(xMatrix.length), (int)Math.sqrt(xMatrix.length), 1, xMatrix);
        BufferedImage xImage = filter.getOutputImage();
        ImageConvolution filter2 = new ImageConvolution(image, (int)Math.sqrt(yMatrix.length), (int)Math.sqrt(yMatrix.length), 1, yMatrix);
        BufferedImage yImage = filter2.getOutputImage();

        int rx, ry, gx, gy, bx, by;
        BufferedImage newImage = new BufferedImage(image.getWidth(), image.getHeight(), java.awt.image.BufferedImage.TYPE_INT_RGB);
        for (int y = 0; y < xImage.getHeight(); y++) {
            for (int x = 0; x < xImage.getWidth(); x++) {
                int rgbaX = xImage.getRGB(x, y);
                int rgbaY = yImage.getRGB(x, y);
                if(x > 1 && (x < image.getWidth() - 1) && y > 1 && (y < image.getHeight() - 1)) {
                    rx = (rgbaX) & 0xff;
                    gx = (rgbaX >> 8) & 0xff;
                    bx = (rgbaX >> 16) & 0xff;

                    ry = (rgbaY) & 0xff;
                    gy = (rgbaY >> 8) & 0xff;
                    by = (rgbaY >> 16) & 0xff;

                    bx = (int) Math.sqrt(Math.pow(bx,2) + Math.pow(by,2));
                    gx = (int) Math.sqrt(Math.pow(gx,2) + Math.pow(gy,2));
                    rx = (int) Math.sqrt(Math.pow(rx,2) + Math.pow(ry,2));

                    int rgba;
                    if(bx > border || gx > border || rx > border) {
                        rgba = 0xffffff;
                    } else {
                        rgba = 0;
                    }
                    newImage.setRGB(x, y, rgba);
                }
            }
        }
        return newImage;
    }

    public static BufferedImage sobelOperator(BufferedImage image, int border) {
        final int[] xMatrix = {-1, -2, -1, 0, 0, 0, 1, 2, 1};
        final int[] yMatrix = {-1, 0, 1, -2, 0, 2, -1, 0, 1};
        return borderOperator(image, border, xMatrix, yMatrix);
    }

    public static BufferedImage robertsOperatr(BufferedImage image, int border) {
        final int[] xMatrix = {1, 0, 0, -1};
        final int[] yMatrix = {0, 1, -1, 0};
        return borderOperator(image, border, xMatrix, yMatrix);
    }
}
