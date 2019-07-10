package FIT_16207_Gild_Init.ru.nsu.model;

import java.awt.*;

public class Function {
    private Point xDefinitionArea;
    private Point yDefinitionArea;

    void setXDefinitionArea(Point xDefinitionArea) {
        this.xDefinitionArea = xDefinitionArea;
    }

    void setYDefinitionArea(Point yDefinitionArea) {
        this.yDefinitionArea = yDefinitionArea;
    }

    Function(Point xDefinitionArea, Point yDefinitionArea) {
        this.xDefinitionArea = xDefinitionArea;
        this.yDefinitionArea = yDefinitionArea;
    }

    public float getValue(float x, float y) {
        //return (float) ((x)*Math.sin(y));
        return (x*y);
    }

    public Point getXDefinitionArea() {
        return xDefinitionArea;
    }

    public Point getYDefinitionArea() {
        return yDefinitionArea;
    }

}
