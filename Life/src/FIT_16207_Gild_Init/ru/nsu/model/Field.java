package FIT_16207_Gild_Init.ru.nsu.model;

import java.awt.*;
import java.io.*;
import java.nio.channels.FileLockInterruptionException;
import java.util.MissingFormatArgumentException;
import java.util.Scanner;

public class Field {
    private Cell[][] field;
    private int height;
    private int width;
    private int lineThickness;
    private int cellSize;
    private int aliveCells;
    private Point lastChanged = null;
    private String mode = "Replace";
    private boolean impact = false; //show/hide cells impact


    public Point getLastChanged() {
        return lastChanged;
    }

    public void setLastChanged(Point lastChanged) {
        this.lastChanged = lastChanged;
    }

    public Field(int height, int width, int lineThickness, int cellSize)  {
        this.height = height;
        this.width = width;
        this.lineThickness = lineThickness;
        field = new Cell[height][width];
        for(int i = 0; i < height; i++) {
            for(int j = 0; j < width; j++) {
                field[i][j] = new Cell(false);
            }
        }
        this.cellSize = cellSize;
    }

    public static Field load(String filePath, Field field) {
       try(BufferedReader bf = new BufferedReader(new FileReader(filePath))) {
            field.clear();
            String currentLine = bf.readLine();
            Scanner scanner = new Scanner(currentLine);
            int height = scanner.nextInt();
            int width = scanner.nextInt();
            currentLine = bf.readLine();
            scanner = new Scanner(currentLine);
            int cellSize = scanner.nextInt();
            currentLine = bf.readLine();
            scanner = new Scanner(currentLine);
            int lineThickness = scanner.nextInt();
            currentLine = bf.readLine();
            scanner = new Scanner(currentLine);
            int aliveCells = scanner.nextInt();
            field = new Field(height, width, lineThickness, cellSize);
            //instance = null;

            while ((currentLine = bf.readLine()) != null) {
                scanner = new Scanner(currentLine);
                int i = scanner.nextInt();
                int j = scanner.nextInt();
                field.field[i][j].setState(true);
            }
            return field;

        } catch (IOException e) {
            System.out.println("Error while reading field information from file");
        }
        return field;
    }

    public void makeCellAlive (int x, int y) {
        field[x][y].setState(true);
    }

    public void countCellsImpact() {
        int currentHeight;
        int currentWidth;
        //for each cell in field
        for (currentHeight = 0; currentHeight < height; currentHeight++) {
           if (currentHeight % 2 == 0) {
               for (currentWidth = 0; currentWidth < width; currentWidth++) {
                   countNeighbours(currentHeight, currentWidth);
               }
           } else {
               for (currentWidth = 0; currentWidth < width - 1; currentWidth++) {
                   countNeighbours(currentHeight, currentWidth);
               }
           }
        }

        //COUNT IMPACT
        for (currentHeight = 0; currentHeight < height; currentHeight++) {
            if (currentHeight % 2 == 0) {
                for (currentWidth = 0; currentWidth < width; currentWidth++) {
                    //COUNT IMPACT
                    field[currentHeight][currentWidth].countImpact();
                }
            } else {
                for (currentWidth = 0; currentWidth < width - 1; currentWidth++) {
                    //COUNT IMPACT
                    field[currentHeight][currentWidth].countImpact();
                }
            }
        }
   }

    public void update() {
        int currentHeight;
        int currentWidth;
        for (currentHeight = 0; currentHeight < height; currentHeight++) {
            if (currentHeight % 2 == 0) {
                for (currentWidth = 0; currentWidth < width; currentWidth++) {
                    field[currentHeight][currentWidth].checkState();
                }
            } else {
                for (currentWidth = 0; currentWidth < width - 1; currentWidth++) {
                    field[currentHeight][currentWidth].checkState();
                }
            }
       }

   }

   public void countNeighbours(int yCell, int xCell) {
       int currentWidth;
       field[yCell][xCell].deleteNeighbors();
       if((yCell % 2) == 0) {
           currentWidth = width;
           evenCellsNeighbors(yCell, xCell, currentWidth);
       } else {
           currentWidth = width - 1;
           oddCellsNeighbors(yCell, xCell, currentWidth);
       }
    }

    //odd cells
    public void oddCellsNeighbors (int yCell, int xCell, int currentWidth) {
        //count first neighbours
        if (yCell >= 1 && field[yCell - 1][xCell].isState()) {
            field[yCell][xCell].increaseFirstCount();
        }

        if (yCell >= 1 && xCell < (currentWidth - 1) && field[yCell - 1][xCell + 1].isState()) {
            field[yCell][xCell].increaseFirstCount();
        }

        if(xCell >= 1 && field[yCell][xCell - 1].isState()) {
            field[yCell][xCell].increaseFirstCount();
        }

        if(xCell < (currentWidth - 1) && field[yCell][xCell + 1].isState()) {
            field[yCell][xCell].increaseFirstCount();
        }

        if(yCell < (height - 1) && field[yCell + 1][xCell].isState()) {
            field[yCell][xCell].increaseFirstCount();
        }

        if(yCell < (height - 1) && xCell < (currentWidth - 1) && field[yCell + 1][xCell + 1].isState()) {
            field[yCell][xCell].increaseFirstCount();
        }

        //count second neighbours
        if (yCell < (height - 1) && xCell >= 1 && field[yCell + 1][xCell - 1].isState()) {
            field[yCell][xCell].increaseSecondCount();
        }

        if (yCell >= 1 && xCell >= 1 && field[yCell - 1][xCell - 1].isState()) {
            field[yCell][xCell].increaseSecondCount();
        }

        if(yCell < (height - 1) && xCell < (currentWidth - 2) && field[yCell + 1][xCell + 2].isState()) {
            field[yCell][xCell].increaseSecondCount();
        }

        if (yCell >= 1 && xCell < (currentWidth - 2) && field[yCell - 1][xCell + 2].isState()) {
            field[yCell][xCell].increaseSecondCount();
        }

        if (yCell < (height - 2) && field[yCell + 2][xCell].isState()) {
            field[yCell][xCell].increaseSecondCount();
        }
        if (yCell >= 2 && field[yCell - 2][xCell].isState()) {
            field[yCell][xCell].increaseSecondCount();
        }
    }

    //even cells
    public void evenCellsNeighbors (int yCell, int xCell, int currentWidth) {
        //looking for first neighbours
        if (yCell >= 1 && xCell >= 1 && field[yCell - 1][xCell - 1].isState()) {
            field[yCell][xCell].increaseFirstCount();
        }

        if (yCell >= 1 && field[yCell - 1][xCell].isState()) {
            field[yCell][xCell].increaseFirstCount();
        }

        if(xCell >= 1 && field[yCell][xCell - 1].isState()) {
            field[yCell][xCell].increaseFirstCount();
        }

        if(xCell < (currentWidth - 1) && field[yCell][xCell + 1].isState()) {
            field[yCell][xCell].increaseFirstCount();
        }

        if(yCell < (height - 1) && xCell >= 1 && field[yCell + 1][xCell - 1].isState()) {
            field[yCell][xCell].increaseFirstCount();
        }

        if(yCell < (height - 1) && field[yCell + 1][xCell].isState()) {
            field[yCell][xCell].increaseFirstCount();
        }
        //count second neighbours
        if (yCell < (height - 1) && xCell < (currentWidth - 1) && field[yCell + 1][xCell + 1].isState()) {
            field[yCell][xCell].increaseSecondCount();
        }

        if (yCell >= 1 && xCell < currentWidth - 1 && field[yCell - 1][xCell + 1].isState()) {
            field[yCell][xCell].increaseSecondCount();
        }

        if(yCell < (height - 1) && xCell >= 2 && field[yCell + 1][xCell - 2].isState()) {
            field[yCell][xCell].increaseSecondCount();
        }

        if (yCell >= 1 && xCell >= 2 && field[yCell - 1][xCell - 2].isState()) {
            field[yCell][xCell].increaseSecondCount();
        }

        if (yCell < (height - 2) && field[yCell + 2][xCell].isState()) {
            field[yCell][xCell].increaseSecondCount();
        }

        if (yCell >= 2 && field[yCell - 2][xCell].isState()) {
            field[yCell][xCell].increaseSecondCount();
        }
    }

    public void changeEarlyState(boolean b, int i, int j) {
        field[i][j].setEarlyChanged(b);
    }

    public boolean isEarlyChanged(int i, int j) {
        return field[i][j].isEarlyChanged();
    }

    public void updateCellsEarlyState() {
        int y;
        int x;
        //for each cell in field
        for (y = 0; y < height; y++) {
            if(y % 2 == 0) {
                for (x = 0; x < width; x++) {
                    field[y][x].setEarlyChanged(false);
                }
            } else {
                for (x = 0; x < width - 1; x++) {
                    field[y][x].setEarlyChanged(false);
                }
            }
        }
    }

    public void clear() {
        int y;
        int x;
        //for each cell in field
        for (y = 0; y < height; y++) {
            if(y % 2 == 0) {
                for (x = 0; x < width; x++) {
                   field[y][x].setState(false);
                   field[y][x].setImpact(0);
                   field[y][x].deleteNeighbors();
                }
            } else {
                for (x = 0; x < width - 1; x++) {
                    field[y][x].setState(false);
                    field[y][x].setImpact(0);
                    field[y][x].deleteNeighbors();
                }
            }
        }
        aliveCells = 0;
    }

    public boolean isEmpty() {
        for (int i = 0; i < height; i++) {
            if (i % 2 == 0) {
                for (int j = 0; j < width; j++) {
                    if(field[i][j].isState()) {
                        return false;
                    }
                }
            } else {
                for (int j = 0; j < width - 1; j++) {
                    if(field[i][j].isState()) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    public Point getCellCoordinate(int xClicked, int yClicked, int pixelColor) {

        int i;
        int j;
        Point mouseClicked = new Point(xClicked, yClicked);
        double minimalDistance = getDistance(mouseClicked, field[0][0].getCenter());
        if(pixelColor == Color.white.getRGB() || pixelColor == Color.BLACK.getRGB()) {
            return null;
        }

        Point currentCenter;
        Point coordinates = new Point(0, 0);
        //for each cell in field
        for (i = 0; i < height; i++) {
            if(i % 2 == 0) {
                for ( j = 0; j < width; j++) {
                    currentCenter = field[i][j].getCenter();
                    if(minimalDistance >= getDistance(mouseClicked, currentCenter)) {
                        minimalDistance = getDistance(mouseClicked, currentCenter);
                        coordinates = new Point(i, j);
                    }
                }
            } else {
                for ( j = 0; j < width - 1; j++) {
                    currentCenter = field[i][j].getCenter();
                    if(minimalDistance >= getDistance(mouseClicked, currentCenter)) {
                        minimalDistance = getDistance(mouseClicked, currentCenter);
                        coordinates = new Point(i, j);
                    }
                }
            }
        }

        return coordinates;
    }

    public void save(String filePath) {
        countAliveCells();
        try(BufferedWriter bw = new BufferedWriter(new FileWriter(filePath))) {
            //save parameters of the field
            bw.write(String.format(this.height + " " + this.width));
            bw.newLine();
            bw.write(String.valueOf(this.cellSize));
            bw.newLine();
            bw.write(String.valueOf(this.lineThickness));
            bw.newLine();
            bw.write(String.valueOf(this.aliveCells));
            bw.newLine();

            for (int i = 0; i < height; i++) {
                if (i % 2 == 0) {
                    for (int j = 0; j < width; j++) {
                        if(field[i][j].isState()) {
                            bw.write(i + " " + j);
                            bw.newLine();
                        }
                    }
                } else {
                    for (int j = 0; j < width - 1; j++) {
                        if(field[i][j].isState()) {
                            bw.write(i + " " + j);
                            bw.newLine();
                        }
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("Error while saving field");
        }
    }

    public void countAliveCells() {
        for (int i = 0; i < height; i++) {
            if (i % 2 == 0) {
                for (int j = 0; j < width; j++) {
                    if(field[i][j].isState()) {
                        aliveCells++;
                    }
                }
            } else {
                for (int j = 0; j < width - 1; j++) {
                    if(field[i][j].isState()) {
                        aliveCells++;
                    }
                }
            }
        }
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getLineThickness() {
        return lineThickness;
    }

    public void setLineThickness(int lineThickness) {
        this.lineThickness = lineThickness;
    }

    public int getCellSize() {
        return cellSize;
    }

    public void setCellSize(int cellSize) {
        this.cellSize = cellSize;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    public String getMode() {
        return this.mode;
    }

    public void setImpact(boolean state) {
        this.impact = state;
    }

    public boolean getImpact() {
        return this.impact;
    }

    public float getCellImpact(int i, int j) {
        return field[i][j].getImpact();
    }

    public void setState(boolean state, int xCell, int yCell) {
        field[xCell][yCell].setState(state);
    }

    public boolean isAlive(int i, int j) {
        return field[i][j].isState();
    }

    public void setCenter(int i, int j, Point center) {
        field[i][j].setCenter(center);
    }

    public Point getCenter(int i, int j) {
        return field[i][j].getCenter();
    }

    public static double getDistance(Point p0, Point p1) {
        int dx = p0.x - p1.x;
        int dy = p0.y - p1.y;

        return Math.sqrt(dx*dx + dy*dy);
    }

    public void resize(int oldHeight, int oldWidth) {
        if (oldHeight < this.height || oldWidth < this.width) {
            Cell[][] oldField = field;

            field = new Cell[height][width];
            for (int i = 0; i < height; i++) {
                for (int j = 0; j < width; j++) {
                    field[i][j] = new Cell(false);
                }
            }

            int currentWidth = Math.min(oldWidth, width);
            int currentHeight = Math.min(oldHeight, height);

            for (int i = 0; i < currentHeight; i++) {
                if (i % 2 == 0) {
                    for (int j = 0; j < currentWidth; j++) {
                        if (oldField[i][j].isState()) {
                            field[i][j].setState(true);
                        }
                    }
                } else {
                    for (int j = 0; j < currentWidth - 1; j++) {
                        if (oldField[i][j].isState()) {
                            field[i][j].setState(true);
                        }
                    }
                }
            }
        }
    }
}
