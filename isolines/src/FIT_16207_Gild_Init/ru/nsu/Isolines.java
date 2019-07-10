package FIT_16207_Gild_Init.ru.nsu;

import FIT_16207_Gild_Init.ru.nsu.model.Colors;
import FIT_16207_Gild_Init.ru.nsu.model.CoordinateConvert;
import FIT_16207_Gild_Init.ru.nsu.model.Function;
import FIT_16207_Gild_Init.ru.nsu.model.Grid;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashMap;

class Isolines {
    private HashMap<Point, Float> functionValueInNodes;
    private DrawZone drawZone;
    private BufferedImage drawField;
    private BufferedImage userIsolines;
    private BufferedImage basicIsolines;
    private BufferedImage dots;
    private Function function;
    private Grid grid;
    private Color isolinesColor;
    private Colors colors;
    private ArrayList<Square> squareGrid;
    private ArrayList<Point> crossDots;
    private static final float EPS = 0.00001f;

    Isolines(Grid grid, Function function, DrawZone drawZone, Color isolinesColor, Colors colors) {
        functionValueInNodes = new HashMap<>();
        this.drawZone = drawZone;
        this.drawField = drawZone.getDrawField();
        this.userIsolines = new BufferedImage(drawField.getWidth(), drawField.getHeight(), BufferedImage.TYPE_INT_ARGB);
        this.basicIsolines = new BufferedImage(drawField.getWidth(), drawField.getHeight(), BufferedImage.TYPE_INT_ARGB);
        this.dots = new BufferedImage(drawField.getWidth(), drawField.getHeight(), BufferedImage.TYPE_INT_ARGB);
        this.function = function;
        this.grid = grid;
        this.isolinesColor = isolinesColor;
        this.colors = colors;
        for(Point point : grid.getGridPoints()) {
            int definitionX = (int) CoordinateConvert.pixelToCoordinate(point.x, function.getXDefinitionArea().x, function.getXDefinitionArea().y, 0, drawField.getWidth());
            int definitionY = (int) CoordinateConvert.pixelToCoordinate(point.y, function.getYDefinitionArea().x, function.getYDefinitionArea().y, 0, drawField.getHeight());
            functionValueInNodes.put(point, function.getValue(definitionX, definitionY));
        }
        this.squareGrid = new ArrayList<>();
        this.crossDots = new ArrayList<>();
        getSquareGrid();
        System.out.println(functionValueInNodes);
    }

    private void getSquareGrid() {
        for (Point point : grid.getGridPoints()) {
            Point rightUp = new Point((int) (point.x + grid.getDx()), point.y);
            Point leftDown = new Point(point.x, (int) (point.y + grid.getDy()));
            Point rightDown = new Point(point.x + (int) grid.getDx(), point.y + (int) grid.getDy());

            if(!grid.getGridPoints().contains(rightUp) ||
                    !grid.getGridPoints().contains(rightDown) ||
                    !grid.getGridPoints().contains(leftDown)) {
                continue;
            }

            squareGrid.add(new Square(point, rightUp, leftDown, rightDown));
        }
    }

    private int getXCrossDotsInLine(float isoLevel, Point point, Point nextPoint) {
        if(((isoLevel < functionValueInNodes.get(point)) && (isoLevel < functionValueInNodes.get(nextPoint))) ||
                ((isoLevel > functionValueInNodes.get(point)) && (isoLevel > functionValueInNodes.get(nextPoint)))) {
            return drawField.getWidth()*drawField.getHeight();
        }
        return (int) (point.x + (nextPoint.x - point.x) * (isoLevel - functionValueInNodes.get(point)) /
                (functionValueInNodes.get(nextPoint) - functionValueInNodes.get(point)));
    }

    private int getYCrossDotsInLine(float isoLevel, Point point, Point nextPoint) {
        if(((isoLevel < functionValueInNodes.get(point)) && (isoLevel < functionValueInNodes.get(nextPoint))) ||
                ((isoLevel > functionValueInNodes.get(point)) && (isoLevel > functionValueInNodes.get(nextPoint)))) {
            return drawField.getWidth()*drawField.getHeight();
        }
        return (int) (point.y + (nextPoint.y - point.y) * (isoLevel - functionValueInNodes.get(point)) /
                (functionValueInNodes.get(nextPoint) - functionValueInNodes.get(point)));
    }

    private void drawIsolines(float isoLevel, BufferedImage image) {
        Graphics2D g2 = image.createGraphics();
        g2.setColor(isolinesColor);
        ArrayList<Point> currentSquareCrossDots = new ArrayList<>();
        int cross;
        int border = drawField.getWidth()*drawField.getHeight();
        System.out.println(grid.getGridPoints().size());
        for (Square square : squareGrid) {

            cross = getXCrossDotsInLine(isoLevel, square.getLeftUp(), square.getRightUp());
            if(cross != border) {
                if(!currentSquareCrossDots.contains(new Point(cross, square.getLeftUp().y)) &&
                        (cross >= square.getLeftUp().x) && (cross <= square.getRightUp().x)) {
                    currentSquareCrossDots.add(new Point(cross, square.getLeftUp().y));
                }
                crossDots.add(new Point(cross, square.getLeftUp().y));
            }

            cross = getXCrossDotsInLine(isoLevel, square.getLeftDown(), square.getRightDown());
            if(cross != border) {
                if(!currentSquareCrossDots.contains(new Point(cross, square.getLeftDown().y)) &&
                        (cross >= square.getLeftDown().x) && (cross <= square.getRightDown().x)) {
                    currentSquareCrossDots.add(new Point(cross, square.getLeftDown().y));
                }
                crossDots.add(new Point(cross, square.getLeftDown().y));
            }

            cross = getYCrossDotsInLine(isoLevel, square.getLeftUp(), square.getLeftDown());
            if (cross != border) {
                if(!currentSquareCrossDots.contains(new Point(square.getLeftUp().x, cross)) &&
                        (cross >= square.getLeftUp().y) && (cross <= square.getLeftDown().y)) {
                    currentSquareCrossDots.add(new Point(square.getLeftUp().x, cross));
                }
                crossDots.add(new Point(square.getLeftUp().x, cross));
            }

            cross = getYCrossDotsInLine(isoLevel, square.getRightUp(), square.getRightDown());
            if(cross != border) {
                if(!currentSquareCrossDots.contains(new Point(square.getRightUp().x, cross)) &&
                        (cross >= square.getRightUp().y) && (cross <= square.getRightDown().y)) {
                    currentSquareCrossDots.add(new Point(square.getRightUp().x, cross));
                }
                crossDots.add(new Point(square.getRightUp().x, cross));
            }

            square.setCrossDotsCoordinates(currentSquareCrossDots);
            connectCrossDots(square, isoLevel, image);
            currentSquareCrossDots.clear();
        }
        g2.dispose();
    }

    void drawIsolines(int xMouseClick, int yMouseClick) {
        xMouseClick = (int) CoordinateConvert.pixelToCoordinate(xMouseClick, function.getXDefinitionArea().x, function.getXDefinitionArea().y, 0,drawField.getWidth());
        yMouseClick = (int) CoordinateConvert.pixelToCoordinate(yMouseClick, function.getYDefinitionArea().x, function.getYDefinitionArea().y, 0, drawField.getHeight());
        float isoLevel = function.getValue(xMouseClick, yMouseClick);
        drawIsolines(isoLevel, userIsolines);
    }

    private void connectCrossDots(Square square, float isolevel, BufferedImage image) {
        ArrayList<Point> crossDots = square.getCrossDotsCoordinates();

        if (crossDots.size() == 0) {
            return;
        }

        Graphics2D g2 = image.createGraphics();
        g2.setColor(isolinesColor);

        if (crossDots.size() == 2) {
            g2.drawLine(crossDots.get(0).x, crossDots.get(0).y, crossDots.get(1).x, crossDots.get(1).y);
            return;
        }

        if (crossDots.size() == 3) {
            isolevel += EPS;
            drawIsolines(isolevel, image);
            return;
        }

        if (crossDots.size() == 4) {
            float leftUp = drawZone.getFunctionDraw().getConvertValue(square.getLeftUp().x, square.getLeftUp().y);
            float rightUp =  drawZone.getFunctionDraw().getConvertValue(square.getRightUp().x, square.getRightUp().y);
            float leftDown =  drawZone.getFunctionDraw().getConvertValue(square.getLeftDown().x, square.getLeftDown().y);
            float rightDown =  drawZone.getFunctionDraw().getConvertValue(square.getRightDown().x, square.getRightDown().y);
            float center = (leftUp + leftDown + rightUp + rightDown);

            if(Math.signum(center) == Math.signum(leftUp - isolevel)) {
                g2.drawLine(crossDots.get(0).x, crossDots.get(0).y, crossDots.get(1).x, crossDots.get(1).y);
                g2.drawLine(crossDots.get(2).x, crossDots.get(2).y, crossDots.get(3).x, crossDots.get(3).y);
                g2.dispose();
                return;
            }
            else {
                g2.drawLine(crossDots.get(0).x, crossDots.get(0).y, crossDots.get(2).x, crossDots.get(2).y);
                g2.drawLine(crossDots.get(1).x, crossDots.get(1).y, crossDots.get(3).x, crossDots.get(3).y);
                g2.dispose();
                return;
            }
        }
            g2.dispose();

    }

    void drawBasicIsolines() {
        for(Float isoLevel : colors.getBounds()) {
            drawIsolines(isoLevel, basicIsolines);
        }
    }

    void dotsMapping() {
        Graphics2D g2 = dots.createGraphics();
        g2.setColor(Color.RED);
        g2.setStroke(new BasicStroke(5));
        for(Point point : crossDots) {
            g2.drawLine(point.x, point.y, point.x, point.y);
        }
        g2.setStroke(new BasicStroke(1));
        g2.dispose();
    }

    void setGrid(Grid grid) {
        this.grid = grid;
    }

    void isolinesOff() {
        userIsolines = new BufferedImage(drawField.getWidth(), drawField.getHeight(), BufferedImage.TYPE_INT_ARGB);
    }

    void basicISolinesOff() {
        basicIsolines = new BufferedImage(drawField.getWidth(), drawField.getHeight(), BufferedImage.TYPE_INT_ARGB);
    }

    void dotsOff() {
        dots = new BufferedImage(drawField.getWidth(), drawField.getHeight(), BufferedImage.TYPE_INT_ARGB);
    }

    BufferedImage getBasicIsolines() {
        return this.basicIsolines;
    }

    BufferedImage getUserIsolines() {
        return this.userIsolines;
    }

    BufferedImage getDots() {
        return this.dots;
    }
}
