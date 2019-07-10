package FIT_16207_Gild_Init.ru.nsu.model;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class FunctionDraw {
    private BufferedImage drawField;
    private int imageWidth;
    private int imageHeight;
    private Function function;
    private ParametersReader reader;
    private Colors colors;
    private ArrayList<Float> functionValue;


    public FunctionDraw(ParametersReader reader, BufferedImage drawField, int imageWidth, int imageHeight,
                            int xDefinitionStart, int xDefinitionEnd,
                                    int yDefinitionStart, int yDefinitionEnd) {
        this.reader = reader;
        this.drawField = drawField;
        this.imageWidth = imageWidth;
        this.imageHeight = imageHeight;
        this.function  = new Function(new Point(xDefinitionStart, xDefinitionEnd), new Point(yDefinitionStart, yDefinitionEnd));
        functionValue = new ArrayList<>();
        countFunctionValues();
        this.colors = new Colors(reader, functionValue);
        //drawFunction();
    }

    public Function getFunction() {
        return function;
    }

    public void setImageWidth(int imageWidth) {
        this.imageWidth = imageWidth;
    }

    public void setImageHeight(int imageHeight) {
        this.imageHeight = imageHeight;
    }

    public void drawFunction() {
        int i = 0;
        float currentValue;
        Color[] functionColors = new Color[imageWidth* imageHeight];

        for(int y = 0; y < imageHeight; y++) {
            for(int x = 0; x < imageWidth; x++) {
                currentValue = functionValue.get(i);
                functionColors[i] = getColor(currentValue);
                drawField.setRGB(x, y, functionColors[i].getRGB());
                i++;
            }
        }
    }

    private Color getColor(float currentValue) {
        int index = 0;
        if (currentValue < colors.getBounds().get(0)) {
            return colors.getColors().get(0);
        }

        for(int i = 0; i < colors.getBounds().size(); i++) {
            if(currentValue >= colors.getBounds().get(i)) {
                index = i;
            } else {
                break;
            }
        }
        return colors.getColors().get(index + 1);
    }

    private void countFunctionValues() {
        float u, v;
        functionValue = new ArrayList<>();
        for(int y = 0; y < imageHeight; y++) {
            for(int x = 0; x < imageWidth; x++) {
                u = CoordinateConvert.pixelToCoordinate(x, function.getXDefinitionArea().x, function.getXDefinitionArea().y, 0, imageWidth);
                v = CoordinateConvert.pixelToCoordinate(y, function.getYDefinitionArea().x, function.getYDefinitionArea().y, 0, imageHeight);
                functionValue.add(function.getValue(u, v));
            }
        }
    }

    public float getConvertValue(int x, int y) {
        float u, v;
        u = CoordinateConvert.pixelToCoordinate(x, function.getXDefinitionArea().x, function.getXDefinitionArea().y, 0, imageWidth);
        v = CoordinateConvert.pixelToCoordinate(y, function.getYDefinitionArea().x, function.getYDefinitionArea().y, 0, imageHeight);
        return function.getValue(u, v);
    }

    public void updateImage(BufferedImage newImage) {
        this.drawField = newImage;
    }

    public ArrayList<Float> getFunctionValue() {
        return functionValue;
    }

    public void setxDefinitionArea(Point point) {
        function.setXDefinitionArea(point);
    }

    public void setyDefinitionArea(Point point) {
        function.setYDefinitionArea(point);
    }

    public void updateData() {
        functionValue = new ArrayList<>();
        countFunctionValues();
        this.colors = new Colors(reader, functionValue);
    }

    public void setNewField(BufferedImage drawField) {
        this.drawField = drawField;
    }
}
