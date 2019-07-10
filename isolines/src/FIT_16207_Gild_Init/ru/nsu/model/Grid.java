package FIT_16207_Gild_Init.ru.nsu.model;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class Grid {
    private ArrayList<Point> gridPoints;
    private BufferedImage drawField;
    private BufferedImage grid;
    private float dx, dy;
    private int xCount, yCount;
    private Graphics2D g2;

    public int getxCount() {
        return xCount;
    }

    public int getyCount() {
        return yCount;
    }

    public Grid(BufferedImage drawField, int xCount, int yCount) {
        this.drawField = drawField;
        this.gridPoints = new ArrayList<>();
        this.xCount = xCount;
        this.yCount = yCount;
        this.dx = (float) drawField.getWidth() / (float) xCount;
        this.dy = (float) drawField.getHeight() / (float)yCount;
        int currentStepY = 0;
        int currentStepX;
        for(int i = 0; i <= xCount; i++) {
            //gridPoints.add(new Point())
            for(currentStepX = 0; currentStepX <= drawField.getWidth(); currentStepX+= dx) {
                gridPoints.add(new Point(currentStepX, currentStepY));
            }
            currentStepY += dy;
        }
        System.out.println(gridPoints.size());
        System.out.println(gridPoints);

        this.grid = new BufferedImage(drawField.getWidth(), drawField.getHeight(), BufferedImage.TYPE_INT_ARGB);
        this.g2 = grid.createGraphics();
    }

    public ArrayList<Point> getGridPoints() {
        return gridPoints;
    }

    public float getDx() {
        return dx;
    }

    public float getDy() {
        return dy;
    }

    public BufferedImage getGrid() {
        return grid;
    }

    public BufferedImage drawGrid() {
        float[] dash = {1.0f, 1.0f};
        BasicStroke dashed = new BasicStroke(1.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 10f, dash, 0.0f);
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);

        g2.setColor(Color.black);
        g2.setStroke(dashed);

        int currentStepY = (int) dy;
        int currentStepX;
        for(int i = 0; i < yCount; i++) {
            g2.setColor(Color.black);
            g2.drawLine(0, currentStepY, grid.getWidth(), currentStepY);//horizontal
            currentStepY += dy;
        }

        currentStepX = (int) dx;
        for(int i = 0; i < xCount; i++) {
            g2.setColor(Color.black);
            g2.drawLine(currentStepX, 0, currentStepX, grid.getHeight());
            currentStepX += dx;
        }

        g2.dispose();
        return grid;
    }

    public void off() {
        grid = new BufferedImage(drawField.getWidth(), drawField.getHeight(), BufferedImage.TYPE_INT_ARGB);
    }
}
