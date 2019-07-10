package FIT_16207_Gild_Init.ru.nsu;

import FIT_16207_Gild_Init.ru.nsu.model.Field;
import javafx.util.Pair;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.util.Stack;

public class FieldDraw {
    public static void bresenhamAlgorithm(BufferedImage imgField, int x0, int y0, int x1, int y1) {
        int dy, dx, sign;
        int signy, signx;
        int x = x0;
        int y = y0;
        int err = 0;
        dy = y1 - y0;
        dx = x0 - x1;
        sign = (Math.abs(dy) > Math.abs(dx)) ? 1 : -1;
        signy = (dy < 0) ? -1 : 1;
        signx = (dx < 0) ? -1 : 1;
        imgField.setRGB(x0, y0, Color.BLACK.getRGB());
        if (sign == -1) {
            while (x != x1 || y != y1) {
                err += dy*signy;
                if (err > 0) {
                    err -= dx*signx;
                    y += signy;
                }
                x -= signx;
                imgField.setRGB(x, y, Color.BLACK.getRGB());
            }
        } else {
            while (x != x1 || y != y1) {
                err += dx*signx;
                if (err > 0) {
                    err -= dy*signy;
                    x -= signx;
                }
                y += signy;
                imgField.setRGB(x, y, Color.BLACK.getRGB());
            }
        }
    }

    public static void drawHexagon(BufferedImage imgField, int xCenter, int yCenter, int radius, int thickness) {
        int halfRadius = halfRadius(radius);
        if(thickness == 1) {
            bresenhamAlgorithm(imgField, xCenter, yCenter + radius, xCenter + radius, yCenter + halfRadius);
            bresenhamAlgorithm(imgField, xCenter + radius, yCenter + halfRadius, xCenter + radius, yCenter - halfRadius);
            bresenhamAlgorithm(imgField, xCenter, yCenter - radius,xCenter + radius, yCenter - halfRadius);
            bresenhamAlgorithm(imgField, xCenter - radius, yCenter - halfRadius, xCenter, yCenter - radius);
            bresenhamAlgorithm(imgField, xCenter - radius, yCenter + halfRadius, xCenter - radius, yCenter - halfRadius);
            bresenhamAlgorithm(imgField, xCenter - radius, yCenter + halfRadius, xCenter, yCenter + radius);
        } else {
            Graphics2D g2 = imgField.createGraphics();
            g2.setStroke(new BasicStroke(thickness));
            g2.setColor(Color.BLACK);
            g2.drawLine(xCenter, yCenter + radius, xCenter + radius, yCenter + halfRadius);
            g2.drawLine(xCenter + radius, yCenter + halfRadius, xCenter + radius, yCenter - halfRadius);
            g2.drawLine(xCenter, yCenter - radius,xCenter + radius, yCenter - halfRadius);
            g2.drawLine(xCenter - radius, yCenter - halfRadius, xCenter, yCenter - radius);
            g2.drawLine(xCenter - radius, yCenter + halfRadius, xCenter - radius, yCenter - halfRadius);
            g2.drawLine(xCenter - radius, yCenter + halfRadius, xCenter, yCenter + radius);
            g2.drawImage(imgField, 0, 0, null);
        }
    }

    public static void drawField(BufferedImage imgField, Field field) {
        int i;
        int j;
        int radius = halfRadius(field.getCellSize());
        int halfRadius = halfRadius(radius);
        int x0 = radius + halfRadius, y = radius + halfRadius;
        int x = x0;
        Graphics2D g2 = imgField.createGraphics();

        for (i = 0; i < field.getHeight(); i++) {
            if(i % 2 == 0) {
                for ( j = 0; j < field.getWidth(); j++) {
                    drawHexagon(imgField, x, y, radius, field.getLineThickness());
                    field.setCenter(i, j, new Point(x, y));
                    spanFill(imgField, x, y, Color.BLACK, Color.WHITE, new Color(240, 255, 240));
                    if(field.isAlive(i, j)) {
                        spanFill(imgField, x, y, Color.BLACK, new Color(240, 255, 240), Color.GREEN);
                    }

                    g2.setColor(Color.BLACK);
                    if(field.getImpact()) {
                        g2.setStroke(new BasicStroke(4));
                        g2.drawString(String.format("%.1f", field.getCellImpact(i, j)), x - 7, y + 5);
                    }
                    x += 2*radius;
                }
                x = x0 + radius;
            } else {
                for ( j = 0; j < field.getWidth() - 1; j++) {
                    drawHexagon(imgField, x, y, radius, field.getLineThickness());
                    field.setCenter(i, j, new Point(x, y));
                    spanFill(imgField, x, y, Color.BLACK, Color.WHITE, new Color(240, 255, 240));
                    if(field.isAlive(i, j)) {
                        spanFill(imgField, x, y, Color.BLACK, new Color(240, 255, 240), Color.GREEN);
                    }

                    g2.setColor(Color.BLACK);
                    if(field.getImpact()) {
                        g2.setStroke(new BasicStroke(4));
                        g2.drawString(String.format("%.1f", field.getCellImpact(i, j)), x - 7, y + 5);
                    }
                    x += 2*radius;
                }
                x = x0;
            }
            y += 3*halfRadius;
        }
        g2.drawImage(imgField, 0, 0, null);
    }

    public static void spanFill(BufferedImage imgField, int x, int y, Color boundaryColor, Color oldColor, Color fillColor) {
        Point spanDown;
        Point spanUp;
        Stack<Pair<Point, Integer>> pointsToColor = new Stack<>();
        Pair<Point, Integer> currentSpan;
        Point currentSpanX;
        //get first span
        currentSpanX = getSpan(x, y, boundaryColor, oldColor, imgField);
        if(currentSpanX == null) {
            return;
        }
        currentSpan = new Pair<>(currentSpanX, y);
        pointsToColor.push(currentSpan);
        int y0 = y++;

        while ((spanUp = getSpan(x, y0, boundaryColor, oldColor, imgField)) != null) {
            pointsToColor.push(new Pair<>(spanUp, y0));
            y0++;
        }

        y0 = y--;
        while ((spanDown = getSpan(x, y0, boundaryColor, oldColor, imgField)) != null) {
            pointsToColor.push(new Pair<>(spanDown, y0));
            y0--;
        }

        while(!pointsToColor.empty()) {
            currentSpan = pointsToColor.pop();
            for (int i = currentSpan.getKey().x; i <= currentSpan.getKey().y; i++) {
                imgField.setRGB(i, currentSpan.getValue(), fillColor.getRGB());
            }
        }
    }

    public static Point getSpan(int x, int y, Color boundary, Color old, BufferedImage imgField) {
        int currentRGB = imgField.getRGB(x, y);
        int x0;
        int x1;
        if (currentRGB != boundary.getRGB() && currentRGB == old.getRGB()) {
            while (imgField.getRGB(x, y) != boundary.getRGB()) {
                x--;
            }
            x++;
            x0 = x;
            while (imgField.getRGB(x, y) != boundary.getRGB()) {
                x++;
            }
            x--;
            x1 = x;
            return new Point(x0, x1);
        }
        return null;
    }

    public static int halfRadius(int radius) {
        if (radius % 2 != 0) {
            //return (int)Math.floor(radius/2);
            if ((int) Math.floor((float) radius / 2) % 2 == 0) {
                return (int) Math.floor((float) radius / 2);
            } else if ((int) Math.ceil((float) radius / 2) % 2 == 0) {
                return (int) Math.ceil((float) radius / 2);
            }
        } else {
                return radius / 2;
        }
        return radius / 2;
    }
}
