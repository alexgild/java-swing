package FIT_16207_Gild_Init.ru.nsu.model;

import java.awt.image.BufferedImage;
import java.util.Collections;
import java.util.LinkedList;

public class Watercolor {
    public static BufferedImage watercolor(BufferedImage image) {
        BufferedImage newImage = new BufferedImage(image.getWidth(), image.getHeight(), java.awt.image.BufferedImage.TYPE_INT_RGB);
        int height = image.getHeight();
        int width = image.getWidth();
        LinkedList<Integer> colorsAround = new LinkedList<>();
        int xStartIndex, yStartIndex;
        int xEndIndex, yEndIndex;

        for(int y = 0; y < height; y++) {
            for(int x = 0; x < width; x++) {
                xStartIndex = xEndIndex = x;
                yStartIndex = yEndIndex = y;

                if(x >= 2 && x < width - 2) {
                    xStartIndex = x - 2;
                    xEndIndex = x + 2;
                }
                if(y >= 2 && y < height - 2) {
                    yStartIndex = y - 2;
                    yEndIndex = y + 2;

                }
                if (x == 1) {
                    xStartIndex = x - 1;
                } if(y == 1) {
                    yStartIndex = y - 1;
                }
                if(x == width - 2) {
                    xEndIndex = x + 1;
                }if(y == height - 2) {
                    yEndIndex = y + 1;
                }
                    for(int i = yStartIndex; i <= yEndIndex; i++) {
                        for (int j = xStartIndex; j <= xEndIndex; j++) {
                            int rgba = image.getRGB(j, i);
                            colorsAround.add(rgba);
                        }
                    }
                    Collections.sort(colorsAround);
                    newImage.setRGB(x, y, colorsAround.get(colorsAround.size()/2));
                    colorsAround.clear();
                }
            }

        return newImage;
    }
}
