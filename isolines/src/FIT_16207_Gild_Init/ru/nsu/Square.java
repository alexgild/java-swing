package FIT_16207_Gild_Init.ru.nsu;

import java.awt.*;
import java.util.ArrayList;

public class Square {
    private Point leftUp;
    private Point rightUp;
    private Point leftDown;
    private Point rightDown;

    private ArrayList<Point> crossDotsCoordinates;

    Square(Point leftUp, Point rightUp, Point leftDown, Point rightDown) {
        this.leftUp = leftUp;
        this.rightUp = rightUp;
        this.leftDown = leftDown;
        this.rightDown = rightDown;
        crossDotsCoordinates = new ArrayList<>();
    }

    Point getLeftUp() {
        return leftUp;
    }

    Point getRightUp() {
        return rightUp;
    }

    Point getLeftDown() {
        return leftDown;
    }

    Point getRightDown() {
        return rightDown;
    }

    ArrayList<Point> getCrossDotsCoordinates() {
        return crossDotsCoordinates;
    }

    void setCrossDotsCoordinates(ArrayList<Point> crossDotsCoordinates) {
        this.crossDotsCoordinates = crossDotsCoordinates;
    }

    public void add(Point point) {
        crossDotsCoordinates.add(point);
    }

}
