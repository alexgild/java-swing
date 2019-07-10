package FIT_16207_Gild_Init.ru.nsu.model;
import FIT_16207_Gild_Init.ru.nsu.Legend;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class Interpolation {
    private Legend legend;

    public Interpolation(Legend legend) {
        this.legend = legend;
    }

    public BufferedImage interpolate(int offset, Colors colors) {
        float r, g, b;
        Graphics2D g2 = legend.getLegend().createGraphics();
        float cellSize = (float)(legend.getWidth() - 2 * offset)/(float)colors.getColors().size();
        float interpolateSquareSize = cellSize/4;

        int x0 = offset + 1;
        int y0 = offset + 1;

        for(int i = 0; i < colors.getColors().size() - 1; i++) {
            int startCoordinate = (int) (offset + cellSize*i + 3*interpolateSquareSize);
            int endCoordinate = (int) (startCoordinate + 2*interpolateSquareSize);
            int rgbaLeft, rgbaRight;

            int left = (int) (offset + cellSize * i + interpolateSquareSize * 3);
            int right = (int) (offset + cellSize * (i + 1) + interpolateSquareSize);
            rgbaLeft = legend.getLegend().getRGB(left, offset + 1);
            rgbaRight = legend.getLegend().getRGB(right, offset + 1);

            for (int y = y0; y < legend.getHeight(); y++) {
                for (int x = x0; x < x0 + cellSize + interpolateSquareSize; x++) {
                    int rgba;
                    if (x > startCoordinate && x < endCoordinate) {
                        int rLeft, gLeft, bLeft, rRight, gRight, bRight;
                        rLeft = (rgbaLeft) & 0xff;
                        gLeft = (rgbaLeft >> 8) & 0xff;
                        bLeft = (rgbaLeft >> 16) & 0xff;

                        rRight = (rgbaRight) & 0xff;
                        gRight = (rgbaRight >> 8) & 0xff;
                        bRight = (rgbaRight >> 16) & 0xff;

                        r = interpolateComponent(x, endCoordinate, startCoordinate, rLeft, rRight);
                        g = interpolateComponent(x, endCoordinate, startCoordinate, gLeft, gRight);
                        b = interpolateComponent(x, endCoordinate, startCoordinate, bLeft, bRight);

                        rgba = ((int)b << 16) + ((int)g << 8) + (int)r;
                        g2.setColor(new Color(rgba));
                        g2.drawLine(x, offset + 1, x, legend.getHeight() - 1);
                    }
                }
            }

            x0 += cellSize;
        }

        g2.dispose();
        return legend.getLegend();
    }

    private int interpolateComponent(int coordinate, int endCoordinate, int startCoordinate, int leftComponent, int rightComponent) {

        return (leftComponent * (endCoordinate - coordinate)/(endCoordinate - startCoordinate) +
                        rightComponent * (coordinate - startCoordinate)/ (endCoordinate - startCoordinate));
    }

    public BufferedImage interpolateField(BufferedImage image, ArrayList<Float> functionValue, int offset, Colors colors) {
        int i = 0;
        for(int y = 0; y < image.getHeight(); y++) {
            for (int x = 0; x < image.getWidth(); x++) {
                float value = functionValue.get(i);
                int index = getClosestLowColor(value, colors);
                if(index >= 0 && index != colors.getBounds().size() - 1) {
                    float dx = (float)legend.getxCellSize() * (value - colors.getBounds().get(index)) / colors.getStep();
                    int boundsCoordinate = getBoundsCoordinate(offset, legend.getxCellSize(), index);
                    image.setRGB(x, y, legend.getLegend().getRGB((int) (boundsCoordinate + dx), offset + 1));
                }
                if(index == -1) {
                    float dx = (float)legend.getxCellSize() *(value - (colors.getBounds().get(0) - colors.getStep())) / colors.getStep();
                    int boundsCoordinate = getBoundsCoordinate(offset, legend.getxCellSize(), index);
                    image.setRGB(x, y, legend.getLegend().getRGB((int) (boundsCoordinate + dx + 1), offset + 1));
                }
                if(index == colors.getBounds().size() - 1) {
                    float dx = (float)legend.getxCellSize() * (value - colors.getBounds().get(index)) / colors.getStep();
                    int boundsCoordinate = getBoundsCoordinate(offset, legend.getxCellSize(), index);
                    image.setRGB(x, y, legend.getLegend().getRGB((int) (boundsCoordinate + dx), offset + 1));
                }
                i++;
            }
        }
        return image;
    }

    private int getBoundsCoordinate(int offset, int xCellSize, int index) {
        return offset + xCellSize * (index + 1);
    }

    private int getClosestLowColor(float value, Colors colors) {
        int index = 0;
        if (value < colors.getBounds().get(0)) {
            return -1;
        }

        for(int i = 0; i < colors.getBounds().size(); i++) {
            if(value >= colors.getBounds().get(i)) {
                index = i;
            } else {
                break;
            }
        }
        return index;
    }
}
